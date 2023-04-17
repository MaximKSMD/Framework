package rx.sales.privateauctions.columnsfilter;

import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.sales.privateauctions.PrivateAuctionsPage;
import rx.BaseTest;
import widgets.common.table.ColumnNames;
import zutils.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static api.preconditionbuilders.ProtectionsPrecondition.protection;
import static api.preconditionbuilders.UsersPrecondition.user;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static java.lang.String.format;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "PrivateAuctions Columns Filter")
public class PrivateAuctionsColumnsFilterWidgetTests extends BaseTest {

    private PrivateAuctionsPage privateAuctionsPage;

    private List<String> selectedPrivateAuctionssNameList;

    public PrivateAuctionsColumnsFilterWidgetTests() {
        privateAuctionsPage = new PrivateAuctionsPage();
    }

    @BeforeClass
    private void login() {
        var tableColumns = privateAuctionsPage.getTable().getShowHideColumns();

        testStart()
                .given()
                .openDirectPath(Path.PRIVATE_AUCTIONS)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, privateAuctionsPage.getNuxtProgress())
                .scrollIntoView(privateAuctionsPage.getTable().getTablePagination().getPageMenu())
                .selectFromDropdown(privateAuctionsPage.getTable().getTablePagination().getPageMenu(),
                        privateAuctionsPage.getTable().getTablePagination().getRowNumbersList(), "10")
                .scrollIntoView(tableColumns.getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .selectCheckBox(tableColumns.getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .testEnd();
    }

    @Test(description = "Check Active/Inactive Chip Widget Component", dependsOnMethods = "testCreatedByChipWidgetComponent")
    public void testActiveInactiveChipWidgetComponent() {
        var filter = privateAuctionsPage.getTable().getColumnFiltersBlock();
        var table = privateAuctionsPage.getTable().getTableData();

        testStart()
                .and("Click on 'Column Filters'")
                .scrollIntoView(privateAuctionsPage.getPageTitle())
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .and("Select Column Filter 'Active/Inactive'")
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.STATUS))
                .then("Title should be displayed")
                .validate(filter.getActiveBooleanFilter().getFilterHeaderLabel(), StringUtils.getFilterHeader(ColumnNames.STATUS.getName()))
                .then("All options should be unselected")
                .validateAttribute(filter.getActiveBooleanFilter().getActiveRadioButton(), "aria-checked", "false")
                .validateAttribute(filter.getActiveBooleanFilter().getInactiveRadioButton(), "aria-checked", "false")
                .and("Select Active")
                .selectRadioButton(filter.getActiveBooleanFilter().getActiveRadioButton())
                .validateAttribute(filter.getActiveBooleanFilter().getActiveRadioButton(), "aria-checked", "true")
                .and("Select Inactive")
                .selectRadioButton(filter.getActiveBooleanFilter().getInactiveRadioButton())
                .then("Only one option should be selected")
                .validateAttribute(filter.getActiveBooleanFilter().getActiveRadioButton(), "aria-checked", "false")
                .validateAttribute(filter.getActiveBooleanFilter().getInactiveRadioButton(), "aria-checked", "true")
                .and("Click on Back")
                .clickOnWebElement(filter.getActiveBooleanFilter().getBackButton())
                .then("Columns Menu should appear")
                .validateList(filter.getFilterOptionItems(), List.of(ColumnNames.PUBLISHER.getName(),
                        ColumnNames.OPTIMIZE.getName(),
                        ColumnNames.STATUS.getName(),
                        ColumnNames.ALWAYS_ON.getName(),
                        ColumnNames.START_DATE.getName(),
                        ColumnNames.END_DATE.getName(),
                        ColumnNames.CREATED_BY.getName(),
                        ColumnNames.UPDATED_DATE.getName(),
                        ColumnNames.UPDATED_BY.getName()))
                .and("Select Column Filter 'Active/Inactive'")
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.STATUS))
                .then("All options should be reset and unselected")
                .validateAttribute(filter.getActiveBooleanFilter().getActiveRadioButton(), "aria-checked", "false")
                .validateAttribute(filter.getActiveBooleanFilter().getInactiveRadioButton(), "aria-checked", "false")
                .and("Select Inactive")
                .selectRadioButton(filter.getActiveBooleanFilter().getInactiveRadioButton())
                .validateAttribute(filter.getActiveBooleanFilter().getInactiveRadioButton(), "aria-checked", "true")
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

    @Test(description = "Check Search Updated By")
    public void testSearchUpdatedByColumnsFilterComponent() {
        var filter = privateAuctionsPage.getTable().getColumnFiltersBlock();
        var userNameList = getUpdatedByListFromBE();
        var searchName = userNameList.get(1).substring(5);
        var expectedUserNameList = getUsersListFromBE(searchName);
        var totalUsers = getTotalUsersFromBE();

        testStart()
                .and("Select Column Filter 'Updated By'")
                .scrollIntoView(privateAuctionsPage.getLogo())
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.UPDATED_BY))
                .and("Clean Search field")
                .setValueWithClean(filter.getSinglepaneFilter().getSearchInput(), "abc")
                .clickOnWebElement(filter.getSinglepaneFilter().getBackButton())
                .waitAndValidate(appear, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.UPDATED_BY))
                .validate(filter.getSinglepaneFilter().getSearchInput(), "")
                .validate(filter.getSinglepaneFilter().getItemsTotalQuantityLabel(), format("(%s)", totalUsers))
                .and(format("Search by Name '%s'", searchName))
                .setValueWithClean(filter.getSinglepaneFilter().getSearchInput(), searchName)
                .pressEnterKey(filter.getSinglepaneFilter().getSearchInput())
                .and("Verify Search Count")
                //TODO: below condition fails sometimes, we need to work on fix.
                .validate(filter.getSinglepaneFilter().countIncludedItems(), expectedUserNameList.size())
                .testEnd();

        expectedUserNameList.forEach(e -> {
            testStart()
                    .validate(visible, filter.getSinglepaneFilter().getFilterItemByName(e).getName())
                    .testEnd();
        });

        testStart()
                .and("Clear Search")
                .setValueWithClean(filter.getSinglepaneFilter().getSearchInput(), "")
                .then("Check total users count, search result should be reset")
                .validate(not(visible), privateAuctionsPage.getTableProgressBar())
                .validate(filter.getSinglepaneFilter().getItemsTotalQuantityLabel(), format("(%s)", totalUsers))
                .testEnd();
    }

    @Test(description = "Check 'Updated By' Chip Widget Component", dependsOnMethods = "testSearchUpdatedByColumnsFilterComponent")
    public void testUpdatedByChipWidgetComponent() {
        var filter = privateAuctionsPage.getTable().getColumnFiltersBlock();
        var table = privateAuctionsPage.getTable().getTableData();

        testStart()
                .and("Select Users")
                .clickOnWebElement(filter.getSinglepaneFilter().getFilterItemByPositionInList(1).getName())
                .clickOnWebElement(filter.getSinglepaneFilter().getFilterItemByPositionInList(2).getName())
                .clickOnWebElement(filter.getSinglepaneFilter().getFilterItemByPositionInList(3).getName())
                .testEnd();

        selectedPrivateAuctionssNameList = List.of(filter.getSinglepaneFilter().getFilterItemByPositionInList(1).getName().text(),
                filter.getSinglepaneFilter().getFilterItemByPositionInList(2).getName().text(),
                filter.getSinglepaneFilter().getFilterItemByPositionInList(3).getName().text());

        testStart()
                .and("Click on Submit")
                .clickOnWebElement(filter.getSinglepaneFilter().getSubmitButton())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .validate(visible, table.getChipItemByName(ColumnNames.UPDATED_BY.getName()).getHeaderLabel())
                .validate(table.countFilterChipsItems(), 1)
                .then("Validate list of selected users")
                .validate(table.getChipItemByName(ColumnNames.UPDATED_BY.getName()).countFilterOptionsChipItems(), 3)
                .testEnd();

        selectedPrivateAuctionssNameList.forEach(e -> {
            testStart()
                    .validate(visible, table.getChipItemByName(ColumnNames.UPDATED_BY.getName()).getChipFilterOptionItemByName(e))
                    .testEnd();
        });

        testStart()
                .clickOnWebElement(table.getChipItemByName(ColumnNames.UPDATED_BY.getName()).getCloseIcon())
                .testEnd();
    }

    @Test(description = "Check Search Created By", dependsOnMethods = "testUpdatedByChipWidgetComponent")
    public void testSearchCreatedByColumnsFilterComponent() {
        var filter = privateAuctionsPage.getTable().getColumnFiltersBlock();
        var userNameList = getCreatedByListFromBE();
        var searchName = userNameList.get(1).substring(5);
        var expectedUserNameList = getUsersListFromBE(searchName);
        var totalUsers = getTotalUsersFromBE();

        testStart()
                .and("Select Column Filter 'Created By'")
                .scrollIntoView(privateAuctionsPage.getPageTitle())
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.CREATED_BY))
                .and()
                .setValueWithClean(filter.getSinglepaneFilter().getSearchInput(), "abc")
                .clickOnWebElement(filter.getSinglepaneFilter().getBackButton())
                .waitAndValidate(appear, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.CREATED_BY))
                .then("Search params should be reset")
                .validate(filter.getSinglepaneFilter().getSearchInput(), "")
                .validate(filter.getSinglepaneFilter().getItemsTotalQuantityLabel(), format("(%s)", totalUsers))
                .and(format("Search by Name '%s'", searchName))
                .setValueWithClean(filter.getSinglepaneFilter().getSearchInput(), searchName)
                .pressEnterKey(filter.getSinglepaneFilter().getSearchInput())
                .and("Verify Search Count")
                .validate(filter.getSinglepaneFilter().countIncludedItems(), expectedUserNameList.size())
                .testEnd();

        expectedUserNameList.forEach(e -> {
            testStart()
                    .validate(visible, filter.getSinglepaneFilter().getFilterItemByName(e).getName())
                    .testEnd();
        });

        testStart()
                .and("Clear Search")
                .setValueWithClean(filter.getSinglepaneFilter().getSearchInput(), "")
                .then("Check total users count, search result should be reset")
                .validate(not(visible), privateAuctionsPage.getTableProgressBar())
                .validate(filter.getSinglepaneFilter().getItemsTotalQuantityLabel(), format("(%s)", totalUsers))
                .and()
                .clickOnWebElement(filter.getSinglepaneFilter().getCancelButton())
                .testEnd();
    }

    @Test(description = "Check Back Created By filter", dependsOnMethods = "testSearchCreatedByColumnsFilterComponent")
    public void testBackCreatedByColumnsFilterComponent() {
        var filter = privateAuctionsPage.getTable().getColumnFiltersBlock();
        var userNameList = getCreatedByListFromBE();
        var searchName = userNameList.get(1).substring(5);
        var expectedUserNameList = getUsersListFromBE(searchName);
        var totalUsers = getTotalUsersFromBE();

        testStart()
                .and("Select Column Filter 'Created By'")
                .scrollIntoView(privateAuctionsPage.getPageTitle())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.CREATED_BY))
                .and()
                .setValueWithClean(filter.getSinglepaneFilter().getSearchInput(), "abc")
                .and("Click on Back")
                .clickOnWebElement(filter.getSinglepaneFilter().getBackButton())
                .waitAndValidate(appear, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.CREATED_BY))
                .then("Search params should be reset")
                .validate(filter.getSinglepaneFilter().getSearchInput(), "")
                .validate(filter.getSinglepaneFilter().getItemsTotalQuantityLabel(), format("(%s)", totalUsers))
                .and(format("Search by Name '%s'", searchName))
                .setValueWithClean(filter.getSinglepaneFilter().getSearchInput(), searchName)
                .pressEnterKey(filter.getSinglepaneFilter().getSearchInput())
                .validate(filter.getSinglepaneFilter().countIncludedItems(), expectedUserNameList.size())
                .testEnd();

        expectedUserNameList.forEach(e -> {
            testStart()
                    .validate(visible, filter.getSinglepaneFilter().getFilterItemByName(e).getName())
                    .testEnd();
        });

        testStart()
                .and("Clear Search")
                .setValueWithClean(filter.getSinglepaneFilter().getSearchInput(), "")
                .then("Check total users count, search result should be reset")
                .validate(not(visible), privateAuctionsPage.getTableProgressBar())
                .validate(filter.getSinglepaneFilter().getItemsTotalQuantityLabel(), format("(%s)", totalUsers))
                .testEnd();
    }

    @Test(description = "Check 'Created By' Chip Widget Component", dependsOnMethods = "testSearchCreatedByColumnsFilterComponent")
    public void testCreatedByChipWidgetComponent() {
        var filter = privateAuctionsPage.getTable().getColumnFiltersBlock();
        var table = privateAuctionsPage.getTable().getTableData();

        testStart()
                .and("Select Users")
                .clickOnWebElement(filter.getSinglepaneFilter().getFilterItemByPositionInList(1).getName())
                .clickOnWebElement(filter.getSinglepaneFilter().getFilterItemByPositionInList(2).getName())
                .clickOnWebElement(filter.getSinglepaneFilter().getFilterItemByPositionInList(3).getName())
                .testEnd();

        selectedPrivateAuctionssNameList = List.of(filter.getSinglepaneFilter().getFilterItemByPositionInList(1).getName().text(),
                filter.getSinglepaneFilter().getFilterItemByPositionInList(2).getName().text(),
                filter.getSinglepaneFilter().getFilterItemByPositionInList(3).getName().text());

        testStart()
                .and("Click on Submit")
                .clickOnWebElement(filter.getSinglepaneFilter().getSubmitButton())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .validate(visible, table.getChipItemByName(ColumnNames.CREATED_BY.getName()).getHeaderLabel())
                .validate(table.countFilterChipsItems(), 1)
                .then("Validate list of selected users")
                .validate(table.getChipItemByName(ColumnNames.CREATED_BY.getName()).countFilterOptionsChipItems(), 3)
                .testEnd();

        selectedPrivateAuctionssNameList.forEach(e -> {
            testStart()
                    .validate(visible, table.getChipItemByName(ColumnNames.CREATED_BY.getName()).getChipFilterOptionItemByName(e))
                    .testEnd();
        });

        testStart()
                .clickOnWebElement(table.getChipItemByName(ColumnNames.CREATED_BY.getName()).getCloseIcon())
                .testEnd();
    }

    @Test(description = "Check Optimize Chip Widget Component", dependsOnMethods = "testCreatedByChipWidgetComponent")
    public void testOptimizeChipWidgetComponent() {
        var filter = privateAuctionsPage.getTable().getColumnFiltersBlock();
        var table = privateAuctionsPage.getTable().getTableData();

        testStart()
                .and("Click on 'Column Filters'")
                .scrollIntoView(privateAuctionsPage.getPageTitle())
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .and("Select Column Filter 'Active/Inactive'")
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.OPTIMIZE))
                .then("Title should be displayed")
                .validate(filter.getActiveBooleanFilter().getFilterHeaderLabel(), StringUtils.getFilterHeader(ColumnNames.OPTIMIZE.getName()))
                .then("All options should be unselected")
                .validateAttribute(filter.getBooleanFilter().getYesRadioButton(), "aria-checked", "false")
                .validateAttribute(filter.getBooleanFilter().getNoRadioButton(), "aria-checked", "false")
                .and("Select Yes")
                .selectRadioButton(filter.getBooleanFilter().getYesRadioButton())
                .validateAttribute(filter.getBooleanFilter().getYesRadioButton(), "aria-checked", "true")
                .and("Select No")
                .selectRadioButton(filter.getBooleanFilter().getNoRadioButton())
                .then("Only one option should be selected")
                .validateAttribute(filter.getBooleanFilter().getYesRadioButton(), "aria-checked", "false")
                .validateAttribute(filter.getBooleanFilter().getNoRadioButton(), "aria-checked", "true")
                .and("Click on Back")
                .clickOnWebElement(filter.getActiveBooleanFilter().getBackButton())
                .then("Columns Menu should appear")
                .validateList(filter.getFilterOptionItems(), List.of(ColumnNames.PUBLISHER.getName(),
                        ColumnNames.OPTIMIZE.getName(),
                        ColumnNames.STATUS.getName(),
                        ColumnNames.ALWAYS_ON.getName(),
                        ColumnNames.START_DATE.getName(),
                        ColumnNames.END_DATE.getName(),
                        ColumnNames.CREATED_BY.getName(),
                        ColumnNames.UPDATED_DATE.getName(),
                        ColumnNames.UPDATED_BY.getName()))
                .and("Select Column Filter 'Optimize'")
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.OPTIMIZE))
                .then("All options should be reset and unselected")
                .validateAttribute(filter.getBooleanFilter().getNoRadioButton(), "aria-checked", "false")
                .validateAttribute(filter.getBooleanFilter().getNoRadioButton(), "aria-checked", "false")
                .and("Select Inactive")
                .selectRadioButton(filter.getBooleanFilter().getNoRadioButton())
                .validateAttribute(filter.getBooleanFilter().getNoRadioButton(), "aria-checked", "true")
                .and("Click on Submit")
                .clickOnWebElement(filter.getActiveBooleanFilter().getSubmitButton())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .validate(visible, table.getChipItemByName(ColumnNames.OPTIMIZE.getName()).getHeaderLabel())
                .validate(table.countFilterChipsItems(), 1)
                .then("Validate value on chip")
                .validate(table.getChipItemByName(ColumnNames.OPTIMIZE.getName()).getChipFilterOptionItemByName("No"))
                .clickOnWebElement(table.getChipItemByName(ColumnNames.OPTIMIZE.getName()).getCloseIcon())
                .testEnd();
    }

    @Test(description = "Check AlwaysOn Chip Widget Component", dependsOnMethods = "testCreatedByChipWidgetComponent")
    public void testAlwaysOnChipWidgetComponent() {
        var filter = privateAuctionsPage.getTable().getColumnFiltersBlock();
        var table = privateAuctionsPage.getTable().getTableData();

        testStart()
                .and("Click on 'Column Filters'")
                .scrollIntoView(privateAuctionsPage.getPageTitle())
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .and("Select Column Filter 'Active/Inactive'")
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.ALWAYS_ON))
                .then("Title should be displayed")
                .validate(filter.getActiveBooleanFilter().getFilterHeaderLabel(), StringUtils.getFilterHeader(ColumnNames.ALWAYS_ON.getName()))
                .then("All options should be unselected")
                .validateAttribute(filter.getBooleanFilter().getYesRadioButton(), "aria-checked", "false")
                .validateAttribute(filter.getBooleanFilter().getNoRadioButton(), "aria-checked", "false")
                .and("Select Yes")
                .selectRadioButton(filter.getBooleanFilter().getYesRadioButton())
                .validateAttribute(filter.getBooleanFilter().getYesRadioButton(), "aria-checked", "true")
                .and("Select No")
                .selectRadioButton(filter.getBooleanFilter().getNoRadioButton())
                .then("Only one option should be selected")
                .validateAttribute(filter.getBooleanFilter().getYesRadioButton(), "aria-checked", "false")
                .validateAttribute(filter.getBooleanFilter().getNoRadioButton(), "aria-checked", "true")
                .and("Click on Back")
                .clickOnWebElement(filter.getActiveBooleanFilter().getBackButton())
                .then("Columns Menu should appear")
                .validateList(filter.getFilterOptionItems(), List.of(ColumnNames.PUBLISHER.getName(),
                        ColumnNames.OPTIMIZE.getName(),
                        ColumnNames.STATUS.getName(),
                        ColumnNames.ALWAYS_ON.getName(),
                        ColumnNames.START_DATE.getName(),
                        ColumnNames.END_DATE.getName(),
                        ColumnNames.CREATED_BY.getName(),
                        ColumnNames.UPDATED_DATE.getName(),
                        ColumnNames.UPDATED_BY.getName()))
                .and("Select Column Filter 'Optimize'")
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.ALWAYS_ON))
                .then("All options should be reset and unselected")
                .validateAttribute(filter.getBooleanFilter().getNoRadioButton(), "aria-checked", "false")
                .validateAttribute(filter.getBooleanFilter().getNoRadioButton(), "aria-checked", "false")
                .and("Select Inactive")
                .selectRadioButton(filter.getBooleanFilter().getNoRadioButton())
                .validateAttribute(filter.getBooleanFilter().getNoRadioButton(), "aria-checked", "true")
                .and("Click on Submit")
                .clickOnWebElement(filter.getActiveBooleanFilter().getSubmitButton())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .validate(visible, table.getChipItemByName(ColumnNames.ALWAYS_ON.getName()).getHeaderLabel())
                .validate(table.countFilterChipsItems(), 1)
                .then("Validate value on chip")
                .validate(table.getChipItemByName(ColumnNames.ALWAYS_ON.getName()).getChipFilterOptionItemByName("No"))
                .clickOnWebElement(table.getChipItemByName(ColumnNames.ALWAYS_ON.getName()).getCloseIcon())
                .testEnd();
    }

    private Integer getTotalUsersFromBE() {

        return user()
                .getAllUsers()
                .build()
                .getUserGetAllResponse()
                .getTotal();
    }

    private List<String> getUpdatedByListFromBE() {

        return protection()
                .getAllProtectionsList()
                .build()
                .getProtectionsGetAllResponse()
                .getItems()
                .stream().map(e -> e.getUpdatedBy()).distinct().collect(Collectors.toList());
    }

    private List<String> getCreatedByListFromBE() {

        return protection()
                .getAllProtectionsList()
                .build()
                .getProtectionsGetAllResponse()
                .getItems()
                .stream().map(e -> e.getCreatedBy()).distinct().collect(Collectors.toList());
    }

    private List<String> getUsersListFromBE(String name) {

        return user()
                .getUsersWithFilter(Map.of("mail", name))
                .build()
                .getUserGetAllResponse()
                .getItems()
                .stream().map(e -> e.getMail()).collect(Collectors.toList());
    }

    @AfterClass
    private void logout() {
        testStart()
                .logOut()
                .testEnd();
    }
}
