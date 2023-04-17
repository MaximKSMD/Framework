package rx.sales.deals;

import api.dto.rx.sales.deals.Deal;
import api.preconditionbuilders.DealPrecondition;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.sales.deals.DealsPage;
import rx.BaseTest;
import widgets.common.table.ColumnNames;
import zutils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature("Deals")
public class DealsSortingTableTests extends BaseTest {

    private int totalDeals;
    private List<String> sortIdsByAsc;
    private List<String> sortIdsByDesc;
    private List<String> sortNamesByAsc;
    private List<String> sortNamesByDesc;
    private List<String> sortPublisherNameByAsc;
    private List<String> sortPublisherNameByDesc;
    private List<String> sortPrivateAuctionByAsc;
    private List<String> sortPrivateAuctionByDesc;
    private List<String> sortStatusByAsc;
    private List<String> sortStatusByDesc;
    private List<String> sortDSPByAsc;
    private List<String> sortDSPByDesc;
    private List<String> sortPriceValueByAsc;
    private List<String> sortPriceValueByDesc;
    private List<String> sortPriceCurrencyByAsc;
    private List<String> sortPriceCurrencyByDesc;
    private List<String> sortAlwaysOnByAsc;
    private List<String> sortAlwaysOnByDesc;
    private List<String> sortStartDateByAsc;
    private List<String> sortStartDateByDesc;
    private List<String> sortEndDateByAsc;
    private List<String> sortEndDateByDesc;
    private List<String> sortCreatedDateByAsc;
    private List<String> sortCreatedDateByDesc;
    private List<String> sortCreatedByByAsc;
    private List<String> sortCreatedByByDesc;
    private List<String> sortUpdatedDateByAsc;
    private List<String> sortUpdatedDateByDesc;
    private List<String> sortUpdatedByByAsc;
    private List<String> sortUpdatedByByDesc;

    private DealsPage dealsPage;

    private static final String ASC = "ascending";
    private static final String DESC = "descending";

    private final SimpleDateFormat incomingSdf = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat outgoingSdf = new SimpleDateFormat("MMM d yyyy");

    public DealsSortingTableTests() {
        dealsPage = new DealsPage();
    }

    @BeforeClass
    private void loginAndCreateExpectedResults() {

        if (getTotalDeals() < 60) generateDeals();

        totalDeals = getTotalDeals();

        sortIdsByAsc = getIdsByAsc();
        sortIdsByDesc = getIdsByDesc();

        sortNamesByAsc = getNamesByAsc();
        sortNamesByDesc = getNamesByDesc();

        sortPublisherNameByAsc = getPublisherNameByAsc();
        sortPublisherNameByDesc = getPublisherNameByDesc();

        sortPrivateAuctionByAsc = getPrivateAuctionByAsc();
        sortPrivateAuctionByDesc = getPrivateAuctionByDesc();

        sortStatusByAsc = getStatusByAsc();
        sortStatusByDesc = getStatusByDesc();

        sortDSPByAsc = getDSPByAsc();
        sortDSPByDesc = getDSPByDesc();

        sortPriceValueByAsc = getPriceValueByAsc();
        sortPriceValueByDesc = getPriceValueByDesc();

        sortPriceCurrencyByAsc = getPriceCurrencyByAsc();
        sortPriceCurrencyByDesc = getPriceCurrencyByDesc();

        sortAlwaysOnByAsc = getAlwaysOnByAsc();
        sortAlwaysOnByDesc = getAlwaysOnByDesc();

        sortStartDateByAsc = getStartDateByAsc();
        sortStartDateByDesc = getStartDateByDesc();

        sortEndDateByAsc = getEndDateByAsc();
        sortEndDateByDesc = getEndDateByDesc();

        sortCreatedDateByAsc = getCreatedAtByAsc();
        sortCreatedDateByDesc = getCreatedAtByDesc();

        sortCreatedByByAsc = getCreatedByByAsc();
        sortCreatedByByDesc = getCreatedByByDesc();

        sortUpdatedDateByAsc = getUpdatedAtByAsc();
        sortUpdatedDateByDesc = getUpdatedAtByDesc();

        sortUpdatedByByAsc = getUpdatedByByAsc();
        sortUpdatedByByDesc = getUpdatedByByDesc();
    }

    @BeforeMethod
    private void login() {
        var table = dealsPage.getDealsTable().getShowHideColumns();
        var tableData = dealsPage.getDealsTable().getTableData();
        testStart()
                .given()
                .openDirectPath(Path.DEALS)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, dealsPage.getNuxtProgress())
                .testEnd();
    }

    @Test(testName = "Sorting 'ID' column by ascending")
    public void adSpotSortingByIdAsc() {

        sortByAscColumnByName(ColumnNames.ID);
        validateSortData(ColumnNames.ID, ASC, sortIdsByAsc);
    }

    @Test(testName = "Sorting 'ID' column by descending")
    public void adSpotSortingByIdDesc() {
        sortByDescColumnByName(ColumnNames.ID);
        validateSortData(ColumnNames.ID, DESC, sortIdsByDesc);
    }

    @Test(testName = "Sorting 'Deal Name' column by ascending")
    public void adSpotSortingByDealNameAsc() {
        sortByAscColumnByName(ColumnNames.NAME);
        validateSortData(ColumnNames.NAME, ASC, sortNamesByAsc);
    }

    @Test(testName = "Sorting 'Deal Name' column by descending")
    public void adSpotSortingByDealNameDesc() {
        sortByDescColumnByName(ColumnNames.NAME);
        validateSortData(ColumnNames.NAME, DESC, sortNamesByDesc);
    }

    @Test(testName = "Sorting 'Publisher' column by ascending")
    public void adSpotSortingByPublisherNameAsc() {
        sortByAscColumnByName(ColumnNames.PUBLISHER);
        validateSortData(ColumnNames.PUBLISHER, ASC, sortPublisherNameByAsc);
    }

    @Test(testName = "Sorting 'Publisher' column by descending")
    public void adSpotSortingByPublisherNameDesc() {
        sortByDescColumnByName(ColumnNames.PUBLISHER);
        validateSortData(ColumnNames.PUBLISHER, DESC, sortPublisherNameByDesc);
    }

    @Test(testName = "Sorting 'Active/Inactive' column by ascending")
    public void adSpotSortingByStatusAsc() {
        sortByAscColumnByName(ColumnNames.STATUS);
        validateSortData(ColumnNames.STATUS, ASC, sortStatusByAsc);
    }

    @Test(testName = "Sorting 'Active/Inactive' column by descending")
    public void adSpotSortingByStatusDesc() {
        sortByDescColumnByName(ColumnNames.STATUS);
        validateSortData(ColumnNames.STATUS, DESC, sortStatusByDesc);
    }

    @Test(testName = "Sorting 'PrivateAuction' column by ascending")
    public void adSpotSortingByPrivateAuctionAsc() {
        sortByAscColumnByName(ColumnNames.PRIVATE_AUCTION);
        validateSortData(ColumnNames.PRIVATE_AUCTION, ASC, sortPrivateAuctionByAsc);
    }

    @Test(testName = "Sorting 'PrivateAuction' column by descending")
    public void adSpotSortingByPrivateAuctionDesc() {
        sortByDescColumnByName(ColumnNames.PRIVATE_AUCTION);
        validateSortData(ColumnNames.PRIVATE_AUCTION, DESC, sortPrivateAuctionByDesc);
    }

    @Test(testName = "Sorting 'DSP' column by ascending")
    public void adSpotSortingByDSPAsc() {
        sortByAscColumnByName(ColumnNames.DSP);
        validateSortData(ColumnNames.DSP, ASC, sortDSPByAsc);
    }

    @Test(testName = "Sorting 'DSP' column by descending")
    public void adSpotSortingByDSPDesc() {
        sortByDescColumnByName(ColumnNames.DSP);
        validateSortData(ColumnNames.DSP, DESC, sortDSPByDesc);
    }

    @Issue("GS-3288")
    @Test(testName = "Sorting 'Price/Value' column by ascending")
    public void adSpotSortingByPriceValueAsc() {
        sortByAscColumnByName(ColumnNames.PRICE_VALUE);
        validateSortData(ColumnNames.PRICE_VALUE, ASC, sortPriceValueByAsc);
    }

    @Issue("GS-3288")
    @Test(testName = "Sorting 'Price/Value' column by descending")
    public void adSpotSortingByPriceValueDesc() {
        sortByDescColumnByName(ColumnNames.PRICE_VALUE);
        validateSortData(ColumnNames.PRICE_VALUE, DESC, sortPriceValueByDesc);
    }

    @Test(testName = "Sorting 'Price/Currency' column by ascending")
    public void adSpotSortingByPriceCurrencyAsc() {
        sortByAscColumnByName(ColumnNames.PRICE_CURRENCY);
        validateSortData(ColumnNames.PRICE_CURRENCY, ASC, sortPriceCurrencyByAsc);
    }

    @Test(testName = "Sorting 'Price/Currency' column by descending")
    public void adSpotSortingByPriceCurrencyDesc() {
        sortByDescColumnByName(ColumnNames.PRICE_CURRENCY);
        validateSortData(ColumnNames.PRICE_CURRENCY, DESC, sortPriceCurrencyByDesc);
    }

    @Test(testName = "Sorting 'AlwaysOn' column by ascending")
    public void adSpotSortingByAlwaysOnAsc() {
        sortByAscColumnByName(ColumnNames.ALWAYS_ON);
        validateSortData(ColumnNames.ALWAYS_ON, ASC, sortAlwaysOnByAsc);
    }

    @Test(testName = "Sorting 'AlwaysOn' column by descending")
    public void adSpotSortingByAlwaysOnDesc() {
        sortByDescColumnByName(ColumnNames.ALWAYS_ON);
        validateSortData(ColumnNames.ALWAYS_ON, DESC, sortAlwaysOnByDesc);
    }

    @Test(testName = "Sorting 'StartDate' column by ascending")
    public void adSpotSortingByStartDateAsc() {
        sortByAscColumnByName(ColumnNames.START_DATE);
        validateSortData(ColumnNames.START_DATE, ASC, sortStartDateByAsc);
    }

    @Test(testName = "Sorting 'StartDate' column by descending")
    public void adSpotSortingByStartDateDesc() {
        sortByDescColumnByName(ColumnNames.START_DATE);
        validateSortData(ColumnNames.START_DATE, DESC, sortStartDateByDesc);
    }

    @Test(testName = "Sorting 'EndDate' column by ascending")
    public void adSpotSortingByEndDateAsc() {
        sortByAscColumnByName(ColumnNames.END_DATE);
        validateSortData(ColumnNames.END_DATE, ASC, sortEndDateByAsc);
    }

    @Test(testName = "Sorting 'EndDate' column by descending")
    public void adSpotSortingByEndDateDesc() {
        sortByDescColumnByName(ColumnNames.END_DATE);
        validateSortData(ColumnNames.END_DATE, DESC, sortEndDateByDesc);
    }

    @Test(testName = "Sorting 'CreatedDate' column by ascending")
    public void adSpotSortingByCreatedDateAsc() {
        sortByAscColumnByName(ColumnNames.CREATED_DATE);
        validateSortData(ColumnNames.CREATED_DATE, ASC, sortCreatedDateByAsc);
    }

    @Test(testName = "Sorting 'CreatedDate' column by descending")
    public void adSpotSortingByCreatedDateDesc() {
        sortByDescColumnByName(ColumnNames.CREATED_DATE);
        validateSortData(ColumnNames.CREATED_DATE, DESC, sortCreatedDateByDesc);
    }

    @Test(testName = "Sorting 'CreatedBy' column by ascending")
    public void adSpotSortingByCreatedByAsc() {
        sortByAscColumnByName(ColumnNames.CREATED_BY);
        validateSortData(ColumnNames.CREATED_BY, ASC, sortCreatedByByAsc);
    }

    @Test(testName = "Sorting 'CreatedBy' column by descending")
    public void adSpotSortingByCreatedByDesc() {
        sortByDescColumnByName(ColumnNames.CREATED_BY);
        validateSortData(ColumnNames.CREATED_BY, DESC, sortCreatedByByDesc);
    }

    @Test(testName = "Sorting 'UpdateDate' column by ascending")
    public void adSpotSortingByUpdateDateAsc() {
        sortByAscColumnByName(ColumnNames.UPDATED_DATE);
        validateSortData(ColumnNames.UPDATED_DATE, ASC, sortUpdatedDateByAsc);
    }

    @Test(testName = "Sorting 'UpdateDate' column by descending")
    public void adSpotSortingByUpdateDateDesc() {
        sortByDescColumnByName(ColumnNames.UPDATED_DATE);
        validateSortData(ColumnNames.UPDATED_DATE, DESC, sortUpdatedDateByDesc);
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
        var tableData = dealsPage.getDealsTable().getTableData();
        var tableOptions = dealsPage.getDealsTable().getShowHideColumns();


        testStart()
                .given()
                .and("'Show' all columns")
                .scrollIntoView(tableOptions.getShowHideColumnsBtn())
                .clickOnWebElement(tableOptions.getShowHideColumnsBtn())
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.UPDATED_DATE))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .clickOnWebElement(tableOptions.getShowHideColumnsBtn())
                .and(String.format("Sort column '%s'", columnName))
                .clickOnWebElement(tableData.getColumnHeader(columnName.getName()))
                .testEnd();

        if (columnName.getName().equals("ID")) {
            testStart()
                    .clickOnWebElement(tableData.getColumnHeader(columnName.getName()))
                    .testEnd();
        }

        testStart()
                .then("Ensure that sort by descending: validate column attribute value")
                .validateAttribute(tableData.getColumnHeader(columnName.getName()),
                        "aria-sort", ASC)
                .clickOnWebElement(tableData.getColumnHeader(columnName.getName()))
                .validateAttribute(tableData.getColumnHeader(columnName.getName()),
                        "aria-sort", DESC)
                .waitAndValidate(disappear, dealsPage.getNuxtProgress())
                .testEnd();

    }

    @Step("Sort column {0} by ASC")
    private void sortByAscColumnByName(ColumnNames columnName) {
        var tableData = dealsPage.getDealsTable().getTableData();
        var tableOptions = dealsPage.getDealsTable().getShowHideColumns();

        testStart()
                .given()
                .and("'Show' all columns")
                .scrollIntoView(tableOptions.getShowHideColumnsBtn())
                .clickOnWebElement(tableOptions.getShowHideColumnsBtn())
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.UPDATED_DATE))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .clickOnWebElement(tableOptions.getShowHideColumnsBtn())
                .and(String.format("Sort column '%s'", columnName))
                .clickOnWebElement(tableData.getColumnHeader(columnName.getName()))
                .testEnd();

        if (columnName.getName().equals("ID")) {
            testStart()
                    .clickOnWebElement(tableData.getColumnHeader(columnName.getName()))
                    .testEnd();
        }

        testStart()
                .then("Ensure that sort by ascending: validate column attribute value")
                .validateAttribute(tableData.getColumnHeader(columnName.getName()),
                        "aria-sort", ASC)
                .waitAndValidate(disappear, dealsPage.getNuxtProgress())
                .testEnd();

    }

    @Step("Validate data in column {0} sorted by {1}")
    private void validateSortData(ColumnNames columnName, String sortType, List<String> expectedResultList) {
        var tableData = dealsPage.getDealsTable().getTableData();
        var tablePagination = dealsPage.getDealsTable().getTablePagination();
        var tableOptions = dealsPage.getDealsTable().getShowHideColumns();

        testStart()
                .given()
                .waitAndValidate(disappear, dealsPage.getNuxtProgress())
                .scrollIntoView(tableOptions.getShowHideColumnsBtn())
                .clickOnWebElement(tableOptions.getShowHideColumnsBtn())
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.UPDATED_DATE))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .clickOnWebElement(tableOptions.getShowHideColumnsBtn())
                .and("Select 50 row per page")
                .scrollIntoView(tablePagination.getPageMenu())
                .selectFromDropdown(tablePagination.getPageMenu(),
                        tablePagination.getRowNumbersList(), "50")
                .waitLoading(visible, dealsPage.getTableProgressBar())
                .waitLoading(disappear, dealsPage.getTableProgressBar())
                .then(String.format("Validate that text in table footer '1-50 of %s'",
                        totalDeals))
                .validateContainsText(tablePagination.getPaginationPanel(),
                        String.format("1-50 of %s", totalDeals))
                .then(String.format("Validate data in column '%s' should be sorted by '%s'",
                        columnName.getName(), sortType))
                .validateList(tableData.getCustomCells(columnName),
                        expectedResultList.subList(0, 50))
                .and("Check next page")
                .clickOnWebElement(tablePagination.getNext())
                .waitLoading(visible, dealsPage.getTableProgressBar())
                .waitLoading(disappear, dealsPage.getTableProgressBar())
                .then(String.format("Validate that text in table footer '51-%s of %s'",
                        Math.min(100, totalDeals), totalDeals))
                .validateContainsText(tablePagination.getPaginationPanel(),
                        String.format("51-%s of %s", Math.min(100, totalDeals), totalDeals))
                .then(String.format("Validate data in column '%s' should be sorted by %s", columnName.getName(), sortType))
                .validateList(tableData.getCustomCells(columnName),
                        expectedResultList.subList(50, Math.min(100, totalDeals)))
                .testEnd();
    }

    private int getTotalDeals() {

        return DealPrecondition.deal()
                .getAllDealsList()
                .build()
                .getDealGetAllResponse()
                .getTotal();
    }

    private List<String> getIdsByAsc() {

        return getAllItemsByParams("id-asc").stream()
                .map(Deal::getId)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    private List<String> getIdsByDesc() {

        return getAllItemsByParams("id-desc").stream()
                .map(Deal::getId)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    private List<String> getNamesByDesc() {

        return getAllItemsByParams("name-desc").stream()
                .map(Deal::getName)
                .collect(Collectors.toList());
    }

    private List<String> getNamesByAsc() {

        return getAllItemsByParams("name-asc").stream()
                .map(Deal::getName)
                .collect(Collectors.toList());
    }

    private List<String> getPublisherNameByAsc() {

        return getAllItemsByParams("publisher_name-asc").stream()
                .map(Deal::getPublisherName)
                .collect(Collectors.toList());
    }

    private List<String> getPublisherNameByDesc() {

        return getAllItemsByParams("publisher_name-desc").stream()
                .map(Deal::getPublisherName)
                .collect(Collectors.toList());
    }

    private List<String> getPrivateAuctionByAsc() {

        return getAllItemsByParams("private_auction_name-asc").stream()
                .map(Deal::getPrivateAuctionName)
                .collect(Collectors.toList());
    }

    private List<String> getPrivateAuctionByDesc() {

        return getAllItemsByParams("private_auction_name-desc").stream()
                .map(Deal::getPrivateAuctionName)
                .collect(Collectors.toList());
    }

    private List<String> getStatusByDesc() {

        return getAllItemsByParams("enabled-desc").stream()
                .map(Deal::getEnabled)
                .map(Object::toString)
                .map(s -> s.equals("true") ? "Active" : "Inactive")
                .collect(Collectors.toList());
    }

    private List<String> getStatusByAsc() {

        return getAllItemsByParams("enabled-asc").stream()
                .map(Deal::getEnabled)
                .map(Object::toString)
                .map(s -> s.equals("true") ? "Active" : "Inactive")
                .collect(Collectors.toList());
    }

    private List<String> getDSPByAsc() {

        return getAllItemsByParams("dsp_name-asc").stream()
                .map(Deal::getDspName)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    private List<String> getDSPByDesc() {

        return getAllItemsByParams("dsp_name-desc").stream()
                .map(Deal::getDspName)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    private List<String> getPriceValueByAsc() {

        return getAllItemsByParams("floor_price-asc").stream()
                .map(Deal::getFloorPrice)
                .map(Objects::toString)
                .collect(Collectors.toList());
    }

    private List<String> getPriceValueByDesc() {

        return getAllItemsByParams("floor_price-desc").stream()
                .map(Deal::getFloorPrice)
                .map(Objects::toString)
                .collect(Collectors.toList());
    }

    private List<String> getPriceCurrencyByAsc() {

        return getAllItemsByParams("currency-asc").stream()
                .map(Deal::getCurrency)
                .collect(Collectors.toList());

    }

    private List<String> getPriceCurrencyByDesc() {

        return getAllItemsByParams("currency-desc").stream()
                .map(Deal::getCurrency)
                .collect(Collectors.toList());
    }

    private List<String> getAlwaysOnByAsc() {

        return getAllItemsByParams("no_end_date-asc").stream()
                .map(Deal::getNoEndDate)
                .map(Object::toString)
                .map(s -> s.equals("true") ? "yes" : "no")
                .collect(Collectors.toList());
    }

    private List<String> getAlwaysOnByDesc() {

        return getAllItemsByParams("no_end_date-desc").stream()
                .map(Deal::getNoEndDate)
                .map(Object::toString)
                .map(s -> s.equals("true") ? "yes" : "no")
                .collect(Collectors.toList());
    }

    private List<String> getStartDateByAsc() {
        return getAllItemsByParams("start_date-asc").stream()
                .map(Deal::getCreatedAt)
                .map(StringUtils::parsedDate)
                .collect(Collectors.toList());
    }

    private List<String> getStartDateByDesc() {

        return getAllItemsByParams("start_date-desc").stream()
                .map(Deal::getCreatedAt)
                .map(StringUtils::parsedDate)
                .collect(Collectors.toList());
    }

    private List<String> getEndDateByAsc() {

        return getAllItemsByParams("end_date-asc").stream()
                .map(Deal::getCreatedAt)
                .map(StringUtils::parsedDate)
                .collect(Collectors.toList());
    }

    private List<String> getEndDateByDesc() {

        return getAllItemsByParams("end_date-desc").stream()
                .map(Deal::getCreatedAt)
                .map(StringUtils::parsedDate)
                .collect(Collectors.toList());
    }

    private List<String> getCreatedAtByAsc() {

        return getAllItemsByParams("created_at-asc").stream()
                .map(Deal::getCreatedAt)
                .map(StringUtils::parsedDate)
                .collect(Collectors.toList());
    }

    private List<String> getCreatedAtByDesc() {

        return getAllItemsByParams("created_at-desc").stream()
                .map(Deal::getCreatedAt)
                .map(StringUtils::parsedDate)
                .collect(Collectors.toList());
    }

    private List<String> getCreatedByByAsc() {

        return getAllItemsByParams("created_by-asc").stream()
                .map(Deal::getCreatedBy)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    private List<String> getCreatedByByDesc() {

        return getAllItemsByParams("created_by-desc").stream()
                .map(Deal::getCreatedBy)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    private List<String> getUpdatedAtByAsc() {

        return getAllItemsByParams("updated_at-asc").stream()
                .map(Deal::getCreatedAt)
                .map(StringUtils::parsedDate)
                .collect(Collectors.toList());
    }

    private List<String> getUpdatedAtByDesc() {

        return getAllItemsByParams("updated_by-asc").stream()
                .map(Deal::getCreatedAt)
                .map(StringUtils::parsedDate)
                .collect(Collectors.toList());
    }

    private List<String> getUpdatedByByAsc() {

        return getAllItemsByParams("updated_by-asc").stream()
                .map(Deal::getUpdatedBy)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    private List<String> getUpdatedByByDesc() {

        return getAllItemsByParams("updated_by-desc").stream()
                .map(Deal::getUpdatedBy)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    private List<Deal> getAllItemsByParams(String strParams) {

        return DealPrecondition.deal()
                .getDealWithFilter(Map.of("sort", strParams))
                .build()
                .getDealGetAllResponse()
                .getItems();
    }

    private void generateDeals() {
        while (getTotalDeals() < 60) {
            DealPrecondition.deal()
                    .createNewDeal()
                    .build();
        }
    }

}

