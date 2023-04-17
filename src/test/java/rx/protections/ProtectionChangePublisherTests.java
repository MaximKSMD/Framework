package rx.protections;

import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.common.Currency;
import api.dto.rx.inventory.media.Media;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Link;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;
import pages.Path;
import pages.protections.ProtectionsPage;
import rx.BaseTest;
import widgets.common.multipane.Multipane;
import widgets.protections.sidebar.CreateProtectionSidebar;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static api.preconditionbuilders.MediaPrecondition.media;
import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static configurations.User.USER_FOR_DELETION;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Protections")
public class ProtectionChangePublisherTests extends BaseTest {

    private ProtectionsPage protectionPage;
    private CreateProtectionSidebar protectionSidebar;
    private Media media1;
    private Media media2;
    private Publisher publisher1;
    private Publisher publisher2;

    private final static String PROTECTION_NAME = captionWithSuffix("autoProtection");
    private final static List<Integer> DSP_IDS_PUBLISHER_1 = List.of(6, 1, 2);
    private final static List<Integer> DSP_IDS_PUBLISHER_2 = List.of(9, 11, 13);
    private final static List<Integer> CATEGORY_IDS = List.of(1, 9);

    private final static List<String> DSP_NAMES_PUBLISHER_1 = List.of("Criteo", "RBidder", "Rubicon");
    private final static List<String> DSP_NAMES_PUBLISHER_2 = List.of("Flurry Web", "SpotX", "TheTradeDesk");

    final private static String BANNER_TEXT =
            "By changing the Publisher the form will be reset and the previous changes will not be saved.";

    public ProtectionChangePublisherTests() {
        protectionPage = new ProtectionsPage();
        protectionSidebar = new CreateProtectionSidebar();
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
                .openDirectPath(Path.PROTECTIONS)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, protectionPage.getNuxtProgress())
                .clickOnWebElement(protectionPage.getCreateProtectionButton())
                .waitSideBarOpened()
                .testEnd();
    }

    @Link("https://rakutenadvertising.atlassian.net/browse/GS-3192")
    @Test(description = "Change Publisher and Click Accept")
    public void changePublisherAndClickAccept() {

        testStart()
                .clickBrowserRefreshButton()
                .and(String.format("Select Publisher %s", publisher1.getName()))
                .selectFromDropdown(protectionSidebar.getPublisherInput(),
                        protectionSidebar.getPublisherItems(),  publisher1.getName())
                .and("Expand  Demand Source multipane and ensure that values in list corresponds with selected publisher")
                .clickOnWebElement(protectionSidebar.getDemandSourcesMultipane().getPanelNameLabel())
                .and("Expand  Inventory multipane and ensure that values in list corresponds with selected publisher")
                .clickOnWebElement(protectionSidebar.getInventoryMultipane().getPanelNameLabel())
                .testEnd();
        fillAllFields();
        changePublisherAndClickCancel(publisher2.getName());
        validateFieldsValuesShouldNotBeChanged(publisher1);
        validateItemsList(List.of(media1.getName()),protectionSidebar.getInventoryMultipane());
        validateItemsList(DSP_NAMES_PUBLISHER_1,protectionSidebar.getDemandSourcesMultipane() );
        changePublisherAndClickAccept(media2.getPublisherName());
        validateAllFieldsAreReseted();
        validateItemsList(List.of(media2.getName()), protectionSidebar.getInventoryMultipane());
        validateItemsList(DSP_NAMES_PUBLISHER_2, protectionSidebar.getDemandSourcesMultipane());
        fillAllFields();
        changePublisherAndClickAccept(publisher1.getName());
        validateAllFieldsAreReseted();
        validateItemsList(List.of(media1.getName()),protectionSidebar.getInventoryMultipane());
        validateItemsList(DSP_NAMES_PUBLISHER_1, protectionSidebar.getDemandSourcesMultipane());

    }

    @Issue("https://rakutenadvertising.atlassian.net/browse/GS-3102")
    @Test(description = "Change Publisher and check selected items multipane")
    public void changePublisherAndCheckSelectedItemsMultipane() {
        var protectionMultipane = protectionSidebar.getInventoryMultipane();

        testStart()
                .clickBrowserRefreshButton()
                .and(String.format("Select Publisher %s", publisher1.getName()))
                .selectFromDropdown(protectionSidebar.getPublisherInput(),
                        protectionSidebar.getPublisherItems(), publisher1.getName())
                .and("Expand 'Inventory' multipane")
                .clickOnWebElement(protectionMultipane.getPanelNameLabel())
                .and("Select first item")
                .then()
                .clickOnWebElement(protectionMultipane.getIncludeAllButton())
                .then("Validate selected inventory items list should not be empty")
                .validate(protectionMultipane.countSelectTableItems(), 1)
                .selectFromDropdown(protectionSidebar.getPublisherInput(),
                        protectionSidebar.getPublisherItems(),  publisher2.getName())
                .then(String.format("Validate selected inventory items list should contains items are belonged to %s",
                                publisher1.getName()))
                .validate(protectionMultipane.countSelectTableItems(), 1)
                .validate(protectionMultipane.countIncludedExcludedTableItems(), 1)
                .testEnd();

    }

    @Step("Fill all fields")
    private void fillAllFields() {
        var protectionMultipane = protectionSidebar.getInventoryMultipane();

        testStart()
                .waitAndValidate(enabled, protectionSidebar.getNameInput())
                .and(String.format("Fill Name %s", PROTECTION_NAME))
                .setValueWithClean(protectionSidebar.getNameInput(), PROTECTION_NAME)
                .and("Expand Inventory Multipane and include all")
                .clickOnWebElement(protectionSidebar.getInventoryMultipane().getPanelNameLabel())
                .clickOnWebElement(protectionSidebar.getInventoryMultipane().getIncludeAllButton())
                .and("Expand Geo Multipane and include all")
                .clickOnWebElement(protectionSidebar.getGeoMultipane().getPanelNameLabel())
                .clickOnWebElement(protectionSidebar.getGeoMultipane().getIncludeAllButton())
                .and("Expand Device Multipane and include all")
                .clickOnWebElement(protectionSidebar.getDeviceMultipane().getPanelNameLabel())
                .clickOnWebElement(protectionSidebar.getDeviceMultipane().getIncludeAllButton())
                .and("Expand Operating System Multipane and include all")
                .clickOnWebElement(protectionSidebar.getOperatingSystemMultipane().getPanelNameLabel())
                .clickOnWebElement(protectionSidebar.getOperatingSystemMultipane().getIncludeAllButton())
                .and("Expand Ad Format Multipane and include all")
                .clickOnWebElement(protectionSidebar.getAdFormatMultipane().getPanelNameLabel())
                .clickOnWebElement(protectionSidebar.getAdFormatMultipane().getIncludeAllButton())
                .and("Expand Ad Size Multipane and include all")
                .clickOnWebElement(protectionSidebar.getAdSizeMultipane().getPanelNameLabel())
                .clickOnWebElement(protectionSidebar.getAdSizeMultipane().getIncludeAllButton())
                .and("Expand Demand Source Multipane and include all")
                .clickOnWebElement(protectionSidebar.getDemandSourcesMultipane().getPanelNameLabel())
                .clickOnWebElement(protectionSidebar.getDemandSourcesMultipane().getIncludeAllButton())
                .testEnd();
    }

    @Step("Change Publisher on {0} and click Accept")
    private void changePublisherAndClickAccept(String publisherName) {
        var changePublisherBanner = protectionSidebar.getChangePublisherBanner();

        testStart()
                .and(String.format("Select Publisher %s", publisherName))
                .selectFromDropdown(protectionSidebar.getPublisherInput(),
                        protectionSidebar.getPublisherItems(), publisherName)
                .then("Check that warning banner appears")
                .validate(visible, changePublisherBanner.getBannerText(BANNER_TEXT))
                .then("Click 'Accept' on Warning Banner")
                .clickOnWebElement(changePublisherBanner.getAcceptButton(BANNER_TEXT))
                .testEnd();
    }

    @Step("Change Publisher on {0} and click Cancel")
    private void changePublisherAndClickCancel(String publisherName) {
        var changePublisherBanner = protectionSidebar.getChangePublisherBanner();

        testStart()
                .and(String.format("Select Publisher %s", publisherName))
                .selectFromDropdown(protectionSidebar.getPublisherInput(),
                        protectionSidebar.getPublisherItems(),  publisherName)
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
                .validate(protectionSidebar.getNameInput(), "")
                .then("Expand Inventory Multipane and ensure that included/excluded items list is empty")
                .clickOnWebElement(protectionSidebar.getInventoryMultipane().getPanelNameLabel())
                .validate(protectionSidebar.getInventoryMultipane().countIncludedExcludedTableItems(), 0)
                .then("Expand Geo Multipane and ensure that included/excluded items list is empty")
                .clickOnWebElement(protectionSidebar.getGeoMultipane().getPanelNameLabel())
                .validate(protectionSidebar.getGeoMultipane().countIncludedExcludedTableItems(), 0)
                .then("Expand Device Multipane and ensure that included/excluded items list is empty")
                .clickOnWebElement(protectionSidebar.getDeviceMultipane().getPanelNameLabel())
                .validate(protectionSidebar.getDeviceMultipane().countIncludedExcludedTableItems(), 0)
                .then("Expand Operating System Multipane and ensure that included/excluded items list is empty")
                .clickOnWebElement(protectionSidebar.getOperatingSystemMultipane().getPanelNameLabel())
                .validate(protectionSidebar.getOperatingSystemMultipane().countIncludedExcludedTableItems(), 0)
                .then("Expand Ad Format Multipane and ensure that included/excluded items list is empty")
                .clickOnWebElement(protectionSidebar.getAdFormatMultipane().getPanelNameLabel())
                .validate(protectionSidebar.getAdFormatMultipane().countIncludedExcludedTableItems(), 0)
                .then("Expand Ad Sizes Multipane and ensure that included/excluded items list is empty")
                .clickOnWebElement(protectionSidebar.getAdSizeMultipane().getPanelNameLabel())
                .validate(protectionSidebar.getAdSizeMultipane().countIncludedExcludedTableItems(), 0)
                .then("Expand Demand source Multipane and ensure that included/excluded items list is empty")
                .clickOnWebElement(protectionSidebar.getDemandSourcesMultipane().getPanelNameLabel())
                .validate(protectionSidebar.getDemandSourcesMultipane().countIncludedExcludedTableItems(), 0)
                .testEnd();
    }

    @Step("Validate fields values should not be changed")
    private void validateFieldsValuesShouldNotBeChanged(Publisher publisher) {
        var changePublisherBanner = protectionSidebar.getChangePublisherBanner();

        testStart()
                .then(String.format("Publisher name should be %s", publisher.getName()))
                .validate(protectionSidebar.getPublisherNameDropdown().getText(), publisher.getName())
                .then(String.format("Name should be %s", PROTECTION_NAME))
                .validateAttribute(protectionSidebar.getNameInput(), "value", PROTECTION_NAME)
                .testEnd();
    }

    @Step("Validate Demand Sources List")
    private void validateItemsList(List<String> itemsList, Multipane multipane) {
        var item = new AtomicInteger(0);

        itemsList.forEach(e ->
        {
            var selectedItem = multipane.getSelectTableItemByPositionInList(item.get()).getName();
                testStart()
                        .validate(selectedItem, itemsList.get(item.getAndIncrement()))
                    .testEnd();
        });
    }


    @AfterMethod(alwaysRun = true)
    private void logout() {

        testStart()
                .and("Close Ad Spot Sidebar")
                .clickOnWebElement(protectionSidebar.getCloseIcon())
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
