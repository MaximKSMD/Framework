package rx.yield.openpricing;

import api.dto.rx.yield.openpricing.OpenPricing;
import api.preconditionbuilders.OpenPricingPrecondition;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Ignore;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Path;
import pages.yield.openpricing.OpenPricingPage;
import rx.BaseTest;
import widgets.common.table.ColumnNames;
import widgets.yield.openPricing.sidebar.EditOpenPricingSidebar;

import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Open Pricing")
public class OpenPricingTests extends BaseTest {

    private OpenPricing openPricing;
    private OpenPricingPage openPricingPage;
    private EditOpenPricingSidebar editOpenPricingSidebar;

    public OpenPricingTests() {
        openPricingPage = new OpenPricingPage();
        editOpenPricingSidebar = new EditOpenPricingSidebar();
    }

    @BeforeClass
    public void createOpenPricing() {
        openPricing = OpenPricingPrecondition.openPricing()
                .createNewOpenPricing()
                .build()
                .getOpenPricingResponse();
    }

    @Test(enabled = true)
    @Ignore
    public void checkPagination() {
        var table = openPricingPage.getOpenPricingTable().getShowHideColumns();
        var tableData = openPricingPage.getOpenPricingTable().getTableData();
        var tablePagination = openPricingPage.getOpenPricingTable().getTablePagination();
        testStart()
                .given()
                .openDirectPath(Path.OPEN_PRICING)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                .and()
                .selectFromDropdown(tablePagination.getPageMenu(), tablePagination.getRowNumbersList(), "10")
                .waitLoading(visible, openPricingPage.getTableProgressBar())
                .waitLoading(disappear, openPricingPage.getTableProgressBar())
                .then()
                .validateListSize(tableData.getRows(), 10)
                .clickOnWebElement(tablePagination.getNext())
                .selectFromDropdown(tablePagination.getPageMenu(), tablePagination.getRowNumbersList(), "20")
                .waitLoading(visible, openPricingPage.getTableProgressBar())
                .waitLoading(disappear, openPricingPage.getTableProgressBar())
                .then()
                .validateListSize(tableData.getRows(), 20)
                .clickOnWebElement(tablePagination.getNext())
                .validateListSize(tableData.getRows(), 20)
                .selectFromDropdown(tablePagination.getPageMenu(), tablePagination.getRowNumbersList(), "15")
                .waitLoading(visible, openPricingPage.getTableProgressBar())
                .waitLoading(disappear, openPricingPage.getTableProgressBar())
                .then()
                .validateListSize(tableData.getRows(), 15)
                .clickOnWebElement(tablePagination.getNext())
                .validateListSize(tableData.getRows(), 15)
                .clickOnWebElement(tablePagination.getPrevious())
                .validateListSize(tableData.getRows(), 15)
                .selectFromDropdown(tablePagination.getPageMenu(), tablePagination.getRowNumbersList(), "25")
                .waitLoading(visible, openPricingPage.getTableProgressBar())
                .waitLoading(disappear, openPricingPage.getTableProgressBar())
                .then()
                .validateListSize(tableData.getRows(), 25)
                .clickOnWebElement(tablePagination.getNext())
                .validateListSize(tableData.getRows(), 25)
                .selectFromDropdown(tablePagination.getPageMenu(), tablePagination.getRowNumbersList(), "50")
                .waitLoading(visible, openPricingPage.getTableProgressBar())
                .waitLoading(disappear, openPricingPage.getTableProgressBar())
                .then()
                .validateListSize(tableData.getRows(), 50)
                .selectFromDropdown(tablePagination.getPageMenu(), tablePagination.getRowNumbersList(), "100")
                .waitLoading(visible, openPricingPage.getTableProgressBar())
                .waitLoading(disappear, openPricingPage.getTableProgressBar())
                .then()
                .validateListSize(tableData.getRows(), 100)
                .clickOnWebElement(tablePagination.getNext())
                .validateListSize(tableData.getRows(), 100)
                .logOut()
                .testEnd();
    }

    @Test(enabled = true)
    @Ignore
    public void verifyColumnOrder() {
        var tableData = openPricingPage.getOpenPricingTable().getTableData();
        var tableOptions = openPricingPage.getOpenPricingTable().getShowHideColumns();
        var tablePagination = openPricingPage.getOpenPricingTable().getTablePagination();
        testStart()
                .given()
                .openDirectPath(Path.OPEN_PRICING)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                .when()
                .selectFromDropdown(tablePagination.getPageMenu(), tablePagination.getRowNumbersList(), "10")
                .waitLoading(visible, openPricingPage.getTableProgressBar())
                .waitLoading(disappear, openPricingPage.getTableProgressBar())
                .and()
                .validateContainsText(tablePagination.getPaginationPanel(), "1-10 of ")
                .validateListSize(tableData.getColumns(),
                        ColumnNames.ID.getName(),
                        ColumnNames.DETAILS.getName(),
                        ColumnNames.NAME.getName(),
                        ColumnNames.PUBLISHER.getName(),
                        ColumnNames.STATUS.getName(),
                        ColumnNames.FLOOR_PRICE.getName())
                .validate((tableData.getColumns()).get(0), ColumnNames.ID.getName())
                .validate((tableData.getColumns()).get(1), ColumnNames.DETAILS.getName())
                .validate((tableData.getColumns()).get(2), ColumnNames.NAME.getName())
                .validate((tableData.getColumns()).get(3), ColumnNames.PUBLISHER.getName())
                .validate((tableData.getColumns()).get(4), ColumnNames.STATUS.getName())
                .validate((tableData.getColumns()).get(5), ColumnNames.FLOOR_PRICE.getName())
                .and()
                .scrollIntoView(tableOptions.getShowHideColumnsBtn())
                .clickOnWebElement(tableOptions.getShowHideColumnsBtn())//
                .and()
                .validateListSize(tableOptions.getOptionItems(),
                        ColumnNames.ID.getName(),
                        ColumnNames.DETAILS.getName(),
                        ColumnNames.NAME.getName(),
                        ColumnNames.PUBLISHER.getName(),
                        ColumnNames.STATUS.getName(),
                        ColumnNames.FLOOR_PRICE.getName(),
                        ColumnNames.CREATED_DATE.getName(),
                        ColumnNames.CREATED_BY.getName(),
                        ColumnNames.UPDATED_DATE.getName(),
                        ColumnNames.UPDATED_BY.getName())
                .then()
                .testEnd();
    }

    @Test(enabled = true)
    @Ignore
    public void checkColumns() {
        var tableData = openPricingPage.getOpenPricingTable().getTableData();
        var tableOptions = openPricingPage.getOpenPricingTable().getShowHideColumns();
        var tablePagination = openPricingPage.getOpenPricingTable().getTablePagination();
        testStart()
                .given()
                .openDirectPath(Path.OPEN_PRICING)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                .and()
                .selectFromDropdown(tablePagination.getPageMenu(), tablePagination.getRowNumbersList(), "10")
                .waitLoading(visible, openPricingPage.getTableProgressBar())
                .waitLoading(disappear, openPricingPage.getTableProgressBar())
                .validateContainsText(tablePagination.getPaginationPanel(), "1-10 of ")
                .clickOnWebElement(tableOptions.getShowHideColumnsBtn())
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.ID))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.ID.getName()))
                .then()
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.DETAILS))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.DETAILS.getName()))
                .then()
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.NAME))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.NAME.getName()))
                .then()
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.PUBLISHER))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.PUBLISHER.getName()))
                .then()
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.STATUS))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.STATUS.getName()))
                .then()
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.FLOOR_PRICE))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.FLOOR_PRICE.getName()))
                .then()
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.CREATED_DATE.getName()))
                .then()
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.CREATED_BY.getName()))
                .then()
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.UPDATED_DATE))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.UPDATED_DATE.getName()))
                .then()
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.UPDATED_BY.getName()))
                .then()
                .validateListSize(tableOptions.getOptionItems(),
                        ColumnNames.ID.getName(),
                        ColumnNames.DETAILS.getName(),
                        ColumnNames.NAME.getName(),
                        ColumnNames.PUBLISHER.getName(),
                        ColumnNames.STATUS.getName(),
                        ColumnNames.FLOOR_PRICE.getName(),
                        ColumnNames.CREATED_DATE.getName(),
                        ColumnNames.CREATED_BY.getName(),
                        ColumnNames.UPDATED_DATE.getName(),
                        ColumnNames.UPDATED_BY.getName())
                .then()
                .testEnd();
    }

    @Test(enabled = false)
    @Ignore
    public void verifySearch(){

        testStart()
                .given()
                .openDirectPath(Path.OPEN_PRICING)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                .and();
    }

    @Test(enabled = true)
    @Ignore
    public void verifySingleDeactivatePublisherActive() {
        var tableData = openPricingPage.getOpenPricingTable().getTableData();

        testStart()
                .given()
                .openDirectPath(Path.OPEN_PRICING)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                .and()
                .waitAndValidate(appear, tableData.getCheckbox(1))
                .clickOnWebElement(tableData.getCheckbox(1))
                .clickOnWebElement(openPricingPage.getDeactivateButton())
                .validate(tableData.getCustomCells(ColumnNames.STATUS).get(0), "Inactive")
                .then()
                .testEnd();


    }

    @Test(enabled = true)
    @Ignore
    public void verifySingleActivatePublisherActive() {
        var tableData = openPricingPage.getOpenPricingTable().getTableData();
        var tableOptions = openPricingPage.getOpenPricingTable().getShowHideColumns();
        var tablePagination = openPricingPage.getOpenPricingTable().getTablePagination();
        testStart()
                .given()
                .openDirectPath(Path.OPEN_PRICING)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                .and()
                .waitAndValidate(appear, tableData.getCheckbox(1))
                .clickOnWebElement(tableData.getCheckbox(1))
                .clickOnWebElement(openPricingPage.getActivateButton())
                .validate(tableData.getCustomCells(ColumnNames.STATUS).get(0), "Active")
                .then()
                .testEnd();
    }

    @Test(enabled = true)
    @Ignore
    public void verifyBulkDeactivatePublisherActive() {
        var tableData = openPricingPage.getOpenPricingTable().getTableData();

        testStart()
                .given()
                .openDirectPath(Path.OPEN_PRICING)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                .and()
                .waitAndValidate(appear, tableData.getCheckbox(1))
                .clickOnWebElement(tableData.getCheckbox(1))
                .clickOnWebElement(tableData.getCheckbox(2))
                .clickOnWebElement(tableData.getCheckbox(3))
                .clickOnWebElement(openPricingPage.getDeactivateButton())
                .validate(tableData.getCustomCells(ColumnNames.STATUS).get(0), "Inactive")
                .validate(tableData.getCustomCells(ColumnNames.STATUS).get(1), "Inactive")
                .validate(tableData.getCustomCells(ColumnNames.STATUS).get(2), "Inactive")
                .then()
                .testEnd();
    }

    @Test(enabled = true)
    @Ignore
    public void verifyBulkActivatePublisherActive() {
        var tableData = openPricingPage.getOpenPricingTable().getTableData();

         testStart()
                .given()
                .openDirectPath(Path.OPEN_PRICING)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                .and()
                .waitAndValidate(appear, tableData.getCheckbox(1))
                .clickOnWebElement(tableData.getCheckbox(1))
                .clickOnWebElement(tableData.getCheckbox(2))
                .clickOnWebElement(tableData.getCheckbox(3))
                .clickOnWebElement(tableData.getCheckbox(4))
                .clickOnWebElement(openPricingPage.getActivateButton())
                .validate(tableData.getCustomCells(ColumnNames.STATUS).get(0), "Active")
                .validate(tableData.getCustomCells(ColumnNames.STATUS).get(1), "Active")
                .validate(tableData.getCustomCells(ColumnNames.STATUS).get(2), "Active")
                .validate(tableData.getCustomCells(ColumnNames.STATUS).get(3), "Active")
                .then()
                .testEnd();
    }

    @Test(enabled = true)
    @Ignore
    public void verifySingleDeactivatePublisherInactive() {
        var tableData = openPricingPage.getOpenPricingTable().getTableData();

        testStart()
                .given()
                .openDirectPath(Path.OPEN_PRICING)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                .and()
                .waitAndValidate(appear, tableData.getCheckbox(1))
                .clickOnWebElement(tableData.getCheckbox(1))
                .clickOnWebElement(openPricingPage.getDeactivateButton())
                .validate(tableData.getCustomCells(ColumnNames.STATUS).get(0), "Inactive")
                .then()
                .testEnd();

    }

    @Test(enabled = true)
    @Ignore
    public void verifySingleActivatePublisherInactive() {
        var tableData = openPricingPage.getOpenPricingTable().getTableData();

        testStart()
                .given()
                .openDirectPath(Path.OPEN_PRICING)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                .and()
                .waitAndValidate(appear, tableData.getCheckbox(1))
                .clickOnWebElement(tableData.getCheckbox(1))
                .clickOnWebElement(openPricingPage.getActivateButton())
                .validate(tableData.getCustomCells(ColumnNames.STATUS).get(0), "Active")
                .then()
                .testEnd();
    }

    @Test(enabled = true)
    @Ignore
    public void verifyBulkDeactivatePublisherInactive() {
        var tableData = openPricingPage.getOpenPricingTable().getTableData();

        testStart()
                .given()
                .openDirectPath(Path.OPEN_PRICING)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                .and()
                .waitAndValidate(appear, tableData.getCheckbox(1))
                .then()
                .clickOnWebElement(tableData.getCheckbox(1))
                .clickOnWebElement(tableData.getCheckbox(2))
                .clickOnWebElement(tableData.getCheckbox(3))
                .clickOnWebElement(tableData.getCheckbox(4))
                .clickOnWebElement(tableData.getCheckbox(5))
                .clickOnWebElement(tableData.getCheckbox(6))
                .clickOnWebElement(tableData.getCheckbox(7))
                .and()
                .clickOnWebElement(openPricingPage.getDeactivateButton())
                .then()
                .validate(tableData.getCustomCells(ColumnNames.STATUS).get(0), "Active")
                .validate(tableData.getCustomCells(ColumnNames.STATUS).get(1), "Active")
                .validate(tableData.getCustomCells(ColumnNames.STATUS).get(2), "Active")
                .validate(tableData.getCustomCells(ColumnNames.STATUS).get(3), "Active")
                .validate(tableData.getCustomCells(ColumnNames.STATUS).get(4), "Active")
                .validate(tableData.getCustomCells(ColumnNames.STATUS).get(5), "Active")
                .validate(tableData.getCustomCells(ColumnNames.STATUS).get(6), "Active")
                .then()
                .testEnd();
    }

    @Test(enabled = true)
    @Ignore
    public void verifyBulkActivatePublisherInactive() {
        var tableData = openPricingPage.getOpenPricingTable().getTableData();

        testStart()
                .given()
                .openDirectPath(Path.OPEN_PRICING)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                .and()
                .waitAndValidate(appear, tableData.getCheckbox(1))
                .then()
                .clickOnWebElement(tableData.getCheckbox(1))
                .clickOnWebElement(tableData.getCheckbox(2))
                .clickOnWebElement(tableData.getCheckbox(3))
                .clickOnWebElement(tableData.getCheckbox(4))
                .clickOnWebElement(tableData.getCheckbox(5))
                .clickOnWebElement(tableData.getCheckbox(6))
                .clickOnWebElement(tableData.getCheckbox(7))
                .clickOnWebElement(openPricingPage.getActivateButton())
                .then()
                .validate(tableData.getCustomCells(ColumnNames.STATUS).get(0), "Active")
                .validate(tableData.getCustomCells(ColumnNames.STATUS).get(1), "Active")
                .validate(tableData.getCustomCells(ColumnNames.STATUS).get(2), "Active")
                .validate(tableData.getCustomCells(ColumnNames.STATUS).get(3), "Active")
                .validate(tableData.getCustomCells(ColumnNames.STATUS).get(4), "Active")
                .validate(tableData.getCustomCells(ColumnNames.STATUS).get(5), "Active")
                .validate(tableData.getCustomCells(ColumnNames.STATUS).get(6), "Active")
                .then()
                .testEnd();
    }

    @Test(enabled = false)
    @Ignore
    public void verifyRuleExpand() {

        testStart()
                .given()
                .openDirectPath(Path.OPEN_PRICING)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                .and();
    }

    @Test(enabled = true)
    @Ignore
    public void verifyUserCreateOpenPricingRule() {

        testStart()
                .given()
                .openDirectPath(Path.OPEN_PRICING)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                .and()
                .waitAndValidate(appear, openPricingPage.getCreateOpenPricingButton())
                .clickOnWebElement(openPricingPage.getCreateOpenPricingButton())
                .and()
                .waitAndValidate(appear,openPricingPage.getSidebar())
                .then();
    }
}
