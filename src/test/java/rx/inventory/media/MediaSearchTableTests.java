package rx.inventory.media;

import api.dto.rx.inventory.media.Media;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.inventory.media.*;
import rx.BaseTest;
import widgets.common.table.ColumnNames;
import widgets.inventory.media.sidebar.EditMediaSidebar;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static api.preconditionbuilders.MediaPrecondition.media;

import static java.lang.String.format;

import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature("Media")
public class MediaSearchTableTests extends BaseTest {

    private MediaPage mediaPage;
    private EditMediaSidebar editMediaSidebar;
    private List<Media> allMediaList;

    public MediaSearchTableTests() {
        mediaPage = new MediaPage();
        editMediaSidebar = new EditMediaSidebar();
    }

    @BeforeClass
    public void loginAndCreateExpectedResults() {

        allMediaList = media()
                .getAllMediaList()
                .build()
                .getMediaGetAllResponse()
                .getItems();

        testStart()
                .given()
                .openDirectPath(Path.MEDIA)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, mediaPage.getNuxtProgress())
                .testEnd();
    }

    @BeforeMethod
    public void refreshPage(){
        testStart()
                .given()
                .clickBrowserRefreshButton()
                .testEnd();
    }

    @AfterClass
    private void logOut() {
        testStart()
                .given()
                .logOut()
                .testEnd();
    }

    @Test(testName = "Search by 'Media Name'", priority = 1)
    public void mediaSearchByMediaName() {
        var tableData = mediaPage.getMediaTable().getTableData();
        var mediaSearchValue = allMediaList.get(0).getName().substring(0,5);

        List<String> expectedList = getAllItemsByParams(Map.of("name", mediaSearchValue, "limit", 15))
                .stream()
                .map(Media::getName)
                .collect(Collectors.toList());

        testStart()
                .given()
                .waitAndValidate(disappear, mediaPage.getNuxtProgress())
                .setValueWithClean(tableData.getSearch(), mediaSearchValue)
                .waitAndValidate(disappear, mediaPage.getTableProgressBar())
                .and("Sort column 'Media Name'")
                .clickOnWebElement(tableData.getColumnHeader(ColumnNames.NAME.getName()))
                .validateList(tableData.getCustomCells(ColumnNames.NAME), expectedList)
                .and("End Test")
                .testEnd();
    }

    @Test(testName = "Search by 'Media Name' and 'Status' active ", priority = 2)
    public void mediaSearchByMediaNameAndStatusActive() {
        var filter = mediaPage.getMediaTable().getColumnFiltersBlock();
        var tableData = mediaPage.getMediaTable().getTableData();

        List<Media> activeMediaList = getAllItemsByParams(Map.of("active", "true"))
                .stream()
                .collect(Collectors.toList());

        var mediaSearchValue = activeMediaList.get(0).getName().substring(0,2);

        List<String> expectedList = getAllItemsByParams(Map.of("name", mediaSearchValue, "active", "true",
                "limit",15, "sort","created_at-desc"))
                .stream()
                .map(Media::getName)
                .collect(Collectors.toList());

        testStart()
                .given()
                .scrollIntoView(mediaPage.getMediaTable().getTablePagination().getPageMenu())
                .selectFromDropdown(mediaPage.getMediaTable().getTablePagination().getPageMenu(),
                        mediaPage.getMediaTable().getTablePagination().getRowNumbersList(), "15")
                .waitAndValidate(disappear, mediaPage.getNuxtProgress())
                .setValueWithClean(tableData.getSearch(), mediaSearchValue)
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.STATUS))
                .and("Select Active option")
                .selectRadioButton(filter.getActiveBooleanFilter().getActiveRadioButton())
                .clickOnWebElement(filter.getSinglepaneFilter().getSubmitButton())
                .then("Check filtered data")
                .waitAndValidate(disappear, mediaPage.getTableProgressBar())
                .validateList(tableData.getCustomCells(ColumnNames.NAME), expectedList)
                .and("End Test")
                .testEnd();
    }

    @Test(testName = "Search by 'Media Name' and 'Status' inactive ", priority = 3)
    public void mediaSearchByMediaNameAndStatusInactive() {
        var filter = mediaPage.getMediaTable().getColumnFiltersBlock();
        var tableData = mediaPage.getMediaTable().getTableData();

        List<Media> inactiveMediaList = getAllItemsByParams(Map.of("active", "false"))
                .stream()
                .collect(Collectors.toList());

        var mediaSearchValue = inactiveMediaList.get(0).getName().substring(0,2);

        List<String> expectedList = getAllItemsByParams(Map.of("name", mediaSearchValue, "active", "false",
                "limit",15, "sort","created_at-desc"))
                .stream()
                .map(Media::getName)
                .collect(Collectors.toList());

        testStart()
                .given()
                .scrollIntoView(mediaPage.getMediaTable().getTablePagination().getPageMenu())
                .selectFromDropdown(mediaPage.getMediaTable().getTablePagination().getPageMenu(),
                        mediaPage.getMediaTable().getTablePagination().getRowNumbersList(), "15")
                .waitAndValidate(disappear, mediaPage.getNuxtProgress())
                .setValueWithClean(tableData.getSearch(), mediaSearchValue)
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.STATUS))
                .and("Select Inactive option")
                .selectRadioButton(filter.getActiveBooleanFilter().getInactiveRadioButton())
                .clickOnWebElement(filter.getSinglepaneFilter().getSubmitButton())
                .then("Check filtered data")
                .waitAndValidate(disappear, mediaPage.getTableProgressBar())
                .validateList(tableData.getCustomCells(ColumnNames.NAME), expectedList)
                .and("End Test")
                .testEnd();
    }

    @Test(testName = "Search by 'Media Name' and 'Publisher' ", priority = 4)
    public void mediaSearchByMediaNameAndPublisher() {
        var tableData = mediaPage.getMediaTable().getTableData();
        var filter = mediaPage.getMediaTable().getColumnFiltersBlock();

        var mediaSearchValue = allMediaList.get(0).getName().substring(0,2);

        List<String> expectedList = getAllItemsByParams(Map.of("name", mediaSearchValue, "publisher_id",
                allMediaList.get(0).getPublisherId() ,"limit",15, "sort","created_at-desc"))
                .stream()
                .map(Media::getName)
                .collect(Collectors.toList());

        testStart()
                .given()
                .waitAndValidate(disappear, mediaPage.getNuxtProgress())
                .scrollIntoView(mediaPage.getMediaTable().getTablePagination().getPageMenu())
                .selectFromDropdown(mediaPage.getMediaTable().getTablePagination().getPageMenu(),
                        mediaPage.getMediaTable().getTablePagination().getRowNumbersList(), "15")
                .setValueWithClean(tableData.getSearch(), mediaSearchValue)
                .waitAndValidate(disappear, mediaPage.getTableProgressBar())
                .and("Select Publisher Filter")
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.PUBLISHER))
                .and(format("Search by Name '%s'", allMediaList.get(0).getPublisherName()))
                .setValueWithClean(filter.getSinglepaneFilter().getSearchInput(), allMediaList.get(0).getPublisherName())
                .clickOnWebElement(filter.getSinglepaneFilter().getFilterItemByName(allMediaList.get(0).getPublisherName()).getName())
                .and("Click on Submit")
                .clickOnWebElement(filter.getSinglepaneFilter().getSubmitButton())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .validateList(tableData.getCustomCells(ColumnNames.NAME), expectedList)
                .and("End Test")
                .testEnd();
    }

    @DataProvider (name = "platform-types")
    public Object[][] platformsDPMethod(){

        return new Object[][] {{"iOS", 0},
                {"Android", 1},
                {"Mobile Web", 2},
                {"PC Web", 3},
                {"iOS Web View", 4},
                {"Android Web View", 5},
                {"CTV", 6}};
    }

    @Test(testName = "Search by 'Media Name' and 'Platform'", priority = 5, dataProvider = "platform-types")
    public void mediaSearchByMediaNameAndPlatform(String platformType, int value) {

        var tableData = mediaPage.getMediaTable().getTableData();
        var filter = mediaPage.getMediaTable().getColumnFiltersBlock();

        List<Media> iOSMediaList = getAllItemsByParams(Map.of("platform_id", value))
                .stream()
                .collect(Collectors.toList());

        if (iOSMediaList.size() > 0) {

            var mediaSearchValue = iOSMediaList.get(0).getName().substring(0, 2);

            List<String> expectedList = getAllItemsByParams(Map.of("name", mediaSearchValue, "platform_id",
                    value, "limit", 15, "sort", "created_at-desc"))
                    .stream()
                    .map(Media::getName)
                    .collect(Collectors.toList());

            testStart()
                    .given()
                    .waitAndValidate(disappear, mediaPage.getNuxtProgress())
                    .scrollIntoView(mediaPage.getMediaTable().getTablePagination().getPageMenu())
                    .selectFromDropdown(mediaPage.getMediaTable().getTablePagination().getPageMenu(),
                            mediaPage.getMediaTable().getTablePagination().getRowNumbersList(), "15")
                    .setValueWithClean(tableData.getSearch(), mediaSearchValue)
                    .waitAndValidate(disappear, mediaPage.getTableProgressBar())
                    .and("Select Publisher Filter")
                    .clickOnWebElement(filter.getColumnsFilterButton())
                    .waitAndValidate(visible, filter.getFilterOptionsMenu())
                    .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.PLATFORM))
                    .and(format("Search by platform %s", platformType))
                    .selectCheckBox(filter.getPlatformFilter().getPlatformTypeCheckboxByName(platformType))
                    .and("Click on Submit")
                    .clickOnWebElement(filter.getSinglepaneFilter().getSubmitButton())
                    .then("ColumnsFilter widget is closed")
                    .validate(not(visible), filter.getFilterOptionsMenu())
                    .validateList(tableData.getCustomCells(ColumnNames.NAME), expectedList)
                    .and("End Test")
                    .testEnd();
        }
    }

    @Test(testName = "Search by 'Media Name' and 'Create date'", priority = 6)
    public void mediaSearchByMediaNameAndCreateDate() {
        var tableData = mediaPage.getMediaTable().getTableData();
        var filter = mediaPage.getMediaTable().getColumnFiltersBlock();
        var tableColumns = mediaPage.getMediaTable().getShowHideColumns();
        var calendar = filter.getCalendarFilter().getCalendar();
        ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("UTC"));

        List<Media> allMediaList = getAllItemsByParams(Map.of("sort","created_at-desc"))
                .stream()
                .collect(Collectors.toList());

        var mediaSearchValue = allMediaList.get(2).getName().substring(0,1);

        List<String> expectedList = getAllItemsByParams(Map.of("name", mediaSearchValue, "created_date",
                allMediaList.get(2).getCreatedAt().substring(0,10) ,"limit",15, "offset", 0,
                "sort","created_at-desc"))
                .stream()
                .map(Media::getName)
                .collect(Collectors.toList());

        testStart()
                .given()
                .scrollIntoView(mediaPage.getMediaTable().getTablePagination().getPageMenu())
                .selectFromDropdown(mediaPage.getMediaTable().getTablePagination().getPageMenu(),
                        mediaPage.getMediaTable().getTablePagination().getRowNumbersList(), "15")
                .waitAndValidate(disappear, mediaPage.getNuxtProgress())
                .setValueWithClean(tableData.getSearch(), mediaSearchValue)
                .clickOnWebElement(tableColumns.getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .clickOnWebElement(tableColumns.getShowHideColumnsBtn())
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.CREATED_DATE))
                .and("Select Period Date of the month")
                .clickOnWebElement(calendar.getMonthOrYearHeaderButton())
                .clickOnWebElement(calendar.getMonthOrYearHeaderButton())
                .clickOnWebElement(calendar.getYearButtonByValue(allMediaList.get(2).getCreatedAt().substring(0,4)))
                .clickOnWebElement(calendar.getMonthButtonByValue(Month.of(Integer.parseInt(allMediaList.get(2).getCreatedAt().substring(5,7))).getDisplayName(TextStyle.SHORT,Locale.US)))
                .clickOnWebElement(calendar.getDayButtonByValue(allMediaList.get(2).getCreatedAt().substring(8,10).replaceFirst("0", "")))
                .and("Click on Submit")
                .clickOnWebElement(filter.getCalendarFilter().getSubmitButton())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .validateList(tableData.getCustomCells(ColumnNames.NAME), expectedList)
                .and("End Test")
                .testEnd();
    }

    @Test(testName = "Search by 'Media Name' and 'Updated date'", priority = 7)
    public void mediaSearchByMediaNameAndUpdatedDate() {
        var tableData = mediaPage.getMediaTable().getTableData();
        var filter = mediaPage.getMediaTable().getColumnFiltersBlock();
        var calendar = filter.getCalendarFilter().getCalendar();
        ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("UTC"));

        List<Media> allMediaList = getAllItemsByParams(Map.of("sort","created_at-desc"))
                .stream()
                .collect(Collectors.toList());

        var mediaSearchValue = allMediaList.get(2).getName().substring(0,1);

        List<String> expectedList = getAllItemsByParams(Map.of("name", mediaSearchValue, "updated_date",
                allMediaList.get(2).getUpdatedAt().substring(0,10) ,"limit",15, "offset", 0,
                "sort","created_at-desc"))
                .stream()
                .map(Media::getName)
                .collect(Collectors.toList());

        testStart()
                .given()
                .scrollIntoView(mediaPage.getMediaTable().getTablePagination().getPageMenu())
                .selectFromDropdown(mediaPage.getMediaTable().getTablePagination().getPageMenu(),
                        mediaPage.getMediaTable().getTablePagination().getRowNumbersList(), "15")
                .waitAndValidate(disappear, mediaPage.getNuxtProgress())
                .setValueWithClean(tableData.getSearch(), mediaSearchValue)
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.UPDATED_DATE))
                .and("Select Period Date of the month")
                .clickOnWebElement(calendar.getMonthOrYearHeaderButton())
                .clickOnWebElement(calendar.getMonthOrYearHeaderButton())
                .clickOnWebElement(calendar.getYearButtonByValue(allMediaList.get(2).getUpdatedAt().substring(0,4)))
                .clickOnWebElement(calendar.getMonthButtonByValue(Month.of(Integer.parseInt(allMediaList.get(2).getUpdatedAt().substring(5,7))).getDisplayName(TextStyle.SHORT,Locale.US)))
                .clickOnWebElement(calendar.getDayButtonByValue(allMediaList.get(2).getUpdatedAt().substring(8,10).replaceFirst("0", "")))
                .and("Click on Submit")
                .clickOnWebElement(filter.getCalendarFilter().getSubmitButton())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .validateList(tableData.getCustomCells(ColumnNames.NAME), expectedList)
                .and("End Test")
                .testEnd();
    }

    private List<Media> getAllItemsByParams(Map strParams) {
        return media()
                .getMediaWithFilter(strParams)
                .build()
                .getMediaGetAllResponse()
                .getItems();
    }

}
