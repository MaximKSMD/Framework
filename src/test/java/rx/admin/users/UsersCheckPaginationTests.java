package rx.admin.users;

import api.dto.rx.admin.user.UserDto;
import api.dto.rx.admin.user.UserRole;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Epic;
import io.qameta.allure.Link;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.admin.users.UsersPage;
import rx.BaseTest;

import java.util.ArrayList;
import java.util.List;

import static api.preconditionbuilders.UsersPrecondition.user;
import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static configurations.User.USER_FOR_DELETION;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
@Epic("Waiting for separate QA env")
@Link("https://rakutenadvertising.atlassian.net/browse/GS-3280")
public class UsersCheckPaginationTests extends BaseTest {
    private UsersPage usersPage;

    private int totalUsers;
    private List<UserDto> listUsers;

    public UsersCheckPaginationTests() {
    	usersPage = new UsersPage();
    }

    @BeforeClass
    private void init() {
        if (getTotalUsers() < 200) generateUsers();

        totalUsers = getTotalUsers();
    }

    @BeforeMethod(alwaysRun = true)
    private void login() {
        testStart()
                .given()
                .openDirectPath(Path.USER)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, usersPage.getNuxtProgress())
                .testEnd();
    }

    @Test(description = "Verify Pagination: 100 rows per page", alwaysRun = true, priority = 1)
    public void checkPagination100() {
        verifyPagination(100);
    }

    @Test(description = "Verify Pagination: 50 rows per page", alwaysRun = true, priority = 4)
    public void checkPagination50() {
        verifyPagination(50);
    }

    @Test(description = "Verify Pagination: 25 rows per page", alwaysRun = true, priority = 5)
    public void checkPagination25() {
        verifyPagination(25);
    }

    @Test(description = "Verify Pagination: 20 rows per page", alwaysRun = true, priority = 6)
    public void checkPagination20() {
        verifyPagination(20);
    }

    @Test(description = "Verify Pagination: 15 rows per page", alwaysRun = true, priority = 3)
    public void checkPagination15() {
        verifyPagination(15);
    }

    @Test(description = "Verify Pagination: 10 rows per page", alwaysRun = true, priority = 2)
    public void checkPagination10() {
        verifyPagination(10);
    }

    @Step("Verify pagination {0}")
    private void verifyPagination(Integer rowsPerPage) {
        var tablePagination = usersPage.getUsersTable().getTablePagination();
        var tableData = usersPage.getUsersTable().getTableData();
        //Todo Add checking of total qauntity in pagination test when
        // https://rakutenadvertising.atlassian.net/browse/GS-3280 will be ready
        testStart()
                .and(String.format("Select %s rows per page", rowsPerPage))
                .scrollIntoView(tablePagination.getPageMenu())
                .selectFromDropdown(tablePagination.getPageMenu(),
                        tablePagination.getRowNumbersList(), rowsPerPage.toString())
                .waitLoading(disappear, usersPage.getTableProgressBar())
                .then(String.format("Validate that text in table footer '1-%s of", rowsPerPage))
                .validateContainsText(tablePagination.getPaginationPanel(),
                        String.format(String.format("1-%s of", rowsPerPage)))
                .then(String.format("Rows in table page equals %s", rowsPerPage))
                .validateListSize(tableData.getRows(), rowsPerPage)
                .and("Click on Next page")
                .scrollIntoView(tablePagination.getNext())
                .clickOnWebElement(tablePagination.getNext())
                .then(String.format("Validate that text in table footer '%s-%s of",
                        rowsPerPage + 1, Math.min(rowsPerPage * 2, totalUsers)))
                .validateContainsText(tablePagination.getPaginationPanel(),
                        String.format("%s-%s of", rowsPerPage + 1, Math.min(rowsPerPage * 2, totalUsers)))
                .then(String.format("Rows in table page equals %s", rowsPerPage))
                .validateListSize(tableData.getRows(),rowsPerPage)
                .and("Click on Previous page")
                .scrollIntoView(tablePagination.getPrevious())
                .clickOnWebElement(tablePagination.getPrevious())
                .then(String.format("Validate that text in table footer '1-%s of", rowsPerPage))
                .validateContainsText(tablePagination.getPaginationPanel(),
                        String.format(String.format("1-%s of", rowsPerPage)))
                .then(String.format("Rows in table page equals %s", rowsPerPage))
                .validateListSize(tableData.getRows(), rowsPerPage)
                .testEnd();
    }

    @AfterMethod(alwaysRun = true)
    public void logout(){
        testStart()
                .and("Logout")
                .logOut()
                .testEnd();
    }

    @AfterClass(alwaysRun = true)
    private void deleteEntities() {

        if (listUsers != null) {
            for (UserDto users : listUsers) {
                deleteUser(users.getId());
                deletePublisher(users.getPublisherId());
            }
        }
    }

    private void deleteUser(Integer id) {
        user()
                .setCredentials(USER_FOR_DELETION)
                .deleteUser(id)
                .build();
    }

    private void deletePublisher(Integer id) {
        publisher()
                .setCredentials(USER_FOR_DELETION)
                .deletePublisher(id)
                .build();
    }

    private int getTotalUsers() {
        return user()
                .getAllUsers()
                .build()
                .getUserGetAllResponse()
                .getTotal();
    }

    private void generateUsers() {

        listUsers = new ArrayList<>();
        while (getTotalUsers() < 210) {
            UserDto user = user()
            		.createNewUser(UserRole.SINGLE_PUBLISHER)
                    .build()
                    .getUserResponse();

            listUsers.add(user);
        }
    }
}
