package rx.protections.columnsfilter;

import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.protections.ProtectionsPage;
import rx.BaseTest;
import widgets.common.table.ColumnNames;
import zutils.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static api.preconditionbuilders.ProtectionsPrecondition.protection;
import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static api.preconditionbuilders.UsersPrecondition.user;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static java.lang.String.format;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Protections Columns Filter")
public class ProtectionsColumnsFilterWidgetTests extends BaseTest {

    private ProtectionsPage protectionPage;

    private List<String> selectedPublishersNameList;

    public ProtectionsColumnsFilterWidgetTests() {
        protectionPage = new ProtectionsPage();
    }

    @BeforeClass
    private void login() {
        var tableColumns = protectionPage.getProtectionsTable().getShowHideColumns();

        testStart()
                .given()
                .openDirectPath(Path.PROTECTIONS)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, protectionPage.getNuxtProgress())
                .scrollIntoView(tableColumns.getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .selectCheckBox(tableColumns.getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .and("Select 15 rows per page")
                .scrollIntoView(protectionPage.getProtectionsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(protectionPage.getProtectionsTable().getTablePagination().getPageMenu(),
                        protectionPage.getProtectionsTable().getTablePagination().getRowNumbersList(), "15")
                .scrollIntoView(protectionPage.getProtectionPageTitle())
                .testEnd();
    }

    @Test(description = "Check Search Publisher")
    public void testSearchPublisherColumnsFilterComponent() {

        var filter = protectionPage.getProtectionsTable().getColumnFiltersBlock();
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
                .validate(not(visible), protectionPage.getTableProgressBar())
                .validate(filter.getSinglepaneFilter().getItemsTotalQuantityLabel(), format("(%s)", totalPublishers))
                .scrollIntoView(filter.getSinglepaneFilter().getBackButton())
                .clickOnWebElement(filter.getSinglepaneFilter().getBackButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .testEnd();
    }

    @Issue("GS-3447")
    @Test(description = "Check Chip Widget Component", dependsOnMethods = "testSearchPublisherColumnsFilterComponent")
    public void testPublisherChipWidgetComponent() {
        var filter = protectionPage.getProtectionsTable().getColumnFiltersBlock();
        var table = protectionPage.getProtectionsTable().getTableData();

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

    @Test(description = "Check Status Chip Widget Component", dependsOnMethods = "testCreatedByChipWidgetComponent")
    public void testStatusChipWidgetComponent() {
        var filter = protectionPage.getProtectionsTable().getColumnFiltersBlock();
        var table = protectionPage.getProtectionsTable().getTableData();

        testStart()
                .and("Click on 'Column Filters'")
                .scrollIntoView(protectionPage.getProtectionPageTitle())
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .and("Select Column Filter 'Status'")
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
                        ColumnNames.STATUS.getName(),
                        ColumnNames.MANAGED_BY_SYSTEM_ADMIN.getName(),
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

    @Test(description = "Check 'Managed By System Admin' Chip Widget Component", dependsOnMethods = "testStatusChipWidgetComponent")
    public void testManageBySystemAdminChipWidgetComponent() {
        var filter = protectionPage.getProtectionsTable().getColumnFiltersBlock();
        var table = protectionPage.getProtectionsTable().getTableData();

        testStart()
                .and("Click on 'Column Filters'")
                .scrollIntoView(protectionPage.getProtectionPageTitle())
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .and("Select Column Filter 'Managed By System Admin'")
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.MANAGED_BY_SYSTEM_ADMIN))
                .then("Title should be displayed")
                .validate(filter.getBooleanFilter().getFilterHeaderLabel(), StringUtils.getFilterHeader(ColumnNames.MANAGED_BY_SYSTEM_ADMIN.getName()))
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
                .clickOnWebElement(filter.getBooleanFilter().getBackButton())
                .then("Columns Menu should appear")
                .validateList(filter.getFilterOptionItems(), List.of(ColumnNames.PUBLISHER.getName(),
                        ColumnNames.STATUS.getName(),
                        ColumnNames.MANAGED_BY_SYSTEM_ADMIN.getName(),
                        ColumnNames.CREATED_BY.getName(),
                        ColumnNames.UPDATED_DATE.getName(),
                        ColumnNames.UPDATED_BY.getName()))
                .and("Select Column Filter 'Managed By System Admin'")
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.MANAGED_BY_SYSTEM_ADMIN))
                .then("All options should be reset and unselected")
                .validateAttribute(filter.getBooleanFilter().getYesRadioButton(), "aria-checked", "false")
                .validateAttribute(filter.getBooleanFilter().getNoRadioButton(), "aria-checked", "false")
                .and("Select No")
                .selectRadioButton(filter.getBooleanFilter().getNoRadioButton())
                .validateAttribute(filter.getBooleanFilter().getNoRadioButton(), "aria-checked", "true")
                .and("Click on Submit")
                .clickOnWebElement(filter.getBooleanFilter().getSubmitButton())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .waitAndValidate(visible, table.getChipItemByName(ColumnNames.MANAGED_BY_SYSTEM_ADMIN.getName()).getHeaderLabel())
                .validate(table.countFilterChipsItems(), 1)
                .then("Validate value on chip")
                .validate(table.getChipItemByName(ColumnNames.MANAGED_BY_SYSTEM_ADMIN.getName()).getChipFilterOptionItemByName("No"))
                .clickOnWebElement(table.getChipItemByName(ColumnNames.MANAGED_BY_SYSTEM_ADMIN.getName()).getCloseIcon())
                .testEnd();
    }

    @Test(description = "Check Search Updated By")
    public void testSearchUpdatedByColumnsFilterComponent() {
        var filter = protectionPage.getProtectionsTable().getColumnFiltersBlock();
        var userNameList = getUpdatedByListFromBE();
        var searchName = userNameList.get(1).substring(5);
        var expectedUserNameList = getUsersListFromBE(searchName);
        var totalUsers = getTotalUsersFromBE();

        testStart()
                .and("Select 15 rows per page")
                .scrollIntoView(protectionPage.getProtectionsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(protectionPage.getProtectionsTable().getTablePagination().getPageMenu(),
                        protectionPage.getProtectionsTable().getTablePagination().getRowNumbersList(), "15")
                .and("Select Column Filter 'Updated By'")
                .scrollIntoView(protectionPage.getLogo())
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
                .waitAndValidate(visible, filter.getSinglepaneFilter().getItemsTotalQuantityLabel())
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
                .validate(not(visible), protectionPage.getTableProgressBar())
                .validate(filter.getSinglepaneFilter().getItemsTotalQuantityLabel(), format("(%s)", totalUsers))
                .testEnd();
    }

    @Test(description = "Check 'Updated By' Chip Widget Component", dependsOnMethods = "testSearchUpdatedByColumnsFilterComponent")
    public void testUpdatedByChipWidgetComponent() {
        var filter = protectionPage.getProtectionsTable().getColumnFiltersBlock();
        var table = protectionPage.getProtectionsTable().getTableData();

        testStart()
                .and("Select Users")
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
                .validate(visible, table.getChipItemByName(ColumnNames.UPDATED_BY.getName()).getHeaderLabel())
                .validate(table.countFilterChipsItems(), 1)
                .then("Validate list of selected users")
                .validate(table.getChipItemByName(ColumnNames.UPDATED_BY.getName()).countFilterOptionsChipItems(), 3)
                .testEnd();

        selectedPublishersNameList.forEach(e -> {
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
        var filter = protectionPage.getProtectionsTable().getColumnFiltersBlock();
        var userNameList = getCreatedByListFromBE();
        var searchName = userNameList.get(1).substring(5);
        var expectedUserNameList = getUsersListFromBE(searchName);
        var totalUsers = getTotalUsersFromBE();

        testStart()
                .and("Select 15 rows per page")
                .scrollIntoView(protectionPage.getProtectionsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(protectionPage.getProtectionsTable().getTablePagination().getPageMenu(),
                        protectionPage.getProtectionsTable().getTablePagination().getRowNumbersList(), "15")
                .and("Select Column Filter 'Created By'")
                .scrollIntoView(protectionPage.getProtectionPageTitle())
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
                .waitSpecifiedTimeAndValidate(visible, 3, filter.getSinglepaneFilter().getFilterItemByPositionInList(0).getName())
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
                .validate(not(visible), protectionPage.getTableProgressBar())
                .validate(filter.getSinglepaneFilter().getItemsTotalQuantityLabel(), format("(%s)", totalUsers))
                .and()
                .clickOnWebElement(filter.getSinglepaneFilter().getCancelButton())
                .testEnd();
    }

    @Test(description = "Check Back Created By filter", dependsOnMethods = "testSearchCreatedByColumnsFilterComponent")
    public void testBackCreatedByColumnsFilterComponent() {
        var filter = protectionPage.getProtectionsTable().getColumnFiltersBlock();
        var userNameList = getCreatedByListFromBE();
        var searchName = userNameList.get(1).substring(5);
        var expectedUserNameList = getUsersListFromBE(searchName);
        var totalUsers = getTotalUsersFromBE();

        testStart()
                .and("Select 15 rows per page")
                .scrollIntoView(protectionPage.getProtectionsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(protectionPage.getProtectionsTable().getTablePagination().getPageMenu(),
                        protectionPage.getProtectionsTable().getTablePagination().getRowNumbersList(), "15")
                .and("Select Column Filter 'Created By'")
                .scrollIntoView(protectionPage.getProtectionPageTitle())
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .waitAndValidate(appear, filter.getFilterOptionsMenu())
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
                .validate(not(visible), protectionPage.getTableProgressBar())
                .validate(filter.getSinglepaneFilter().getItemsTotalQuantityLabel(), format("(%s)", totalUsers))
                .testEnd();
    }

    @Test(description = "Check 'Created By' Chip Widget Component", dependsOnMethods = "testSearchCreatedByColumnsFilterComponent")
    public void testCreatedByChipWidgetComponent() {
        var filter = protectionPage.getProtectionsTable().getColumnFiltersBlock();
        var table = protectionPage.getProtectionsTable().getTableData();

        testStart()
                .setValueWithClean(filter.getSinglepaneFilter().getSearchInput(), "")
                .and("Select Users")
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
                .validate(visible, table.getChipItemByName(ColumnNames.CREATED_BY.getName()).getHeaderLabel())
                .validate(table.countFilterChipsItems(), 1)
                .then("Validate list of selected users")
                .validate(table.getChipItemByName(ColumnNames.CREATED_BY.getName()).countFilterOptionsChipItems(), 3)
                .testEnd();

        selectedPublishersNameList.forEach(e -> {
            testStart()
                    .validate(visible, table.getChipItemByName(ColumnNames.CREATED_BY.getName()).getChipFilterOptionItemByName(e))
                    .testEnd();
        });

        testStart()
                .clickOnWebElement(table.getChipItemByName(ColumnNames.CREATED_BY.getName()).getCloseIcon())
                .testEnd();
    }

    private List<String> getFilterPublishersListFromBE(String name) {

        return publisher()
                .getPublishersListWithFilter(Map.of("name", name))
                .build()
                .getPublisherGetAllResponse()
                .getItems().stream().map(pub -> pub.getName()).collect(Collectors.toList());
    }

    private Integer getTotalPublishersFromBE() {

        return publisher()
                .getPublishersList()
                .build()
                .getPublisherGetAllResponse()
                .getTotal();
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
