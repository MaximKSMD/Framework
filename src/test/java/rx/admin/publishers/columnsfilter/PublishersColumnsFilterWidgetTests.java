package rx.admin.publishers.columnsfilter;

import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.admin.publisher.PublishersPage;
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
@Feature(value = "Publishers Columns Filter")
public class PublishersColumnsFilterWidgetTests extends BaseTest {

    private PublishersPage publishersPage;

    private List<String> selectedPublishersNameList;

    public PublishersColumnsFilterWidgetTests() {
        publishersPage = new PublishersPage();
    }

    @BeforeClass
    private void login() {
        var tableColumns = publishersPage.getTable().getShowHideColumns();

        testStart()
                .given()
                .openDirectPath(Path.PUBLISHER)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, publishersPage.getNuxtProgress())
                .scrollIntoView(tableColumns.getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .selectCheckBox(tableColumns.getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .selectCheckBox(tableColumns.getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .and("Select 15 rows per page")
                .scrollIntoView(publishersPage.getTable().getTablePagination().getPageMenu())
                .selectFromDropdown(publishersPage.getTable().getTablePagination().getPageMenu(),
                        publishersPage.getTable().getTablePagination().getRowNumbersList(), "15")
                .scrollIntoView(publishersPage.getPageTitle())
                .testEnd();
    }
    @Test(description = "Check Active/Inactive Chip Widget Component", dependsOnMethods = "testCreatedByChipWidgetComponent")
    public void testActiveInactiveChipWidgetComponent() {
        var filter = publishersPage.getTable().getColumnFiltersBlock();
        var table = publishersPage.getTable().getTableData();

        testStart()
                .and("Click on 'Column Filters'")
                .scrollIntoView(publishersPage.getPageTitle())
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
                .validateList(filter.getFilterOptionItems(), List.of(ColumnNames.STATUS.getName(),
                        ColumnNames.CURRENCY.getName(),
                        ColumnNames.CREATED_DATE.getName(),
                        ColumnNames.CREATED_BY.getName(),
                        ColumnNames.UPDATED_DATE.getName(),
                        ColumnNames.UPDATED_BY.getName()))
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

    @Test(description = "Check Search Updated By")
    public void testSearchUpdatedByColumnsFilterComponent() {
        var filter = publishersPage.getTable().getColumnFiltersBlock();
        var userNameList = getUpdatedByListFromBE();
        var searchName = userNameList.get(1).substring(5);
        var expectedUserNameList = getUsersListFromBE(searchName);
        var totalUsers = getTotalUsersFromBE();

        testStart()
                .and("Select Column Filter 'Updated By'")
                .scrollIntoView(publishersPage.getLogo())
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.UPDATED_BY))
                .and("Clean Search field")
                .setValueWithClean(filter.getSinglepaneFilter().getSearchInput(), "abc")
                .clickOnWebElement(filter.getSinglepaneFilter().getBackButton())
                .waitAndValidate(appear, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.UPDATED_BY))
                .validate(filter.getSinglepaneFilter().getSearchInput(),"")
                .validate(filter.getSinglepaneFilter().getItemsTotalQuantityLabel(), format("(%s)",totalUsers))
                .and(format("Search by Name '%s'", searchName))
                .setValueWithClean(filter.getSinglepaneFilter().getSearchInput(), searchName)
                .pressEnterKey(filter.getSinglepaneFilter().getSearchInput())
                .and("Verify Search Count")
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
                .setValueWithClean(filter.getSinglepaneFilter().getSearchInput(),"")
                .then("Check total users count, search result should be reset")
                .validate(not(visible), publishersPage.getTableProgressBar())
                .validate(filter.getSinglepaneFilter().getItemsTotalQuantityLabel(), format("(%s)",totalUsers))
                .testEnd();
    }

    @Test(description = "Check 'Updated By' Chip Widget Component", dependsOnMethods = "testSearchUpdatedByColumnsFilterComponent")
    public void testUpdatedByChipWidgetComponent() {
        var filter = publishersPage.getTable().getColumnFiltersBlock();
        var table = publishersPage.getTable().getTableData();

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
        var filter = publishersPage.getTable().getColumnFiltersBlock();
        var userNameList = getCreatedByListFromBE();
        var searchName = userNameList.get(1).substring(5);
        var expectedUserNameList = getUsersListFromBE(searchName);
        var totalUsers = getTotalUsersFromBE();

        testStart()
                .and("Select Column Filter 'Created By'")
                .scrollIntoView(publishersPage.getPageTitle())
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.CREATED_BY))
                .and()
                .setValueWithClean(filter.getSinglepaneFilter().getSearchInput(), "abc")
                .clickOnWebElement(filter.getSinglepaneFilter().getBackButton())
                .waitAndValidate(appear, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.CREATED_BY))
                .then("Search params should be reset")
                .validate(filter.getSinglepaneFilter().getSearchInput(),"")
                .validate(filter.getSinglepaneFilter().getItemsTotalQuantityLabel(), format("(%s)",totalUsers))
                .and(format("Search by Name '%s'", searchName))
                .setValueWithClean(filter.getSinglepaneFilter().getSearchInput(), searchName)
                .pressEnterKey(filter.getSinglepaneFilter().getSearchInput())
                .and("Verify Search Count")
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
                .setValueWithClean(filter.getSinglepaneFilter().getSearchInput(),"")
                .then("Check total users count, search result should be reset")
                .validate(not(visible), publishersPage.getTableProgressBar())
                .validate(filter.getSinglepaneFilter().getItemsTotalQuantityLabel(), format("(%s)",totalUsers))
                .and()
                .clickOnWebElement(filter.getSinglepaneFilter().getCancelButton())
                .testEnd();
    }

    @Test(description = "Check Back Created By filter", dependsOnMethods = "testSearchCreatedByColumnsFilterComponent")
    public void testBackCreatedByColumnsFilterComponent() {
        var filter = publishersPage.getTable().getColumnFiltersBlock();
        var userNameList = getCreatedByListFromBE();
        var searchName = userNameList.get(1).substring(5);
        var expectedUserNameList = getUsersListFromBE(searchName);
        var totalUsers = getTotalUsersFromBE();

        testStart()
                .and("Select Column Filter 'Created By'")
                .scrollIntoView(publishersPage.getPageTitle())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.CREATED_BY))
                .and()
                .setValueWithClean(filter.getSinglepaneFilter().getSearchInput(), "abc")
                .and("Click on Back")
                .clickOnWebElement(filter.getSinglepaneFilter().getBackButton())
                .waitAndValidate(appear, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.CREATED_BY))
                .then("Search params should be reset")
                .validate(filter.getSinglepaneFilter().getSearchInput(),"")
                .validate(filter.getSinglepaneFilter().getItemsTotalQuantityLabel(), format("(%s)",totalUsers))
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
                .setValueWithClean(filter.getSinglepaneFilter().getSearchInput(),"")
                .then("Check total users count, search result should be reset")
                .validate(not(visible), publishersPage.getTableProgressBar())
                .validate(filter.getSinglepaneFilter().getItemsTotalQuantityLabel(), format("(%s)",totalUsers))
                .testEnd();
    }

    @Test(description = "Check 'Created By' Chip Widget Component", dependsOnMethods = "testSearchCreatedByColumnsFilterComponent")
    public void testCreatedByChipWidgetComponent() {
        var filter = publishersPage.getTable().getColumnFiltersBlock();
        var table = publishersPage.getTable().getTableData();

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
                .getUsersWithFilter(Map.of("mail",name))
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
