package rx.inventory.adspot;

import api.dto.rx.adsize.AdSize;
import api.dto.rx.inventory.adspot.AdSpot;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.inventory.adspots.AdSpotsPage;
import rx.BaseTest;
import widgets.common.table.ColumnNames;
import widgets.inventory.adSpots.sidebar.EditAdSpotSidebar;

import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static api.preconditionbuilders.AdSizePrecondition.adSize;
import static api.preconditionbuilders.AdSpotPrecondition.adSpot;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static java.lang.String.format;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature("Ad Spots")
public class AdSpotSearchTableTests extends BaseTest {

    private AdSpotsPage adSpotsPage;
    private List<AdSpot> allAdSpotsList;

    public AdSpotSearchTableTests() {
        adSpotsPage = new AdSpotsPage();
    }

    @BeforeClass
    public void createExpectedResults() {

        allAdSpotsList = adSpot()
                .getAllAdSpotsList()
                .build()
                .getAdSpotsGetAllResponse()
                .getItems();
    }

    @Test(testName = "Search by 'Ad Spot Name'", priority = 1)
    public void adSpotsSearchByAdSpotName() {
        var tableData = adSpotsPage.getAdSpotsTable().getTableData();
        var adSpotSearchValue = allAdSpotsList.get(0).getName().substring(0,5);
        List<String> expectedList = getAllItemsByParams(Map.of("search", adSpotSearchValue))
                .stream()
                .map(AdSpot::getName)
                .collect(Collectors.toList());

        testStart()
                .given()
                .waitAndValidate(disappear, adSpotsPage.getNuxtProgress())
                .setValueWithClean(tableData.getSearch(), adSpotSearchValue)
                .waitAndValidate(disappear, adSpotsPage.getTableProgressBar())
                .and("Sort column 'Ad spot Name'")
                .clickOnWebElement(tableData.getColumnHeader(ColumnNames.NAME.getName()))
                .validateList(tableData.getCustomCells(ColumnNames.NAME), expectedList)
                .clickOnWebElement(tableData.getClear())
                .and("End Test")
                .testEnd();
    }

    @Test(testName = "Search by 'Ad Spot Name' and 'Status' active ", priority = 2)
    public void adSpotsSearchByAdSpotNameAndStatusActive() {
        var filter = adSpotsPage.getAdSpotsTable().getColumnFiltersBlock();
        var tableData = adSpotsPage.getAdSpotsTable().getTableData();

        var activeAdSpotsList = getAllItemsByParams(Map.of("active", "true"));

        var adSpotSearchValue = activeAdSpotsList.get(0).getName().substring(0,3);
        List<String> expectedList = getAllItemsByParams(Map.of("offset", "0",  "sort","created_at-desc",
                "limit", 15, "name", adSpotSearchValue, "active", "true"))
                .stream()
                .map(AdSpot::getName)
                .collect(Collectors.toList());

        testStart()
                .given()
                .scrollIntoView(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu(),
                        adSpotsPage.getAdSpotsTable().getTablePagination().getRowNumbersList(), "15")
                .waitAndValidate(disappear, adSpotsPage.getNuxtProgress())
                .setValueWithClean(tableData.getSearch(), adSpotSearchValue)
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.STATUS))
                .and("Select Active option")
                .selectRadioButton(filter.getActiveBooleanFilter().getActiveRadioButton())
                .clickOnWebElement(filter.getSinglepaneFilter().getSubmitButton())
                .then("Check filtered data")
                .waitAndValidate(disappear, adSpotsPage.getTableProgressBar())
                .validateList(tableData.getCustomCells(ColumnNames.NAME), expectedList)
                .clickOnWebElement(tableData.getChipItemByPositionInList(0).getCloseIcon())
                .and("End Test")
                .testEnd();
    }

    @Test(testName = "Search by 'Ad Spot Name' and 'Status' inactive ", priority = 3)
    public void adSpotsSearchByAdSpotNameAndStatusInactive() {
        var filter = adSpotsPage.getAdSpotsTable().getColumnFiltersBlock();
        var tableData = adSpotsPage.getAdSpotsTable().getTableData();
        var inactiveAdSpotsList = getAllItemsByParams(Map.of("active", "false")).subList(0, 14);

        var adSpotSearchValue = inactiveAdSpotsList.get(0).getName().substring(0,3);
        List<String> expectedList = getAllItemsByParams(Map.of("sort","created_at-desc", "limit", 15,
                "name", adSpotSearchValue, "active", "false"))
                .stream()
                .map(AdSpot::getName)
                .collect(Collectors.toList());

        testStart()
                .given()
                .scrollIntoView(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu(),
                        adSpotsPage.getAdSpotsTable().getTablePagination().getRowNumbersList(), "15")
                .waitAndValidate(disappear, adSpotsPage.getNuxtProgress())
                .setValueWithClean(tableData.getSearch(), adSpotSearchValue)
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.STATUS))
                .and("Select Inactive option")
                .selectRadioButton(filter.getActiveBooleanFilter().getInactiveRadioButton())
                .clickOnWebElement(filter.getSinglepaneFilter().getSubmitButton())
                .then("Check filtered data")
                .waitAndValidate(disappear, adSpotsPage.getTableProgressBar())
                .validateList(tableData.getCustomCells(ColumnNames.NAME), expectedList)
                .clickOnWebElement(tableData.getChipItemByPositionInList(0).getCloseIcon())
                .and("End Test")
                .testEnd();
    }

    @Test(testName = "Search by 'Ad Spot Name' and 'Publisher' ", priority = 4)
    public void adSpotsSearchByAdSpotNameAndPublisher() {
        var tableData = adSpotsPage.getAdSpotsTable().getTableData();
        var filter = adSpotsPage.getAdSpotsTable().getColumnFiltersBlock();

        var adSpotSearchValue = allAdSpotsList.get(0).getName().substring(0,3);
        List<String> expectedList = getAllItemsByParams(Map.of("name", adSpotSearchValue, "publisher_id",
                allAdSpotsList.get(0).getPublisherId() ,"limit",15, "sort","created_at-desc"))
                .stream()
                .map(AdSpot::getName)
                .collect(Collectors.toList());

        testStart()
                .given()
                .waitAndValidate(disappear, adSpotsPage.getNuxtProgress())
                .scrollIntoView(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu(),
                        adSpotsPage.getAdSpotsTable().getTablePagination().getRowNumbersList(), "15")
                .setValueWithClean(tableData.getSearch(), adSpotSearchValue)
                .waitAndValidate(disappear, adSpotsPage.getTableProgressBar())
                .and("Select Publisher Filter")
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.PUBLISHER))
                .and(format("Search by Name '%s'", allAdSpotsList.get(0).getPublisherName()))
                .setValueWithClean(filter.getSinglepaneFilter().getSearchInput(), allAdSpotsList.get(0).getPublisherName())
                .clickOnWebElement(filter.getSinglepaneFilter().getFilterItemByName( allAdSpotsList.get(0).getPublisherName()).getName())
                .and("Click on Submit")
                .clickOnWebElement(filter.getSinglepaneFilter().getSubmitButton())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .validateList(tableData.getCustomCells(ColumnNames.NAME), expectedList)
                .clickOnWebElement(tableData.getChipItemByPositionInList(0).getCloseIcon())
                .and("End Test")
                .testEnd();
    }


    @Test(testName = "Search by 'Ad Spot Name' and 'Default Size' ", priority = 5)
    public void adSpotsSearchByAdSpotNameAndDefaultSize() {
        var tableData = adSpotsPage.getAdSpotsTable().getTableData();
        var filter = adSpotsPage.getAdSpotsTable().getColumnFiltersBlock();
        var adSpotSearchValue = allAdSpotsList.get(1).getName().substring(0,3);
        String defaultSizeFullValue = adSize()
                .getAllAdSizesList()
                .build()
                .getAdSizesGetAllResponse()
                .getItems()
                .stream().filter(e ->e.getId()==allAdSpotsList.get(1).getSizeIds().get(0))
                .map(AdSize::getName)
                .collect(Collectors.toList())
                .get(0);

        String defaultSizeValue = defaultSizeFullValue.substring(defaultSizeFullValue.indexOf("(")+1,defaultSizeFullValue.indexOf(")"));

        List<String> expectedList = getAllItemsByParams(Map.of("name", adSpotSearchValue, "size_id",
                allAdSpotsList.get(1).getSizeIds().get(0) ,"limit",15, "sort","created_at-desc"))
                .stream()
                .map(AdSpot::getName)
                .collect(Collectors.toList());

        testStart()
                .given()
                .scrollIntoView(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu(),
                        adSpotsPage.getAdSpotsTable().getTablePagination().getRowNumbersList(), "15")
                .waitAndValidate(disappear, adSpotsPage.getNuxtProgress())
                .setValueWithClean(tableData.getSearch(), adSpotSearchValue)
                .waitAndValidate(disappear, adSpotsPage.getTableProgressBar())
                .and("Select Ad Size Filter")
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.DEFAULT_SIZES))
                .pressEnterKey(filter.getSinglepaneFilter().getSearchInput())
                .and("Search by Default Size")
                .setValueWithClean(filter.getSinglepaneFilter().getSearchInput(), defaultSizeValue)
                .waitAndValidate(visible,filter.getSinglepaneFilter().getItemsIncludedQuantityLabel())
                .clickOnWebElement(filter.getSinglepaneFilter().getFilterItemByName(defaultSizeValue).getName())
                .and("Click on Submit")
                .clickOnWebElement(filter.getSinglepaneFilter().getSubmitButton())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .validateList(tableData.getCustomCells(ColumnNames.NAME), expectedList)
                .clickOnWebElement(tableData.getChipItemByPositionInList(0).getCloseIcon())
                .and("End Test")
                .testEnd();
    }

    @Test(testName = "Search by 'Ad Spot Name' and 'Related Media' ", priority = 6)
    public void adSpotsSearchByAdSpotNameAndRelatedMedia() {
        var tableData = adSpotsPage.getAdSpotsTable().getTableData();
        var filter = adSpotsPage.getAdSpotsTable().getColumnFiltersBlock();
        var adSpotSearchValue = allAdSpotsList.get(2).getName().substring(0,3);
        List<String> expectedList = getAllItemsByParams(Map.of("name", adSpotSearchValue, "media_id",
                allAdSpotsList.get(2).getMediaId(),"limit",15, "sort","created_at-desc"))
                .stream()
                .map(AdSpot::getName)
                .collect(Collectors.toList());

        testStart()
                .given()
                .scrollIntoView(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu(),
                        adSpotsPage.getAdSpotsTable().getTablePagination().getRowNumbersList(), "15")
                .waitAndValidate(disappear, adSpotsPage.getNuxtProgress())
                .setValueWithClean(tableData.getSearch(), adSpotSearchValue)
                .waitAndValidate(disappear, adSpotsPage.getTableProgressBar())
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.RELATED_MEDIA))
                .clickOnWebElement(filter.getSinglepaneFilter().getFilterItemByName(allAdSpotsList.get(2).getMediaName()).getName())
                .and("Click on Submit")
                .clickOnWebElement(filter.getSinglepaneFilter().getSubmitButton())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .validateList(tableData.getCustomCells(ColumnNames.NAME), expectedList)
                .clickOnWebElement(tableData.getChipItemByPositionInList(0).getCloseIcon())
                .and("End Test")
                .testEnd();
    }

    @Test(testName = "Search by 'Ad Spot Name' and 'Test Mode' Disabled ", priority = 7)
    public void adSpotsSearchByAdSpotNameAndTestModeDisabled() {
        var tableData = adSpotsPage.getAdSpotsTable().getTableData();
        var filter = adSpotsPage.getAdSpotsTable().getColumnFiltersBlock();
        var tableColumns = adSpotsPage.getAdSpotsTable().getShowHideColumns();
        var adSpotSearchValue = allAdSpotsList.get(2).getName();
        List<String> expectedList = getAllItemsByParams(Map.of("name", adSpotSearchValue, "test_mode",
                "false" ,"limit",15, "sort","created_at-desc"))
                .stream()
                .map(AdSpot::getName)
                .collect(Collectors.toList());

        testStart()
                .given()
                .scrollIntoView(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu(),
                        adSpotsPage.getAdSpotsTable().getTablePagination().getRowNumbersList(), "15")
                .waitAndValidate(disappear, adSpotsPage.getNuxtProgress())
                .setValueWithClean(tableData.getSearch(), adSpotSearchValue)
                .waitAndValidate(disappear, adSpotsPage.getTableProgressBar())
                .clickOnWebElement(tableColumns.getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getMenuItemCheckbox(ColumnNames.TEST_MODE))
                .clickOnWebElement(tableColumns.getShowHideColumnsBtn())
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.TEST_MODE))
                .and("Select Disabled option")
                .selectRadioButton(filter.getEnableBooleanFilter().getDisabledRadioButton())
                .and("Click on Submit")
                .clickOnWebElement(filter.getSinglepaneFilter().getSubmitButton())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .validateList(tableData.getCustomCells(ColumnNames.NAME), expectedList)
                .clickOnWebElement(tableData.getChipItemByPositionInList(0).getCloseIcon())
                .and("End Test")
                .testEnd();
    }

    @Test(testName = "Search by 'Ad Spot Name' and 'Test Mode' Enabled", priority = 8)
    public void adSpotsSearchByAdSpotNameAndTestModeEnabled() {
        var tableData = adSpotsPage.getAdSpotsTable().getTableData();
        var filter = adSpotsPage.getAdSpotsTable().getColumnFiltersBlock();
        var tableColumns = adSpotsPage.getAdSpotsTable().getShowHideColumns();
        var adSpotSearchValue = allAdSpotsList.get(2).getName().substring(0,1);
        List<String> expectedList = getAllItemsByParams(Map.of("name", adSpotSearchValue, "test_mode",
                "true" ,"limit",15, "sort","created_at-desc"))
                .stream()
                .map(AdSpot::getName)
                .collect(Collectors.toList());

        testStart()
                .given()
                .scrollIntoView(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu(),
                        adSpotsPage.getAdSpotsTable().getTablePagination().getRowNumbersList(), "15")
                .waitAndValidate(disappear, adSpotsPage.getNuxtProgress())
                .setValueWithClean(tableData.getSearch(), adSpotSearchValue)
                .waitAndValidate(disappear, adSpotsPage.getTableProgressBar())
                .clickOnWebElement(tableColumns.getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getMenuItemCheckbox(ColumnNames.TEST_MODE))
                .clickOnWebElement(tableColumns.getShowHideColumnsBtn())
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.TEST_MODE))
                .and("Select Enabled option")
                .selectRadioButton(filter.getEnableBooleanFilter().getEnabledRadioButton())
                .and("Click on Submit")
                .clickOnWebElement(filter.getSinglepaneFilter().getSubmitButton())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .validateList(tableData.getCustomCells(ColumnNames.NAME), expectedList)
                .clickOnWebElement(tableData.getChipItemByPositionInList(0).getCloseIcon())
                .and("End Test")
                .testEnd();
    }

    @Test(testName = "Search by 'Ad Spot Name' and 'Create date'", priority = 9)
    public void adSpotsSearchByAdSpotNameAndCreateDate() {
        var tableData = adSpotsPage.getAdSpotsTable().getTableData();
        var filter = adSpotsPage.getAdSpotsTable().getColumnFiltersBlock();
        var tableColumns = adSpotsPage.getAdSpotsTable().getShowHideColumns();
        var adSpotSearchValue = allAdSpotsList.get(2).getName().substring(0,1);
        var calendar = filter.getCalendarFilter().getCalendar();

        List<String> expectedList = getAllItemsByParams(Map.of("name", adSpotSearchValue, "created_date",
                allAdSpotsList.get(2).getCreatedAt().substring(0,10) ,"limit",15, "offset", 0,
                "sort","created_at-desc"))
                .stream()
                .map(AdSpot::getName)
                .collect(Collectors.toList());

        testStart()
                .given()
                .scrollIntoView(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu(),
                        adSpotsPage.getAdSpotsTable().getTablePagination().getRowNumbersList(), "15")
                .waitAndValidate(disappear, adSpotsPage.getNuxtProgress())
                .setValueWithClean(tableData.getSearch(), adSpotSearchValue)
                .clickOnWebElement(tableColumns.getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .clickOnWebElement(tableColumns.getShowHideColumnsBtn())
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.CREATED_DATE))
                .and("Select Period Date of the month")
                .clickOnWebElement(calendar.getMonthOrYearHeaderButton())
                .clickOnWebElement(calendar.getMonthOrYearHeaderButton())
                .clickOnWebElement(calendar.getYearButtonByValue(allAdSpotsList.get(2).getCreatedAt().substring(0,4)))
                .clickOnWebElement(calendar.getMonthButtonByValue(Month.of(Integer.parseInt(allAdSpotsList.get(2).getCreatedAt().substring(5,7))).getDisplayName(TextStyle.SHORT,Locale.US)))
                .clickOnWebElement(calendar.getDayButtonByValue(allAdSpotsList.get(2).getCreatedAt().substring(8,10).replaceFirst("0", "")))
                .and("Click on Submit")
                .clickOnWebElement(filter.getCalendarFilter().getSubmitButton())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .validateList(tableData.getCustomCells(ColumnNames.NAME), expectedList)
                .clickOnWebElement(tableData.getChipItemByPositionInList(0).getCloseIcon())
                .and("End Test")
                .testEnd();

    }

    @Test(testName = "Search by 'Ad Spot Name' and 'Updated date'", priority = 10)
    public void adSpotsSearchByAdSpotNameAndUpdatedDate() {
        var tableData = adSpotsPage.getAdSpotsTable().getTableData();
        var filter = adSpotsPage.getAdSpotsTable().getColumnFiltersBlock();
        var adSpotSearchValue = allAdSpotsList.get(2).getName().substring(0,1);
        var calendar = filter.getCalendarFilter().getCalendar();

        List<String> expectedList = getAllItemsByParams(Map.of("name", adSpotSearchValue, "updated_date",
                allAdSpotsList.get(2).getUpdatedAt().substring(0,10) ,"limit",15, "offset", 0,
                "sort","created_at-desc"))
                .stream()
                .map(AdSpot::getName)
                .collect(Collectors.toList());

        testStart()
                .given()
                .scrollIntoView(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu(),
                        adSpotsPage.getAdSpotsTable().getTablePagination().getRowNumbersList(), "15")
                .waitAndValidate(disappear, adSpotsPage.getNuxtProgress())
                .setValueWithClean(tableData.getSearch(), adSpotSearchValue)
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.UPDATED_DATE))
                .and("Select Period Date of the month")
                .clickOnWebElement(calendar.getMonthOrYearHeaderButton())
                .clickOnWebElement(calendar.getMonthOrYearHeaderButton())
                .clickOnWebElement(calendar.getYearButtonByValue(allAdSpotsList.get(2).getUpdatedAt().substring(0,4)))
                .clickOnWebElement(calendar.getMonthButtonByValue(Month.of(Integer.parseInt(allAdSpotsList.get(2).getUpdatedAt().substring(5,7))).getDisplayName(TextStyle.SHORT,Locale.US)))
                .clickOnWebElement(calendar.getDayButtonByValue(allAdSpotsList.get(2).getUpdatedAt().substring(8,10).replaceFirst("0", "")))
                .and("Click on Submit")
                .clickOnWebElement(filter.getCalendarFilter().getSubmitButton())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .validateList(tableData.getCustomCells(ColumnNames.NAME), expectedList)
                .clickOnWebElement(tableData.getChipItemByPositionInList(0).getCloseIcon())
                .and("End Test")
                .testEnd();
    }

    @BeforeMethod
    private void login() {
        testStart()
                .given()
                .openDirectPath(Path.AD_SPOT)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, adSpotsPage.getNuxtProgress())
                .testEnd();
    }

    @AfterMethod(alwaysRun = true)
    private void logOut() {
        testStart()
                .given()
                .logOut()
                .testEnd();
    }
    private List<AdSpot> getAllItemsByParams(Map<String, Object> strParams) {

        return adSpot()
                .getAdSpotsWithFilter(strParams)
                .build()
                .getAdSpotsGetAllResponse()
                .getItems();
    }
}