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

import java.util.List;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Demand Source Columns Filter")
public class DemandSourceColumnsFilterTableTests extends BaseTest {

    private DemandPage demandPage;

    public DemandSourceColumnsFilterTableTests() {
        demandPage = new DemandPage();
    }

    @BeforeClass
    private void login() {

         testStart()
                .given()
                .openDirectPath(Path.DEMAND)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, demandPage.getNuxtProgress())
                .testEnd();
    }

    @Test(description = "Check columns filter options by default")
    public void defaultColumnsFilter(){
        var tableColumns = demandPage.getDemandTable();

        testStart()
                .scrollIntoView(demandPage.getDemandTable().getTablePagination().getPageMenu())
                .selectFromDropdown(demandPage.getDemandTable().getTablePagination().getPageMenu(),
                        demandPage.getDemandTable().getTablePagination().getRowNumbersList(), "10")
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(), List.of(ColumnNames.STATUS.getName(),
                        ColumnNames.UPDATED_DATE.getName()))
                .testEnd();
    }

    @Test(description = "Select show all columns and check columns filter options")
    public void showAllColumns(){
        var tableColumns = demandPage.getDemandTable();

        testStart()
                .scrollIntoView(demandPage.getDemandTable().getTablePagination().getPageMenu())
                .selectFromDropdown(demandPage.getDemandTable().getTablePagination().getPageMenu(),
                        demandPage.getDemandTable().getTablePagination().getRowNumbersList(), "10")
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.DSP_ID))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.DETAILS))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.NAME))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .and("Select 10 rows per page")
                .scrollIntoView(demandPage.getDemandTable().getTablePagination().getPageMenu())
                .selectFromDropdown(demandPage.getDemandTable().getTablePagination().getPageMenu(),
                        demandPage.getDemandTable().getTablePagination().getRowNumbersList(), "10")
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(), List.of(ColumnNames.STATUS.getName(),
                        ColumnNames.CREATED_DATE.getName(),
                        ColumnNames.CREATED_BY.getName(),
                        ColumnNames.UPDATED_DATE.getName(),
                        ColumnNames.UPDATED_BY.getName()))
                .testEnd();
    }

    @Test(description = "Hide all columns and check columns filter options")
    public void hideAllColumns(){
        var tableColumns = demandPage.getDemandTable();

        testStart()
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.DSP_ID))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.DETAILS))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.STATUS))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.NAME))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.UPDATED_DATE))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .and("Select 10 rows per page")
                .scrollIntoView(demandPage.getDemandTable().getTablePagination().getPageMenu())
                .selectFromDropdown(demandPage.getDemandTable().getTablePagination().getPageMenu(),
                        demandPage.getDemandTable().getTablePagination().getRowNumbersList(), "10")
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(), List.of("No columns available"))
                .testEnd();
    }

    @Test(description = "Show all columns and refresh page. Check columns filter options")
    public void showAllAndRefreshPage(){
        var tableColumns = demandPage.getDemandTable();

        testStart()
                .scrollIntoView(demandPage.getDemandTable().getTablePagination().getPageMenu())
                .selectFromDropdown(demandPage.getDemandTable().getTablePagination().getPageMenu(),
                        demandPage.getDemandTable().getTablePagination().getRowNumbersList(), "10")
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.DSP_ID))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.DETAILS))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.NAME))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .and("Refresh page")
                .clickBrowserRefreshButton()
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(), List.of(ColumnNames.STATUS.getName(),
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
