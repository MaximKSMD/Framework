package rx.sales.deals.columnsfilter;

import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Path;
import pages.sales.deals.DealsPage;
import rx.BaseTest;
import widgets.common.table.ColumnNames;

import java.util.List;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Deals Columns Filter")
public class DealsColumnsFilterTableTests extends BaseTest {

    private DealsPage dealsPage;

    public DealsColumnsFilterTableTests() {
        dealsPage = new DealsPage();
    }

    @BeforeClass
    private void login() {

        testStart()
                .given()
                .openDirectPath(Path.DEALS)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, dealsPage.getNuxtProgress())
                .and("Select 10 rows per page")
                .scrollIntoView(dealsPage.getDealsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(dealsPage.getDealsTable().getTablePagination().getPageMenu(),
                        dealsPage.getDealsTable().getTablePagination().getRowNumbersList(), "10")
                .scrollIntoView(dealsPage.getPageTitle())
                .testEnd();
    }

    @Test(description = "Check columns filter options by default")
    public void defaultColumnsFilter() {

        var tableColumns = dealsPage.getDealsTable();

        testStart()
                .scrollIntoView(dealsPage.getPageTitle())
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(),
                        List.of(ColumnNames.PUBLISHER.getName(),
                                ColumnNames.PRIVATE_AUCTION.getName(),
                                ColumnNames.STATUS.getName(),
                                ColumnNames.DSP.getName(),
                                ColumnNames.PRICE_CURRENCY.getName(),
                                ColumnNames.ALWAYS_ON.getName(),
                                ColumnNames.START_DATE.getName(),
                                ColumnNames.END_DATE.getName(),
                                ColumnNames.UPDATED_DATE.getName()))
                .testEnd();
    }

    @Test(description = "Select show all columns and check columns filter options")
    public void showAllColumns() {

        var tableColumns = dealsPage.getDealsTable();

        testStart()
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.UPDATED_DATE))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .and("Select 10 rows per page")
                .scrollIntoView(dealsPage.getDealsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(dealsPage.getDealsTable().getTablePagination().getPageMenu(),
                        dealsPage.getDealsTable().getTablePagination().getRowNumbersList(), "10")
                .scrollIntoView(dealsPage.getPageTitle())
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(),
                        List.of(ColumnNames.PUBLISHER.getName(),
                                ColumnNames.PRIVATE_AUCTION.getName(),
                                ColumnNames.STATUS.getName(),
                                ColumnNames.DSP.getName(),
                                ColumnNames.PRICE_CURRENCY.getName(),
                                ColumnNames.ALWAYS_ON.getName(),
                                ColumnNames.START_DATE.getName(),
                                ColumnNames.END_DATE.getName(),
                                ColumnNames.CREATED_DATE.getName(),
                                ColumnNames.CREATED_BY.getName(),
                                ColumnNames.UPDATED_DATE.getName(),
                                ColumnNames.UPDATED_BY.getName()))
                .testEnd();
    }

    @Test(description = "Hide all columns and check columns filter options")
    public void hideAllColumns() {

        var tableColumns = dealsPage.getDealsTable();

        testStart()
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.PRIVATE_AUCTION))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.PRICE_CURRENCY))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.UPDATED_DATE))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.PRICE_VALUE))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.START_DATE))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.ALWAYS_ON))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.PUBLISHER))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.END_DATE))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.DETAILS))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.STATUS))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.NAME))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.DSP))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.ID))
                .and("Select 10 rows per page")
                .scrollIntoView(dealsPage.getDealsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(dealsPage.getDealsTable().getTablePagination().getPageMenu(),
                        dealsPage.getDealsTable().getTablePagination().getRowNumbersList(), "10")
                .scrollIntoView(dealsPage.getPageTitle())
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(), List.of("No columns available"))
                .testEnd();
    }

    @Test(description = "Show all columns and refresh page. Check columns filter options")
    public void showAllAndRefreshPage() {

        var tableColumns = dealsPage.getDealsTable();

        testStart()
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.UPDATED_DATE))
                .and("Refresh page")
                .clickBrowserRefreshButton()
                .and("Select 10 rows per page")
                .scrollIntoView(dealsPage.getDealsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(dealsPage.getDealsTable().getTablePagination().getPageMenu(),
                        dealsPage.getDealsTable().getTablePagination().getRowNumbersList(), "10")
                .scrollIntoView(dealsPage.getPageTitle())
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(),
                        List.of(ColumnNames.PUBLISHER.getName(),
                                ColumnNames.PRIVATE_AUCTION.getName(),
                                ColumnNames.STATUS.getName(),
                                ColumnNames.DSP.getName(),
                                ColumnNames.PRICE_CURRENCY.getName(),
                                ColumnNames.ALWAYS_ON.getName(),
                                ColumnNames.START_DATE.getName(),
                                ColumnNames.END_DATE.getName(),
                                ColumnNames.UPDATED_DATE.getName()))
                .testEnd();
    }

    @Test(description = "Show all columns and reload page. Check columns filter options")
    public void showAllAndReloadPage() {

        var tableColumns = dealsPage.getDealsTable();

        testStart()
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.UPDATED_DATE))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .and("Navigate to Open Pricing page")
                .openDirectPath(Path.OPEN_PRICING)
                .and("Navigate to Deals page again")
                .openDirectPath(Path.DEALS)
                .and("Select 10 rows per page")
                .scrollIntoView(dealsPage.getDealsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(dealsPage.getDealsTable().getTablePagination().getPageMenu(),
                        dealsPage.getDealsTable().getTablePagination().getRowNumbersList(), "10")
                .scrollIntoView(dealsPage.getPageTitle())
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(),
                        List.of(ColumnNames.PUBLISHER.getName(),
                                ColumnNames.PRIVATE_AUCTION.getName(),
                                ColumnNames.STATUS.getName(),
                                ColumnNames.DSP.getName(),
                                ColumnNames.PRICE_CURRENCY.getName(),
                                ColumnNames.ALWAYS_ON.getName(),
                                ColumnNames.START_DATE.getName(),
                                ColumnNames.END_DATE.getName(),
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