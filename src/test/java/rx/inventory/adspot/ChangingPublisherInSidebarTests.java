package rx.inventory.adspot;

import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.common.Currency;
import api.dto.rx.inventory.media.Media;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;
import pages.Path;
import pages.inventory.adspots.AdSpotsPage;
import rx.BaseTest;
import widgets.common.adSizes.AdSizesList;
import widgets.common.categories.CategoriesList;
import widgets.inventory.adSpots.sidebar.EditAdSpotSidebar;

import java.util.List;

import static api.preconditionbuilders.MediaPrecondition.media;
import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static configurations.User.USER_FOR_DELETION;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature("Ad Spots")
public class ChangingPublisherInSidebarTests extends BaseTest {

    private Media media1;
    private Media media2;
    private Publisher publisher1;
    private Publisher publisher2;
    private AdSpotsPage adSpotPage;
    private EditAdSpotSidebar editAdSpotSidebar;

    private static final String VIDEO_FLOOR_PRICE = "15";
    private static final String BANNER_FLOOR_PRICE = "12";
    private static final String NATIVE_FLOOR_PRICE = "18";
    private static final String DEFAULT_FLOOR_PRICE = "10";
    private final static List<Integer> CATEGORY_IDS = List.of(1, 9);
    private final static List<Integer> DSP_IDS_PUBLISHER = List.of(9, 11, 13);

    private static final String BANNER_TEXT =
            "By changing the Publisher the form will be reset and the previous changes will not be saved.";

    public ChangingPublisherInSidebarTests() {
        adSpotPage = new AdSpotsPage();
        editAdSpotSidebar = new EditAdSpotSidebar();
    }

    @BeforeClass
    private void init() {
        publisher1 = publisher()
                .createNewPublisher(captionWithSuffix("0000autoPub1"), true,
                        Currency.JPY, CATEGORY_IDS, DSP_IDS_PUBLISHER)
                .build()
                .getPublisherResponse();

        publisher2 = publisher()
                .createNewPublisher(captionWithSuffix("0000autoPub2"), true,
                        Currency.EUR, CATEGORY_IDS, DSP_IDS_PUBLISHER)
                .build()
                .getPublisherResponse();

        media1 = media()
                .createNewMedia(captionWithSuffix("auto1Media"), publisher1.getId(), true)
                .build()
                .getMediaResponse();

        media2 = media()
                .createNewMedia(captionWithSuffix("auto2Media"), publisher2.getId(), true)
                .build()
                .getMediaResponse();
    }

    @BeforeMethod
    private void login() {

        testStart()
                .given()
                .openDirectPath(Path.AD_SPOT)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, adSpotPage.getNuxtProgress())
                .clickOnWebElement(adSpotPage.getCreateAdSpotButton())
                .waitSideBarOpened()
                .testEnd();
    }


    @Test(description = "Change Publisher and check fields")
    public void adSpotChangePublisher() {

        testStart()
                .clickBrowserRefreshButton()
                .and(String.format("Select Publisher '%s'", publisher1.getName()))
                .selectFromDropdown(editAdSpotSidebar.getGeneralTab().getPublisherInput(),
                        editAdSpotSidebar.getGeneralTab().getPublisherItems(), publisher1.getName())
                .testEnd();

        fillGeneralFields(publisher1.getName(), media1.getName());
        fillBannerFormat();
        fillVideoFormat();
        fillNativeFormat();
        changePublisherAndClickAccept(publisher2.getName());
        validateFieldsAreCleanedAndRelatedMediaListIsCorrect(media2.getName());
        changePublisherAndClickAccept(publisher1.getName());
        validateFieldsAreCleanedAndRelatedMediaListIsCorrect(media1.getName());
        changePublisherAndClickAccept(publisher2.getName());
        validateFieldsAreCleanedAndRelatedMediaListIsCorrect(media2.getName());
        changePublisherAndClickAccept(publisher1.getName());
        validateFieldsAreCleanedAndRelatedMediaListIsCorrect(media1.getName());
        changePublisherAndClickCancel(publisher2.getName());
        validateFieldsValuesShouldNotBeChanged(publisher1.getName(), media1.getName());
        changePublisherAndClickAccept(publisher2.getName());
        validateFieldsAreCleanedAndRelatedMediaListIsCorrect(media2.getName());
        fillGeneralFields(publisher2.getName(), media2.getName());
        changePublisherAndClickCancel(publisher1.getName());
        validateFieldsValuesShouldNotBeChanged(publisher2.getName(), media2.getName());
    }


    @Step("Fill general fields")
    private void fillGeneralFields(String publisherName, String mediaName) {
        var adSpotName = captionWithSuffix("4autoAdSpot");
        var categories = editAdSpotSidebar.getGeneralTab().getCategoriesPanel();
        testStart()
                .and("Fill Ad Spot Name")
                .setValueWithClean(editAdSpotSidebar.getGeneralTab().getNameInput(), adSpotName)
                .and("Select Related Media")
                .selectFromDropdown(editAdSpotSidebar.getGeneralTab().getRelatedMedia(),
                        editAdSpotSidebar.getGeneralTab().getRelatedMediaItems(), mediaName)
                .and("Select Position value")
                .selectFromDropdown(editAdSpotSidebar.getGeneralTab().getPosition(),
                        editAdSpotSidebar.getGeneralTab().getPositionItems(), "Header")
                .and("Select Ad size")
                .clickOnWebElement(editAdSpotSidebar.getGeneralTab().getDefaultAdSizes())
                .clickOnWebElement(editAdSpotSidebar.getGeneralTab().getAdSizesPanel().getAdSizeCheckbox(AdSizesList.A120x20))
                .clickOnWebElement(editAdSpotSidebar.getGeneralTab().getNameInput())
                .and("Set Default Floor Price")
                .setValueWithClean(editAdSpotSidebar.getGeneralTab().getDefaultFloorPrice(), DEFAULT_FLOOR_PRICE)
                .and("Select Categories")
                .clickOnWebElement(editAdSpotSidebar.getGeneralTab().getCategories())
                .clickOnWebElement(categories.getCategoryCheckbox(CategoriesList.EDUCATION))
                .clickOnWebElement(categories.getCategoryGroupIcon(CategoriesList.AUTOMOTIVE))
                .clickOnWebElement(categories.getCategoryCheckbox(CategoriesList.AUTO_REPAIR))
                .clickOnWebElement(editAdSpotSidebar.getGeneralTab().getNameInput())
                .testEnd();
    }

    @Step("Change Publisher on {0} and click Accept")
    private void changePublisherAndClickAccept(String publisherName) {
        var changePublisherBanner = editAdSpotSidebar.getGeneralTab().getChangePublisherBanner();

        testStart()
                .and(String.format("Select Publisher %s", publisherName))
                .selectFromDropdown(editAdSpotSidebar.getGeneralTab().getPublisherInput(),
                        editAdSpotSidebar.getGeneralTab().getPublisherItems(), publisherName)
                .then("Check that warning banner appears")
                .validate(visible, changePublisherBanner.getBannerText(BANNER_TEXT))
                .then("Click 'Accept' on Warning Banner")
                .clickOnWebElement(changePublisherBanner.getAcceptButton(BANNER_TEXT))
                .testEnd();
    }

    @Step("Change Publisher on {0} and click Cancel")
    private void changePublisherAndClickCancel(String publisherName) {
        var changePublisherBanner = editAdSpotSidebar.getGeneralTab().getChangePublisherBanner();

        testStart()
                .and(String.format("Select Publisher %s", publisherName))
                .selectFromDropdown(editAdSpotSidebar.getGeneralTab().getPublisherInput(),
                        editAdSpotSidebar.getGeneralTab().getPublisherItems(), publisherName)
                .then("Check that warning banner appears")
                .validate(visible, changePublisherBanner.getBannerText(BANNER_TEXT))
                .and("Click 'Accept' on Warning Banner")
                .clickOnWebElement(changePublisherBanner.getCancelButton(BANNER_TEXT))
                .testEnd();
    }

    @Step("Related media list should correspond Publisher and all fields should be cleaned")
    private void validateFieldsAreCleanedAndRelatedMediaListIsCorrect(String mediaName) {
        var videoCard = editAdSpotSidebar.getGeneralTab().getVideoCard();
        var bannerCard = editAdSpotSidebar.getGeneralTab().getBannerCard();
        var nativeCard = editAdSpotSidebar.getGeneralTab().getNativeCard();

        testStart()
                .then("Name should be cleaned")
                .validate(editAdSpotSidebar.getGeneralTab().getNameInput(), "")
                .then("Default Floor Price should be cleaned")
                .validate(editAdSpotSidebar.getGeneralTab().getDefaultFloorPrice(), "")
                .then("Default Floor Price should be cleaned")
                .validate(editAdSpotSidebar.getGeneralTab().getDefaultAdSizesInput(), "")
                .then("Categories should be cleaned")
                .validate(editAdSpotSidebar.getGeneralTab().getCategories().getText(), "")
                .then("Position should be cleaned")
                .validate(editAdSpotSidebar.getGeneralTab().getPositionInput().getText(), "")
                .clickOnWebElement(videoCard.getVideoCardHeader())
                .clickOnWebElement(nativeCard.getNativeCardHeader())
                .clickOnWebElement(bannerCard.getBannerCardHeader())
                .then("Video should be disabled")
                .validateAttribute(videoCard.getEnabledToggle(), "aria-checked", "false")
                .then("Video Placement type should be empty")
                .validate(videoCard.getVideoPlacementType(), "")
                .then("Video Playback Method should be empty")
                .validate(videoCard.getVideoPlaybackMethods(), "")
                .then("Video Floor Price should be empty")
                .validate(videoCard.getFloorPriceField().getFloorPriceInput(), "")
                .then("Video Max duration should be empty")
                .validate(videoCard.getMaxVideoDuration(), "")
                .then("VideoMin duration should be empty")
                .validate(videoCard.getMinVideoDuration(), "")
                .then("Video Ad sizes should be empty")
                .validate(videoCard.getVideoAdSizes(), "")
                .then("Native card should be disabled")
                .validateAttribute(nativeCard.getEnabledToggle(), "aria-checked", "false")
                .then("Native Floor Price should be empty")
                .validate(nativeCard.getFloorPriceField().getFloorPriceInput(), "")
                .then("Banner card should be disabled")
                .validateAttribute(bannerCard.getEnabledToggle(), "aria-checked", "false")
                .then("Banner Ad sizes should be empty")
                .validate(bannerCard.getAdSizes(), "")
                .then("Banner Floor price should be empty")
                .validate(bannerCard.getFloorPriceField().getFloorPriceInput(), "")

                .and("Select Related Media")
                .selectFromDropdown(editAdSpotSidebar.getGeneralTab().getRelatedMedia(),
                        editAdSpotSidebar.getGeneralTab().getRelatedMediaItems(), mediaName)
                .testEnd();

    }

    @Step("Expand Banner Card and fill fields")
    private void fillBannerFormat() {
        var bannerCard = editAdSpotSidebar.getGeneralTab().getBannerCard();

        testStart()
                .and("Expand Banner Card")
                .clickOnWebElement(bannerCard.getBannerCardHeader())
                .and("Switch toggle to Enabled state")
                .turnToggleOn(bannerCard.getEnabledToggle())
                .and("Select Ad Size")
                .clickOnWebElement(bannerCard.getAdSizes())
                .clickOnWebElement(bannerCard.getAdSizesPanel().getAdSizeCheckbox(AdSizesList.A120x60))
                .and("Set Floor Price")
                .setValueWithClean(bannerCard.getFloorPriceField().getFloorPriceInput(), BANNER_FLOOR_PRICE)
                .testEnd();
    }

    @Step("Expand Video Card and fill fields")
    private void fillVideoFormat() {
        var videoCard = editAdSpotSidebar.getGeneralTab().getVideoCard();

        testStart()
                .and("Expand Video Card")
                .scrollIntoView(videoCard.getVideoCardHeader())
                .clickOnWebElement(videoCard.getVideoCardHeader())
                .and("Switch toggle to enabled state ")
                .turnToggleOn(videoCard.getEnabledToggle())
                .and("Fill Video Placement Type")
                .selectFromDropdown(videoCard.getVideoPlacementType(),
                        videoCard.getVideoPlacementTypeItems(), "In-Stream")
                .and("Fill Video Playback Method")
                .scrollIntoView(videoCard.getVideoPlaybackMethods())
                .selectFromDropdown(videoCard.getVideoPlaybackMethods(),
                        videoCard.getVideoPlaybackMethodsItems(), "Click Sound On")
                .and("Set Floor Price")
                .setValueWithClean(videoCard.getFloorPriceField().getFloorPriceInput(), VIDEO_FLOOR_PRICE)
                .testEnd();

    }

    @Step("Expand Native Card and fill fields")
    private void fillNativeFormat() {
        var nativeCard = editAdSpotSidebar.getGeneralTab().getNativeCard();
        testStart()
                .and("Expand Native Card")
                .clickOnWebElement(nativeCard.getNativeCardHeader())
                .and("Switch toggle to enabled state ")
                .turnToggleOn(nativeCard.getEnabledToggle())
                .and("Set Floor Price")
                .setValueWithClean(nativeCard.getFloorPriceField().getFloorPriceInput(), NATIVE_FLOOR_PRICE)
                .testEnd();
    }

    @Step("Validate fields values should not be changed")
    private void validateFieldsValuesShouldNotBeChanged(String publisherName, String mediaName) {
        testStart()
                .then(String.format("Publisher should be %s", publisherName))
                .validate(editAdSpotSidebar.getGeneralTab().getPublisherInput().getText(), publisherName)
                .then(String.format("Related Media should be %s", mediaName))
                .validate(editAdSpotSidebar.getGeneralTab().getRelatedMediaInput().getText(), mediaName)
                .testEnd();

    }

    @AfterMethod(alwaysRun = true)
    private void logout() {
        testStart()
                .and("Close Ad Spot Sidebar")
                .clickOnWebElement(editAdSpotSidebar.getCloseIcon())
                .waitSideBarClosed()
                .and("Logout")
                .logOut()
                .testEnd();
    }

    @AfterClass(alwaysRun = true)
    private void deleteTestData() {
        deletePublisher(publisher1.getId());
        deletePublisher(publisher2.getId());
        deleteMedia(media1.getId());
        deleteMedia(media2.getId());

    }

    private void deletePublisher(int id) {
        if (publisher()
                .setCredentials(USER_FOR_DELETION)
                .deletePublisher(id)
                .build()
                .getResponseCode() == HttpStatus.SC_NO_CONTENT)
            log.info(String.format("Deleted publisher %s", publisher1.getId()));

    }

    private void deleteMedia(int id) {
        if (media()
                .setCredentials(USER_FOR_DELETION)
                .deleteMedia(id)
                .build()
                .getResponseCode() == HttpStatus.SC_NO_CONTENT)
            log.info(String.format("Deleted media %s", id));
    }
}
