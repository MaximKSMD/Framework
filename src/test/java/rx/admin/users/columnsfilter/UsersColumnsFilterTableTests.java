package rx.admin.users.columnsfilter;

import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Path;
import pages.admin.users.UsersPage;
import rx.BaseTest;
import widgets.common.table.ColumnNames;

import java.util.List;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Users Columns Filter")
public class UsersColumnsFilterTableTests extends BaseTest {

    private UsersPage usersPage;

    public UsersColumnsFilterTableTests() {
        usersPage = new UsersPage();
    }

    @BeforeClass
    private void login() {

         testStart()
                .given()
                .openDirectPath(Path.USER)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, usersPage.getNuxtProgress())
                 .and("Select 15 rows per page")
                 .scrollIntoView(usersPage.getUsersTable().getTablePagination().getPageMenu())
                 .selectFromDropdown(usersPage.getUsersTable().getTablePagination().getPageMenu(),
                         usersPage.getUsersTable().getTablePagination().getRowNumbersList(), "15")
                 .testEnd();
    }

    @Test(description = "Check columns filter options by default")
    public void defaultColumnsFilter(){
        var tableColumns = usersPage.getUsersTable();

        testStart()
                .scrollIntoView(usersPage.getPageTitle())
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(), List.of(ColumnNames.ROLE.getName(),
                                ColumnNames.STATUS.getName(),
                                ColumnNames.PUBLISHER.getName(),
                                ColumnNames.UPDATED_DATE.getName()))
                .testEnd();
    }

    @Test(description = "Select show all columns and check columns filter options")
    public void showAllColumns(){
        var tableColumns = usersPage.getUsersTable();

        testStart()
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .and("Select 10 rows per page")
                .scrollIntoView(usersPage.getUsersTable().getTablePagination().getPageMenu())
                .selectFromDropdown(usersPage.getUsersTable().getTablePagination().getPageMenu(),
                        usersPage.getUsersTable().getTablePagination().getRowNumbersList(), "10")
                .scrollIntoView(usersPage.getPageTitle())
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(), List.of(ColumnNames.ROLE.getName(),
                        ColumnNames.STATUS.getName(),
                        ColumnNames.PUBLISHER.getName(),
                        ColumnNames.CREATED_DATE.getName(),
                        ColumnNames.UPDATED_DATE.getName()))
                .testEnd();
    }

    @Test(description = "Hide all columns and check columns filter options")
    public void hideAllColumns(){
        var tableColumns = usersPage.getUsersTable();

        testStart()
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.ID))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.NAME))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.EMAIL))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.ROLE))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.STATUS))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.PUBLISHER))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .unSelectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.UPDATED_DATE))
                .and("Select 10 rows per page")
                .scrollIntoView(usersPage.getUsersTable().getTablePagination().getPageMenu())
                .selectFromDropdown(usersPage.getUsersTable().getTablePagination().getPageMenu(),
                        usersPage.getUsersTable().getTablePagination().getRowNumbersList(), "10")
                .scrollIntoView(usersPage.getPageTitle())
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(), List.of("No columns available"))
                .testEnd();
    }

    @Test(description = "Show all columns and refresh page. Check columns filter options")
    public void showAllAndRefreshPage(){
        var tableColumns = usersPage.getUsersTable();

        testStart()
                .clickBrowserRefreshButton()
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .and("Refresh page")
                .clickBrowserRefreshButton()
                .scrollIntoView(usersPage.getPageTitle())
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(), List.of(ColumnNames.ROLE.getName(),
                        ColumnNames.STATUS.getName(),
                        ColumnNames.PUBLISHER.getName(),
                        ColumnNames.UPDATED_DATE.getName()))
                .testEnd();
    }

    @Test(description = "Show all columns and reload page. Check columns filter options")
    public void showAllAndReloadPage(){
        var tableColumns = usersPage.getUsersTable();

        testStart()
                .scrollIntoView(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumns().getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .and("Navigate to Open Pricing page")
                .openDirectPath(Path.USER)
                .and("Navigate to Protection page again")
                .openDirectPath(Path.USER)
                .scrollIntoView(usersPage.getPageTitle())
                .clickOnWebElement(tableColumns.getColumnFiltersBlock().getColumnsFilterButton())
                .waitAndValidate(visible, tableColumns.getColumnFiltersBlock().getFilterOptionsMenu())
                .then("Validate options list")
                .validateList(tableColumns.getColumnFiltersBlock().getFilterOptionItems(), List.of(ColumnNames.ROLE.getName(),
                        ColumnNames.STATUS.getName(),
                        ColumnNames.PUBLISHER.getName(),
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
