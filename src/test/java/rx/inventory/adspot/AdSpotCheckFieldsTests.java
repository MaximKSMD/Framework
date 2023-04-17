package rx.inventory.adspot;

import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.inventory.media.Media;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;
import pages.Path;
import pages.inventory.adspots.AdSpotsPage;
import rx.BaseTest;
import widgets.common.adSizes.AdSizesList;
import widgets.errormessages.ErrorMessages;
import widgets.inventory.adSpots.sidebar.EditAdSpotSidebar;

import java.util.List;

import static api.preconditionbuilders.MediaPrecondition.media;
import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.disabled;
import static configurations.User.TEST_USER;
import static configurations.User.USER_FOR_DELETION;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature("Ad Spots")
public class AdSpotCheckFieldsTests extends BaseTest {

    private Media media1;
    private Publisher publisher;
    private AdSpotsPage adSpotPage;
    private EditAdSpotSidebar editAdSpotSidebar;


    public AdSpotCheckFieldsTests() {
        adSpotPage = new AdSpotsPage();
        editAdSpotSidebar = new EditAdSpotSidebar();
    }

    @BeforeClass
    public void init() {

        publisher = publisher()
                .createNewPublisher(captionWithSuffix("000autoPub"))
                .build()
                .getPublisherResponse();

        media1 = media()
                .createNewMedia(captionWithSuffix("auto1Media"), publisher.getId(), true)
                .build()
                .getMediaResponse();

        testStart()
                .given()
                .openDirectPath(Path.AD_SPOT)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, adSpotPage.getNuxtProgress())
                .testEnd();
    }

    @BeforeMethod
    public void openSidebar() {
        testStart()
                .given()
                .clickOnWebElement(adSpotPage.getCreateAdSpotButton())
                .waitSideBarOpened()
                .testEnd();
    }

    @Test(description = "Check fields by default", priority = 2)
    public void checkDefaultFields() {
        testStart()
                .then("Validate fields by default")
                .validate(disabled, editAdSpotSidebar.getGeneralTab().getActiveToggle())
                .validateAttribute(editAdSpotSidebar.getGeneralTab().getActiveToggle(), "aria-checked", "true")
                .validate(visible, editAdSpotSidebar.getGeneralTab().getPublisherInput())
                .validate(editAdSpotSidebar.getGeneralTab().getPublisherInput(), "")
                .validate(disabled, editAdSpotSidebar.getGeneralTab().getCategoriesInput())
                .validate(disabled, editAdSpotSidebar.getGeneralTab().getNameInput())
                .validate(disabled, editAdSpotSidebar.getGeneralTab().getRelatedMediaInput())
                .validate(disabled, editAdSpotSidebar.getGeneralTab().getPositionInput())
                .validate(disabled, editAdSpotSidebar.getGeneralTab().getDefaultAdSizesInput())
                .validate(disabled, editAdSpotSidebar.getGeneralTab().getDefaultFloorPrice())
                .validate(disabled, editAdSpotSidebar.getGeneralTab().getTestModeToggle())
                .validate(disabled, editAdSpotSidebar.getGeneralTab().getContentForChildrenToggle())
                .validateAttribute(editAdSpotSidebar.getGeneralTab().getBannerCard().getBannerPanel(), "aria-expanded", "false")
                .validateAttribute(editAdSpotSidebar.getGeneralTab().getVideoCard().getVideoPanel(), "aria-expanded", "false")
                .validateAttribute(editAdSpotSidebar.getGeneralTab().getNativeCard().getNativePanel(), "aria-expanded", "false")
                .and("Close Ad Spot Sidebar")
                .clickOnWebElement(editAdSpotSidebar.getCloseIcon())
                .waitSideBarClosed()
                .testEnd();
    }

    @Test(description = "Check required fields", priority = 1)
    public void checkRequiredFields() {
        var videoCard = editAdSpotSidebar.getGeneralTab().getVideoCard();
        var bannerCard = editAdSpotSidebar.getGeneralTab().getBannerCard();
        var nativeCard = editAdSpotSidebar.getGeneralTab().getNativeCard();
        var errorsList = editAdSpotSidebar.getGeneralTab().getErrorAlert().getErrorsList();
        var adSpotName = captionWithSuffix("autoAdSpot");

        testStart()
                .and("Expand all cards")
                .clickOnWebElement(bannerCard.getBannerCardHeader())
                .clickOnWebElement(nativeCard.getNativeCardHeader())
                .scrollIntoView(videoCard.getVideoCardHeader())
                .clickOnWebElement(videoCard.getVideoCardHeader())

                .and("Click 'Save'")
                .scrollIntoView(editAdSpotSidebar.getSaveButton())
                .clickOnWebElement(editAdSpotSidebar.getSaveButton())
                .then("Validate errors for all required fields in Error Panel")
                .waitAndValidate(visible, editAdSpotSidebar.getGeneralTab().getErrorAlert().getErrorPanel())
                .validateListSize(errorsList, 6)
                .validateList(errorsList, List.of(
                        ErrorMessages.PUBLISHER_NAME_ERROR_ALERT.getText(),
                        ErrorMessages.NAME_ERROR_ALERT.getText(),
                        ErrorMessages.RELATED_MEDIA_TYPE_ERROR_ALERT.getText(),
                        ErrorMessages.POSITION_ERROR_ALERT.getText(),
                        ErrorMessages.DEFAULT_AD_SIZE_TYPE_ERROR_ALERT.getText(),
                        ErrorMessages.DEFAULT_FLOOR_PRICE_ERROR_ALERT.getText())
                )
                .then("Validate error under the 'Publisher' field")
                .waitAndValidate(visible, editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Publisher Name"))
                .validate(editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Publisher Name"),
                        ErrorMessages.PUBLISHER_NAME_ERROR_ALERT.getText())
                .and(String.format("Select Publisher '%s'", media1.getPublisherName()))
                .selectFromDropdown(editAdSpotSidebar.getGeneralTab().getPublisherInput(),
                        editAdSpotSidebar.getGeneralTab().getPublisherItems(), media1.getPublisherName())
                .then("Validate error under the 'Publisher field' disappeared")
                .waitAndValidate(not(visible), editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Publisher Name"))
                .and("Click 'Save'")
                .clickOnWebElement(editAdSpotSidebar.getSaveButton())
                .then("Validate error under the 'Ad Spot Name' field")
                .waitAndValidate(visible, editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Name"))
                .then("Validate error under the 'Related Media' field")
                .waitAndValidate(visible, editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Related Media"))
                .then("Validate error under the 'Position' field")
                .waitAndValidate(visible, editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Position"))
                .then("Validate error under the 'Default Ad size' field")
                .waitAndValidate(visible, editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Default Ad Sizes"))
                .then("Validate error under the 'Default Floor Price' field")
                .waitAndValidate(visible, editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Default Floor Price"))
                .validateListSize(errorsList, 5)
                .validateList(errorsList, List.of(
                        ErrorMessages.NAME_ERROR_ALERT.getText(),
                        ErrorMessages.RELATED_MEDIA_TYPE_ERROR_ALERT.getText(),
                        ErrorMessages.POSITION_ERROR_ALERT.getText(),
                        ErrorMessages.DEFAULT_AD_SIZE_TYPE_ERROR_ALERT.getText(),
                        ErrorMessages.DEFAULT_FLOOR_PRICE_ERROR_ALERT.getText())
                )
                .and("Fill Ad Spot Name")
                .setValueWithClean(editAdSpotSidebar.getGeneralTab().getNameInput(), adSpotName)
                .then("Validate error under the 'Ad Spot field' disappeared")
                .waitAndValidate(not(visible), editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Ad Spot Name"))
                .and("Click 'Save'")
                .clickOnWebElement(editAdSpotSidebar.getSaveButton())
                .then("Validate error under the 'Related Media' field")
                .waitAndValidate(visible, editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Related Media"))
                .then("Validate error under the 'Position' field")
                .waitAndValidate(visible, editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Position"))
                .then("Validate error under the 'Default Ad size' field")
                .waitAndValidate(visible, editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Default Ad Sizes"))
                .then("Validate error under the 'Default Floor Price' field")
                .waitAndValidate(visible, editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Default Floor Price"))
                .validateListSize(errorsList, 4)
                .validateList(errorsList, List.of(
                        ErrorMessages.RELATED_MEDIA_TYPE_ERROR_ALERT.getText(),
                        ErrorMessages.POSITION_ERROR_ALERT.getText(),
                        ErrorMessages.DEFAULT_AD_SIZE_TYPE_ERROR_ALERT.getText(),
                        ErrorMessages.DEFAULT_FLOOR_PRICE_ERROR_ALERT.getText())
                )

                .and("Select Related Media")
                .selectFromDropdown(editAdSpotSidebar.getGeneralTab().getRelatedMedia(),
                        editAdSpotSidebar.getGeneralTab().getRelatedMediaItems(), media1.getName())
                .then("Validate error under the 'Related Media field' disappeared")
                .waitAndValidate(not(visible), editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Related Media"))
                .and("Click 'Save'")
                .clickOnWebElement(editAdSpotSidebar.getSaveButton())
                .then("Validate error under the 'Position' field")
                .waitAndValidate(visible, editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Position"))
                .then("Validate error under the 'Default Ad size' field")
                .waitAndValidate(visible, editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Default Ad Sizes"))
                .then("Validate error under the 'Default Floor Price' field")
                .waitAndValidate(visible, editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Default Floor Price"))
                .validateListSize(errorsList, 3)
                .validateList(errorsList, List.of(
                        ErrorMessages.POSITION_ERROR_ALERT.getText(),
                        ErrorMessages.DEFAULT_AD_SIZE_TYPE_ERROR_ALERT.getText(),
                        ErrorMessages.DEFAULT_FLOOR_PRICE_ERROR_ALERT.getText())
                )

                .and("Select Position")
                .selectFromDropdown(editAdSpotSidebar.getGeneralTab().getPosition(),
                        editAdSpotSidebar.getGeneralTab().getPositionItems(), "Header")
                .then("Validate error under the 'Position field' disappeared")
                .waitAndValidate(not(visible), editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Position"))
                .and("Click 'Save'")
                .clickOnWebElement(editAdSpotSidebar.getSaveButton())
                .then("Validate error under the 'Default Ad size' field")
                .waitAndValidate(visible, editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Default Ad Sizes"))
                .then("Validate error under the 'Default Floor Price' field")
                .waitAndValidate(visible, editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Default Floor Price"))
                .validateListSize(errorsList, 2)
                .validateList(errorsList, List.of(
                        ErrorMessages.DEFAULT_AD_SIZE_TYPE_ERROR_ALERT.getText(),
                        ErrorMessages.DEFAULT_FLOOR_PRICE_ERROR_ALERT.getText())
                )

                .and("Select Ad Size")
                .clickOnWebElement(editAdSpotSidebar.getGeneralTab().getDefaultAdSizes())
                .clickOnWebElement(editAdSpotSidebar.getGeneralTab().getAdSizesPanel().getAdSizeCheckbox(AdSizesList.A120x20))
                .clickOnWebElement(editAdSpotSidebar.getGeneralTab().getNameInput())
                .then("Validate error under the 'Default Ad Sizes' field disappeared")
                .waitAndValidate(not(visible), editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Default Ad Sizes"))
                .and("Click 'Save'")
                .clickOnWebElement(editAdSpotSidebar.getSaveButton())
                .then("Validate error under the 'Default Floor Price' field")
                .waitAndValidate(visible, editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Default Floor Price"))
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(
                        ErrorMessages.DEFAULT_FLOOR_PRICE_ERROR_ALERT.getText())
                )
                .and("Fill Default Floor Price")
                .setValueWithClean(editAdSpotSidebar.getGeneralTab().getDefaultFloorPrice(), "0.00")
                .pressEnterKey(editAdSpotSidebar.getGeneralTab().getDefaultFloorPrice())
                .then("Validate error under the 'Default Floor Price' field disappeared")
                .waitAndValidate(not(visible), editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Default Floor Price"))
                .then("Validate errors disappeared")
                .waitAndValidate(not(visible), editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Default Floor Price"))

                .and("Toaster Error message appears")
                .waitAndValidate(visible, adSpotPage.getToasterMessage().getPanelError())
                .clickOnWebElement(adSpotPage.getToasterMessage().getViewErrorDetails())
                .waitAndValidate(visible, adSpotPage.getToasterMessage().getMessageError())
                .validateContainsText(adSpotPage.getToasterMessage().getMessageError(),
                        ErrorMessages.AT_LEAST_ONE_FORMAT_MUST_BE_ENABLED.getText())
                .clickOnWebElement(adSpotPage.getToasterMessage().getRemoveIcon())
                .waitAndValidate(not(visible), adSpotPage.getToasterMessage().getPanelError())
                .and("Native is enabled")
                .turnToggleOn(nativeCard.getEnabledToggle())
                .and("Click 'Save'")
                .scrollIntoView(editAdSpotSidebar.getSaveButton())
                .clickOnWebElement(editAdSpotSidebar.getSaveButton())
                .validate(not(visible), editAdSpotSidebar.getGeneralTab().getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), adSpotPage.getToasterMessage().getPanelError())
                .and("Close Ad Spot Sidebar")
                .waitSideBarClosed()
                .testEnd();
    }

    @Test(description = "Check Video Card fields", priority = 3)
    public void checkVideoCardFields() {
        var videoCard = editAdSpotSidebar.getGeneralTab().getVideoCard();
        var bannerCard = editAdSpotSidebar.getGeneralTab().getBannerCard();
        var nativeCard = editAdSpotSidebar.getGeneralTab().getNativeCard();
        var errorsList = editAdSpotSidebar.getGeneralTab().getErrorAlert().getErrorsList();

        fillGeneralFields(media1.getPublisherName(), media1.getName());
        testStart()
                .and("Expand Video Card")
                .scrollIntoView(videoCard.getVideoCardHeader())
                .clickOnWebElement(videoCard.getVideoCardHeader())
                .turnToggleOn(videoCard.getEnabledToggle())
                .turnToggleOn(bannerCard.getEnabledToggle())
                .turnToggleOn(nativeCard.getEnabledToggle())
                .and("Click 'Save'")
                .clickOnWebElement(editAdSpotSidebar.getSaveButton())
                .then("Validate error under the 'Video Placement Type' field")
                .waitAndValidate(visible, editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Video Placement Type"))
                .then("Validate error under the 'Video Playback Methods' field")
                .waitAndValidate(visible, editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Video Playback Methods"))
                .validateListSize(errorsList, 2)
                .validateList(errorsList, List.of(
                        ErrorMessages.VIDEO_PLACEMENT_TYPE_ERROR_ALERT.getText(),
                        ErrorMessages.VIDEO_PLAYBACK_METHOD_ERROR_ALERT.getText())
                )
                .and("Fill Video Placement Type")
                .selectFromDropdown(videoCard.getVideoPlacementType(),
                        videoCard.getVideoPlacementTypeItems(), "In-Stream")
                .then("Validate error under the 'Video Placement Type' field disappeared")
                .waitAndValidate(not(visible), editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Video Placement Type"))
                .then("Validate error under the 'Video Playback Methods' field")
                .waitAndValidate(visible, editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Video Playback Methods"))
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(
                        ErrorMessages.VIDEO_PLAYBACK_METHOD_ERROR_ALERT.getText())
                )

                .and("Fill Video Playback Method")
                .selectFromDropdown(videoCard.getVideoPlaybackMethods(),
                        videoCard.getVideoPlaybackMethodsItems(), "Click Sound On")

                .then("Validate errors disappeared")
                .waitAndValidate(not(visible), editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Video Playback Methods"))
                .validate(not(visible), editAdSpotSidebar.getGeneralTab().getErrorAlert().getErrorPanel())
                .and("Close Ad Spot Sidebar")
                .clickOnWebElement(editAdSpotSidebar.getCloseIcon())
                .waitSideBarClosed()
                .testEnd();
    }

    @Test(description = "Check Minimum Value Default Floor Price", priority = 4)
    public void checkMinValueDefaultFloorPrice() {
        var errorsList = editAdSpotSidebar.getGeneralTab().getErrorAlert().getErrorsList();

        fillGeneralFields(media1.getPublisherName(), media1.getName());
        testStart()
                .setValueWithClean(editAdSpotSidebar.getGeneralTab().getDefaultFloorPrice(), "-1.00")
                .and("Click 'Save'")
                .clickOnWebElement(editAdSpotSidebar.getSaveButton())
                .then("Validate error under the 'Default Floor Price' field")
                .waitAndValidate(visible, editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Default Floor Price"))
                .validate(editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Default Floor Price"),
                        ErrorMessages.MIN_MAX_VALUE_FLOOR_PRICE.getText())
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(
                        ErrorMessages.MIN_MAX_VALUE_FLOOR_PRICE.getText())
                )
                .setValueWithClean(editAdSpotSidebar.getGeneralTab().getDefaultFloorPrice(), "0.00")
                .pressEnterKey(editAdSpotSidebar.getGeneralTab().getDefaultFloorPrice())
                .then("Validate error under the 'Default Floor Price' field disappeared")
                .waitAndValidate(not(visible), editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Default Floor Price"))
                .then("Validate errors disappeared")
                .waitAndValidate(not(visible), editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Default Floor Price"))
                .validate(not(visible), editAdSpotSidebar.getGeneralTab().getErrorAlert().getErrorPanel())
                .and("Toaster Error message appears")
                .waitAndValidate(visible, adSpotPage.getToasterMessage().getPanelError())
                .clickOnWebElement(adSpotPage.getToasterMessage().getViewErrorDetails())
                .waitAndValidate(visible, adSpotPage.getToasterMessage().getMessageError())
                .validateContainsText(adSpotPage.getToasterMessage().getMessageError(),
                        ErrorMessages.AT_LEAST_ONE_FORMAT_MUST_BE_ENABLED.getText())
                .clickOnWebElement(adSpotPage.getToasterMessage().getRemoveIcon())
                .waitAndValidate(not(visible), adSpotPage.getToasterMessage().getPanelError())
                .and("Close Ad Spot Sidebar")
                .clickOnWebElement(editAdSpotSidebar.getCloseIcon())
                .waitSideBarClosed()
                .testEnd();
    }

    @Epic("GS-3712")
    @Test(description = "Check Minimum Value Banner Floor Price", priority = 5)
    public void checkMinValueBannerFloorPrice() {
        var bannerCard = editAdSpotSidebar.getGeneralTab().getBannerCard();
        var errorsList = editAdSpotSidebar.getGeneralTab().getErrorAlert().getErrorsList();

        fillGeneralFields(media1.getPublisherName(), media1.getName());

        if (bannerCard.getBannerPanel().getAttribute("aria-expanded").equals("false")) {
            testStart()
                    .clickOnWebElement(bannerCard.getBannerCardHeader())
                    .testEnd();
        }

        testStart()
                .turnToggleOn(bannerCard.getEnabledToggle())
                .setValueWithClean(bannerCard.getFloorPriceField().getFloorPriceInput(), "-1.00")
                .and("Click 'Save'")
                .clickOnWebElement(editAdSpotSidebar.getSaveButton())
                .then("Validate error under the 'Floor Price' field")
                .waitAndValidate(visible, bannerCard.getErrorAlertByFieldName("Floor Price"))
                .validate(bannerCard.getErrorAlertByFieldName("Floor Price"),
                        ErrorMessages.MIN_MAX_VALUE_FLOOR_PRICE.getText())
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(
                        ErrorMessages.MIN_MAX_VALUE_FLOOR_PRICE.getText())
                )
                .setValueWithClean(bannerCard.getFloorPriceField().getFloorPriceInput(), "0.00")
                .pressEnterKey(bannerCard.getFloorPriceField().getFloorPriceInput())
                .then("Validate error under the 'Floor Price' field disappeared")
                .waitAndValidate(not(visible), bannerCard.getErrorAlertByFieldName("Floor Price"))
                .then("Validate errors disappeared")
                .waitAndValidate(not(visible), bannerCard.getErrorAlertByFieldName("Floor Price"))
                .validate(not(visible), editAdSpotSidebar.getGeneralTab().getErrorAlert().getErrorPanel())
                .and("Close Ad Spot Sidebar")
                .clickOnWebElement(editAdSpotSidebar.getCloseIcon())
                .waitSideBarClosed()
                .testEnd();
    }

    @Test(description = "Check Minimum Value Native Floor Price", priority = 6)
    public void checkMinValueNativeFloorPrice() {
        var nativeCard = editAdSpotSidebar.getGeneralTab().getNativeCard();
        var errorsList = editAdSpotSidebar.getGeneralTab().getErrorAlert().getErrorsList();

        fillGeneralFields(media1.getPublisherName(), media1.getName());

        if (nativeCard.getNativePanel().getAttribute("aria-expanded").equals("false")) {
            testStart()
                    .clickOnWebElement(nativeCard.getNativeCardHeader())
                    .testEnd();
        }

        testStart()
                .turnToggleOn(nativeCard.getEnabledToggle())
                .setValueWithClean(nativeCard.getFloorPriceField().getFloorPriceInput(), "-1.00")
                .and("Click 'Save'")
                .clickOnWebElement(editAdSpotSidebar.getSaveButton())
                .then("Validate error under the 'Floor Price' field")
                .waitAndValidate(visible, nativeCard.getErrorAlertByFieldName("Floor Price"))
                .validate(nativeCard.getErrorAlertByFieldName("Floor Price"),
                        ErrorMessages.MIN_MAX_VALUE_FLOOR_PRICE.getText())
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(
                        ErrorMessages.MIN_MAX_VALUE_FLOOR_PRICE.getText())
                )
                .setValueWithClean(nativeCard.getFloorPriceField().getFloorPriceInput(), "0.00")
                .pressEnterKey(nativeCard.getFloorPriceField().getFloorPriceInput())
                .then("Validate error under the 'Floor Price' field disappeared")
                .waitAndValidate(not(visible), nativeCard.getErrorAlertByFieldName("Floor Price"))
                .then("Validate errors disappeared")
                .waitAndValidate(not(visible), nativeCard.getErrorAlertByFieldName("Floor Price"))
                .validate(not(visible), editAdSpotSidebar.getGeneralTab().getErrorAlert().getErrorPanel())
                .and("Close Ad Spot Sidebar")
                .waitSideBarClosed()
                .testEnd();
    }

    @Test(description = "Check Minimum Value Video Floor Price", priority = 7)
    public void checkMinValueVideoFloorPrice() {
        var videoCard = editAdSpotSidebar.getGeneralTab().getVideoCard();
        var errorsList = editAdSpotSidebar.getGeneralTab().getErrorAlert().getErrorsList();

        fillGeneralFields(media1.getPublisherName(), media1.getName());

        if (videoCard.getVideoPanel().getAttribute("aria-expanded").equals("false")) {
            testStart()
                    .clickOnWebElement(videoCard.getVideoCardHeader())
                    .testEnd();
        }

        testStart()
                .turnToggleOn(videoCard.getEnabledToggle())
                .and("Fill Video Placement Type")
                .selectFromDropdown(videoCard.getVideoPlacementType(),
                        videoCard.getVideoPlacementTypeItems(), "In-Stream")
                .and("Fill Video Playback Method")
                .scrollIntoView(videoCard.getVideoPlaybackMethods())
                .selectFromDropdown(videoCard.getVideoPlaybackMethods(),
                        videoCard.getVideoPlaybackMethodsItems(), "Click Sound On")
                .setValueWithClean(videoCard.getFloorPriceField().getFloorPriceInput(), "-1.00")
                .and("Click 'Save'")
                .clickOnWebElement(editAdSpotSidebar.getSaveButton())
                .then("Validate error under the 'Floor Price' field")
                .waitAndValidate(visible, videoCard.getErrorAlertByFieldName("Floor Price"))
                .validate(videoCard.getErrorAlertByFieldName("Floor Price"),
                        ErrorMessages.MIN_MAX_VALUE_FLOOR_PRICE.getText())
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(
                        ErrorMessages.MIN_MAX_VALUE_FLOOR_PRICE.getText())
                )
                .setValueWithClean(videoCard.getFloorPriceField().getFloorPriceInput(), "0.00")
                .pressEnterKey(videoCard.getFloorPriceField().getFloorPriceInput())
                .then("Validate error under the 'Floor Price' field disappeared")
                .waitAndValidate(not(visible), videoCard.getErrorAlertByFieldName("Floor Price"))
                .then("Validate errors disappeared")
                .waitAndValidate(not(visible), videoCard.getErrorAlertByFieldName("Floor Price"))
                .validate(not(visible), editAdSpotSidebar.getGeneralTab().getErrorAlert().getErrorPanel())
                .and("Close Ad Spot Sidebar")
                .waitSideBarClosed()
                .testEnd();
    }

    @Test(description = "Check Maximum Value Default Floor Price", priority = 8)
    public void checkMaxValueDefaultFloorPrice() {
        var errorsList = editAdSpotSidebar.getGeneralTab().getErrorAlert().getErrorsList();

        fillGeneralFields(media1.getPublisherName(), media1.getName());
        testStart()
                .setValueWithClean(editAdSpotSidebar.getGeneralTab().getDefaultFloorPrice(), "1000000.00")
                .and("Click 'Save'")
                .clickOnWebElement(editAdSpotSidebar.getSaveButton())
                .then("Validate error under the 'Default Floor Price' field")
                .waitAndValidate(visible, editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Default Floor Price"))
                .validate(editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Default Floor Price"),
                        ErrorMessages.MIN_MAX_VALUE_FLOOR_PRICE.getText())
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(
                        ErrorMessages.MIN_MAX_VALUE_FLOOR_PRICE.getText())
                )
                .setValueWithClean(editAdSpotSidebar.getGeneralTab().getDefaultFloorPrice(), "")
                .setValueWithClean(editAdSpotSidebar.getGeneralTab().getDefaultFloorPrice(), "0.00")
                .pressEnterKey(editAdSpotSidebar.getGeneralTab().getDefaultFloorPrice())
                .then("Validate error under the 'Default Floor Price' field disappeared")
                .waitAndValidate(not(visible), editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Default Floor Price"))
                .then("Validate errors disappeared")
                .waitAndValidate(not(visible), editAdSpotSidebar.getGeneralTab().getErrorAlertByFieldName("Default Floor Price"))
                .validate(not(visible), editAdSpotSidebar.getGeneralTab().getErrorAlert().getErrorPanel())
                .and("Toaster Error message appears")
                .waitAndValidate(visible, adSpotPage.getToasterMessage().getPanelError())
                .clickOnWebElement(adSpotPage.getToasterMessage().getViewErrorDetails())
                .waitAndValidate(visible, adSpotPage.getToasterMessage().getMessageError())
                .validateContainsText(adSpotPage.getToasterMessage().getMessageError(),
                        ErrorMessages.AT_LEAST_ONE_FORMAT_MUST_BE_ENABLED.getText())
                .clickOnWebElement(adSpotPage.getToasterMessage().getRemoveIcon())
                .waitAndValidate(not(visible), adSpotPage.getToasterMessage().getPanelError())
                .and("Close Ad Spot Sidebar")
                .clickOnWebElement(editAdSpotSidebar.getCloseIcon())
                .waitSideBarClosed()
                .testEnd();
    }

    @Test(description = "Check Maximum Value Banner Floor Price", priority = 9)
    public void checkMaxValueBannerFloorPrice() {
        var bannerCard = editAdSpotSidebar.getGeneralTab().getBannerCard();
        var errorsList = editAdSpotSidebar.getGeneralTab().getErrorAlert().getErrorsList();

        fillGeneralFields(media1.getPublisherName(), media1.getName());

        if (bannerCard.getBannerPanel().getAttribute("aria-expanded").equals("false")) {
            testStart()
                    .clickOnWebElement(bannerCard.getBannerCardHeader())
                    .testEnd();
        }

        testStart()
                .turnToggleOn(bannerCard.getEnabledToggle())
                .setValueWithClean(bannerCard.getFloorPriceField().getFloorPriceInput(), "1000000.00")
                .and("Click 'Save'")
                .clickOnWebElement(editAdSpotSidebar.getSaveButton())
                .then("Validate error under the 'Floor Price' field")
                .waitAndValidate(visible, bannerCard.getErrorAlertByFieldName("Floor Price"))
                .validate(bannerCard.getErrorAlertByFieldName("Floor Price"),
                        ErrorMessages.MIN_MAX_VALUE_AD_SPOT_FLOOR_PRICE.getText())
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(
                        ErrorMessages.MIN_MAX_VALUE_AD_SPOT_FLOOR_PRICE.getText())
                )
                .setValueWithClean(bannerCard.getFloorPriceField().getFloorPriceInput(), "10000.00")
                .pressEnterKey(bannerCard.getFloorPriceField().getFloorPriceInput())
                .then("Validate error under the 'Floor Price' field disappeared")
                .waitAndValidate(not(visible), bannerCard.getErrorAlertByFieldName("Floor Price"))
                .then("Validate errors disappeared")
                .waitAndValidate(not(visible), bannerCard.getErrorAlertByFieldName("Floor Price"))
                .validate(not(visible), editAdSpotSidebar.getGeneralTab().getErrorAlert().getErrorPanel())
                .and("Close Ad Spot Sidebar")
                .waitSideBarClosed()
                .testEnd();
    }

    @Test(description = "Check Maximum Value Native Floor Price", priority = 10)
    public void checkMaxValueNativeFloorPrice() {
        var nativeCard = editAdSpotSidebar.getGeneralTab().getNativeCard();
        var errorsList = editAdSpotSidebar.getGeneralTab().getErrorAlert().getErrorsList();

        fillGeneralFields(media1.getPublisherName(), media1.getName());

        if (nativeCard.getNativePanel().getAttribute("aria-expanded").equals("false")) {
            testStart()
                    .clickOnWebElement(nativeCard.getNativeCardHeader())
                    .testEnd();
        }

        testStart()
                .turnToggleOn(nativeCard.getEnabledToggle())
                .setValueWithClean(nativeCard.getFloorPriceField().getFloorPriceInput(), "1000000.00")
                .and("Click 'Save'")
                .clickOnWebElement(editAdSpotSidebar.getSaveButton())
                .then("Validate error under the 'Floor Price' field")
                .waitAndValidate(visible, nativeCard.getErrorAlertByFieldName("Floor Price"))
                .validate(nativeCard.getErrorAlertByFieldName("Floor Price"),
                        ErrorMessages.MIN_MAX_VALUE_FLOOR_PRICE.getText())
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(
                        ErrorMessages.MIN_MAX_VALUE_FLOOR_PRICE.getText())
                )
                .setValueWithClean(nativeCard.getFloorPriceField().getFloorPriceInput(), "0.00")
                .pressEnterKey(nativeCard.getFloorPriceField().getFloorPriceInput())
                .then("Validate error under the 'Floor Price' field disappeared")
                .waitAndValidate(not(visible), nativeCard.getErrorAlertByFieldName("Floor Price"))
                .then("Validate errors disappeared")
                .waitAndValidate(not(visible), nativeCard.getErrorAlertByFieldName("Floor Price"))
                .validate(not(visible), editAdSpotSidebar.getGeneralTab().getErrorAlert().getErrorPanel())
                .and("Close Ad Spot Sidebar")
                .waitSideBarClosed()
                .testEnd();
    }

    @Test(description = "Check Maximum Value Video Floor Price", priority = 11)
    private void checkMaxValueVideoFloorPrice() {
        var videoCard = editAdSpotSidebar.getGeneralTab().getVideoCard();
        var errorsList = editAdSpotSidebar.getGeneralTab().getErrorAlert().getErrorsList();

        fillGeneralFields(media1.getPublisherName(), media1.getName());

        if (videoCard.getVideoPanel().getAttribute("aria-expanded").equals("false")) {
            testStart()
                    .clickOnWebElement(videoCard.getVideoCardHeader())
                    .testEnd();
        }

        testStart()
                .turnToggleOn(videoCard.getEnabledToggle())
                .and("Fill Video Placement Type")
                .selectFromDropdown(videoCard.getVideoPlacementType(),
                        videoCard.getVideoPlacementTypeItems(), "In-Stream")
                .and("Fill Video Playback Method")
                .scrollIntoView(videoCard.getVideoPlaybackMethods())
                .selectFromDropdown(videoCard.getVideoPlaybackMethods(),
                        videoCard.getVideoPlaybackMethodsItems(), "Click Sound On")
                .setValueWithClean(videoCard.getFloorPriceField().getFloorPriceInput(), "1000000.00")
                .and("Click 'Save'")
                .clickOnWebElement(editAdSpotSidebar.getSaveButton())
                .then("Validate error under the 'Floor Price' field")
                .waitAndValidate(visible, videoCard.getErrorAlertByFieldName("Floor Price"))
                .validate(videoCard.getErrorAlertByFieldName("Floor Price"),
                        ErrorMessages.MIN_MAX_VALUE_FLOOR_PRICE.getText())
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(
                        ErrorMessages.MIN_MAX_VALUE_FLOOR_PRICE.getText())
                )
                .setValueWithClean(videoCard.getFloorPriceField().getFloorPriceInput(), "999,999.99")
                .pressEnterKey(videoCard.getFloorPriceField().getFloorPriceInput())
                .then("Validate error under the 'Floor Price' field disappeared")
                .waitAndValidate(not(visible), videoCard.getErrorAlertByFieldName("Floor Price"))
                .then("Validate errors disappeared")
                .waitAndValidate(not(visible), videoCard.getErrorAlertByFieldName("Floor Price"))
                .validate(not(visible), editAdSpotSidebar.getGeneralTab().getErrorAlert().getErrorPanel())
                .and("Close Ad Spot Sidebar")
                .waitSideBarClosed()
                .testEnd();
    }

    @Test(description = "Check Minimum Video Duration Value", priority = 12)
    public void checkMinVideoDurationValue() {
        var videoCard = editAdSpotSidebar.getGeneralTab().getVideoCard();
        var errorsList = editAdSpotSidebar.getGeneralTab().getErrorAlert().getErrorsList();

        fillGeneralFields(media1.getPublisherName(), media1.getName());

        if (videoCard.getVideoPanel().getAttribute("aria-expanded").equals("false")) {
            testStart()
                    .clickOnWebElement(videoCard.getVideoCardHeader())
                    .testEnd();
        }

        testStart()
                .turnToggleOn(videoCard.getEnabledToggle())
                .and("Fill Video Placement Type")
                .selectFromDropdown(videoCard.getVideoPlacementType(),
                        videoCard.getVideoPlacementTypeItems(), "In-Stream")
                .and("Fill Video Playback Method")
                .scrollIntoView(videoCard.getVideoPlaybackMethods())
                .selectFromDropdown(videoCard.getVideoPlaybackMethods(),
                        videoCard.getVideoPlaybackMethodsItems(), "Click Sound On")
                .clickOnWebElement(videoCard.getFloorPriceField().getFloorPricePrefix())
                .setValueWithClean(videoCard.getMinVideoDuration(), "-1")
                .clickOnWebElement(editAdSpotSidebar.getAdSpotSideBarTitle())
                .then("Validate error under the 'Minimum Video Duration' field")
                .waitAndValidate(visible, videoCard.getErrorAlertByFieldName("Minimum Video Duration (seconds)"))
                .validate(videoCard.getErrorAlertByFieldName("Minimum Video Duration (seconds)"),
                        ErrorMessages.DURATION_ERROR_ALERT.getText())
                .validateListSize(errorsList, 1)
                .pressEscapeKey(videoCard.getMinVideoDuration())
                .setValueWithClean(videoCard.getMinVideoDuration(), "0")
                .then("Validate error under the 'Minimum Video Duration (seconds)' field disappeared")
                .waitAndValidate(not(visible), videoCard.getErrorAlertByFieldName("Minimum Video Duration (seconds)"))
                .then("Validate errors disappeared")
                .waitAndValidate(not(visible), videoCard.getErrorAlertByFieldName("Minimum Video Duration (seconds)"))
                .validate(not(visible), editAdSpotSidebar.getGeneralTab().getErrorAlert().getErrorPanel())
                .and("Close Ad Spot Sidebar")
                .clickOnWebElement(editAdSpotSidebar.getCloseIcon())
                .waitSideBarClosed()
                .testEnd();
    }

    @Step("Fill general fields")
    private void fillGeneralFields(String publisherName, String mediaName) {
        var adSpotName = captionWithSuffix("4autoAdSpot");

        testStart()
                .and(String.format("Select Publisher '%s'", publisherName))
                .selectFromDropdown(editAdSpotSidebar.getGeneralTab().getPublisherInput(),
                        editAdSpotSidebar.getGeneralTab().getPublisherItems(), publisherName)
                .and("Fill Ad Spot Name")
                .setValueWithClean(editAdSpotSidebar.getGeneralTab().getNameInput(), adSpotName)
                .selectFromDropdown(editAdSpotSidebar.getGeneralTab().getRelatedMedia(),
                        editAdSpotSidebar.getGeneralTab().getRelatedMediaItems(), mediaName)
                .selectFromDropdown(editAdSpotSidebar.getGeneralTab().getPosition(),
                        editAdSpotSidebar.getGeneralTab().getPositionItems(), "Header")
                .clickOnWebElement(editAdSpotSidebar.getGeneralTab().getDefaultAdSizes())
                .clickOnWebElement(editAdSpotSidebar.getGeneralTab().getAdSizesPanel().getAdSizeCheckbox(AdSizesList.A120x20))
                .clickOnWebElement(editAdSpotSidebar.getGeneralTab().getNameInput())
                .setValueWithClean(editAdSpotSidebar.getGeneralTab().getDefaultFloorPrice(), "0.00")
                .testEnd();
    }

    @AfterClass(alwaysRun = true)
    private void deletePublisher() {

        testStart()
                .and("Logout")
                .logOut()
                .testEnd();

        if (media()
                .setCredentials(USER_FOR_DELETION)
                .deleteMedia(media1.getId())
                .build()
                .getResponseCode() == HttpStatus.SC_NO_CONTENT)
            log.info(String.format("Deleted media %s", media1.getId()));

        if (publisher()
                .setCredentials(USER_FOR_DELETION)
                .deletePublisher(media1.getPublisherId())
                .build()
                .getResponseCode() == HttpStatus.SC_NO_CONTENT)
            log.info(String.format("Deleted publisher %s", media1.getPublisherId()));
    }
}
