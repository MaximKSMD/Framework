package rx.inventory.adspot.columnsfilter;

import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Path;
import pages.inventory.adspots.AdSpotsPage;
import rx.BaseTest;
import widgets.common.adSizes.AdSizesList;
import widgets.common.table.ColumnNames;
import zutils.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static api.preconditionbuilders.MediaPrecondition.media;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static java.lang.String.format;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Adspot Columns Filter")
public class AdspotColumnsFilterWidgetTests extends BaseTest {

    private AdSpotsPage adSpotsPage;
    private List<String> selectedPublishersNameList;
    private List<String> selectedMediaList;

    public AdspotColumnsFilterWidgetTests() {
        adSpotsPage = new AdSpotsPage();
    }

    @BeforeClass
    private void login() {
        var tableColumns = adSpotsPage.getAdSpotsTable().getShowHideColumns();

        testStart()
                .given()
                .openDirectPath(Path.AD_SPOT)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, adSpotsPage.getNuxtProgress())
                .scrollIntoView(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu(),
                        adSpotsPage.getAdSpotsTable().getTablePagination().getRowNumbersList(), "10")
                .scrollIntoView(tableColumns.getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .selectCheckBox(tableColumns.getMenuItemCheckbox(ColumnNames.UPDATED_DATE))
                .testEnd();
    }

    @Test(description = "Check Active/Inactive Chip Widget Component", priority = 1)
    public void testActiveInactiveChipWidgetComponent() {
        var filter = adSpotsPage.getAdSpotsTable().getColumnFiltersBlock();
        var table = adSpotsPage.getAdSpotsTable().getTableData();

        testStart()
                .and("Click on 'Column Filters'")
                .scrollIntoView(adSpotsPage.getPageTitle())
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .and("Select Column Filter 'Active/Inactive'")
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.STATUS))
                .then("Title should be displayed")
                .validate(filter.getActiveBooleanFilter().getFilterHeaderLabel(), StringUtils.getFilterHeader(ColumnNames.STATUS.getName()))
                .then("All options should be unselected")
                .validateAttribute(filter.getActiveBooleanFilter().getActiveRadioButton(),"aria-checked","false")
                .validateAttribute(filter.getActiveBooleanFilter().getInactiveRadioButton(),"aria-checked","false")
                .and("Select Active")
                .selectRadioButton(filter.getActiveBooleanFilter().getActiveRadioButton())
                .validateAttribute(filter.getActiveBooleanFilter().getActiveRadioButton(),"aria-checked","true")
                .and("Select Inactive")
                .selectRadioButton(filter.getActiveBooleanFilter().getInactiveRadioButton())
                .then("Only one option should be selected")
                .validateAttribute(filter.getActiveBooleanFilter().getActiveRadioButton(),"aria-checked","false")
                .validateAttribute(filter.getActiveBooleanFilter().getInactiveRadioButton(),"aria-checked","true")
                .and("Click on Back")
                .clickOnWebElement(filter.getActiveBooleanFilter().getBackButton())
                .then("Columns Menu should appear")
                .validateList(filter.getFilterOptionItems(), List.of(ColumnNames.PUBLISHER.getName(),
                        ColumnNames.RELATED_MEDIA.getName(),
                        ColumnNames.STATUS.getName(),
                        ColumnNames.DEFAULT_SIZES.getName(),
                        ColumnNames.CREATED_DATE.getName(),
                        ColumnNames.UPDATED_DATE.getName()))
                .and("Select Column Filter 'Active/Inactive'")
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.STATUS))
                .then("All options should be reset and unselected")
                .validateAttribute(filter.getActiveBooleanFilter().getActiveRadioButton(),"aria-checked","false")
                .validateAttribute(filter.getActiveBooleanFilter().getInactiveRadioButton(),"aria-checked","false")
                .and("Select Inactive")
                .selectRadioButton(filter.getActiveBooleanFilter().getInactiveRadioButton())
                .validateAttribute(filter.getActiveBooleanFilter().getInactiveRadioButton(),"aria-checked","true")
                .and("Click on Submit")
                .clickOnWebElement(filter.getActiveBooleanFilter().getSubmitButton())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .validate(visible, table.getChipItemByName(ColumnNames.STATUS.getName()).getHeaderLabel())
                .validate(table.countFilterChipsItems(), 1)
                .then("Validate value on chip")
                .validate(table.getChipItemByName(ColumnNames.STATUS.getName()).getChipFilterOptionItemByName("Inactive"))
                .clickOnWebElement(table.getChipItemByName(ColumnNames.STATUS.getName()).getCloseIcon())
                .testEnd();
    }

    @Test(description = "Check Enabled/Disabled TestMode Chip Widget Component", priority = 2)
    public void testTestModeChipWidgetComponent() {
        var filter = adSpotsPage.getAdSpotsTable().getColumnFiltersBlock();
        var table = adSpotsPage.getAdSpotsTable().getTableData();
        var tableColumns = adSpotsPage.getAdSpotsTable();

        testStart()
                .and("Click on 'Show Hide Columns'")
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.TEST_MODE))
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .and("Select Column Filter 'Enabled/Disabled'")
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.TEST_MODE))
                .then("Title should be displayed")
                .validate(filter.getEnableBooleanFilter().getFilterHeaderLabel(), StringUtils.getFilterHeader(ColumnNames.TEST_MODE.getName()))
                .then("All options should be unselected")
                .validateAttribute(filter.getEnableBooleanFilter().getEnabledRadioButton(),"aria-checked","false")
                .validateAttribute(filter.getEnableBooleanFilter().getDisabledRadioButton(),"aria-checked","false")
                .and("Select Active")
                .selectRadioButton(filter.getEnableBooleanFilter().getEnabledRadioButton())
                .validateAttribute(filter.getEnableBooleanFilter().getEnabledRadioButton(),"aria-checked","true")
                .and("Select Inactive")
                .selectRadioButton(filter.getEnableBooleanFilter().getDisabledRadioButton())
                .then("Only one option should be selected")
                .validateAttribute(filter.getEnableBooleanFilter().getEnabledRadioButton(),"aria-checked","false")
                .validateAttribute(filter.getEnableBooleanFilter().getDisabledRadioButton(),"aria-checked","true")
                .and("Click on Back")
                .clickOnWebElement(filter.getEnableBooleanFilter().getBackButton())
                .then("Columns Menu should appear")
                .validateList(filter.getFilterOptionItems(), List.of(ColumnNames.PUBLISHER.getName(),
                        ColumnNames.RELATED_MEDIA.getName(),
                        ColumnNames.STATUS.getName(),
                        ColumnNames.TEST_MODE.getName(),
                        ColumnNames.DEFAULT_SIZES.getName(),
                        ColumnNames.CREATED_DATE.getName(),
                        ColumnNames.UPDATED_DATE.getName()))
                .and("Select Column Filter 'Enable/Disabled'")
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.TEST_MODE))
                .then("All options should be reset and unselected")
                .validateAttribute(filter.getEnableBooleanFilter().getEnabledRadioButton(),"aria-checked","false")
                .validateAttribute(filter.getEnableBooleanFilter().getDisabledRadioButton(),"aria-checked","false")
                .and("Select Inactive")
                .selectRadioButton(filter.getEnableBooleanFilter().getDisabledRadioButton())
                .validateAttribute(filter.getEnableBooleanFilter().getDisabledRadioButton(),"aria-checked","true")
                .and("Click on Submit")
                .clickOnWebElement(filter.getEnableBooleanFilter().getSubmitButton())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .validate(visible, table.getChipItemByName(ColumnNames.TEST_MODE.getName()).getHeaderLabel())
                .validate(table.countFilterChipsItems(), 1)
                .then("Validate value on chip")
                .validate(table.getChipItemByName(ColumnNames.TEST_MODE.getName()).getChipFilterOptionItemByName("Disabled"))
                .clickOnWebElement(table.getChipItemByName(ColumnNames.TEST_MODE.getName()).getCloseIcon())
                .testEnd();
    }

    @Test(description = "Check Search Publisher", priority = 3)
    public void testSearchPublisherColumnsFilterComponent() {

        var filter = adSpotsPage.getAdSpotsTable().getColumnFiltersBlock();
        var searchPubName = "rak";
        var expectedPubNameList = getFilterPublishersListFromBE(searchPubName);
        var totalPublishers = getTotalPublishersFromBE();


        testStart()
                .and("Select Column Filter 'PUBLISHER'")
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.PUBLISHER))
                .and(format("Search by Name '%s'", searchPubName))
                .setValueWithClean(filter.getSinglepaneFilter().getSearchInput(), searchPubName)
                .pressEnterKey(filter.getSinglepaneFilter().getSearchInput())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .validate(filter.getSinglepaneFilter().countIncludedItems(), expectedPubNameList.size())
                .testEnd();

        expectedPubNameList.forEach(e -> {
            testStart()
                    .validate(visible, filter.getSinglepaneFilter().getFilterItemByName(e).getName())
                    .testEnd();
        });

        testStart()
                .and("Clear Search")
                .clearField(filter.getSinglepaneFilter().getSearchInput())
                .then("Check total publishers count, search result should be reset")
                .validate(not(visible), adSpotsPage.getTableProgressBar())
                .validate(filter.getSinglepaneFilter().getItemsTotalQuantityLabel(), format("(%s)", totalPublishers))
                .scrollIntoView(filter.getSinglepaneFilter().getBackButton())
                .clickOnWebElement(filter.getSinglepaneFilter().getBackButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .testEnd();
    }

    @Test(description = "Check Chip Widget Component", dependsOnMethods = "testSearchPublisherColumnsFilterComponent")
    public void testPublisherChipWidgetComponent() {
        var filter = adSpotsPage.getAdSpotsTable().getColumnFiltersBlock();
        var table = adSpotsPage.getAdSpotsTable().getTableData();

        testStart()
                .and("Select Column Filter 'PUBLISHER'")
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.PUBLISHER))
                .and("Select Publishers")
                .clickOnWebElement(filter.getSinglepaneFilter().getFilterItemByPositionInList(1).getName())
                .clickOnWebElement(filter.getSinglepaneFilter().getFilterItemByPositionInList(2).getName())
                .clickOnWebElement(filter.getSinglepaneFilter().getFilterItemByPositionInList(3).getName())
                .testEnd();

        selectedPublishersNameList = List.of(filter.getSinglepaneFilter().getFilterItemByPositionInList(1).getName().text(),
                filter.getSinglepaneFilter().getFilterItemByPositionInList(2).getName().text(),
                filter.getSinglepaneFilter().getFilterItemByPositionInList(3).getName().text());

        testStart()
                .and("Click on Submit")
                .clickOnWebElement(filter.getSinglepaneFilter().getSubmitButton())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .validate(visible, table.getChipItemByName(ColumnNames.PUBLISHER.getName()).getHeaderLabel())
                .validate(table.countFilterChipsItems(), 1)
                .then("Wait for the publisher chip loading")
                .waitAndValidate(visible, table.getChipItemByName(ColumnNames.PUBLISHER.getName()).getHeaderLabel())
                .then("Validate list of selected publishers")
                .validate(table.getChipItemByName(ColumnNames.PUBLISHER.getName()).countFilterOptionsChipItems(), 3)
                .testEnd();

        selectedPublishersNameList.forEach(e -> {
            testStart()
                    .validate(visible, table.getChipItemByName(ColumnNames.PUBLISHER.getName()).getChipFilterOptionItemByName(e))
                    .testEnd();
        });

        testStart()
                .clickOnWebElement(table.getChipItemByName(ColumnNames.PUBLISHER.getName()).getCloseIcon())
                .testEnd();
    }

    @Test(description = "Check Search Related Media", priority = 4)
    public void testSearchRelatedMediaColumnsFilterComponent() {

        var filter = adSpotsPage.getAdSpotsTable().getColumnFiltersBlock();
        var searchRelatedMedia = "rak";
        var expectedRelatedMediaList = getFilterRelatedMediaListFromBE(searchRelatedMedia);
        var totalMedias = getTotalMediasFromBE();

        testStart()
                .and("Select Column Filter 'Related Media'")
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.RELATED_MEDIA))
                .and(format("Search by Name '%s'", searchRelatedMedia))
                .setValueWithClean(filter.getSinglepaneFilter().getSearchInput(), searchRelatedMedia)
                .pressEnterKey(filter.getSinglepaneFilter().getSearchInput())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .validate(filter.getSinglepaneFilter().countIncludedItems(), expectedRelatedMediaList.size())
                .testEnd();

        expectedRelatedMediaList.forEach(e -> {
            testStart()
                    .validate(visible, filter.getSinglepaneFilter().getFilterItemByName(e).getName())
                    .testEnd();
        });

        testStart()
                .and("Clear Search")
                .clearField(filter.getSinglepaneFilter().getSearchInput())
                .then("Check total related media count, search result should be reset")
                .validate(not(visible), adSpotsPage.getTableProgressBar())
                .validate(filter.getSinglepaneFilter().getItemsTotalQuantityLabel(), format("(%s)", totalMedias))
                .scrollIntoView(filter.getSinglepaneFilter().getBackButton())
                .clickOnWebElement(filter.getSinglepaneFilter().getBackButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .testEnd();
    }

    @Test(description = "Check Chip Widget Component", dependsOnMethods = "testSearchRelatedMediaColumnsFilterComponent")
    public void testRelatedMediaChipWidgetComponent() {
        var filter = adSpotsPage.getAdSpotsTable().getColumnFiltersBlock();
        var table = adSpotsPage.getAdSpotsTable().getTableData();

        testStart()
                .and("Select Column Filter 'Related Media'")
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.RELATED_MEDIA))
                .and("Select Media")
                .clickOnWebElement(filter.getSinglepaneFilter().getFilterItemByPositionInList(1).getName())
                .clickOnWebElement(filter.getSinglepaneFilter().getFilterItemByPositionInList(2).getName())
                .clickOnWebElement(filter.getSinglepaneFilter().getFilterItemByPositionInList(3).getName())
                .testEnd();

        selectedMediaList = List.of(filter.getSinglepaneFilter().getFilterItemByPositionInList(1).getName().text(),
                filter.getSinglepaneFilter().getFilterItemByPositionInList(2).getName().text(),
                filter.getSinglepaneFilter().getFilterItemByPositionInList(3).getName().text());

        testStart()
                .and("Click on Submit")
                .clickOnWebElement(filter.getSinglepaneFilter().getSubmitButton())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .validate(visible, table.getChipItemByName(ColumnNames.RELATED_MEDIA.getName()).getHeaderLabel())
                .validate(table.countFilterChipsItems(), 1)
                .then("Validate list of selected related media")
                .validate(table.getChipItemByName(ColumnNames.RELATED_MEDIA.getName()).countFilterOptionsChipItems(), 3)
                .testEnd();

        selectedMediaList.forEach(e -> {
            testStart()
                    .validate(visible, table.getChipItemByName(ColumnNames.RELATED_MEDIA.getName()).getChipFilterOptionItemByName(e))
                    .testEnd();
        });

        testStart()
                .clickOnWebElement(table.getChipItemByName(ColumnNames.RELATED_MEDIA.getName()).getCloseIcon())
                .testEnd();
    }

    @Test(description = "Check Search Default size", priority = 5)
    public void testSearchDefaultSizeColumnsFilterComponent() {

        var filter = adSpotsPage.getAdSpotsTable().getColumnFiltersBlock();
        List<AdSizesList> expectedSizesList = Arrays.stream(AdSizesList.values()).
                filter(e -> e.getSize().contains("1")).collect(Collectors.toList());

        testStart()
                .and("Select Column Filter 'PUBLISHER'")
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.DEFAULT_SIZES))
                .and(format("Search by Name '1'"))
                .setValueWithClean(filter.getSinglepaneFilter().getSearchInput(), "1")
                .pressEnterKey(filter.getSinglepaneFilter().getSearchInput())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .validate(filter.getSinglepaneFilter().countIncludedItems(), 9)
                .testEnd();

        expectedSizesList.forEach(e -> {
            testStart()
                    .validate(visible, filter.getSinglepaneFilter().getFilterItemByName(e.getSize()).getName())
                    .testEnd();
        });

        testStart()
                .and("Clear Search")
                .clearField(filter.getSinglepaneFilter().getSearchInput())
                .then("Check total publishers count, search result should be reset")
                .validate(not(visible), adSpotsPage.getTableProgressBar())
                .validate(filter.getSinglepaneFilter().getItemsTotalQuantityLabel(), format("(%s)", "16"))
                .scrollIntoView(filter.getSinglepaneFilter().getBackButton())
                .clickOnWebElement(filter.getSinglepaneFilter().getBackButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .testEnd();
    }

    @Test(description = "Check Chip Widget Component", dependsOnMethods = "testSearchDefaultSizeColumnsFilterComponent")
    public void testDefaultSizeChipWidgetComponent() {
        var filter = adSpotsPage.getAdSpotsTable().getColumnFiltersBlock();
        var table = adSpotsPage.getAdSpotsTable().getTableData();
        List<String> selectedDefaultSizeList;

        testStart()
                .and("Select Column Filter 'Default size'")
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.DEFAULT_SIZES))
                .and("Select Default Size")
                .clickOnWebElement(filter.getSinglepaneFilter().getFilterItemByPositionInList(1).getName())
                .clickOnWebElement(filter.getSinglepaneFilter().getFilterItemByPositionInList(2).getName())
                .clickOnWebElement(filter.getSinglepaneFilter().getFilterItemByPositionInList(3).getName())
                .testEnd();

        selectedDefaultSizeList = List.of(filter.getSinglepaneFilter().getFilterItemByPositionInList(1).getName().text(),
                filter.getSinglepaneFilter().getFilterItemByPositionInList(2).getName().text(),
                filter.getSinglepaneFilter().getFilterItemByPositionInList(3).getName().text());

        testStart()
                .and("Click on Submit")
                .clickOnWebElement(filter.getSinglepaneFilter().getSubmitButton())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .validate(visible, table.getChipItemByName(ColumnNames.DEFAULT_SIZES.getName()).getHeaderLabel())
                .validate(table.countFilterChipsItems(), 1)
                .then("Validate list of selected publishers")
                .waitAndValidate(visible, table.getChipItemByName(ColumnNames.DEFAULT_SIZES.getName()).getHeaderLabel())
                .validate(table.getChipItemByName(ColumnNames.DEFAULT_SIZES.getName()).countFilterOptionsChipItems(), 3)
                .testEnd();

        selectedDefaultSizeList.forEach(e -> {
            testStart()
                    .validate(visible, table.getChipItemByName(ColumnNames.DEFAULT_SIZES.getName()).getChipFilterOptionItemByName(e))
                    .testEnd();
        });

        testStart()
                .clickOnWebElement(table.getChipItemByName(ColumnNames.DEFAULT_SIZES.getName()).getCloseIcon())
                .testEnd();
    }

    @AfterClass
    private void logout() {
        testStart()
                .logOut()
                .testEnd();
    }

    private List<String> getFilterPublishersListFromBE(String name) {

        return publisher()
                .getPublishersListWithFilter(Map.of("name", name))
                .build()
                .getPublisherGetAllResponse()
                .getItems().stream().map(pub -> pub.getName()).collect(Collectors.toList());
    }

    private List<String> getFilterRelatedMediaListFromBE(String name) {

        return media()
                .getMediaWithFilter(Map.of("name", name))
                .build()
                .getMediaGetAllResponse()
                .getItems().stream().map(media -> media.getName()).collect(Collectors.toList());
    }

    private Integer getTotalPublishersFromBE() {

        return publisher()
                .getPublishersList()
                .build()
                .getPublisherGetAllResponse()
                .getTotal();
    }

    private Integer getTotalMediasFromBE() {

        return media()
                .getAllMediaList()
                .build()
                .getMediaGetAllResponse()
                .getTotal();
    }

}
