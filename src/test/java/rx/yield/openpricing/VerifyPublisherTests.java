package rx.yield.openpricing;

import api.dto.rx.yield.openpricing.OpenPricing;
import api.preconditionbuilders.OpenPricingPrecondition;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
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
public class VerifyPublisherTests extends BaseTest {

    private OpenPricing openPricing;
    private OpenPricingPage openPricingPage;
    private EditOpenPricingSidebar editOpenPricingSidebar;

    public VerifyPublisherTests() {
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
    public void verifySingleActivatePublisherActive() {
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
}
