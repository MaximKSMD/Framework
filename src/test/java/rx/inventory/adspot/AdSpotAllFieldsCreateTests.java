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
import widgets.common.detailsmenu.menu.TableItemDetailsMenu;
import widgets.common.detailsmenu.menu.sections.DetailsSection;
import widgets.common.table.ColumnNames;
import widgets.common.table.Statuses;
import widgets.common.table.TableData;
import widgets.inventory.adSpots.sidebar.EditAdSpotSidebar;
import zutils.StringUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static api.preconditionbuilders.MediaPrecondition.media;
import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static configurations.User.USER_FOR_DELETION;
import static java.lang.String.format;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature("Ad Spots")
public class AdSpotAllFieldsCreateTests extends BaseTest {

    private Media media;
    private Publisher publisher;
    private AdSpotsPage adSpotPage;
    private EditAdSpotSidebar editAdSpotSidebar;
    private TableItemDetailsMenu adSpotTableDetailsMenu;

    final private static String POSITION = "Header";
    final private static String VIDEO_FLOOR_PRICE = "15.32";
    final private static String NATIVE_FLOOR_PRICE = "18.45";
    final private static String BANNER_FLOOR_PRICE = "12.11";
    final private static String DEFAULT_FLOOR_PRICE = "10.98";

    final private static String VIDEO_PLACEMENT_TYPE = "In-Stream";
    final private static String VIDEO_PLAYBACK_METHOD = "Click Sound On";

    final private static String VIDEO_MIN_DURATION = "2000";
    final private static String VIDEO_MAX_DURATION = "7000";

    final private static Currency CURRENCY = Currency.JPY;

    final private static AdSizesList VIDEO_AD_SIZE = AdSizesList.A216x36;
    final private static AdSizesList BANNER_AD_SIZE = AdSizesList.A120x20;
    final private static AdSizesList DEFAULT_AD_SIZE = AdSizesList.A300x1050;

    final private static CategoriesList CATEGORY_EDUCATION = CategoriesList.EDUCATION;
    final private static CategoriesList CATEGORY_AUTO_REPAIR = CategoriesList.AUTO_REPAIR;

    final private static String AD_SPOT_NAME = captionWithSuffix("4autoAdSpot");

    public AdSpotAllFieldsCreateTests() {
        adSpotPage = new AdSpotsPage();
        editAdSpotSidebar = new EditAdSpotSidebar();
        adSpotTableDetailsMenu = new TableItemDetailsMenu();
    }

    @BeforeClass
    private void init() {

        publisher = publisher()
                .createNewPublisher(captionWithSuffix("000000autoPub1"), true, CURRENCY, List.of(), List.of())
                .build()
                .getPublisherResponse();

        media = media()
                .createNewMedia(captionWithSuffix("autoMedia"), publisher.getId(), true)
                .build()
                .getMediaResponse();

        testStart()
                .given()
                .openDirectPath(Path.AD_SPOT)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, adSpotPage.getNuxtProgress())
                .clickOnWebElement(adSpotPage.getCreateAdSpotButton())
                .waitSideBarOpened()
                .testEnd();
    }

    @Test(description = "Create Ad Spot with all filled fields")
    public void createAdSpotWithAllFields() {
        var tableData = adSpotPage.getAdSpotsTable().getTableData();
        var tablePagination = adSpotPage.getAdSpotsTable().getTablePagination();

        fillGeneralFields();
        fillBannerCardFields();
        fillNativeCardFields();
        fillVideoCardFields();
        testStart()
                .clickOnWebElement(editAdSpotSidebar.getSaveButton())
                .waitSideBarClosed()
                .and("Toaster Error message is absent")
                .waitAndValidate(not(visible), adSpotPage.getToasterMessage().getPanelError())
                .and("Search new media")
                .setValueWithClean(tableData.getSearch(), AD_SPOT_NAME)
                .pressEnterKey(tableData.getSearch())
                .then("Validate that text in table footer '1-1 of 1")
                .validateContainsText(tablePagination.getPaginationPanel(), "1-1 of 1")
                .clickOnTableCellLink(tableData, ColumnNames.AD_SPOT_NAME, AD_SPOT_NAME)
                .waitSideBarOpened()
                .testEnd();

        validateGeneralFieldsValues();
        validateBannerFieldsValues();
        validateNativeFieldsValues();
        validateVideoFieldsValues();

        testStart()
                .clickOnWebElement(editAdSpotSidebar.getSaveButton())
                .waitSideBarClosed()
                .and("Toaster Error message is absent")
                .waitAndValidate(not(visible), adSpotPage.getToasterMessage().getPanelError())
                .testEnd();
    }

    @Test(description = "Check columns data in the Ad Spots table for created Ad Spot",
            dependsOnMethods = "createAdSpotWithAllFields")
    public void checkTableColumns() {
        var tableData = adSpotPage.getAdSpotsTable().getTableData();
        var tableOptions = adSpotPage.getAdSpotsTable().getShowHideColumns();
        ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("UTC"));

        testStart()
                .and("'Show' all columns")
                .scrollIntoView(tableOptions.getShowHideColumnsBtn())
                .clickOnWebElement(tableOptions.getShowHideColumnsBtn())
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.ID))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.DETAILS))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.AD_SPOT_NAME))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.PUBLISHER))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.RELATED_MEDIA))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.PAGE_CATEGORY))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.STATUS))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.TEST_MODE))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.DEFAULT_SIZES))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.DEFAULT_FLOOR_PRICE))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.UPDATED_DATE))
                .clickOnWebElement(adSpotPage.getPageTitle())
                .then("Validate data in table")
                .validate(tableData.getCellByRowValue(ColumnNames.STATUS, ColumnNames.AD_SPOT_NAME, AD_SPOT_NAME), Statuses.ACTIVE.getStatus())
                .validate(tableData.getCellByRowValue(ColumnNames.PUBLISHER, ColumnNames.AD_SPOT_NAME, AD_SPOT_NAME), publisher.getName())
                .validate(tableData.getCellByRowValue(ColumnNames.RELATED_MEDIA, ColumnNames.AD_SPOT_NAME, AD_SPOT_NAME), media.getName())
                .validate(tableData.getCellByRowValue(ColumnNames.DEFAULT_FLOOR_PRICE, ColumnNames.AD_SPOT_NAME, AD_SPOT_NAME),
                        String.format("%s %s", DEFAULT_FLOOR_PRICE, publisher.getCurrency()))
                .validate(tableData.getCellByRowValue(ColumnNames.DEFAULT_SIZES, ColumnNames.AD_SPOT_NAME, AD_SPOT_NAME), DEFAULT_AD_SIZE.getSize())
                .validate(tableData.getCellByRowValue(ColumnNames.TEST_MODE, ColumnNames.AD_SPOT_NAME, AD_SPOT_NAME), "Enabled")
                .validate(tableData.getCellByRowValue(ColumnNames.PAGE_CATEGORY, ColumnNames.AD_SPOT_NAME, AD_SPOT_NAME),
                        String.format("%s, %s", CATEGORY_AUTO_REPAIR.getName(), CATEGORY_EDUCATION.getName()))
                .validate(tableData.getCellByRowValue(ColumnNames.CREATED_DATE, ColumnNames.AD_SPOT_NAME, AD_SPOT_NAME), StringUtils.getDateAsString(currentDate))
                .validate(tableData.getCellByRowValue(ColumnNames.UPDATED_DATE, ColumnNames.AD_SPOT_NAME, AD_SPOT_NAME), StringUtils.getDateAsString(currentDate))
                .testEnd();
    }

    @Test(description = "Check details info: Native Floor Price", dependsOnMethods = "createAdSpotWithAllFields")
    public void checkInfoNativeFloorPricePanel() {
        var tableData = adSpotPage.getAdSpotsTable().getTableData();
        var nativeDetailsSection = adSpotTableDetailsMenu.getNativeDetailsSection();

        hoverMouseCursorOnDetailsIcon(tableData);

        verifySelectionInDetailsMenuForTableItem(nativeDetailsSection,
                String.format("%s: %s%s", publisher.getCurrency(), CURRENCY.getAbbr(), NATIVE_FLOOR_PRICE));

        removeMouseCursorFromDetailsIcon();
    }

    @Test(description = "Check details info: Banner Ad Size", dependsOnMethods = "createAdSpotWithAllFields")
    public void checkInfoBannerAdSize() {
        var tableData = adSpotPage.getAdSpotsTable().getTableData();
        var bannerDetailsSection = adSpotTableDetailsMenu.getBannerAdSizeDetailsSection();

        hoverMouseCursorOnDetailsIcon(tableData);

        verifySelectionInDetailsMenuForTableItem(bannerDetailsSection, BANNER_AD_SIZE.getSize());

        removeMouseCursorFromDetailsIcon();
    }

    @Test(description = "Check details info: Banner Floor Price", dependsOnMethods = "createAdSpotWithAllFields")
    public void checkInfoBannerFloorPrice() {
        var tableData = adSpotPage.getAdSpotsTable().getTableData();
        var bannerDetailsSection = adSpotTableDetailsMenu.getBannerFloorPriceDetailsSection();

        hoverMouseCursorOnDetailsIcon(tableData);

        verifySelectionInDetailsMenuForTableItem(bannerDetailsSection,
                String.format("%s: %s%s", publisher.getCurrency(), CURRENCY.getAbbr(), BANNER_FLOOR_PRICE));

        removeMouseCursorFromDetailsIcon();
    }

    @Test(description = "Check details info: Video Floor Price", dependsOnMethods = "createAdSpotWithAllFields")
    public void checkInfoVideoFloorPrice() {
        var tableData = adSpotPage.getAdSpotsTable().getTableData();
        var videoFloorPriceDetailsSection = adSpotTableDetailsMenu.getVideoFloorPriceDetailsSection();

        hoverMouseCursorOnDetailsIcon(tableData);

        verifySelectionInDetailsMenuForTableItem(videoFloorPriceDetailsSection,
                String.format("%s: %s%s", publisher.getCurrency(), CURRENCY.getAbbr(), VIDEO_FLOOR_PRICE));

        removeMouseCursorFromDetailsIcon();
    }

    @Test(description = "Check details info: Video Min Duration", dependsOnMethods = "createAdSpotWithAllFields")
    public void checkInfoMinDurationPrice() {
        var tableData = adSpotPage.getAdSpotsTable().getTableData();
        var videoMinDurationDetailsSection = adSpotTableDetailsMenu.getVideoMinDurationDetailsSection();

        hoverMouseCursorOnDetailsIcon(tableData);

        verifySelectionInDetailsMenuForTableItem(videoMinDurationDetailsSection, String.format("%s seconds", VIDEO_MIN_DURATION));

        removeMouseCursorFromDetailsIcon();
    }

    @Test(description = "Check details info: Video Max Duration", dependsOnMethods = "createAdSpotWithAllFields")
    public void checkInfoMaxDurationPrice() {
        var tableData = adSpotPage.getAdSpotsTable().getTableData();
        var videoMaxDurationDetailsSection = adSpotTableDetailsMenu.getVideoMaxDurationDetailsSection();

        hoverMouseCursorOnDetailsIcon(tableData);

        verifySelectionInDetailsMenuForTableItem(videoMaxDurationDetailsSection, String.format("%s seconds", VIDEO_MAX_DURATION));

        removeMouseCursorFromDetailsIcon();
    }

    @Test(description = "Check details info: Video Playback Method", dependsOnMethods = "createAdSpotWithAllFields")
    public void checkInfoPlaybackMethodPrice() {
        var tableData = adSpotPage.getAdSpotsTable().getTableData();
        var videoPlaybackMethodDetailsSection = adSpotTableDetailsMenu.getVideoPlaybackMethodDetailsSection();

        hoverMouseCursorOnDetailsIcon(tableData);

        verifySelectionInDetailsMenuForTableItem(videoPlaybackMethodDetailsSection, VIDEO_PLAYBACK_METHOD);

        removeMouseCursorFromDetailsIcon();
    }

    @Test(description = "Check details info: Video Placement Type Method", dependsOnMethods = "createAdSpotWithAllFields")
    public void checkInfoPlacementTypePrice() {
        var tableData = adSpotPage.getAdSpotsTable().getTableData();
        var videoPlacementTypeDetailsSection = adSpotTableDetailsMenu.getVideoPlacementTypeDetailsSection();

        hoverMouseCursorOnDetailsIcon(tableData);

        verifySelectionInDetailsMenuForTableItem(videoPlacementTypeDetailsSection, VIDEO_PLACEMENT_TYPE);

        removeMouseCursorFromDetailsIcon();
    }

    private void hoverMouseCursorOnDetailsIcon(TableData tableData) {
        testStart()
                .and("Hovering mouse cursor on 'Details' column in Pricing Table")
                .hoverMouseOnWebElement(tableData.getCellByPositionInTable(ColumnNames.DETAILS, 0))
                .testEnd();
    }


    private void verifySelectionInDetailsMenuForTableItem(DetailsSection detailsSection, String... expectedItemNames) {

        AtomicInteger currentItemPosition = new AtomicInteger();

        Stream.of(expectedItemNames).forEach(item -> {
            testStart()
                    .then(format("Check that %s with name '%s' is presented in 'Details' Menu on %s position",
                            detailsSection.getDetailsSection().getName(), item, currentItemPosition))
                    .validate(visible, detailsSection.getMenuItemByPositionInList(currentItemPosition.get()).getName())
                    .validate(detailsSection.getMenuItemByPositionInList(currentItemPosition.get()).getName(),
                            expectedItemNames[currentItemPosition.get()])
                    .validate(visible, detailsSection.getMenuItemByPositionInList(currentItemPosition.get()).getIncludedIcon())
                    .validate(not(visible), detailsSection.getMenuItemByPositionInList(currentItemPosition.get()).getExcludedIcon())
                    .testEnd();
            currentItemPosition.getAndIncrement();
        });
    }

    @Step("Remove mouse cursor from details icon")
    private void removeMouseCursorFromDetailsIcon() {

        testStart()
                .clickOnWebElement(adSpotPage.getPageTitle())
                .testEnd();
    }

    @Step("Fill general field")
    private void fillGeneralFields() {
        var categories = editAdSpotSidebar.getGeneralTab().getCategoriesPanel();

        testStart()
                .and(String.format("Select Publisher '%s'", media.getPublisherName()))
                .selectFromDropdown(editAdSpotSidebar.getGeneralTab().getPublisherInput(),
                        editAdSpotSidebar.getGeneralTab().getPublisherItems(), media.getPublisherName())
                .and("Fill Ad Spot Name")
                .setValueWithClean(editAdSpotSidebar.getGeneralTab().getNameInput(), AD_SPOT_NAME)
                .selectFromDropdown(editAdSpotSidebar.getGeneralTab().getRelatedMedia(),
                        editAdSpotSidebar.getGeneralTab().getRelatedMediaItems(), media.getName())
                .selectFromDropdown(editAdSpotSidebar.getGeneralTab().getPosition(),
                        editAdSpotSidebar.getGeneralTab().getPositionItems(), POSITION)
                .clickOnWebElement(editAdSpotSidebar.getGeneralTab().getDefaultAdSizes())
                .clickOnWebElement(editAdSpotSidebar.getGeneralTab().getAdSizesPanel().getAdSizeCheckbox(DEFAULT_AD_SIZE))
                .clickOnWebElement(editAdSpotSidebar.getGeneralTab().getNameInput())
                .setValueWithClean(editAdSpotSidebar.getGeneralTab().getDefaultFloorPrice(), DEFAULT_FLOOR_PRICE)
                .clickOnWebElement(editAdSpotSidebar.getGeneralTab().getCategories())
                .clickOnWebElement(categories.getCategoryCheckbox(CATEGORY_EDUCATION))
                .clickOnWebElement(categories.getCategoryGroupIcon(CategoriesList.AUTOMOTIVE))
                .clickOnWebElement(categories.getCategoryCheckbox(CATEGORY_AUTO_REPAIR))
                .clickOnWebElement(editAdSpotSidebar.getGeneralTab().getNameInput())
                .turnToggleOn(editAdSpotSidebar.getGeneralTab().getContentForChildrenToggle())
                .turnToggleOn(editAdSpotSidebar.getGeneralTab().getTestModeToggle())
                .testEnd();
    }

    @Step("Fill Banner card fields")
    private void fillBannerCardFields() {
        var bannerCard = editAdSpotSidebar.getGeneralTab().getBannerCard();

        if (bannerCard.getBannerPanel().getAttribute( "aria-expanded").equals("false")){
            testStart()
                    .clickOnWebElement(bannerCard.getBannerCardHeader())
                    .testEnd();
        }

        testStart()
                .turnToggleOn(bannerCard.getEnabledToggle())
                .clickOnWebElement(bannerCard.getAdSizes())
                .clickOnWebElement(bannerCard.getAdSizesPanel().getAdSizeCheckbox(BANNER_AD_SIZE))
                .setValueWithClean(bannerCard.getFloorPriceField().getFloorPriceInput(), BANNER_FLOOR_PRICE)
                .testEnd();
    }

    @Step("Fill Native card fields")
    private void fillNativeCardFields() {
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

    @Step("Fill Video card fields")
    private void fillVideoCardFields() {
        var videoCard = editAdSpotSidebar.getGeneralTab().getVideoCard();

        if (videoCard.getVideoPanel().getAttribute( "aria-expanded").equals("false")){
            testStart()
                    .scrollIntoView(videoCard.getVideoCardHeader())
                    .clickOnWebElement(videoCard.getVideoCardHeader())
                    .testEnd();
        }

        testStart()
                .turnToggleOn(videoCard.getEnabledToggle())
                .clickOnWebElement(videoCard.getVideoAdSizes())
                .clickOnWebElement(videoCard.getAdSizesPanel().getAdSizeCheckbox(VIDEO_AD_SIZE))
                .and("Fill Video Placement Type")
                .selectFromDropdown(videoCard.getVideoPlacementType(),
                        videoCard.getVideoPlacementTypeItems(), VIDEO_PLACEMENT_TYPE)
                .and("Fill Video Playback Method")
                .scrollIntoView(videoCard.getVideoPlaybackMethods())
                .selectFromDropdown(videoCard.getVideoPlaybackMethods(),
                        videoCard.getVideoPlaybackMethodsItems(), VIDEO_PLAYBACK_METHOD)
                .clickOnWebElement(videoCard.getVideoPanel())
                .setValueWithClean(videoCard.getFloorPriceField().getFloorPriceInput(), VIDEO_FLOOR_PRICE)
                .setValueWithClean(videoCard.getMinVideoDuration(), VIDEO_MIN_DURATION)
                .setValueWithClean(videoCard.getMaxVideoDuration(), VIDEO_MAX_DURATION)
                .testEnd();
    }

    @Step("Validate General fields values")
    private void validateGeneralFieldsValues() {
        var categories = editAdSpotSidebar.getGeneralTab().getCategoriesPanel();
        testStart()
                .then("")
                .validateAttribute(editAdSpotSidebar.getGeneralTab().getActiveToggle(), "aria-checked", "true")
                .validateAttribute(editAdSpotSidebar.getGeneralTab().getContentForChildrenToggle(), "aria-checked", "true")
                .validateAttribute(editAdSpotSidebar.getGeneralTab().getTestModeToggle(), "aria-checked", "true")
                .validate(editAdSpotSidebar.getGeneralTab().getPublisherInput().getText(), media.getPublisherName())
                .validate(editAdSpotSidebar.getGeneralTab().getNameInput().getText(), AD_SPOT_NAME)
                .validate(editAdSpotSidebar.getGeneralTab().getRelatedMedia().getText(), media.getName())
                .validate(editAdSpotSidebar.getGeneralTab().getDefaultAdSizes().getText(), DEFAULT_AD_SIZE.getSize())
                .validateAttribute(editAdSpotSidebar.getGeneralTab().getDefaultFloorPrice(), "value", DEFAULT_FLOOR_PRICE)

                .validate(editAdSpotSidebar.getGeneralTab().getDefaultFloorPriceCurrency().getText(), publisher.getCurrency())
                .validateList(categories.getCategoriesSelectedItems(), List.of(CATEGORY_AUTO_REPAIR.getName(),
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
                .validateAttribute(bannerCard.getEnabledToggle(), "aria-checked", "true")
                .validate(bannerCard.getAdSizes().getText(), BANNER_AD_SIZE.getSize())
                .validateAttribute(bannerCard.getFloorPriceField().getFloorPriceInput(), "value", BANNER_FLOOR_PRICE)
                .validate(bannerCard.getFloorPriceField().getFloorPricePrefix().getText(), publisher.getCurrency())
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
                .validate(nativeCard.getFloorPriceField().getFloorPricePrefix().getText(), publisher.getCurrency())
                .testEnd();
    }

    @Step("Validate Video Card fields values")
    private void validateVideoFieldsValues() {
        var videoCard = editAdSpotSidebar.getGeneralTab().getVideoCard();

        if (videoCard.getVideoPanel().getAttribute( "aria-expanded").equals("false")){
            testStart()
                    .scrollIntoView(videoCard.getVideoCardHeader())
                    .clickOnWebElement(videoCard.getVideoCardHeader())
                    .testEnd();
        }

        testStart()
                .validateAttribute(videoCard.getEnabledToggle(), "aria-checked", "true")
                .validate(videoCard.getVideoAdSizes().getText(), VIDEO_AD_SIZE.getSize())
                .validateAttribute(videoCard.getFloorPriceField().getFloorPriceInput(), "value", VIDEO_FLOOR_PRICE)

                .validate(videoCard.getVideoPlacementType().getText(), VIDEO_PLACEMENT_TYPE)
                .validateList(videoCard.getVideoPlaybackMethodsSelectedItems(), List.of(VIDEO_PLAYBACK_METHOD))
                .validateAttribute(videoCard.getFloorPriceField().getFloorPriceInput(), "value", VIDEO_FLOOR_PRICE)
                .validate(videoCard.getFloorPriceField().getFloorPricePrefix().getText(), publisher.getCurrency())
                .validateAttribute(videoCard.getMinVideoDuration(), "value", VIDEO_MIN_DURATION)
                .validateAttribute(videoCard.getMaxVideoDuration(), "value", VIDEO_MAX_DURATION)
                .testEnd();
    }

    private void logout() {
        testStart()
                .and("Logout")
                .logOut()
                .testEnd();
    }

    @AfterClass(alwaysRun = true)
    private void deletePublisher() {

        logout();
        if (media()
                .setCredentials(USER_FOR_DELETION)
                .deleteMedia(media.getId())
                .build()
                .getResponseCode() == HttpStatus.SC_NO_CONTENT)
            log.info(String.format("Deleted media %s", media.getId()));

        if (publisher()
                .setCredentials(USER_FOR_DELETION)
                .deletePublisher(media.getPublisherId())
                .build()
                .getResponseCode() == HttpStatus.SC_NO_CONTENT)
            log.info(String.format("Deleted publisher %s", media.getPublisherId()));
    }
}
