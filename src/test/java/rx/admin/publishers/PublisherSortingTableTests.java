package rx.admin.publishers;

import api.dto.rx.admin.publisher.Publisher;
import api.preconditionbuilders.PublisherPrecondition;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.admin.publisher.PublishersPage;
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
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature("Publishers")
@Epic("Waiting for separate QA env")
public class PublisherSortingTableTests extends BaseTest {

    private int totalPublishers;
    private List<String> sortIdsByAsc;
    private List<String> sortIdsByDesc;
    private List<String> sortPublisherNameByDesc;
    private List<String> sortPublisherNameByAsc;
    private List<String> sortPublisherMailByAsc;
    private List<String> sortPublisherMailByDesc;
    private PublishersPage publishersPage;

    private static final String ASC = "ascending";
    private static final String DESC = "descending";

    public PublisherSortingTableTests() {
        publishersPage = new PublishersPage();
    }

    @BeforeClass
    private void loginAndCreateExpectedResults() {

        if (getTotalPublishers() < 60) generatePublishers();

        totalPublishers = getTotalPublishers();

        sortIdsByAsc = getIdsByAsc();
        sortIdsByDesc = getIdsByDesc();

        sortPublisherNameByAsc = getPublisherNameByAsc();
        sortPublisherNameByDesc = getPublisherNameByDesc();

        sortPublisherMailByAsc = getMailByAsc();
        sortPublisherMailByDesc = getMailByDesc();

    }

    @Test(testName = "Sorting 'ID' column by descending", alwaysRun = true)
    public void publisherSortingByIdDesc() {
        sortByDescColumnByName(ColumnNames.ID);
        validateSortData(ColumnNames.ID, DESC, sortIdsByDesc);
    }

    @Test(testName = "Sorting 'ID' column by ascending", alwaysRun = true)
    public void publisherSortingByIdAsc() {

        sortByAscColumnByName(ColumnNames.ID);
        validateSortData(ColumnNames.ID, ASC, sortIdsByAsc);
    }

    @Test(testName = "Sorting 'Name' column by ascending", alwaysRun = true)
    public void publisherSortingByPublisherNameAsc() {
        sortByAscColumnByName(ColumnNames.NAME);
        validateSortData(ColumnNames.NAME, ASC, sortPublisherNameByAsc);
    }

    @Test(testName = "Sorting 'Name' column by descending", alwaysRun = true)
    public void publisherSortingByPublisherNameDesc() {
        sortByDescColumnByName(ColumnNames.NAME);
        validateSortData(ColumnNames.NAME, DESC, sortPublisherNameByDesc);
    }

    @Test(testName = "Sorting 'Publisher Mail' column by ascending", alwaysRun = true)
    public void publisherSortingByMailAsc() {
        sortByAscColumnByName(ColumnNames.MAIL);
        validateSortData(ColumnNames.MAIL, ASC, sortPublisherMailByAsc);
    }

    @Test(testName = "Sorting 'Publisher Mail' column by descending", alwaysRun = true)
    public void publisherSortingByMailDesc() {
        sortByDescColumnByName(ColumnNames.MAIL);
        validateSortData(ColumnNames.MAIL, DESC, sortPublisherMailByDesc);
    }

    @BeforeMethod
    private void login() {
        var table = publishersPage.getTable().getShowHideColumns();
        var tableData = publishersPage.getTable().getTableData();
        testStart()
                .given()
                .openDirectPath(Path.PUBLISHER)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, publishersPage.getNuxtProgress())
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
        var tableData = publishersPage.getTable().getTableData();

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
                .waitAndValidate(disappear, publishersPage.getNuxtProgress())
                .testEnd();

    }

    @Step("Sort column {0} by ASC")
    private void sortByAscColumnByName(ColumnNames columnName) {
        var tableData = publishersPage.getTable().getTableData();
        testStart()
                .given()
                .and(String.format("Sort column '%s'", columnName))
                .clickOnWebElement(tableData.getColumnHeader(columnName.getName()))
                .then("Ensure that sort by ascending: validate column attribute value")
                .validateAttribute(tableData.getColumnHeader(columnName.getName()),
                        "aria-sort", ASC)
                .waitAndValidate(disappear, publishersPage.getNuxtProgress())
                .testEnd();

    }

    @Step("Validate data in column {0} sorted by {1}")
    private void validateSortData(ColumnNames columnName, String sortType, List<String> expectedResultList) {
        var tableData = publishersPage.getTable().getTableData();
        var tablePagination = publishersPage.getTable().getTablePagination();
        //Todo Add checking of total quantity in pagination test when
        // https://rakutenadvertising.atlassian.net/browse/GS-3280 will be ready
        testStart()
                .given()
                .waitAndValidate(disappear, publishersPage.getNuxtProgress())
                .and("Select 50 row per page")
                .scrollIntoView(tablePagination.getPageMenu())
                .selectFromDropdown(tablePagination.getPageMenu(),
                        tablePagination.getRowNumbersList(), "50")
                .waitLoading(visible, publishersPage.getTableProgressBar())
                .waitLoading(disappear, publishersPage.getTableProgressBar())
                .then(String.format("Validate that text in table footer '1-50 of %s'",
                        totalPublishers))
                .validateContainsText(tablePagination.getPaginationPanel(),
                        String.format("1-50 of %s", totalPublishers))
                .then(String.format("Validate data in column '%s' should be sorted by %ss",
                        columnName.getName(), sortType))
                .validateList(tableData.getCustomCells(columnName),
                        expectedResultList.subList(0, 50))
                .and("Check next page")
                .clickOnWebElement(tablePagination.getNext())
                .waitLoading(visible, publishersPage.getTableProgressBar())
                .waitLoading(disappear, publishersPage.getTableProgressBar())
                .then(String.format("Validate that text in table footer '51-%s of %s'",
                        Math.min(100, totalPublishers), totalPublishers))
                .then(String.format("Validate data in column '%s' should be sorted by %s", columnName.getName(), sortType))
                .validateList(tableData.getCustomCells(columnName),
                        expectedResultList.subList(50, Math.min(100, totalPublishers)))

                .testEnd();
    }

    private int getTotalPublishers() {

        return PublisherPrecondition.publisher()
                .getPublishersList()
                .build()
                .getPublisherGetAllResponse()
                .getTotal();
    }

    private List<String> getIdsByAsc() {

        return getAllItemsByParams("id-asc").stream()
                .map(Publisher::getId)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    private List<String> getIdsByDesc() {

        return getAllItemsByParams("id-desc").stream()
                .map(Publisher::getId)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    private List<String> getMailByAsc() {

        return getAllItemsByParams("mail-asc").stream()
                .map(Publisher::getMail)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    private List<String> getMailByDesc() {

        return getAllItemsByParams("mail-desc").stream()
                .map(Publisher::getMail)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    private List<String> getPublisherNameByAsc() {

        return getAllItemsByParams("name-asc").stream()
                .map(Publisher::getName)
                .collect(Collectors.toList());
    }

    private List<String> getPublisherNameByDesc() {

        return getAllItemsByParams("name-desc").stream()
                .map(Publisher::getName)
                .collect(Collectors.toList());
    }

    private List<Publisher> getAllItemsByParams(String strParams) {
        Map<String, Object> queryParams = new HashMap();
        queryParams.put("sort", strParams);

        return PublisherPrecondition.publisher()
                .getPublishersListWithFilter(Map.of("sort", strParams))
                .build()
                .getPublisherGetAllResponse()
                .getItems();
    }

    private void generatePublishers() {
        while (getTotalPublishers() < 60) {
            PublisherPrecondition.publisher()
                    .createNewPublisher(captionWithSuffix("auto"))
                    .build();
        }
    }
}
