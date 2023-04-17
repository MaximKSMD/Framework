package rx.inventory.adspot;

import api.dto.rx.common.Currency;
import api.dto.rx.inventory.adspot.*;
import api.dto.rx.inventory.media.Media;
import api.preconditionbuilders.MediaPrecondition;
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
import widgets.common.table.ColumnNames;
import widgets.inventory.adSpots.sidebar.EditAdSpotSidebar;

import java.util.List;

import static api.preconditionbuilders.AdSpotPrecondition.adSpot;
import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static configurations.User.USER_FOR_DELETION;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature("Ad Spots")
public class AdSpotEditTests extends BaseTest {
    private AdSpot adSpot;
    private AdSpotsPage adSpotPage;
    private EditAdSpotSidebar editAdSpotSidebar;

    final private static String POSITION = "Sidebar";
    final private static String VIDEO_FLOOR_PRICE = "44.77";
    final private static String NATIVE_FLOOR_PRICE = "23.65";
    final private static String BANNER_FLOOR_PRICE = "9.40";
    final private static String DEFAULT_FLOOR_PRICE = "5.60";

    final private static String VIDEO_PLACEMENT_TYPE = "In-Banner";
    final private static String VIDEO_PLAYBACK_METHOD = "Autoplay Sound On";
    final private static String VIDEO_PLAYBACK_METHOD_CREATED = "Enter Viewport Sound On";

    final private static String VIDEO_MIN_DURATION = "1000";
    final private static String VIDEO_MAX_DURATION = "9000";

    final private static AdSizesList VIDEO_AD_SIZE = AdSizesList.A216x36;
    final private static AdSizesList BANNER_AD_SIZE = AdSizesList.A120x20;
    final private static AdSizesList DEFAULT_AD_SIZE = AdSizesList.A300x1050;
    final private static AdSizesList AD_SIZE_CREATED = AdSizesList.A16x600;

    final private static CategoriesList CATEGORY_EDUCATION = CategoriesList.EDUCATION;
    final private static CategoriesList CATEGORY_AUTO_REPAIR = CategoriesList.AUTO_REPAIR;

    private static final String MEDIA_NAME = captionWithSuffix("4autoMediaEdit");

    final private static String AD_SPOT_NAME = captionWithSuffix("4autoAdSpotEdit");

    public AdSpotEditTests() {
        adSpotPage = new AdSpotsPage();
        editAdSpotSidebar = new EditAdSpotSidebar();
    }


    @BeforeClass
    private void loginAndPrepareTestData() {
        var tableData = adSpotPage.getAdSpotsTable().getTableData();

        var adSpotRequest = createRequest();

        adSpot = adSpot()
                .createNewAdSpot(adSpotRequest)
                .build()
                .getAdSpotResponse();

        testStart()
                .given()
                .openDirectPath(Path.AD_SPOT)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, adSpotPage.getNuxtProgress())
                .setValueWithClean(tableData.getSearch(), adSpot.getName())
                .pressEnterKey(tableData.getSearch())
                .waitAndValidate(disappear, adSpotPage.getTableProgressBar())
                .testEnd();
    }

    @BeforeMethod
    private void openSidebar(){
        var tableData = adSpotPage.getAdSpotsTable().getTableData();
        var tablePagination = adSpotPage.getAdSpotsTable().getTablePagination();


        testStart()
                .pressEnterKey(tableData.getSearch())
                .then("Validate that text in table footer '1-1 of 1")
                .validateContainsText(tablePagination.getPaginationPanel(), "1-1 of 1")
                .and("Open Sidebar and check data")
                .clickOnTableCellLink(tableData, ColumnNames.AD_SPOT_NAME, adSpot.getName())
                .waitSideBarOpened()
                .testEnd();
    }

    @Test(description = "Save existing Ad Spot without changes", priority = 1)
    public void saveAdSpotWithoutChanges() {

     }

    @Test(description = "Change all enabled fields and save", priority = 3)
    private void changeAllFieldsAndSave() {
        var tableData = adSpotPage.getAdSpotsTable().getTableData();
        var tablePagination = adSpotPage.getAdSpotsTable().getTablePagination();

        changeGeneralFields();
        changeBannerCardFields();
        changeNativeCardFields();
        changeVideoCardFields();

        testStart()
                .and("Click Save")
                .clickOnWebElement(editAdSpotSidebar.getSaveButton())
                .waitAndValidate(not(visible), editAdSpotSidebar.getGeneralTab().getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), adSpotPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .and("Open Sidebar and check data")
                .waitAndValidate(disappear, adSpotPage.getTableProgressBar())
                .setValueWithClean(tableData.getSearch(), AD_SPOT_NAME)
                .pressEnterKey(tableData.getSearch())
                .then("Validate that text in table footer '1-1 of 1")
                .validateContainsText(tablePagination.getPaginationPanel(), "1-1 of 1")
                .and("Open Sidebar and check data")
                .clickOnTableCellLink(tableData, ColumnNames.AD_SPOT_NAME, AD_SPOT_NAME)
                .waitSideBarOpened()
                .testEnd();

        validateGeneralFieldsValues();
        validateBannerFieldsValues();
        validateNativeFieldsValues();
        validateVideoFieldsValues();
    }

    @Test(description = "Check fields enabled/disabled state", priority = 2)
    private void checkEnabledDisabledFields() {

        testStart()
                .then("Publisher and Ad Spot NAme should be disabled")
                .validate(disabled, editAdSpotSidebar.getGeneralTab().getPublisherNameInput())
                .validate(disabled, editAdSpotSidebar.getGeneralTab().getRelatedMediaInput())
                .testEnd();
    }

    @Step("Change general field")
    private void changeGeneralFields() {
        var categories = editAdSpotSidebar.getGeneralTab().getCategoriesPanel();
        testStart()
                .and("Fill Ad Spot Name")
                .setValueWithClean(editAdSpotSidebar.getGeneralTab().getNameInput(), AD_SPOT_NAME)
                .selectFromDropdown(editAdSpotSidebar.getGeneralTab().getPosition(),
                        editAdSpotSidebar.getGeneralTab().getPositionItems(), POSITION)
                .clickOnWebElement(editAdSpotSidebar.getGeneralTab().getDefaultAdSizes())
                .clickOnWebElement(editAdSpotSidebar.getGeneralTab().getAdSizesPanel().getAdSizeCheckbox(AD_SIZE_CREATED))
                .clickOnWebElement(editAdSpotSidebar.getGeneralTab().getAdSizesPanel().getAdSizeCheckbox(DEFAULT_AD_SIZE))
                .clickOnWebElement(editAdSpotSidebar.getGeneralTab().getNameInput())
                .setValueWithClean(editAdSpotSidebar.getGeneralTab().getDefaultFloorPrice(), DEFAULT_FLOOR_PRICE)
                .clickOnWebElement(editAdSpotSidebar.getGeneralTab().getCategories())
                .clickOnWebElement(categories.getCategoryCheckbox(CategoriesList.ART_ENTERTAIMENTS))
                .clickOnWebElement(categories.getCategoryGroupIcon(CategoriesList.AUTOMOTIVE))
                .clickOnWebElement(categories.getCategoryCheckbox(CATEGORY_AUTO_REPAIR))
                .clickOnWebElement(categories.getCategoryCheckbox(CATEGORY_EDUCATION))
                .clickOnWebElement(editAdSpotSidebar.getGeneralTab().getNameInput())
                .turnToggleOff(editAdSpotSidebar.getGeneralTab().getContentForChildrenToggle())
                .turnToggleOff(editAdSpotSidebar.getGeneralTab().getTestModeToggle())
                .turnToggleOff(editAdSpotSidebar.getGeneralTab().getActiveToggle())
                .testEnd();
    }

    @Step("Change Banner card fields")
    private void changeBannerCardFields() {
        var bannerCard = editAdSpotSidebar.getGeneralTab().getBannerCard();

        if (bannerCard.getBannerPanel().getAttribute( "aria-expanded").equals("false")){
            testStart()
                    .clickOnWebElement(bannerCard.getBannerCardHeader())
                    .testEnd();
        }

        testStart()
                .clickOnWebElement(bannerCard.getAdSizes())
                .clickOnWebElement(bannerCard.getAdSizesPanel().getAdSizeCheckbox(AD_SIZE_CREATED))
                .clickOnWebElement(bannerCard.getAdSizesPanel().getAdSizeCheckbox(BANNER_AD_SIZE))
                .clickOnWebElement(editAdSpotSidebar.getGeneralTab().getNameInput())
                .setValueWithClean(bannerCard.getFloorPriceField().getFloorPriceInput(), BANNER_FLOOR_PRICE)
                .turnToggleOff(bannerCard.getEnabledToggle())
                .testEnd();
    }

    @Step("Change Native card fields")
    private void changeNativeCardFields() {
        var nativeCard = editAdSpotSidebar.getGeneralTab().getNativeCard();

        if (nativeCard.getNativePanel().getAttribute( "aria-expanded").equals("false")){
            testStart()
                    .clickOnWebElement(nativeCard.getNativeCardHeader())
                    .testEnd();
        }

        testStart()
                .turnToggleOn(nativeCard.getEnabledToggle())
                .setValueWithClean(nativeCard.getFloorPriceField().getFloorPriceInput(), NATIVE_FLOOR_PRICE)
                .testEnd();
    }

    @Step("Change Video card fields")
    private void changeVideoCardFields() {
        var videoCard = editAdSpotSidebar.getGeneralTab().getVideoCard();

        if (videoCard.getVideoPanel().getAttribute( "aria-expanded").equals("false")){
            testStart()
                    .clickOnWebElement(videoCard.getVideoCardHeader())
                    .testEnd();
        }

        testStart()
                .setValueWithClean(videoCard.getMinVideoDuration(), VIDEO_MIN_DURATION)
                .pressEscapeKey(videoCard.getMinVideoDuration())
                .setValueWithClean(videoCard.getMaxVideoDuration(), VIDEO_MAX_DURATION)
                .pressEscapeKey(videoCard.getMaxVideoDuration())
                .clickOnWebElement(videoCard.getVideoAdSizes())
                .clickOnWebElement(videoCard.getAdSizesPanel().getAdSizeCheckbox(AD_SIZE_CREATED))
                .clickOnWebElement(videoCard.getAdSizesPanel().getAdSizeCheckbox(VIDEO_AD_SIZE))
                .and("Fill Video Placement Type")
                .selectFromDropdown(videoCard.getVideoPlacementType(),
                        videoCard.getVideoPlacementTypeItems(), VIDEO_PLACEMENT_TYPE)
                .and("Fill Video Playback Method")
                .scrollIntoView(videoCard.getVideoPlaybackMethods())
                .selectFromDropdown(videoCard.getVideoPlaybackMethods(),
                        videoCard.getVideoPlaybackMethodsItems(), VIDEO_PLAYBACK_METHOD_CREATED)
                .clickOnWebElement(videoCard.getVideoPanel())
                .setValueWithClean(videoCard.getFloorPriceField().getFloorPriceInput(), VIDEO_FLOOR_PRICE)

                .turnToggleOff(videoCard.getEnabledToggle())
                .testEnd();
    }

    @Step("Validate General fields values")
    private void validateGeneralFieldsValues() {
        var categories = editAdSpotSidebar.getGeneralTab().getCategoriesPanel();
        testStart()
                .then("Validate fields state in General tab")
                .validateAttribute(editAdSpotSidebar.getGeneralTab().getActiveToggle(), "aria-checked", "false")
                .validateAttribute(editAdSpotSidebar.getGeneralTab().getContentForChildrenToggle(), "aria-checked", "false")
                .validateAttribute(editAdSpotSidebar.getGeneralTab().getTestModeToggle(), "aria-checked", "false")
                .validate(editAdSpotSidebar.getGeneralTab().getPublisherNameInput().getText(),adSpot.getPublisherName())
                .validate(disabled, editAdSpotSidebar.getGeneralTab().getPublisherNameInput())
                .validate(editAdSpotSidebar.getGeneralTab().getNameInput().getText(), AD_SPOT_NAME)
                .validate(editAdSpotSidebar.getGeneralTab().getRelatedMedia().getText(), adSpot.getMediaName())
                .validate(editAdSpotSidebar.getGeneralTab().getDefaultAdSizes().getText(), DEFAULT_AD_SIZE.getSize())
                .validateAttribute(editAdSpotSidebar.getGeneralTab().getDefaultFloorPrice(), "value", DEFAULT_FLOOR_PRICE)
                .validateList(categories.getCategoriesSelectedItems(), List.of(CategoriesList.AUTOMOTIVE.getName(),  CATEGORY_AUTO_REPAIR.getName(),
                        CategoriesList.EDUCATION.getName()))
                .testEnd();
    }

    @Step("Validate Banner Card fields values")
    private void validateBannerFieldsValues() {
        var bannerCard = editAdSpotSidebar.getGeneralTab().getBannerCard();

        if (bannerCard.getBannerPanel().getAttribute( "aria-expanded").equals("false")){
            testStart()
                    .clickOnWebElement(bannerCard.getBannerCardHeader())
                    .testEnd();
        }

        testStart()
                .validateAttribute(bannerCard.getEnabledToggle(), "aria-checked", "false")
                .validate(bannerCard.getAdSizes().getText(), BANNER_AD_SIZE.getSize())
                .validateAttribute(bannerCard.getFloorPriceField().getFloorPriceInput(), "value", BANNER_FLOOR_PRICE)
                .testEnd();
    }

    @Step("Validate Native Card fields values")
    private void validateNativeFieldsValues() {
        var nativeCard = editAdSpotSidebar.getGeneralTab().getNativeCard();

        if (nativeCard.getNativePanel().getAttribute( "aria-expanded").equals("false")){
            testStart()
                    .clickOnWebElement(nativeCard.getNativeCardHeader())
                    .testEnd();
        }

        testStart()
                .validateAttribute(nativeCard.getEnabledToggle(), "aria-checked", "true")
                .validateAttribute(nativeCard.getFloorPriceField().getFloorPriceInput(), "value", NATIVE_FLOOR_PRICE)
                .testEnd();
    }

    @Step("Validate Video Card fields values")
    private void validateVideoFieldsValues() {
        var videoCard = editAdSpotSidebar.getGeneralTab().getVideoCard();

        if (videoCard.getVideoPanel().getAttribute( "aria-expanded").equals("false")){
            testStart()
                    .clickOnWebElement(videoCard.getVideoCardHeader())
                    .testEnd();
        }

        testStart()
                .validateAttribute(videoCard.getEnabledToggle(), "aria-checked", "false")
                .validate(videoCard.getVideoAdSizes().getText(), VIDEO_AD_SIZE.getSize())
                .validateAttribute(videoCard.getFloorPriceField().getFloorPriceInput(), "value", VIDEO_FLOOR_PRICE)

                .validate(videoCard.getVideoPlacementType().getText(), VIDEO_PLACEMENT_TYPE)
                .validateList(videoCard.getVideoPlaybackMethodsSelectedItems(), List.of(VIDEO_PLAYBACK_METHOD))
                .validateAttribute(videoCard.getFloorPriceField().getFloorPriceInput(), "value", VIDEO_FLOOR_PRICE)
                .validateAttribute(videoCard.getMinVideoDuration(), "value", VIDEO_MIN_DURATION)
                .validateAttribute(videoCard.getMaxVideoDuration(), "value", VIDEO_MAX_DURATION)
                .testEnd();
    }

    private AdSpotRequest createRequest() {

        var media = createMedia();

        return AdSpotRequest.builder()
                .name(AD_SPOT_NAME+"new")
                .enabled(true)
                .publisherId(media.getPublisherId())
                .currency(Currency.JPY.name())
                .floorPrice(11.00)
                .mediaId(media.getId())
                .positionId(1)
                .coppa(true)
                .sizeIds(List.of(10))
                .floorPriceAutomated(true)
                .testMode(true)
                .categoryIds(List.of(1, 9))
                .video(createVideo())
                .banner(createBanner())
                .anative(createNative())
                .build();
    }

    private Video createVideo() {

        return Video.builder()
                .floorPrice(23.00)
                .maxDuration(10)
                .enabled(true)
                .sizeIds(List.of(10))
                .playbackMethodIds(List.of(5,1))
                .placementType(1)
                .build();
    }

    private Banner createBanner() {

        return Banner.builder()
                .floorPrice(23.00)
                .enabled(true)
                .sizeIds(List.of(10))
                .build();
    }

    private Native createNative() {

        return Native.builder()
                .floorPrice(23.00)
                .enabled(true)
                .build();
    }

    private Media createMedia() {

        return MediaPrecondition.media()
                .createNewMedia(MEDIA_NAME)
                .build()
                .getMediaResponse();
    }

    @AfterMethod(alwaysRun = true)
    private void clickSaveAndWaitSidebarClosed() {

        testStart()
                .and("Click Save")
                .clickOnWebElement(editAdSpotSidebar.getSaveButton())
                .waitAndValidate(not(visible), editAdSpotSidebar.getGeneralTab().getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), adSpotPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .testEnd();
    }

    @AfterClass(alwaysRun = true)
    private void deletePublisher() {

        testStart()
                .and("Logout")
                .logOut()
                .testEnd();

        if (adSpot()
                .setCredentials(USER_FOR_DELETION)
                .deleteAdSpot(adSpot.getId())
                .build()
                .getResponseCode() == HttpStatus.SC_NO_CONTENT)
            log.info(String.format("Deleted media %s", adSpot.getId()));

        if (publisher()
                .setCredentials(USER_FOR_DELETION)
                .deletePublisher(adSpot.getPublisherId())
                .build()
                .getResponseCode() == HttpStatus.SC_NO_CONTENT)
            log.info(String.format("Deleted publisher %s", adSpot.getPublisherId()));
    }

}
