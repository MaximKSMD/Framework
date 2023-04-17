package rx.yield.openpricing.columnsfilter;

import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Path;
import pages.yield.openpricing.OpenPricingPage;
import rx.BaseTest;
import widgets.common.table.ColumnNames;

import java.util.List;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Open Pricing Columns Filter")
public class OpenPricingColumnsFilterTableTests extends BaseTest {

    private OpenPricingPage openPricingPage;

    public OpenPricingColumnsFilterTableTests() {
        openPricingPage = new OpenPricingPage();
    }

    @BeforeClass
    private void login() {

         testStart()
                .given()
                .openDirectPath(Path.OPEN_PRICING)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                 .scrollIntoView(openPricingPage.getOpenPricingTable().getTablePagination().getPageMenu())
                 .selectFromDropdown(openPricingPage.getOpenPricingTable().getTablePagination().getPageMenu(),
                         openPricingPage.getOpenPricingTable().getTablePagination().getRowNumbersList(), "15")
                 .testEnd();
    }

    @Test(description = "Check columns filter options by default")
    public void defaultColumnsFilter(){
        var tableColumns = openPricingPage.getOpenPricingTable();

        testStart()
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(), List.of(ColumnNames.PUBLISHER.getName(),
                        ColumnNames.STATUS.getName(),
                        ColumnNames.UPDATED_DATE.getName()))
                .testEnd();
    }

    @Test(description = "Select show all columns and check columns filter options")
    public void showAllColumns(){
        var tableColumns = openPricingPage.getOpenPricingTable();

        testStart()
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .and("Select 15 rows per page")
                .scrollIntoView(openPricingPage.getOpenPricingTable().getTablePagination().getPageMenu())
                .selectFromDropdown(openPricingPage.getOpenPricingTable().getTablePagination().getPageMenu(),
                        openPricingPage.getOpenPricingTable().getTablePagination().getRowNumbersList(), "15")
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(), List.of(ColumnNames.PUBLISHER.getName(),
                        ColumnNames.STATUS.getName(),
                        ColumnNames.CREATED_DATE.getName(),
                        ColumnNames.CREATED_BY.getName(),
                        ColumnNames.UPDATED_DATE.getName(),
                        ColumnNames.UPDATED_BY.getName()))
                .testEnd();
    }

    @Test(description = "Hide all columns and check columns filter options")
    public void hideAllColumns(){
        var tableColumns = openPricingPage.getOpenPricingTable();

        testStart()
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.ID))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.DETAILS))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.NAME))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.PUBLISHER))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.STATUS))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.FLOOR_PRICE))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.UPDATED_DATE))
                .and("Select 15 rows per page")
                .scrollIntoView(openPricingPage.getOpenPricingTable().getTablePagination().getPageMenu())
                .selectFromDropdown(openPricingPage.getOpenPricingTable().getTablePagination().getPageMenu(),
                        openPricingPage.getOpenPricingTable().getTablePagination().getRowNumbersList(), "15")
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(), List.of("No columns available"))
                .testEnd();
    }

    @Test(description = "Show all columns and refresh page. Check columns filter options")
    public void showAllAndRefreshPage(){
        var tableColumns = openPricingPage.getOpenPricingTable();

        testStart()
                .clickBrowserRefreshButton()
                .scrollIntoView(openPricingPage.getOpenPricingTable().getTablePagination().getPageMenu())
                .selectFromDropdown(openPricingPage.getOpenPricingTable().getTablePagination().getPageMenu(),
                        openPricingPage.getOpenPricingTable().getTablePagination().getRowNumbersList(), "15")
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .and("Refresh page")
                .clickBrowserRefreshButton()
                .and("Select 15 rows per page")
                .scrollIntoView(openPricingPage.getOpenPricingTable().getTablePagination().getPageMenu())
                .selectFromDropdown(openPricingPage.getOpenPricingTable().getTablePagination().getPageMenu(),
                        openPricingPage.getOpenPricingTable().getTablePagination().getRowNumbersList(), "15")
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(), List.of(
                        ColumnNames.PUBLISHER.getName(),
                        ColumnNames.STATUS.getName(),
                        ColumnNames.UPDATED_DATE.getName()))
                .testEnd();
    }

    @Test(description = "Show all columns and reload page. Check columns filter options")
    public void showAllAndReloadPage(){
        var tableColumns = openPricingPage.getOpenPricingTable();

        testStart()
                .scrollIntoView(openPricingPage.getOpenPricingTable().getTablePagination().getPageMenu())
                .selectFromDropdown(openPricingPage.getOpenPricingTable().getTablePagination().getPageMenu(),
                        openPricingPage.getOpenPricingTable().getTablePagination().getRowNumbersList(), "15")
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .and("Navigate to Open Pricing page")
                .openDirectPath(Path.OPEN_PRICING)
                .and("Navigate to Protection page again")
                .openDirectPath(Path.OPEN_PRICING)
                .scrollIntoView(openPricingPage.getOpenPricingTable().getTablePagination().getPageMenu())
                .selectFromDropdown(openPricingPage.getOpenPricingTable().getTablePagination().getPageMenu(),
                        openPricingPage.getOpenPricingTable().getTablePagination().getRowNumbersList(), "10")
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(), List.of(
                        ColumnNames.PUBLISHER.getName(),
                        ColumnNames.STATUS.getName(),
                        ColumnNames.UPDATED_DATE.getName()))
                .testEnd();
    }

    @AfterClass
    private void logout() {
        testStart()
                .logOut()
                .testEnd();
    }
}
