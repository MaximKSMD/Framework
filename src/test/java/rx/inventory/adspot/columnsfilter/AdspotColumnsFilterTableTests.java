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
import widgets.common.table.ColumnNames;

import java.util.List;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Adspot Columns Filter")
public class AdspotColumnsFilterTableTests extends BaseTest {

    private AdSpotsPage adSpotsPage;

    public AdspotColumnsFilterTableTests() {
        adSpotsPage = new AdSpotsPage();
    }

    @BeforeClass
    private void login() {

         testStart()
                .given()
                .openDirectPath(Path.AD_SPOT)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, adSpotsPage.getNuxtProgress())
                .testEnd();
    }

    @Test(description = "Check columns filter options by default")
    public void defaultColumnsFilter(){
        var tableColumns = adSpotsPage.getAdSpotsTable();

        testStart()
                .scrollIntoView(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu(),
                        adSpotsPage.getAdSpotsTable().getTablePagination().getRowNumbersList(), "10")
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(), List.of(ColumnNames.PUBLISHER.getName(),
                        ColumnNames.RELATED_MEDIA.getName(),
                        ColumnNames.STATUS.getName(),
                        ColumnNames.DEFAULT_SIZES.getName(),
                        ColumnNames.UPDATED_DATE.getName()))
                .testEnd();
    }

    @Test(description = "Select show all columns and check columns filter options")
    public void showAllColumns(){
        var tableColumns = adSpotsPage.getAdSpotsTable();

        testStart()
                .scrollIntoView(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu(),
                        adSpotsPage.getAdSpotsTable().getTablePagination().getRowNumbersList(), "10")
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.UPDATED_DATE))
                .and("Select 10 rows per page")
                .scrollIntoView(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu(),
                        adSpotsPage.getAdSpotsTable().getTablePagination().getRowNumbersList(), "10")
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(), List.of(ColumnNames.PUBLISHER.getName(),
                        ColumnNames.RELATED_MEDIA.getName(),
                        ColumnNames.STATUS.getName(),
                        ColumnNames.DEFAULT_SIZES.getName(),
                        ColumnNames.CREATED_DATE.getName(),
                        ColumnNames.UPDATED_DATE.getName()))
                .testEnd();
    }

    @Test(description = "Hide all columns and check columns filter options")
    public void hideAllColumns(){
        var tableColumns = adSpotsPage.getAdSpotsTable();

        testStart()
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.ID))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.DETAILS))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.NAME))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.PUBLISHER))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.RELATED_MEDIA))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.STATUS))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.PAGE_CATEGORY))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.TEST_MODE))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.DEFAULT_SIZES))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.DEFAULT_FLOOR_PRICE))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.UPDATED_DATE))
                .and("Select 10 rows per page")
                .scrollIntoView(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu(),
                        adSpotsPage.getAdSpotsTable().getTablePagination().getRowNumbersList(), "10")
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(), List.of("No columns available"))
                .testEnd();
    }

    @Test(description = "Show all columns and refresh page. Check columns filter options")
    public void showAllAndRefreshPage(){
        var tableColumns = adSpotsPage.getAdSpotsTable();

        testStart()
                .scrollIntoView(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu(),
                        adSpotsPage.getAdSpotsTable().getTablePagination().getRowNumbersList(), "10")
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.ID))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.DETAILS))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.NAME))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.PUBLISHER))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.RELATED_MEDIA))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.STATUS))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.PAGE_CATEGORY))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.TEST_MODE))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.DEFAULT_SIZES))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.DEFAULT_FLOOR_PRICE))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.UPDATED_DATE))
                .and("Refresh page")
                .clickBrowserRefreshButton()
                .scrollIntoView(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(adSpotsPage.getAdSpotsTable().getTablePagination().getPageMenu(),
                        adSpotsPage.getAdSpotsTable().getTablePagination().getRowNumbersList(), "10")
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(), List.of(ColumnNames.PUBLISHER.getName(),
                        ColumnNames.RELATED_MEDIA.getName(),
                        ColumnNames.STATUS.getName(),
                        ColumnNames.DEFAULT_SIZES.getName(),
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
