package rx;

import api.dto.rx.yield.openpricing.OpenPricing;
import api.preconditionbuilders.OpenPricingPrecondition;
import com.codeborne.selenide.testng.ScreenShooter;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Path;
import pages.yield.openpricing.OpenPricingPage;
import widgets.common.table.ColumnNames;
import widgets.yield.openPricing.sidebar.EditOpenPricingSidebar;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
public class OpenPricingTest extends BaseTest {

    private OpenPricing openPricing;
    private OpenPricingPage openPricingPage;
    private EditOpenPricingSidebar editOpenPricingSidebar;

    public OpenPricingTest() {
        openPricingPage = new OpenPricingPage();
        editOpenPricingSidebar = new EditOpenPricingSidebar();
    }

    @BeforeClass
    public void createOpenPricing() {
        //Creating publisher to edit Using API
        openPricing = OpenPricingPrecondition.openPricing()
                .createNewOpenPricing()
                .build()
                .getOpenPricingResponse();
    }

    //TODO: need to update
    @Test
    public void editOpenPricingTest() {
        var tableData = openPricingPage.getOpenPricingTable().getTableData();

        //Opening Browser and check Open pricing from precondition
        testStart()
                .given()
                .openDirectPath(Path.OPEN_PRICING)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                .waitAndValidate(disappear, openPricingPage.getTableProgressBar())
                .setValueWithClean(tableData.getSearch(), openPricing.getName())
                .waitLoading(visible, openPricingPage.getTableProgressBar())
                .waitLoading(disappear, openPricingPage.getTableProgressBar())
                .then()
                .validateListContainsTextOnly(tableData.getCustomCells(ColumnNames.NAME),
                        openPricing.getName())
                .and()
                .clickOnTableCellLink(tableData, ColumnNames.NAME, openPricing.getName())
                .waitSideBarOpened()
                .validateAttribute(editOpenPricingSidebar.getNameInput(), "value", openPricing.getName())
           //     .validate(editOpenPricingSidebar.getPublisherNameDropdown(), openPricing.getPublisherName())
                .validateAttribute(editOpenPricingSidebar.getFloorPriceField().getFloorPriceInput(),
                        "value", openPricing.getFloorPrice().toString())

                .clickOnWebElement(editOpenPricingSidebar.getSaveButton())
                .waitSideBarClosed()

                .testEnd();

        //allure serve
    }
}
