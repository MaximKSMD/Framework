package rx.admin.demand.columnsfilter;

import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Path;
import pages.admin.demand.DemandPage;
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
@Feature(value = "Demand Source Columns Filter")
public class DemandSourceColumnsFilterWidgetTests extends BaseTest {

    private DemandPage demandPage;

    public DemandSourceColumnsFilterWidgetTests() {
        demandPage = new DemandPage();
    }

    @BeforeClass
    private void login() {
        var tableColumns = demandPage.getDemandTable().getShowHideColumns();

        testStart()
                .given()
                .openDirectPath(Path.DEMAND)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, demandPage.getNuxtProgress())
                .scrollIntoView(demandPage.getDemandTable().getTablePagination().getPageMenu())
                .selectFromDropdown(demandPage.getDemandTable().getTablePagination().getPageMenu(),
                        demandPage.getDemandTable().getTablePagination().getRowNumbersList(), "10")
                .scrollIntoView(tableColumns.getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .selectCheckBox(tableColumns.getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .testEnd();
    }

    @Test(description = "Check Active/Inactive Chip Widget Component", priority = 1)
    public void testActiveInactiveChipWidgetComponent() {
        var filter = demandPage.getDemandTable().getColumnFiltersBlock();
        var table = demandPage.getDemandTable().getTableData();

        testStart()
                .and("Click on 'Column Filters'")
                .scrollIntoView(demandPage.getPageTitle())
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

    @Test(description = "Check Search Updated By", priority = 2)
    public void testSearchUpdatedByColumnsFilterComponent() {
        var filter = demandPage.getDemandTable().getColumnFiltersBlock();
        var userNameList = getUpdatedByListFromBE();
        var searchName = userNameList.get(1).substring(5);
        var expectedUserNameList = getUsersListFromBE(searchName);
        var totalUsers = getTotalUsersFromBE();
        var tableColumns = demandPage.getDemandTable();

        testStart()
                .and("Select Column Filter 'Updated By'")
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
                .validate(not(visible), demandPage.getTableProgressBar())
                .validate(filter.getSinglepaneFilter().getItemsTotalQuantityLabel(), format("(%s)",totalUsers))
                .testEnd();
    }

    @Test(description = "Check 'Updated By' Chip Widget Component", dependsOnMethods = "testSearchUpdatedByColumnsFilterComponent")
    public void testUpdatedByChipWidgetComponent() {
        var filter = demandPage.getDemandTable().getColumnFiltersBlock();
        var table = demandPage.getDemandTable().getTableData();

        testStart()
                .and("Select Users")
                .clickOnWebElement(filter.getSinglepaneFilter().getFilterItemByPositionInList(1).getName())
                .clickOnWebElement(filter.getSinglepaneFilter().getFilterItemByPositionInList(2).getName())
                .clickOnWebElement(filter.getSinglepaneFilter().getFilterItemByPositionInList(3).getName())
                .and("Click on Submit")
                .clickOnWebElement(filter.getSinglepaneFilter().getSubmitButton())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .validate(visible, table.getChipItemByName(ColumnNames.UPDATED_BY.getName()).getHeaderLabel())
                .validate(table.countFilterChipsItems(), 1)
                .then("Validate list of selected users")
                .validate(table.getChipItemByName(ColumnNames.UPDATED_BY.getName()).countFilterOptionsChipItems(), 3)
                .clickOnWebElement(table.getChipItemByName(ColumnNames.UPDATED_BY.getName()).getCloseIcon())
                .testEnd();
    }

    @Test(description = "Check Search Created By", dependsOnMethods = "testUpdatedByChipWidgetComponent")
    public void testSearchCreatedByColumnsFilterComponent() {
        var filter = demandPage.getDemandTable().getColumnFiltersBlock();
        var userNameList = getCreatedByListFromBE();
        var searchName = userNameList.get(1).substring(5);
        var expectedUserNameList = getUsersListFromBE(searchName);
        var totalUsers = getTotalUsersFromBE();
        var tableColumns = demandPage.getDemandTable().getShowHideColumns();

        testStart()
                .and("Select Column Filter 'Created By'")
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
                .validate(not(visible), demandPage.getTableProgressBar())
                .validate(filter.getSinglepaneFilter().getItemsTotalQuantityLabel(), format("(%s)",totalUsers))
                .and()
                .clickOnWebElement(filter.getSinglepaneFilter().getCancelButton())
                .testEnd();
    }

    @Test(description = "Check Back Created By filter", dependsOnMethods = "testSearchCreatedByColumnsFilterComponent")
    public void testBackCreatedByColumnsFilterComponent() {
        var filter = demandPage.getDemandTable().getColumnFiltersBlock();
        var userNameList = getCreatedByListFromBE();
        var searchName = userNameList.get(1).substring(5);
        var expectedUserNameList = getUsersListFromBE(searchName);
        var totalUsers = getTotalUsersFromBE();

        testStart()
                .and("Select Column Filter 'Created By'")
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
                .validate(not(visible), demandPage.getTableProgressBar())
                .validate(filter.getSinglepaneFilter().getItemsTotalQuantityLabel(), format("(%s)",totalUsers))
                .testEnd();
    }

    @Test(description = "Check 'Created By' Chip Widget Component", dependsOnMethods = "testSearchCreatedByColumnsFilterComponent")
    public void testCreatedByChipWidgetComponent() {
        var filter = demandPage.getDemandTable().getColumnFiltersBlock();
        var table = demandPage.getDemandTable().getTableData();

        testStart()
                .and("Select Users")
                .clickOnWebElement(filter.getSinglepaneFilter().getFilterItemByPositionInList(1).getName())
                .clickOnWebElement(filter.getSinglepaneFilter().getFilterItemByPositionInList(2).getName())
                .clickOnWebElement(filter.getSinglepaneFilter().getFilterItemByPositionInList(3).getName())
                .and("Click on Submit")
                .clickOnWebElement(filter.getSinglepaneFilter().getSubmitButton())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .validate(visible, table.getChipItemByName(ColumnNames.CREATED_BY.getName()).getHeaderLabel())
                .validate(table.countFilterChipsItems(), 1)
                .then("Validate list of selected users")
                .validate(table.getChipItemByName(ColumnNames.CREATED_BY.getName()).countFilterOptionsChipItems(), 3)
                .clickOnWebElement(table.getChipItemByName(ColumnNames.CREATED_BY.getName()).getCloseIcon())
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
