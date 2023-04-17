package rx.sales.privateauctions;

import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.sales.privateauctions.PrivateAuctionsPage;
import rx.BaseTest;
import rx.enums.MultipaneConstants;
import widgets.errormessages.ErrorMessages;
import widgets.sales.deals.sidebar.CreateDealSidebar;
import widgets.sales.privateauctions.sidebar.CreatePrivateAuctionSidebar;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Private Auctions")
public class PrivateAuctionCheckFields extends BaseTest {

    private PrivateAuctionsPage privateAuctionsPage;
    private CreatePrivateAuctionSidebar privateAuctionSidebar;
    private CreateDealSidebar dealSidebar;

    public PrivateAuctionCheckFields() {

        dealSidebar = new CreateDealSidebar();
        privateAuctionsPage = new PrivateAuctionsPage();
        privateAuctionSidebar = new CreatePrivateAuctionSidebar();
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

    @BeforeMethod
    private void openSidebar() {

        testStart()
                .openDirectPath(Path.PRIVATE_AUCTIONS)
                .and("Click on 'Create Private Auction' button")
                .clickOnWebElement(privateAuctionsPage.getCreatePrivateAuctionsButton())
                .waitSideBarOpened()
                .testEnd();
    }

    @Test(description = "Check fields by default")
    public void checkDefaultFieldsState() {

        testStart()
                .then("Validate fields by default")
                .validate(disabled, privateAuctionSidebar.getActiveToggle())
                .validateAttribute(privateAuctionSidebar.getActiveToggle(), "aria-checked", "true")
                .validate(visible, privateAuctionSidebar.getPublisherNameInput())
                .validate(privateAuctionSidebar.getPublisherNameInput(), "")
                .validate(disabled, privateAuctionSidebar.getNameInput())
                .validate(privateAuctionSidebar.getNameInput(), "")
                .validate(disabled, privateAuctionSidebar.getDateRangeField().getDateRangeInput())
                .validate(privateAuctionSidebar.getDateRangeField().getDateRangeInput(), "")
                .validate(disabled, privateAuctionSidebar.getAlwaysOnToggle())
                .validateAttribute(privateAuctionSidebar.getAlwaysOnToggle(), "aria-checked", "false")
                .validateContainsText(privateAuctionSidebar.getInventoryMultipane().getPanelNameLabel(), MultipaneConstants.ALL_INVENTORY_IS_INCLUDED.setQuantity(0))
                .validateContainsText(privateAuctionSidebar.getOperatingSystemMultipane().getPanelNameLabel(), MultipaneConstants.ALL_OPERATING_SYSTEMS_ARE_INCLUDED.setQuantity(0))
                .validateContainsText(privateAuctionSidebar.getAdFormatMultipane().getPanelNameLabel(), MultipaneConstants.ALL_AD_FORMATS_ARE_INCLUDED.setQuantity(0))
                .validateContainsText(privateAuctionSidebar.getAdSizeMultipane().getPanelNameLabel(), MultipaneConstants.ALL_AD_SIZES_ARE_INCLUDED.setQuantity(0))
                .validateContainsText(privateAuctionSidebar.getGeoMultipane().getPanelNameLabel(), MultipaneConstants.ALL_GEOS_ARE_INCLUDED.setQuantity(0))
                .validateContainsText(privateAuctionSidebar.getDeviceMultipane().getPanelNameLabel(), MultipaneConstants.ALL_DEVICES_ARE_INCLUDED.setQuantity(0))
                .clickOnWebElement(privateAuctionSidebar.getCloseIcon())
                .waitSideBarClosed()
                .testEnd();
    }

    @Test(description = "Check required fields. Save and Close button")
    public void checkRequiredFields() {
        var calendar = privateAuctionSidebar.getDateRangeField();
        ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("UTC"));
        var errorsList = privateAuctionSidebar.getErrorAlert().getErrorsList();

        testStart()
                .clickOnWebElement(privateAuctionSidebar.getSaveAndCloseButton())
                .then("Validate errors for all required fields in Error Panel")
                .waitAndValidate(visible, privateAuctionSidebar.getErrorAlert().getErrorPanel())
                .validateListSize(errorsList, 3)
                .validateList(errorsList, List.of(
                        ErrorMessages.PUBLISHER_NAME_ERROR_ALERT.getText(),
                        ErrorMessages.NAME_ERROR_ALERT.getText(),
                        ErrorMessages.DATE_RANGE_ERROR_ALERT.getText())
                )
                .and("Select Publisher")
                .selectFromDropdownByPosition(privateAuctionSidebar.getPublisherNameDropdown(), privateAuctionSidebar.getPublisherItems(), 1)
                .then("Validate error under the 'Publisher field' disappeared")
                .waitAndValidate(not(visible), privateAuctionSidebar.getErrorAlertByFieldName("Publisher Name"))
                .and("Click 'Save'")
                .clickOnWebElement(privateAuctionSidebar.getSaveAndCloseButton())
                .then("Validate errors for all required fields in Error Panel")
                .waitAndValidate(visible, privateAuctionSidebar.getErrorAlert().getErrorPanel())
                .validateListSize(errorsList, 2)
                .validateList(errorsList, List.of(
                        ErrorMessages.NAME_ERROR_ALERT.getText(),
                        ErrorMessages.DATE_RANGE_ERROR_ALERT.getText())
                )
                .then("Validate error under the 'Name' field")
                .waitAndValidate(visible, privateAuctionSidebar.getErrorAlertByFieldName("Name"))
                .validate(privateAuctionSidebar.getErrorAlertByFieldName("Name"), ErrorMessages.NAME_ERROR_ALERT.getText())
                .and("Fill the name")
                .setValueWithClean(privateAuctionSidebar.getNameInput(), captionWithSuffix("auto auction name66"))
                .waitAndValidate(not(visible), privateAuctionSidebar.getErrorAlertByFieldName("Name"))
                .and("Click 'Save'")
                .clickOnWebElement(privateAuctionSidebar.getSaveAndCloseButton())
                .then("Validate errors for all required fields in Error Panel")
                .waitAndValidate(visible, privateAuctionSidebar.getErrorAlert().getErrorPanel())
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(
                        ErrorMessages.DATE_RANGE_ERROR_ALERT.getText())
                )
                .and("Fill Date Range")
                .clickOnWebElement(privateAuctionSidebar.getDateRangeInput())
                .clickOnWebElement(calendar.getNextMonthButton())
                .clickOnWebElement(calendar.getDayButtonByValue("12"))
                .clickOnWebElement(privateAuctionSidebar.getDateRangeInput())
                .turnToggleOn(privateAuctionSidebar.getAlwaysOnToggle())
                .and("Click 'Save'")
                .clickOnWebElement(privateAuctionSidebar.getSaveAndCloseButton())
                .then("Validate errors for all required fields in Error Panel")
                .waitAndValidate(not(visible), privateAuctionSidebar.getErrorAlert().getErrorPanel())
                .waitSideBarClosed()
                .testEnd();
    }

    @Test(description = "Check required fields. Save and Create Deal button")
    public void checkRequiredFieldsCreateDeal() {
        var calendar = privateAuctionSidebar.getDateRangeField();
        ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("UTC"));
        var errorsList = privateAuctionSidebar.getErrorAlert().getErrorsList();

        testStart()
                .clickOnWebElement(privateAuctionSidebar.getSaveAndCreateDealButton())
                .then("Validate errors for all required fields in Error Panel")
                .waitAndValidate(visible, privateAuctionSidebar.getErrorAlert().getErrorPanel())
                .validateListSize(errorsList, 3)
                .validateList(errorsList, List.of(
                        ErrorMessages.PUBLISHER_NAME_ERROR_ALERT.getText(),
                        ErrorMessages.NAME_ERROR_ALERT.getText(),
                        ErrorMessages.DATE_RANGE_ERROR_ALERT.getText())
                )
                .and("Select Publisher")
                .selectFromDropdownByPosition(privateAuctionSidebar.getPublisherNameDropdown(), privateAuctionSidebar.getPublisherItems(), 1)
                .then("Validate error under the 'Publisher field' disappeared")
                .waitAndValidate(not(visible), privateAuctionSidebar.getErrorAlertByFieldName("Publisher Name"))
                .and("Click 'Save'")
                .clickOnWebElement(privateAuctionSidebar.getSaveAndCreateDealButton())
                .then("Validate errors for all required fields in Error Panel")
                .waitAndValidate(visible, privateAuctionSidebar.getErrorAlert().getErrorPanel())
                .validateListSize(errorsList, 2)
                .validateList(errorsList, List.of(
                        ErrorMessages.NAME_ERROR_ALERT.getText(),
                        ErrorMessages.DATE_RANGE_ERROR_ALERT.getText())
                )
                .then("Validate error under the 'Name' field")
                .waitAndValidate(visible, privateAuctionSidebar.getErrorAlertByFieldName("Name"))
                .validate(privateAuctionSidebar.getErrorAlertByFieldName("Name"), ErrorMessages.NAME_ERROR_ALERT.getText())
                .and("Fill the name")
                .setValueWithClean(privateAuctionSidebar.getNameInput(), captionWithSuffix("auto auction name66"))
                .waitAndValidate(not(visible), privateAuctionSidebar.getErrorAlertByFieldName("Name"))
                .and("Click 'Save'")
                .clickOnWebElement(privateAuctionSidebar.getSaveAndCloseButton())
                .then("Validate errors for all required fields in Error Panel")
                .waitAndValidate(visible, privateAuctionSidebar.getErrorAlert().getErrorPanel())
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(
                        ErrorMessages.DATE_RANGE_ERROR_ALERT.getText())
                )
                .and("Fill Date Range")
                .clickOnWebElement(privateAuctionSidebar.getDateRangeInput())
                .clickOnWebElement(calendar.getNextMonthButton())
                .clickOnWebElement(calendar.getDayButtonByValue("12"))
                .clickOnWebElement(privateAuctionSidebar.getDateRangeInput())
                .turnToggleOn(privateAuctionSidebar.getAlwaysOnToggle())
                .and("Click 'Save'")
                .clickOnWebElement(privateAuctionSidebar.getSaveAndCreateDealButton())
                .then("Validate errors for all required fields in Error Panel")
                .waitAndValidate(not(visible), privateAuctionSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), privateAuctionsPage.getToasterMessage().getPanelError())
                .waitSideBarOpened()
                .clickOnWebElement(dealSidebar.getCloseSideBarButton())
                .testEnd();
    }

    @Test(description = "Check Date Range field")
    public void checkDateRangeField() {
        var calendar = privateAuctionSidebar.getDateRangeField();
        ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("UTC"));
        var errorsList = privateAuctionSidebar.getErrorAlert().getErrorsList();

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(privateAuctionSidebar.getPublisherNameDropdown(), privateAuctionSidebar.getPublisherItems(), 1)
                .and("Fill the name")
                .setValueWithClean(privateAuctionSidebar.getNameInput(), captionWithSuffix("auto auction"))
                .and("Fill Date Range")
                .clickOnWebElement(privateAuctionSidebar.getDateRangeInput())
                .clickOnWebElement(calendar.getNextMonthButton())
                .clickOnWebElement(calendar.getDayButtonByValue("12"))
                .clickOnWebElement(privateAuctionSidebar.getDateRangeInput())
                .and("Click 'Save'")
                .clickOnWebElement(privateAuctionSidebar.getSaveAndCloseButton())
                .then("Validate errors for all required fields in Error Panel")
                .waitAndValidate(visible, privateAuctionSidebar.getErrorAlert().getErrorPanel())
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(
                        ErrorMessages.END_DATE_IS_REQUIRED.getText())
                )
                .validate(privateAuctionSidebar.getErrorAlertByFieldName("Date Range"), ErrorMessages.END_DATE_IS_REQUIRED.getText())
                .and("Fill End Date")
                .clickOnWebElement(privateAuctionSidebar.getDateRangeInput())
                .clickOnWebElement(calendar.getNextMonthButton())
                .clickOnWebElement(calendar.getDayButtonByValue("12"))
                .clickOnWebElement(privateAuctionSidebar.getDateRangeInput())
                .and("Click 'Save'")
                .clickOnWebElement(privateAuctionSidebar.getSaveAndCloseButton())
                .then("Validate errors for all required fields in Error Panel")
                .waitAndValidate(not(visible), privateAuctionSidebar.getErrorAlert().getErrorPanel())
                .waitSideBarClosed()
                .testEnd();
    }

    @Test(description = "Check Date Range field. Close and Create Deal")
    public void checkDateRangeFieldCloseAndCreateDeal() {
        var calendar = privateAuctionSidebar.getDateRangeField();
        ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("UTC"));
        var errorsList = privateAuctionSidebar.getErrorAlert().getErrorsList();

        testStart()

                .and("Select Publisher")
                .selectFromDropdownByPosition(privateAuctionSidebar.getPublisherNameDropdown(), privateAuctionSidebar.getPublisherItems(), 1)
                .and("Fill the name")
                .setValueWithClean(privateAuctionSidebar.getNameInput(), captionWithSuffix("auto auction"))
                .and("Fill Date Range")
                .clickOnWebElement(privateAuctionSidebar.getDateRangeInput())
                .clickOnWebElement(calendar.getNextMonthButton())
                .clickOnWebElement(calendar.getDayButtonByValue("12"))
                .clickOnWebElement(privateAuctionSidebar.getDateRangeInput())
                .and("Click 'Save'")
                .clickOnWebElement(privateAuctionSidebar.getSaveAndCloseButton())
                .then("Validate errors for all required fields in Error Panel")
                .waitAndValidate(visible, privateAuctionSidebar.getErrorAlert().getErrorPanel())
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(
                        ErrorMessages.END_DATE_IS_REQUIRED.getText())
                )
                .validate(privateAuctionSidebar.getErrorAlertByFieldName("Date Range"), ErrorMessages.END_DATE_IS_REQUIRED.getText())
                .and("Fill End Date")
                .clickOnWebElement(privateAuctionSidebar.getDateRangeInput())
                .clickOnWebElement(calendar.getNextMonthButton())
                .clickOnWebElement(calendar.getDayButtonByValue("12"))
                .clickOnWebElement(privateAuctionSidebar.getDateRangeInput())
                .and("Click 'Save'")
                .clickOnWebElement(privateAuctionSidebar.getSaveAndCreateDealButton())
                .then("Validate errors for all required fields in Error Panel")
                .waitAndValidate(not(visible), privateAuctionSidebar.getErrorAlert().getErrorPanel())
                .waitSideBarOpened()
                .clickOnWebElement(dealSidebar.getCloseSideBarButton())
                .testEnd();
    }

    @Test(description = "Date from past")
    public void checkDateRangeFieldDateFromPast() {
        var calendar = privateAuctionSidebar.getDateRangeField();
        var errorsList = privateAuctionSidebar.getErrorAlert().getErrorsList();

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(privateAuctionSidebar.getPublisherNameDropdown(), privateAuctionSidebar.getPublisherItems(), 1)
                .and("Fill the name")
                .setValueWithClean(privateAuctionSidebar.getNameInput(), captionWithSuffix("auto auction"))
                .and("Fill Date Range")
                .clickOnWebElement(privateAuctionSidebar.getDateRangeInput())
                .clickOnWebElement(calendar.getPreviousMonthButton())
                .clickOnWebElement(calendar.getDayButtonByValue("12"))
                .clickOnWebElement(privateAuctionSidebar.getDateRangeInput())
                .and("Click 'Save'")
                .clickOnWebElement(privateAuctionSidebar.getSaveAndCloseButton())
                .then("Validate errors for all required fields in Error Panel")
                .waitAndValidate(visible, privateAuctionSidebar.getErrorAlert().getErrorPanel())
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(
                        ErrorMessages.START_DATE_CANNOT_BE_IN_THE_PAST.getText())
                )
                .validate(privateAuctionSidebar.getErrorAlertByFieldName("Date Range"), ErrorMessages.START_DATE_CANNOT_BE_IN_THE_PAST.getText())
                .clickOnWebElement(privateAuctionSidebar.getCloseIcon())
                .waitSideBarClosed()
                .testEnd();
    }

    @Test(description = "Date from past. Close and Create Deal")
    public void checkDateRangeFieldDateFromPastCloseAndCreateDeal() {
        var calendar = privateAuctionSidebar.getDateRangeField();
        var errorsList = privateAuctionSidebar.getErrorAlert().getErrorsList();

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(privateAuctionSidebar.getPublisherNameDropdown(), privateAuctionSidebar.getPublisherItems(), 1)
                .and("Fill the name")
                .setValueWithClean(privateAuctionSidebar.getNameInput(), captionWithSuffix("auto auction"))
                .and("Fill Date Range")
                .clickOnWebElement(privateAuctionSidebar.getDateRangeInput())
                .clickOnWebElement(calendar.getPreviousMonthButton())
                .clickOnWebElement(calendar.getDayButtonByValue("12"))
                .clickOnWebElement(privateAuctionSidebar.getDateRangeInput())
                .and("Click 'Save'")
                .clickOnWebElement(privateAuctionSidebar.getSaveAndCreateDealButton())
                .then("Validate errors for all required fields in Error Panel")
                .waitAndValidate(visible, privateAuctionSidebar.getErrorAlert().getErrorPanel())
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(
                        ErrorMessages.START_DATE_CANNOT_BE_IN_THE_PAST.getText())
                )
                .validate(privateAuctionSidebar.getErrorAlertByFieldName("Date Range"), ErrorMessages.START_DATE_CANNOT_BE_IN_THE_PAST.getText())
                .clickOnWebElement(privateAuctionSidebar.getCloseIcon())
                .waitSideBarClosed()
                .testEnd();
    }

    @Test(description = "Start Date = End Date")
    public void checkDateRangeFieldDateWrongPeriod() {
        var calendar = privateAuctionSidebar.getDateRangeField();
        var errorsList = privateAuctionSidebar.getErrorAlert().getErrorsList();

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(privateAuctionSidebar.getPublisherNameDropdown(), privateAuctionSidebar.getPublisherItems(), 1)
                .and("Fill the name")
                .setValueWithClean(privateAuctionSidebar.getNameInput(), captionWithSuffix("auto auction"))
                .and("Fill Date Range")
                .clickOnWebElement(privateAuctionSidebar.getDateRangeInput())
                .clickOnWebElement(calendar.getNextMonthButton())
                .clickOnWebElement(calendar.getDayButtonByValue("15"))
                .clickOnWebElement(calendar.getDayButtonByValue("15"))
                .and("Click 'Save'")
                .clickOnWebElement(privateAuctionSidebar.getSaveAndCloseButton())
                .then("Validate errors for all required fields in Error Panel")
                .waitAndValidate(visible, privateAuctionSidebar.getErrorAlert().getErrorPanel())
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(
                        ErrorMessages.END_DATE_MUST_BE_GREATER_THEN_START_DATE.getText())
                )
                .validate(privateAuctionSidebar.getErrorAlertByFieldName("Date Range"), ErrorMessages.END_DATE_MUST_BE_GREATER_THEN_START_DATE.getText())
                .clickOnWebElement(privateAuctionSidebar.getCloseIcon())
                .waitSideBarClosed()
                .testEnd();
    }

    @Test(description = "Start Date = End Date. Close and Create Deal")
    public void checkDateRangeFieldDateWrongPeriodCloseAndCreateDeal() {
        var calendar = privateAuctionSidebar.getDateRangeField();
        var errorsList = privateAuctionSidebar.getErrorAlert().getErrorsList();

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(privateAuctionSidebar.getPublisherNameDropdown(), privateAuctionSidebar.getPublisherItems(), 1)
                .and("Fill the name")
                .setValueWithClean(privateAuctionSidebar.getNameInput(), captionWithSuffix("auto auction"))
                .and("Fill Date Range")
                .clickOnWebElement(privateAuctionSidebar.getDateRangeInput())
                .clickOnWebElement(calendar.getNextMonthButton())
                .clickOnWebElement(calendar.getDayButtonByValue("15"))
                .clickOnWebElement(calendar.getDayButtonByValue("15"))
                .and("Click 'Save'")
                .clickOnWebElement(privateAuctionSidebar.getSaveAndCreateDealButton())
                .then("Validate errors for all required fields in Error Panel")
                .waitAndValidate(visible, privateAuctionSidebar.getErrorAlert().getErrorPanel())
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(
                        ErrorMessages.END_DATE_MUST_BE_GREATER_THEN_START_DATE.getText())
                )
                .validate(privateAuctionSidebar.getErrorAlertByFieldName("Date Range"), ErrorMessages.END_DATE_MUST_BE_GREATER_THEN_START_DATE.getText())
                .clickOnWebElement(privateAuctionSidebar.getCloseIcon())
                .waitSideBarClosed()
                .testEnd();
    }
}
