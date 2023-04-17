package rx.yield.openpricing;

import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.common.Currency;
import api.dto.rx.inventory.media.Media;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;
import pages.Path;
import pages.yield.openpricing.OpenPricingPage;
import rx.BaseTest;
import widgets.common.multipane.Multipane;
import widgets.common.multipane.MultipaneNameImpl;
import widgets.yield.openPricing.sidebar.CreateOpenPricingSidebar;

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
@Feature(value = "Open Pricing")
public class OpenPricingChangePublisherTests extends BaseTest {
    private OpenPricingPage openPricingPage;
    private CreateOpenPricingSidebar openPricingSidebar;
    private Multipane pricingMultipane;
    private Media media1;
    private Media media2;
    private Publisher publisher1;
    private Publisher publisher2;

    private final static String OPEN_PRICING_NAME = captionWithSuffix("autoPricing");
    private final static String FLOOR_PRICE = "15.00";
    private final static List<Integer> DSP_IDS_PUBLISHER_1 = List.of(6, 1, 2);
    private final static List<Integer> DSP_IDS_PUBLISHER_2 = List.of(9, 11, 13);
    private final static List<Integer> CATEGORY_IDS = List.of(1, 9);

    private final static List<String> DSP_NAMES_PUBLISHER_1 = List.of("Criteo", "RBidder", "Rubicon");
    private final static List<String> DSP_NAMES_PUBLISHER_2 = List.of("Flurry Web", "SpotX", "TheTradeDesk");

    final private static String BANNER_TEXT =
            "By changing the Publisher the form will be reset and the previous changes will not be saved.";

    public OpenPricingChangePublisherTests() {
        openPricingPage = new OpenPricingPage();
        openPricingSidebar = new CreateOpenPricingSidebar();
        pricingMultipane = new Multipane(MultipaneNameImpl.INVENTORY);
    }

    @BeforeClass
    private void createTestData() {
        publisher1 = publisher()
                .createNewPublisher(captionWithSuffix("00000000autoPub1"), true,
                        Currency.JPY, CATEGORY_IDS, DSP_IDS_PUBLISHER_1)
                .build()
                .getPublisherResponse();

        publisher2 = publisher()
                .createNewPublisher(captionWithSuffix("00000000autoPub2"), true,
                        Currency.EUR, CATEGORY_IDS, DSP_IDS_PUBLISHER_2)
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
    private void loginUI() {

        testStart()
                .given()
                .openDirectPath(Path.OPEN_PRICING)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                .clickOnWebElement(openPricingPage.getCreateOpenPricingButton())
                .waitSideBarOpened()
                .testEnd();
    }

    @Test(description = "Change Publisher and Click Accept")
    public void changePublisherAndClickAccept() {

        testStart()
                .clickBrowserRefreshButton()
                .and(String.format("Select Publisher %s", publisher1.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherNameDropdown(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisher1.getName())
                .testEnd();
        fillAllFields();
        changePublisherAndClickCancel(publisher2.getName());
        validateFieldsValuesShouldNotBeChanged(publisher1);
        validateListInventory(List.of(media1.getName()));
        validateListDemandSources(DSP_NAMES_PUBLISHER_1);
        changePublisherAndClickAccept(media2.getPublisherName());
        validateAllFieldsAreReseted();
        validateListInventory(List.of(media2.getName()));
        validateListDemandSources(DSP_NAMES_PUBLISHER_2);
        fillAllFields();
        changePublisherAndClickAccept(publisher1.getName());
        validateAllFieldsAreReseted();
        validateListInventory(List.of(media1.getName()));
        validateListDemandSources(DSP_NAMES_PUBLISHER_1);

    }

    @Issue("https://rakutenadvertising.atlassian.net/browse/GS-3102")
    @Test(description = "Change Publisher and check selected items multipane")
    public void changePublisherAndCheckSelectedItemsMultipane() {

        testStart()
                .clickBrowserRefreshButton()
                .and(String.format("Select Publisher %s", publisher1.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherNameDropdown(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisher1.getName())
                .and("Expand 'Inventory' multipane")
                .clickOnWebElement(pricingMultipane.getPanelNameLabel())
                .and("Select first item")
                .then()
                .clickOnWebElement(pricingMultipane.getIncludeAllButton())
                .then("Validate selected inventory items list should not be empty")
                .validate(pricingMultipane.countSelectTableItems(), 1)
                .selectFromDropdown(openPricingSidebar.getPublisherNameDropdown(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisher2.getName())
                .then(String.format("Validate selected inventory items list should contains items are belonged to %s",
                                publisher1.getName()))
                .validate(pricingMultipane.countSelectTableItems(), 1)
                .validate(pricingMultipane.countIncludedExcludedTableItems(), 1)
                .testEnd();

    }

    @Step("Fill all fields")
    private void fillAllFields() {

        testStart()
                .waitAndValidate(enabled, openPricingSidebar.getNameInput())
                .and(String.format("Fill Name %s", OPEN_PRICING_NAME))
                .setValueWithClean(openPricingSidebar.getNameInput(), OPEN_PRICING_NAME)
                .and(String.format("Set Floor Price %s", FLOOR_PRICE))
                .setValueWithClean(openPricingSidebar.getFloorPriceField().getFloorPriceInput(), FLOOR_PRICE)
                .and("Expand Inventory Multipane and include all")
                .clickOnWebElement(openPricingSidebar.getInventoryMultipane().getPanelNameLabel())
                .clickOnWebElement(openPricingSidebar.getInventoryMultipane().getIncludeAllButton())
                .and("Expand Geo Multipane and include all")
                .clickOnWebElement(openPricingSidebar.getGeoMultipane().getPanelNameLabel())
                .clickOnWebElement(openPricingSidebar.getGeoMultipane().getIncludeAllButton())
                .and("Expand Device Multipane and include all")
                .clickOnWebElement(openPricingSidebar.getDeviceMultipane().getPanelNameLabel())
                .clickOnWebElement(openPricingSidebar.getDeviceMultipane().getIncludeAllButton())
                .and("Expand Operating System Multipane and include all")
                .clickOnWebElement(openPricingSidebar.getOperatingSystemMultipane().getPanelNameLabel())
                .clickOnWebElement(openPricingSidebar.getOperatingSystemMultipane().getIncludeAllButton())
                .and("Expand Ad Format Multipane and include all")
                .clickOnWebElement(openPricingSidebar.getAdFormatMultipane().getPanelNameLabel())
                .clickOnWebElement(openPricingSidebar.getAdFormatMultipane().getIncludeAllButton())
                .and("Expand Ad Size Multipane and include all")
                .clickOnWebElement(openPricingSidebar.getAdSizeMultipane().getPanelNameLabel())
                .clickOnWebElement(openPricingSidebar.getAdSizeMultipane().getIncludeAllButton())
                .and("Expand Demand Source Multipane and include all")
                .clickOnWebElement(openPricingSidebar.getDemandSourcesMultipane().getPanelNameLabel())
                .clickOnWebElement(openPricingSidebar.getDemandSourcesMultipane().getIncludeAllButton())
                .testEnd();
    }

    @Step("Change Publisher on {0} and click Accept")
    private void changePublisherAndClickAccept(String publisherName) {
        var changePublisherBanner = openPricingSidebar.getChangePublisherBanner();

        testStart()
                .and(String.format("Select Publisher %s", publisherName))
                .selectFromDropdown(openPricingSidebar.getPublisherNameDropdown(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherName)
                .then("Check that warning banner appears")
                .validate(visible, changePublisherBanner.getBannerText(BANNER_TEXT))
                .then("Click 'Accept' on Warning Banner")
                .clickOnWebElement(changePublisherBanner.getAcceptButton(BANNER_TEXT))
                .testEnd();
    }

    @Step("Change Publisher on {0} and click Cancel")
    private void changePublisherAndClickCancel(String publisherName) {
        var changePublisherBanner = openPricingSidebar.getChangePublisherBanner();

        testStart()
                .and(String.format("Select Publisher %s", publisherName))
                .selectFromDropdown(openPricingSidebar.getPublisherNameDropdown(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherName)
                .then("Check that warning banner appears")
                .validate(visible, changePublisherBanner.getBannerText(BANNER_TEXT))
                .then("Click 'Cancel' on Warning Banner")
                .clickOnWebElement(changePublisherBanner.getCancelButton(BANNER_TEXT))
                .testEnd();
    }

    @Step("Validate all fields should be reseted")
    private void validateAllFieldsAreReseted() {

        testStart()
                .then("Name should be cleaned")
                .validate(openPricingSidebar.getNameInput(), "")
                .then("Floor Price should be cleaned")
                .validate(openPricingSidebar.getFloorPriceField().getFloorPriceInput(), "")
                .then("Expand Inventory Multipane and ensure that included/excluded items list is empty")
                .clickOnWebElement(openPricingSidebar.getInventoryMultipane().getPanelNameLabel())
                .validate(openPricingSidebar.getInventoryMultipane().countIncludedExcludedTableItems(), 0)
                .then("Expand Geo Multipane and ensure that included/excluded items list is empty")
                .clickOnWebElement(openPricingSidebar.getGeoMultipane().getPanelNameLabel())
                .validate(openPricingSidebar.getGeoMultipane().countIncludedExcludedTableItems(), 0)
                .then("Expand Device Multipane and ensure that included/excluded items list is empty")
                .clickOnWebElement(openPricingSidebar.getDeviceMultipane().getPanelNameLabel())
                .validate(openPricingSidebar.getDeviceMultipane().countIncludedExcludedTableItems(), 0)
                .then("Expand Operating System Multipane and ensure that included/excluded items list is empty")
                .clickOnWebElement(openPricingSidebar.getOperatingSystemMultipane().getPanelNameLabel())
                .validate(openPricingSidebar.getOperatingSystemMultipane().countIncludedExcludedTableItems(), 0)
                .then("Expand Ad Format Multipane and ensure that included/excluded items list is empty")
                .clickOnWebElement(openPricingSidebar.getAdFormatMultipane().getPanelNameLabel())
                .validate(openPricingSidebar.getAdFormatMultipane().countIncludedExcludedTableItems(), 0)
                .then("Expand Ad Sizes Multipane and ensure that included/excluded items list is empty")
                .clickOnWebElement(openPricingSidebar.getAdSizeMultipane().getPanelNameLabel())
                .validate(openPricingSidebar.getAdSizeMultipane().countIncludedExcludedTableItems(), 0)
                .then("Expand Demand source Multipane and ensure that included/excluded items list is empty")
                .clickOnWebElement(openPricingSidebar.getDemandSourcesMultipane().getPanelNameLabel())
                .validate(openPricingSidebar.getDemandSourcesMultipane().countIncludedExcludedTableItems(), 0)
                .testEnd();
    }

    @Step("Validate fields values should not be changed")
    private void validateFieldsValuesShouldNotBeChanged(Publisher publisher) {
        var changePublisherBanner = openPricingSidebar.getChangePublisherBanner();

        testStart()
                .then(String.format("Publisher name should be %s", publisher.getName()))
                .validate(openPricingSidebar.getPublisherNameDropdown().getText(), publisher.getName())
                .then(String.format("Name should be %s", OPEN_PRICING_NAME))
                .validateAttribute(openPricingSidebar.getNameInput(), "value", OPEN_PRICING_NAME)
                .then(String.format("Floor Price should be %s", FLOOR_PRICE))
                .validateAttribute(openPricingSidebar.getFloorPriceField().getFloorPriceInput(), "value", FLOOR_PRICE)
                .then(String.format("Currency should be %s", publisher.getCurrency()))
                .validate(openPricingSidebar.getFloorPriceField().getFloorPricePrefix().getText(), publisher.getCurrency())
                .testEnd();
    }

    @Step("Validate Inventory List")
    private void validateListInventory(List<String> inventory) {

        testStart()
                .and("Expand Inventory multipane and ensure that values in list corresponds with selected publisher")
                .clickOnWebElement(openPricingSidebar.getInventoryMultipane().getPanelNameLabel())
                .validate(openPricingSidebar.getInventoryMultipane().getSelectTableItemByPositionInList(0).getName(), inventory.get(0))
                .testEnd();

    }

    @Step("Validate Demand Sources List")
    private void validateListDemandSources(List<String> dsp) {

        testStart()
                .and("Expand  Demand Source multipane and ensure that values in list corresponds with selected publisher")
                .clickOnWebElement(openPricingSidebar.getDemandSourcesMultipane().getPanelNameLabel())
                .validate(openPricingSidebar.getDemandSourcesMultipane().getSelectTableItemByPositionInList(0).getName(), dsp.get(0))
                .validate(openPricingSidebar.getDemandSourcesMultipane().getSelectTableItemByPositionInList(1).getName(), dsp.get(1))
                .validate(openPricingSidebar.getDemandSourcesMultipane().getSelectTableItemByPositionInList(2).getName(), dsp.get(2))
                .testEnd();
    }

    @AfterMethod(alwaysRun = true)
    private void logout() {

        testStart()
                .and("Close Ad Spot Sidebar")
                .clickOnWebElement(openPricingSidebar.getCloseIcon())
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
            log.info(String.format("Deleted publisher %s", id));
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
