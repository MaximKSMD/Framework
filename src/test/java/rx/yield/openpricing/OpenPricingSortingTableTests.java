package rx.yield.openpricing;

import api.dto.rx.yield.openpricing.OpenPricing;
import api.preconditionbuilders.OpenPricingPrecondition;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.yield.openpricing.OpenPricingPage;
import rx.BaseTest;
import widgets.common.table.ColumnNames;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Open Pricing")
@Epic("Waiting for separate QA env")
@Link("https://rakutenadvertising.atlassian.net/browse/GS-3280")
public class OpenPricingSortingTableTests extends BaseTest {

    private int totalOpenPricing;

    private List<String> sortIdsByAsc;
    private List<String> sortIdsByDesc;
    private List<String> sortNamesByAsc;
    private List<String> sortNamesByDesc;

    private List<String> sortFloorPriceDesc;
    private List<String> sortFloorPriceAsc;
    private OpenPricingPage openPricingPage;
    private List<String> sortPublisherNameByAsc;
    private List<String> sortPublisherNameByDesc;
    private List<String> sortActiveInactiveByDesc;
    private List<String> sortActiveInactiveByAsc;

    private static final String ASC = "ascending";
    private static final String DESC = "descending";

    public OpenPricingSortingTableTests() {
        openPricingPage = new OpenPricingPage();
    }

    @BeforeClass
    public void loginAndCreateExpectedResults() {

        totalOpenPricing = OpenPricingPrecondition.openPricing()
                .getOpenPricingList()
                .build()
                .getOpenPricingGetAllResponse()
                .getTotal();

        sortNamesByDesc = getAllItemsByParams("name-desc").stream()
                .map(OpenPricing::getName)
                .collect(Collectors.toList());

        sortNamesByAsc = getAllItemsByParams("name-asc").stream()
                .map(OpenPricing::getName)
                .collect(Collectors.toList());

        sortIdsByAsc = getAllItemsByParams("id-asc").stream()
                .map(OpenPricing::getId)
                .map(Object::toString)
                .collect(Collectors.toList());

        sortIdsByDesc = getAllItemsByParams("id-desc").stream()
                .map(OpenPricing::getId)
                .map(Object::toString)
                .collect(Collectors.toList());

        sortActiveInactiveByAsc = getAllItemsByParams("active-asc").stream()
                .map(OpenPricing::getActive)
                .map(Object::toString)
                .map(elem -> elem.equals("true") ? "Active" : "Inactive")
                .collect(Collectors.toList());

        sortActiveInactiveByDesc = getAllItemsByParams("active-desc").stream()
                .map(OpenPricing::getActive)
                .map(Object::toString)
                .map(elem -> elem.equals("true") ? "Active" : "Inactive")
                .collect(Collectors.toList());

        sortFloorPriceAsc = getAllItemsByParams("floor_price-asc").stream()
                .map(OpenPricing::getFloorPrice)
                .map(this::getCustomFormat)
                .collect(Collectors.toList());

        sortFloorPriceDesc = getAllItemsByParams("floor_price-desc").stream()
                .map(OpenPricing::getFloorPrice)
                .map(this::getCustomFormat)
                .collect(Collectors.toList());

        sortPublisherNameByAsc = getAllItemsByParams("publisher_name-asc").stream()
                .map(OpenPricing::getPublisherName)
                .collect(Collectors.toList());

        sortPublisherNameByDesc = getAllItemsByParams("publisher_name-desc").stream()
                .map(OpenPricing::getPublisherName)
                .collect(Collectors.toList());
    }

    @BeforeMethod
    private void login() {

        testStart()
                .given()
                .openDirectPath(Path.OPEN_PRICING)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                .testEnd();
    }

    @AfterMethod(alwaysRun = true)
    private void logOut() {

        testStart()
                .given()
                .logOut()
                .testEnd();
    }

    @Test(testName = "Sorting 'Name' column by descending")
    public void OpenPricingSortingByNameDesc() {
        sortByDescColumnByName(ColumnNames.NAME);
        validateSorting(ColumnNames.NAME, DESC, sortNamesByDesc);
    }

    @Test(testName = "Sorting 'Name' column by ascending")
    public void OpenPricingSortingByNameAsc() {
        sortByAscColumnByName(ColumnNames.NAME);
        validateSorting(ColumnNames.NAME, ASC, sortNamesByAsc);
    }

    @Test(testName = "Sorting 'Publisher' column by descending")
    public void OpenPricingSortingByPublisherNameDesc() {
        sortByDescColumnByName(ColumnNames.PUBLISHER);
        validateSorting(ColumnNames.PUBLISHER, DESC, sortPublisherNameByDesc);
    }


    @Test(testName = "Sorting 'Publisher' column by ascending")
    public void OpenPricingSortingByPublisherNameAsc() {
        sortByAscColumnByName(ColumnNames.PUBLISHER);
        validateSorting(ColumnNames.PUBLISHER, ASC, sortPublisherNameByAsc);
    }

    @Test(testName = "Sorting 'ID' column by descending")
    public void OpenPricingSortingByIdDesc() {
        sortByDescColumnByName(ColumnNames.ID);
        validateSorting(ColumnNames.ID, DESC, sortIdsByDesc);
    }

    @Test(testName = "Sorting 'ID' column by ascending")
    public void OpenPricingSortingByIdAsc() {
        sortByAscColumnByName(ColumnNames.ID);
        validateSorting(ColumnNames.ID, ASC, sortIdsByAsc);
    }

    @Test(testName = "Sorting 'Active/Inactive' column by descending")
    public void OpenPricingSortingByActiveInactiveDesc() {
        sortByDescColumnByName(ColumnNames.STATUS);
        validateSorting(ColumnNames.STATUS, DESC, sortActiveInactiveByDesc);
    }

    @Test(testName = "Sorting 'Active/Inactive' column by ascending")
    public void OpenPricingSortingByActiveInactiveAsc() {
        sortByAscColumnByName(ColumnNames.STATUS);
        validateSorting(ColumnNames.STATUS, ASC, sortActiveInactiveByAsc);
    }

    @Test(testName = "Sorting 'Floor Price' column by descending")
    public void OpenPricingSortingByFloorPriceDesc() {
        sortByDescColumnByName(ColumnNames.FLOOR_PRICE);
        validateSorting(ColumnNames.FLOOR_PRICE, DESC, sortFloorPriceDesc);
    }

    @Test(testName = "Sorting 'Floor Price' column by ascending")
    public void OpenPricingSortingByFloorPriceAsc() {
        sortByAscColumnByName(ColumnNames.FLOOR_PRICE);
        validateSorting(ColumnNames.FLOOR_PRICE, ASC, sortFloorPriceAsc);
    }

    private void validateSorting(ColumnNames columnName, String sortType, List<String> expectedSortedList) {
        var tableData = openPricingPage.getOpenPricingTable().getTableData();
        var tablePagination = openPricingPage.getOpenPricingTable().getTablePagination();
        //Todo Add checking of total qauntity in pagination test when
        // https://rakutenadvertising.atlassian.net/browse/GS-3280 will be ready
        testStart()
                .given()
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                .and("Select 50 row per page")
                .selectFromDropdown(tablePagination.getPageMenu(),
                        tablePagination.getRowNumbersList(), "50")
                .waitLoading(visible, openPricingPage.getTableProgressBar())
                .waitLoading(disappear, openPricingPage.getTableProgressBar())
                .then("Validate that text in table footer '1-50")
                .validateContainsText(tablePagination.getPaginationPanel(), "1-50 of")
                .then(String.format("Validate data in column '%s' should be sorted by %s", columnName.getName(), sortType))
                .validateList(tableData.getCustomCells(columnName), expectedSortedList.subList(0, 50))
                .and("Check next page")
                .clickOnWebElement(tablePagination.getNext())
                .waitLoading(visible, openPricingPage.getTableProgressBar())
                .waitLoading(disappear, openPricingPage.getTableProgressBar())
                .then(String.format("Validate that text in table footer '51-%s of %s'",
                        Math.min(100, totalOpenPricing), totalOpenPricing))
                .validateContainsText(tablePagination.getPaginationPanel(),
                        String.format("51-%s of", Math.min(100, totalOpenPricing)))
                .then(String.format("Validate data in column '%s' should be sorted by %s", columnName.getName(), sortType))
                .validateList(tableData.getCustomCells(columnName),
                        expectedSortedList.subList(50, Math.min(100, totalOpenPricing)))
                .and("Test Finished")
                .testEnd();
    }

    @Step("Sort column {0} by DESC")
    private void sortByDescColumnByName(ColumnNames columnName) {
        var tableData = openPricingPage.getOpenPricingTable().getTableData();

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
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                .testEnd();
    }

    @Step("Sort column {0} by ASC")
    private void sortByAscColumnByName(ColumnNames columnName) {
        var tableData = openPricingPage.getOpenPricingTable().getTableData();
        testStart()
                .given()
                .and(String.format("Sort column '%s'", columnName))
                .clickOnWebElement(tableData.getColumnHeader(columnName.getName()))
                .then("Ensure that sort by ascending: validate column attribute value")
                .validateAttribute(tableData.getColumnHeader(columnName.getName()),
                        "aria-sort", ASC)
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                .testEnd();
    }

    private List<OpenPricing> getAllItemsByParams(String strParams) {

        return OpenPricingPrecondition.openPricing()
                .getOpenPricingWithFilter(Map.of("sort", strParams))
                .build()
                .getOpenPricingGetAllResponse()
                .getItems();
    }

    private String getCustomFormat(Double value) {

        return new DecimalFormat("###,###.###").format(value);
    }

}