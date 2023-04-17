package rx.admin.users;

import api.dto.rx.admin.user.UserDto;
import api.dto.rx.admin.user.UserRole;
import api.preconditionbuilders.UsersPrecondition;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Epic;
import io.qameta.allure.Link;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.admin.users.UsersPage;
import rx.BaseTest;
import widgets.common.table.ColumnNames;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Epic("Waiting for separate QA env")
@Link("https://rakutenadvertising.atlassian.net/browse/GS-3280")
public class UsersSortingTableTests extends BaseTest {

    private int totalUsers;
    private List<String> sortIdsByAsc;
    private List<String> sortIdsByDesc;
    private List<String> sortNamesByAsc;
    private List<String> sortNamesByDesc;

    private List<String> sortEmailByDesc;
    private List<String> sortEmailByAsc;
    private UsersPage usersPage;

    private static final String ASC = "ascending";
    private static final String DESC = "descending";

    public UsersSortingTableTests() {
        usersPage = new UsersPage();
    }

    @BeforeClass
    private void loginAndCreateExpectedResuts() {

        if (getTotalUsers() < 60) generateUsers();

        totalUsers = getTotalUsers();

        //expected results for Media Name column
        sortNamesByDesc = getNamesByDesc();
        sortNamesByAsc = getNamesByAsc();

        //Expected result for ID column
        sortIdsByAsc = getIdsByAsc();
        sortIdsByDesc = getIdsByDesc();

        //Expected result for  Publisher Name column
        sortEmailByAsc = getUserEmailByAsc();
        sortEmailByDesc = getUserEmailByDesc();
    }

    @Test(testName = "Sorting 'ID' column by descending", alwaysRun = true)
    public void userSortingByIdDesc() {
        sortByDescColumnByName(ColumnNames.ID);
        validateSortData(ColumnNames.ID, DESC, sortIdsByDesc);
    }

    @Test(testName = "Sorting 'ID' column by ascending", alwaysRun = true)
    public void userSortingByIdAsc() {

        sortByAscColumnByName(ColumnNames.ID);
        validateSortData(ColumnNames.ID, ASC, sortIdsByAsc);
    }

    @Test(testName = "Sorting 'User Name' column by descending", alwaysRun = true)
    public void userSortingByUserNameDesc() {
        sortByDescColumnByName(ColumnNames.NAME);
        validateSortData(ColumnNames.NAME, DESC, sortNamesByDesc);
    }

    @Test(testName = "Sorting 'User Name' column by ascending", alwaysRun = true)
    public void userSortingByUserNameAsc() {
        sortByAscColumnByName(ColumnNames.NAME);
        validateSortData(ColumnNames.NAME, ASC, sortNamesByAsc);
    }

    @Test(testName = "Sorting 'Email' column by ascending", alwaysRun = true)
    public void userSortingByEmailAsc() {
        sortByAscColumnByName(ColumnNames.EMAIL);
        validateSortData(ColumnNames.EMAIL, ASC, sortEmailByAsc);
    }

    @Test(testName = "Sorting 'Email' column by descending", alwaysRun = true)
    public void userSortingByEmailDesc() {
        sortByDescColumnByName(ColumnNames.EMAIL);
        validateSortData(ColumnNames.EMAIL, DESC, sortEmailByDesc);
    }

    @BeforeMethod
    private void login() {
        var table = usersPage.getUsersTable().getShowHideColumns();
        var tableData = usersPage.getUsersTable().getTableData();
        testStart()
                .given()
                .openDirectPath(Path.USER)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, usersPage.getNuxtProgress())
                .testEnd();
    }

    @AfterMethod(alwaysRun = true)
    private void logOut() {
        testStart()
                .given()
                .logOut()
                .testEnd();
    }

    @Step("Sort column {0} by DESC")
    private void sortByDescColumnByName(ColumnNames columnName) {
        var tableData = usersPage.getUsersTable().getTableData();

        testStart()
                .given()
                .and(String.format("Sort column '%s'", columnName))
                .clickOnWebElement(tableData.getColumnHeader(columnName.getName()))
                .then("Ensure that sort by descending: validate column attribute value")
                .validateAttribute(tableData.getColumnHeader(columnName.getName()),
                        "aria-sort", ASC)
                .clickOnWebElement(tableData.getColumnHeader(columnName.getName()))
                .validateAttribute(tableData.getColumnHeader(columnName.getName()),
                        "aria-sort", DESC)
                .waitAndValidate(disappear, usersPage.getNuxtProgress())
                .testEnd();

    }

    @Step("Sort column {0} by ASC")
    private void sortByAscColumnByName(ColumnNames columnName) {
        var tableData = usersPage.getUsersTable().getTableData();
        testStart()
                .given()
                .and(String.format("Sort column '%s'", columnName))
                .clickOnWebElement(tableData.getColumnHeader(columnName.getName()))
                .then("Ensure that sort by ascending: validate column attribute value")
                .validateAttribute(tableData.getColumnHeader(columnName.getName()),
                        "aria-sort", ASC)
                .waitAndValidate(disappear, usersPage.getNuxtProgress())
                .testEnd();

    }

    @Step("Validate data in column {0} sorted by {1}")
    private void validateSortData(ColumnNames columnName, String sortType, List<String> expectedResultList) {
        var tableData = usersPage.getUsersTable().getTableData();
        var tablePagination = usersPage.getUsersTable().getTablePagination();
        //Todo Add checking of total qauntity in pagination test when
        // https://rakutenadvertising.atlassian.net/browse/GS-3280 will be ready
        testStart()
                .given()
                .waitAndValidate(disappear, usersPage.getNuxtProgress())
                .and("Select 50 row per page")
                .scrollIntoView(tablePagination.getPageMenu())
                .selectFromDropdown(tablePagination.getPageMenu(),
                        tablePagination.getRowNumbersList(), "50")
                .waitLoading(visible, usersPage.getTableProgressBar())
                .waitLoading(disappear, usersPage.getTableProgressBar())
                .then(String.format("Validate that text in table footer '1-50 of %s'", totalUsers))
                .validateContainsText(tablePagination.getPaginationPanel(), "1-50 of")
                .then(String.format("Validate data in column '%s' should be sorted by %s",
                        columnName.getName(), sortType))
                .validateList(tableData.getCustomCells(columnName),
                        expectedResultList.subList(0, 50))
                .and("Check next page")
                .clickOnWebElement(tablePagination.getNext())
                .waitLoading(visible, usersPage.getTableProgressBar())
                .waitLoading(disappear, usersPage.getTableProgressBar())
                .then(String.format("Validate that text in table footer '51-%s of %s'",
                        Math.min(100, totalUsers), totalUsers))
                .validateContainsText(tablePagination.getPaginationPanel(),
                        String.format("51-%s", Math.min(100, totalUsers)))
                .then(String.format("Validate data in column '%s' should be sorted by %s", columnName.getName(), sortType))
                .validateList(tableData.getCustomCells(columnName), expectedResultList.subList(50, Math.min(100, totalUsers)))
                .and("Finish test")
                .testEnd();
    }


    private int getTotalUsers() {

        return UsersPrecondition.user()
                .getAllUsers()
                .build()
                .getUserGetAllResponse()
                .getTotal();
    }

    private List<String> getNamesByDesc() {

        return getAllItemsByParams("name-desc").stream()
                .map(UserDto::getName)
                .collect(Collectors.toList());
    }

    private List<String> getNamesByAsc() {

        return getAllItemsByParams("name-asc").stream()
                .map(UserDto::getName)
                .collect(Collectors.toList());
    }

    private List<String> getIdsByAsc() {

        return getAllItemsByParams("id-asc").stream()
                .map(UserDto::getId)
                .map(x -> x.toString())
                .collect(Collectors.toList());
    }

    private List<String> getIdsByDesc() {

        return getAllItemsByParams("id-desc").stream()
                .map(UserDto::getId)
                .map(x -> x.toString())
                .collect(Collectors.toList());
    }

    private List<String> getUserEmailByAsc() {

        return getAllItemsByParams("mail-asc").stream()
                .map(UserDto::getMail)
                .collect(Collectors.toList());
    }

    private List<String> getUserEmailByDesc() {

        return getAllItemsByParams("mail-desc").stream()
                .map(UserDto::getMail)
                .collect(Collectors.toList());
    }
    private List<UserDto> getAllItemsByParams(String strParams) {
        Map<String, Object> queryParams = new HashMap();
        queryParams.put("sort", strParams);

        return UsersPrecondition.user()
                .getUsersWithFilter(queryParams)
                .build()
                .getUserGetAllResponse()
                .getItems();
    }

    private void generateUsers() {

        while (getTotalUsers() < 60) {
            UsersPrecondition.user()
                    .createNewUser(UserRole.SINGLE_PUBLISHER)
                    .build();
        }
    }
}
