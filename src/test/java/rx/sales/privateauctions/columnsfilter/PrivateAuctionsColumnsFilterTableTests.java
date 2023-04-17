package rx.sales.privateauctions.columnsfilter;

import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Path;
import pages.sales.privateauctions.PrivateAuctionsPage;
import rx.BaseTest;
import widgets.common.table.ColumnNames;

import java.util.List;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "PrivateAuctions Columns Filter")
public class PrivateAuctionsColumnsFilterTableTests extends BaseTest {

    private PrivateAuctionsPage privateAuctionsPage;

    public PrivateAuctionsColumnsFilterTableTests() {
        privateAuctionsPage = new PrivateAuctionsPage();
    }

    @BeforeClass
    private void login() {

         testStart()
                .given()
                .openDirectPath(Path.PRIVATE_AUCTIONS)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, privateAuctionsPage.getNuxtProgress())
                .testEnd();
    }

    @Test(description = "Check columns filter options by default")
    public void defaultColumnsFilter(){
        var tableColumns = privateAuctionsPage.getTable();

        testStart()
                .scrollIntoView(privateAuctionsPage.getPageTitle())
                .scrollIntoView(privateAuctionsPage.getTable().getTablePagination().getPageMenu())
                .selectFromDropdown(privateAuctionsPage.getTable().getTablePagination().getPageMenu(),
                        privateAuctionsPage.getTable().getTablePagination().getRowNumbersList(), "10")
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(), List.of(ColumnNames.PUBLISHER.getName(),
                        ColumnNames.OPTIMIZE.getName(),
                        ColumnNames.STATUS.getName(),
                        ColumnNames.ALWAYS_ON.getName(),
                        ColumnNames.START_DATE.getName(),
                        ColumnNames.END_DATE.getName(),
                        ColumnNames.UPDATED_DATE.getName()))
                .testEnd();
    }

    @Test(description = "Select show all columns and check columns filter options")
    public void showAllColumns(){
        var tableColumns = privateAuctionsPage.getTable();

        testStart()
                .scrollIntoView(privateAuctionsPage.getTable().getTablePagination().getPageMenu())
                .selectFromDropdown(privateAuctionsPage.getTable().getTablePagination().getPageMenu(),
                        privateAuctionsPage.getTable().getTablePagination().getRowNumbersList(), "10")
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .scrollIntoView(privateAuctionsPage.getPageTitle())
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(), List.of(ColumnNames.PUBLISHER.getName(),
                        ColumnNames.OPTIMIZE.getName(),
                        ColumnNames.STATUS.getName(),
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
    public void hideAllColumns(){
        var tableColumns = privateAuctionsPage.getTable();

        testStart()
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.ID))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.DETAILS))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.NAME))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.PUBLISHER))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.OPTIMIZE))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.STATUS))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.ALWAYS_ON))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.START_DATE))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.END_DATE))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.UPDATED_DATE))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .and("Select 10 rows per page")
                .scrollIntoView(privateAuctionsPage.getTable().getTablePagination().getPageMenu())
                .selectFromDropdown(privateAuctionsPage.getTable().getTablePagination().getPageMenu(),
                        privateAuctionsPage.getTable().getTablePagination().getRowNumbersList(), "10")
                .scrollIntoView(privateAuctionsPage.getPageTitle())
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(), List.of("No columns available"))
                .testEnd();
    }

    @Test(description = "Show all columns and refresh page. Check columns filter options")
    public void showAllAndRefreshPage(){
        var tableColumns = privateAuctionsPage.getTable();

        testStart()
                .clickBrowserRefreshButton()
                .selectFromDropdown(privateAuctionsPage.getTable().getTablePagination().getPageMenu(),
                        privateAuctionsPage.getTable().getTablePagination().getRowNumbersList(), "10")
                .clickOnWebElement(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .and("Refresh page")
                .clickBrowserRefreshButton()
                .selectFromDropdown(privateAuctionsPage.getTable().getTablePagination().getPageMenu(),
                        privateAuctionsPage.getTable().getTablePagination().getRowNumbersList(), "10")
                .scrollIntoView(privateAuctionsPage.getPageTitle())
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(), List.of(ColumnNames.PUBLISHER.getName(),
                        ColumnNames.OPTIMIZE.getName(),
                        ColumnNames.STATUS.getName(),
                        ColumnNames.ALWAYS_ON.getName(),
                        ColumnNames.START_DATE.getName(),
                        ColumnNames.END_DATE.getName(),
                        ColumnNames.UPDATED_DATE.getName()))
                .testEnd();
    }

    @Test(description = "Show all columns and reload page. Check columns filter options")
    public void showAllAndReloadPage(){
        var tableColumns = privateAuctionsPage.getTable();

        testStart()
                .scrollIntoView(privateAuctionsPage.getTable().getTablePagination().getPageMenu())
                .selectFromDropdown(privateAuctionsPage.getTable().getTablePagination().getPageMenu(),
                        privateAuctionsPage.getTable().getTablePagination().getRowNumbersList(), "10")
                .clickOnWebElement(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .and("Navigate to Open Pricing page")
                .openDirectPath(Path.PRIVATE_AUCTIONS)
                .and("Navigate to Protection page again")
                .openDirectPath(Path.PRIVATE_AUCTIONS)
                .scrollIntoView(privateAuctionsPage.getTable().getTablePagination().getPageMenu())
                .selectFromDropdown(privateAuctionsPage.getTable().getTablePagination().getPageMenu(),
                        privateAuctionsPage.getTable().getTablePagination().getRowNumbersList(), "10")
                .scrollIntoView(privateAuctionsPage.getPageTitle())
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(), List.of(ColumnNames.PUBLISHER.getName(),
                        ColumnNames.OPTIMIZE.getName(),
                        ColumnNames.STATUS.getName(),
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
