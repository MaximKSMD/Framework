package rx.admin.users.columnsfilter;

import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.admin.users.UsersPage;
import rx.BaseTest;
import widgets.common.table.ColumnNames;
import zutils.StringUtils;

import java.util.List;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Users Columns Filter")
public class UsersColumnsFilterWidgetTests extends BaseTest {

    private UsersPage usersPage;

    public UsersColumnsFilterWidgetTests() {
        usersPage = new UsersPage();
    }

    @BeforeClass
    private void login() {
        var tableColumns = usersPage.getUsersTable().getShowHideColumns();

        testStart()
                .given()
                .openDirectPath(Path.USER)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, usersPage.getNuxtProgress())
                .scrollIntoView(tableColumns.getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .and("Select 15 rows per page")
                .scrollIntoView(usersPage.getUsersTable().getTablePagination().getPageMenu())
                .selectFromDropdown(usersPage.getUsersTable().getTablePagination().getPageMenu(),
                        usersPage.getUsersTable().getTablePagination().getRowNumbersList(), "15")
                .scrollIntoView(usersPage.getPageTitle())
                .testEnd();
    }

    @Test(description = "Check Active/Inactive Chip Widget Component")
    public void testActiveInactiveChipWidgetComponent() {
        var filter = usersPage.getUsersTable().getColumnFiltersBlock();
        var table = usersPage.getUsersTable().getTableData();

        testStart()
                .and("Click on 'Column Filters'")
                .scrollIntoView(usersPage.getPageTitle())
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
                .validateList(filter.getFilterOptionItems(), List.of(ColumnNames.ROLE.getName(),
                        ColumnNames.STATUS.getName(),
                        ColumnNames.PUBLISHER.getName(),
                        ColumnNames.CREATED_DATE.getName(),
                        ColumnNames.UPDATED_DATE.getName()))
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

    // TODO Fix checkbox issues
    @Ignore
    @Test(description = "Check Active/Inactive Chip Widget Component", dependsOnMethods = "testActiveInactiveChipWidgetComponent")
    public void testRoleChipWidgetComponent() {
        var filter = usersPage.getUsersTable().getColumnFiltersBlock();
        var table = usersPage.getUsersTable().getTableData();

        testStart()
                .and("Click on 'Column Filters'")
                .scrollIntoView(usersPage.getPageTitle())
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .and("Select Column Filter 'Role'")
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.ROLE))
                .then("Title should be displayed")
                .validate(filter.getRoleFilter().getFilterHeaderLabel(), StringUtils.getFilterHeader(ColumnNames.ROLE.getName()))
                .then("All options should be unselected")
                .validateAttribute(filter.getRoleFilter().getAdminPublisherCheckBox(),"aria-checked","false")
                .validateAttribute(filter.getRoleFilter().getCrossPublisherCheckBox(),"aria-checked","false")
                .validateAttribute(filter.getRoleFilter().getSinglePublisherCheckBox(),"aria-checked","false")
                .and("Select Admin")
                .selectCheckBox(filter.getRoleFilter().getAdminPublisherCheckBox())
                .validateAttribute(filter.getRoleFilter().getAdminPublisherCheckBox(),"aria-checked","true")
                .and("Select Cross Publisher")
                .selectCheckBox(filter.getRoleFilter().getCrossPublisherCheckBox())
                .then("Both Admin and Cross Publisher options should be selected")
                .validateAttribute(filter.getRoleFilter().getAdminPublisherCheckBox(),"aria-checked","true")
                .validateAttribute(filter.getRoleFilter().getCrossPublisherCheckBox(),"aria-checked","true")
                .and("Select Single Publisher")
                .selectCheckBox(filter.getRoleFilter().getSinglePublisherCheckBox())
                .then("All Admin, Cross Publisher and Single Publisher options should be selected")
                .validateAttribute(filter.getRoleFilter().getAdminPublisherCheckBox(),"aria-checked","true")
                .validateAttribute(filter.getRoleFilter().getCrossPublisherCheckBox(),"aria-checked","true")
                .validateAttribute(filter.getRoleFilter().getSinglePublisherCheckBox(),"aria-checked","true")
                .and("Click on Back")
                .clickOnWebElement(filter.getRoleFilter().getBackButton())
                .then("Columns Menu should appear")
                .validateList(filter.getFilterOptionItems(), List.of(ColumnNames.ROLE.getName(),
                        ColumnNames.STATUS.getName(),
                        ColumnNames.PUBLISHER.getName(),
                        ColumnNames.CREATED_DATE.getName(),
                        ColumnNames.UPDATED_DATE.getName()))
                .and("Select Column Filter 'Role'")
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.ROLE))
                .then("All options should be reset and unselected")
                .validateAttribute(filter.getRoleFilter().getAdminPublisherCheckBox(),"aria-checked","false")
                .validateAttribute(filter.getRoleFilter().getCrossPublisherCheckBox(),"aria-checked","false")
                .validateAttribute(filter.getRoleFilter().getSinglePublisherCheckBox(),"aria-checked","false")
                .and("Select Admin")
                .selectCheckBox(filter.getRoleFilter().getAdminPublisherCheckBox())
                .validateAttribute(filter.getRoleFilter().getAdminPublisherCheckBox(),"aria-checked","true")
                .and("Click on Submit")
                .clickOnWebElement(filter.getRoleFilter().getSubmitButton())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .validate(visible, table.getChipItemByName(ColumnNames.ROLE.getName()).getHeaderLabel())
                .validate(table.countFilterChipsItems(), 1)
                .then("Validate value on chip")
                .validate(table.getChipItemByName(ColumnNames.ROLE.getName()).getChipFilterOptionItemByName("Admin"))
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .and("Select Column Filter 'Role'")
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.ROLE))
                .and("Select Cross Publisher")
                .selectCheckBox(filter.getRoleFilter().getCrossPublisherCheckBox())
                .validateAttribute(filter.getRoleFilter().getCrossPublisherCheckBox(),"aria-checked","true")
                .and("Click on Submit")
                .clickOnWebElement(filter.getRoleFilter().getSubmitButton())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .validate(visible, table.getChipItemByName(ColumnNames.ROLE.getName()).getHeaderLabel())
                .validate(table.countFilterChipsItems(), 1)
                .then("Validate value on chip")
                .validate(table.getChipItemByName(ColumnNames.ROLE.getName()).getChipFilterOptionItemByName("Admin"))
                .validate(table.getChipItemByName(ColumnNames.ROLE.getName()).getChipFilterOptionItemByName("Cross-Publisher"))
                .clickOnWebElement(table.getChipItemByName(ColumnNames.ROLE.getName()).getCloseIcon())
                .testEnd();
    }

    @AfterClass
    private void logout() {
        testStart()
                .logOut()
                .testEnd();
    }
}
