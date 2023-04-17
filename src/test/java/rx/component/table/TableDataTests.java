package rx.component.table;

import api.dto.rx.admin.publisher.Publisher;
import api.preconditionbuilders.PublisherPrecondition;
import com.codeborne.selenide.testng.ScreenShooter;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Path;
import pages.admin.publisher.PublishersPage;
import rx.BaseTest;
import widgets.admin.publisher.sidebar.EditPublisherSidebar;
import widgets.common.table.ColumnNames;
import widgets.common.table.Statuses;

import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
public class TableDataTests extends BaseTest {

    private Publisher publisher;
    private PublishersPage publishersPage;
    private EditPublisherSidebar editPublisherSidebar;

    public TableDataTests() {
        publishersPage = new PublishersPage();
        editPublisherSidebar = new EditPublisherSidebar();
    }

    @BeforeClass
    public void createNewPublisher() {

        //Creating publisher to edit Using API
        publisher = PublisherPrecondition.publisher()
                .createNewPublisher()
                .build()
                .getPublisherResponse();
    }

    @Test
    public void checkColumns() {
        var tableData = publishersPage.getTable().getTableData();
        var table = publishersPage.getTable().getShowHideColumns();
        var filterOptions = publishersPage.getTable().getColumnFiltersBlock();

        testStart()
                .given()
                .openDirectPath(Path.PUBLISHER)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, publishersPage.getNuxtProgress())
                .and()
                .clickOnWebElement(table.getShowHideColumnsBtn())
                .selectCheckBox(table.getMenuItemCheckbox(ColumnNames.NAME))
                .selectCheckBox(table.getMenuItemCheckbox(ColumnNames.CATEGORY))
                .selectCheckBox(table.getMenuItemCheckbox(ColumnNames.ID))
                .selectCheckBox(table.getMenuItemCheckbox(ColumnNames.DOMAIN))
                .selectCheckBox(table.getMenuItemCheckbox(ColumnNames.CURRENCY))
                .selectCheckBox(table.getMenuItemCheckbox(ColumnNames.AD_OPS_PERSON))
                .selectCheckBox(table.getMenuItemCheckbox(ColumnNames.MAIL))
                .clickOnWebElement(table.getShowHideColumnsBtn())
                .clickOnWebElement(filterOptions.getColumnsFilterButton())
                .clickOnWebElement(filterOptions.getFilterOptionByName(ColumnNames.STATUS))
                .selectRadioButton(filterOptions.getActiveBooleanFilter().getActiveRadioButton())
                .clickOnWebElement(filterOptions.getActiveBooleanFilter().getSubmitButton())
                .then()
                .validateListSize(tableData.getColumns(),
                        ColumnNames.NAME.getName(),
                        ColumnNames.CATEGORY.getName(),
                        ColumnNames.STATUS.getName(),
                        ColumnNames.ID.getName(),
                        ColumnNames.DOMAIN.getName(),
                        ColumnNames.CURRENCY.getName(),
                        ColumnNames.AD_OPS_PERSON.getName(),
                        ColumnNames.MAIL.getName(),
                        ColumnNames.UPDATED_DATE.getName())
                .clickOnWebElement(table.getShowHideColumnsBtn())
                .unSelectCheckBox(table.getMenuItemCheckbox(ColumnNames.NAME))
                .unSelectCheckBox(table.getMenuItemCheckbox(ColumnNames.CATEGORY))
                .unSelectCheckBox(table.getMenuItemCheckbox(ColumnNames.DOMAIN))
                .unSelectCheckBox(table.getMenuItemCheckbox(ColumnNames.CURRENCY))
                .unSelectCheckBox(table.getMenuItemCheckbox(ColumnNames.AD_OPS_PERSON))
                .unSelectCheckBox(table.getMenuItemCheckbox(ColumnNames.MAIL))
                .unSelectCheckBox(table.getMenuItemCheckbox(ColumnNames.UPDATED_DATE))
                .clickOnWebElement(filterOptions.getColumnsFilterButton())
                .clickOnWebElement(filterOptions.getFilterOptionByName(ColumnNames.STATUS))
                .selectRadioButton(filterOptions.getActiveBooleanFilter().getInactiveRadioButton())
                .clickOnWebElement(filterOptions.getActiveBooleanFilter().getSubmitButton())
                .then()
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.PUBLISHER.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.CATEGORY.getName()))
                .validate(visible, tableData.getColumnHeader(ColumnNames.STATUS.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.DOMAIN.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.CURRENCY.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.AD_OPS_PERSON.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.MAIL.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.UPDATED_DATE.getName()))
                .validate(visible, tableData.getColumnHeader(ColumnNames.ID.getName()))
                .and()
                .clickOnWebElement(table.getShowHideColumnsBtn())
                .logOut()
                .testEnd();

    }

    @Test
    public void checkPagination() {
        var tableData = publishersPage.getTable().getTableData();
        var filterOptions = publishersPage.getTable().getColumnFiltersBlock();
        var tablePagenation = publishersPage.getTable().getTablePagination();
        testStart()
                .given()
                .openDirectPath(Path.PUBLISHER)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, publishersPage.getNuxtProgress())
                .and()
                .clickOnWebElement(filterOptions.getColumnsFilterButton())
                .clickOnWebElement(filterOptions.getFilterOptionByName(ColumnNames.STATUS))
                .selectRadioButton(filterOptions.getActiveBooleanFilter().getActiveRadioButton())
                .clickOnWebElement(filterOptions.getActiveBooleanFilter().getSubmitButton())
                .selectFromDropdown(tablePagenation.getPageMenu(), tablePagenation.getRowNumbersList(), "10")
                .waitLoading(visible, publishersPage.getTableProgressBar())
                .waitLoading(disappear, publishersPage.getTableProgressBar())
                .validateListSize(tableData.getRows(), 10)
                .validateListContainsTextOnly(tableData.getCustomCells(ColumnNames.STATUS),
                        Statuses.ACTIVE.getStatus())
                .clickOnWebElement(tablePagenation.getNext())
                .waitLoading(visible, publishersPage.getTableProgressBar())
                .waitLoading(disappear, publishersPage.getTableProgressBar())
                .validateListContainsTextOnly(tableData.getCustomCells(ColumnNames.STATUS),
                        Statuses.ACTIVE.getStatus())
                .logOut()
                .testEnd();

    }

    @Test
    public void checkSearch() {
        var tableData = publishersPage.getTable().getTableData();
        testStart()
                .given()
                .openDirectPath(Path.PUBLISHER)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, publishersPage.getNuxtProgress())
                .and()
                .selectCheckBox(tableData.getCheckbox(1))
                .setValueWithClean(tableData.getSearch(), publisher.getName())
                .pressEnterKey(tableData.getSearch())
                .waitLoading(visible, publishersPage.getTableProgressBar())
                .waitLoading(disappear, publishersPage.getTableProgressBar())
                .then()
                .validateListContainsTextOnly(tableData.getCustomCells(ColumnNames.NAME),
                        publisher.getName())
                .and()
                .clickOnTableCellLink(tableData, ColumnNames.NAME, publisher.getName())
                .waitSideBarOpened()
                .clickOnWebElement(editPublisherSidebar.getSaveButton())
                .waitSideBarClosed()
                .logOut()
                .testEnd();
    }
}
