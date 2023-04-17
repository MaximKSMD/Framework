package rx.sales.deals;

import api.dto.rx.privateauction.PrivateAuction;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.sales.deals.DealsPage;
import rx.BaseTest;
import widgets.errormessages.ErrorMessages;
import widgets.sales.deals.buyerscard.BuyersCard;
import widgets.sales.deals.sidebar.CreateDealSidebar;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static api.preconditionbuilders.PrivateAuctionPrecondition.privateAuction;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.disabled;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Deals")
public class DealsCheckFieldsTests extends BaseTest {

    private DealsPage dealsPage;
    private CreateDealSidebar dealSidebar;

    private PrivateAuction privateAuction;

    public DealsCheckFieldsTests() {

        dealsPage = new DealsPage();
        dealSidebar = new CreateDealSidebar();
    }

    @BeforeClass
    private void login() {
        privateAuction = createPrivateAuction();

        testStart()
                .given()
                .openDirectPath(Path.DEALS)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, dealsPage.getNuxtProgress())
                .testEnd();
    }

    @Test(description = "Check fields by default")
    public void checkFieldsByDefault() {
        BuyersCard buyersCard = new BuyersCard(1);

        testStart()
                .and("Click on 'Create Deal' button")
                .clickOnWebElement(dealsPage.getCreateNewDealButton())
                .waitSideBarOpened()
                .validateContainsText(dealSidebar.getTitle(),"Create Deal")
                .then("Validate fields by default")
                .validate(disabled, dealSidebar.getActiveToggle())
                .validateAttribute(dealSidebar.getActiveToggle(), "aria-checked", "true")
                .validate(visible, dealSidebar.getPublisherNameInput())
                .validate(dealSidebar.getPublisherNameInput(), "")
                .validate(disabled, dealSidebar.getPrivateAuctionInput())
                .validate(disabled, dealSidebar.getFloorPriceInput())
                .validate(disabled, dealSidebar.getCurrencyInput())
                .validate(disabled, dealSidebar.getDspInput())
                .validate(disabled, dealSidebar.getAddMoreSeatsButton())
                .validate(disabled, dealSidebar.getNameInput())
                .validate(dealSidebar.getNameInput(), "")
                .validate(disabled, dealSidebar.getDateRangeField().getDateRangeInput())
                .validate(dealSidebar.getDateRangeField().getDateRangeInput(), "")
                .validate(disabled, dealSidebar.getAlwaysOnToggle())
                .validateAttribute(dealSidebar.getAlwaysOnToggle(), "aria-checked", "false")
                .validate(disabled, buyersCard.getActiveCheckBox())
                .validate(disabled, buyersCard.getAdvertiserNameInput())
                .validate(disabled, buyersCard.getAdvertiserIdInput())
                .validate(disabled, buyersCard.getDspSeatIdCombo())
                .validate(disabled, buyersCard.getDspSeatNameInput())
                .validate(disabled, buyersCard.getDeleteCardIcon())
                .validate(disabled, buyersCard.getDspSeatPassthroughStringInput())
                .validate(disabled, buyersCard.getDspDomainAdvertiserPassthroughStringInput())
                .and("Close Sidebar")
                .clickOnWebElement(dealSidebar.getCloseSideBarButton())
                .waitSideBarClosed()
                .testEnd();
    }

    @Test(description = "Check error messages for required fields")
    public void checkRequiredFields() {
        var errorsList = dealSidebar.getErrorAlert().getErrorsList();

        testStart()
                .and("Click on 'Create Deal' button")
                .clickOnWebElement(dealsPage.getCreateNewDealButton())
                .waitSideBarOpened()
                .and("Click Save")
                .clickOnWebElement(dealSidebar.getSaveDealButton())
                .then("Validate error messages under the fields")
                .waitAndValidate(visible, dealSidebar.getErrorAlertByFieldName("Publisher Name"))
                .validate(dealSidebar.getErrorAlertByFieldName("Publisher Name"), ErrorMessages.PUBLISHER_NAME_ERROR_ALERT.getText())
                .waitAndValidate(visible, dealSidebar.getErrorAlertByFieldName("Name"))
                .validate(dealSidebar.getErrorAlertByFieldName("Name"), ErrorMessages.NAME_ERROR_ALERT.getText())
                .waitAndValidate(visible, dealSidebar.getErrorAlertByFieldName("Private Auction"))
                .validate(dealSidebar.getErrorAlertByFieldName("Private Auction"), ErrorMessages.PRIVATE_AUCTION_ERROR_ALERT.getText())
                .waitAndValidate(visible, dealSidebar.getErrorAlertByFieldName("Date Range"))
                .validate(dealSidebar.getErrorAlertByFieldName("Date Range"), ErrorMessages.DATE_RANGE_ERROR_ALERT.getText())
                .waitAndValidate(visible, dealSidebar.getErrorAlertByFieldName("Floor Price"))
                .validate(dealSidebar.getErrorAlertByFieldName("Floor Price"), ErrorMessages.VALUE_ERROR_ALERT.getText())
                .waitAndValidate(visible, dealSidebar.getErrorAlertByFieldName("Currency"))
                .validate(dealSidebar.getErrorAlertByFieldName("Currency"), ErrorMessages.CURRENCY_ERROR_ALERT.getText())
                .waitAndValidate(visible, dealSidebar.getErrorAlertByFieldName("DSP"))
                .validate(dealSidebar.getErrorAlertByFieldName("DSP"), ErrorMessages.DSP_ERROR_ALERT.getText())

                .then("Validate errors for all required fields in Error Panel")
                .waitAndValidate(visible, dealSidebar.getErrorAlert().getErrorPanel())
                .validateListSize(errorsList, 7)
                .validateList(errorsList, List.of(
                        ErrorMessages.PUBLISHER_NAME_ERROR_ALERT.getText(),
                        ErrorMessages.NAME_ERROR_ALERT.getText(),
                        ErrorMessages.PRIVATE_AUCTION_ERROR_ALERT.getText(),
                        ErrorMessages.DATE_RANGE_ERROR_ALERT.getText(),
                        ErrorMessages.VALUE_ERROR_ALERT.getText(),
                        ErrorMessages.CURRENCY_ERROR_ALERT.getText(),
                        ErrorMessages.DSP_ERROR_ALERT.getText())
                )
                .and("Select Publisher")
                .selectFromDropdown(dealSidebar.getPublisherDropdown(), dealSidebar.getDropDownItems(), privateAuction.getPublisherName())
                .then("Validate error under the 'Publisher field' disappeared")
                .waitAndValidate(not(visible), dealSidebar.getErrorAlertByFieldName("Publisher Name"))
                .and("Click 'Save'")
                .clickOnWebElement(dealSidebar.getSaveDealButton())
                .then("Validate errors for all required fields in Error Panel")
                .waitAndValidate(visible, dealSidebar.getErrorAlert().getErrorPanel())
                .validateListSize(errorsList, 5)
                .validateList(errorsList, List.of(
                        ErrorMessages.NAME_ERROR_ALERT.getText(),
                        ErrorMessages.PRIVATE_AUCTION_ERROR_ALERT.getText(),
                        ErrorMessages.DATE_RANGE_ERROR_ALERT.getText(),
                        ErrorMessages.VALUE_ERROR_ALERT.getText(),
                        ErrorMessages.DSP_ERROR_ALERT.getText())
                )
                .and("Fill name")
                .setValueWithClean(dealSidebar.getNameInput(), captionWithSuffix("auto deal"))
                .and("Click 'Save'")
                .clickOnWebElement(dealSidebar.getSaveDealButton())
                .then("Validate errors for all required fields in Error Panel")
                .waitAndValidate(not(visible), dealSidebar.getErrorAlertByFieldName("Name"))
                .waitAndValidate(not(visible), dealSidebar.getErrorAlertByFieldName("Publisher Name"))
                .validate(dealSidebar.getErrorAlertByFieldName("Private Auction"), ErrorMessages.PRIVATE_AUCTION_ERROR_ALERT.getText())
                .waitAndValidate(visible, dealSidebar.getErrorAlertByFieldName("Date Range"))
                .validate(dealSidebar.getErrorAlertByFieldName("Date Range"), ErrorMessages.DATE_RANGE_ERROR_ALERT.getText())
                .waitAndValidate(visible, dealSidebar.getErrorAlertByFieldName("Floor Price"))
                .validate(dealSidebar.getErrorAlertByFieldName("Floor Price"), ErrorMessages.VALUE_ERROR_ALERT.getText())
                .waitAndValidate(visible, dealSidebar.getErrorAlertByFieldName("DSP"))
                .validate(dealSidebar.getErrorAlertByFieldName("DSP"), ErrorMessages.DSP_ERROR_ALERT.getText())
                .then("Validate errors for not filled required fields in Error Panel")
                .waitAndValidate(visible, dealSidebar.getErrorAlert().getErrorPanel())
                .validateListSize(errorsList, 4)
                .validateList(errorsList, List.of(
                        ErrorMessages.PRIVATE_AUCTION_ERROR_ALERT.getText(),
                        ErrorMessages.DATE_RANGE_ERROR_ALERT.getText(),
                        ErrorMessages.VALUE_ERROR_ALERT.getText(),
                        ErrorMessages.DSP_ERROR_ALERT.getText())
                )
                .and("Select Private Auction")
                .selectFromDropdownByPosition(dealSidebar.getPrivateAuctionDropdown(), dealSidebar.getDropDownItems(), 0)
                .and("Click 'Save'")
                .clickOnWebElement(dealSidebar.getSaveDealButton())
                .then("Validate errors for all required fields in Error Panel")
                .waitAndValidate(not(visible), dealSidebar.getErrorAlertByFieldName("Name"))
                .waitAndValidate(not(visible), dealSidebar.getErrorAlertByFieldName("Publisher Name"))
                .waitAndValidate(not(visible), dealSidebar.getErrorAlertByFieldName("Private Auction"))
                .waitAndValidate(not(visible), dealSidebar.getErrorAlertByFieldName("Date Range"))
                .waitAndValidate(visible, dealSidebar.getErrorAlertByFieldName("Floor Price"))
                .validate(dealSidebar.getErrorAlertByFieldName("Floor Price"), ErrorMessages.VALUE_ERROR_ALERT.getText())
                .waitAndValidate(visible, dealSidebar.getErrorAlertByFieldName("DSP"))
                .validate(dealSidebar.getErrorAlertByFieldName("DSP"), ErrorMessages.DSP_ERROR_ALERT.getText())
                .then("Validate errors for not filled required fields in Error Panel")
                .waitAndValidate(visible, dealSidebar.getErrorAlert().getErrorPanel())
                .validateListSize(errorsList, 2)
                .validateList(errorsList, List.of(
                        ErrorMessages.VALUE_ERROR_ALERT.getText(),
                        ErrorMessages.DSP_ERROR_ALERT.getText())
                )
                .and("Fill Floor Price value")
                .setValueWithClean(dealSidebar.getFloorPriceInput(),"5.00")
                .and("Click 'Save'")
                .clickOnWebElement(dealSidebar.getSaveDealButton())
                .then("Validate errors for not filled required fields in Error Panel")
                .waitAndValidate(visible, dealSidebar.getErrorAlert().getErrorPanel())
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(
                        ErrorMessages.DSP_ERROR_ALERT.getText())
                )
                .and("Choose DSP")
                .selectFromDropdownByPosition(dealSidebar.getDspDropdown(),dealSidebar.getDropDownItems(), 0)
                .and("Click 'Save'")
                .clickOnWebElement(dealSidebar.getSaveDealButton())
                .waitAndValidate(not(visible), dealSidebar.getErrorAlert().getErrorPanel())
                .validate(not(visible),dealsPage.getToasterMessage().getPanelError())
                .and("Close Sidebar")
                .clickOnWebElement(dealSidebar.getCloseSideBarButton())
                .waitSideBarClosed()
                .testEnd();
    }

    private PrivateAuction createPrivateAuction(){

        return privateAuction()
                .createPrivateAuction()
                .build()
                .getPrivateAuctionResponse();
    }

    @AfterClass(alwaysRun = true)
    private void closeSidebarAndLogout() {
        testStart()
                .logOut()
                .testEnd();
    }
}
