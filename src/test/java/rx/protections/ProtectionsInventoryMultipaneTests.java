package rx.protections;

import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.inventory.adspot.AdSpot;
import api.dto.rx.inventory.media.Media;
import api.preconditionbuilders.AdSpotPrecondition;
import api.preconditionbuilders.MediaPrecondition;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;
import pages.Path;
import pages.protections.ProtectionsPage;
import rx.BaseTest;
import io.qameta.allure.Step;
import rx.enums.MultipaneConstants;
import widgets.common.multipane.Multipane;
import widgets.common.multipane.MultipaneNameImpl;
import widgets.common.multipane.item.SelectTableItem;
import widgets.common.multipane.item.included.IncludedTableItem;
import widgets.protections.sidebar.CreateProtectionSidebar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static api.preconditionbuilders.AdSpotPrecondition.adSpot;
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
public class ProtectionsInventoryMultipaneTests extends BaseTest {

    private Publisher publisherEmpty;
    private Publisher publisherActive;
    private Publisher publisherInactive;
    private List<Media> mediaActiveList;
    private List<Media> mediaInactiveList;
    private List<AdSpot> adSpotActiveListOne;
    private List<AdSpot> adSpotInactiveListOne;
    private List<AdSpot> adSpotActiveListInactiveMediaOne;
    private List<AdSpot> adSpotInactiveListInactiveMediaOne;
    private List<AdSpot> adSpotActiveListTwo;
    private List<AdSpot> adSpotInactiveListTwo;
    private ProtectionsPage protectionsPage;
    private CreateProtectionSidebar protectionSidebar;
    private Multipane protectionMultipane;
    private final static Integer MEDIA_ACTIVE_COUNT = 5;
    private final static Integer MEDIA_INACTIVE_COUNT = 3;
    private final static Integer AD_SPOT_ACTIVE_COUNT = 4;
    private final static Integer AD_SPOT_INACTIVE_COUNT = 2;

    public ProtectionsInventoryMultipaneTests() {
        protectionsPage = new ProtectionsPage();
        protectionSidebar = new CreateProtectionSidebar();
        protectionMultipane = new Multipane(MultipaneNameImpl.INVENTORY);

        mediaActiveList = new ArrayList<>();
        mediaInactiveList = new ArrayList<>();
    }

    @BeforeClass
    private void login() {
        publisherEmpty = createPublisher(captionWithSuffix("0000autoPubProtections_empty"), true);
        publisherActive = createPublisher(captionWithSuffix("0000autoPubProtections_active"), true);
        publisherInactive = createPublisher(captionWithSuffix("0000autoPubProtections_inactive"), false);

        mediaActiveList = createMedia(publisherActive, true, MEDIA_ACTIVE_COUNT);
        mediaInactiveList = createMedia(publisherActive, false, MEDIA_INACTIVE_COUNT);

        adSpotActiveListOne = createAdSpot(mediaActiveList.get(0), true, AD_SPOT_ACTIVE_COUNT);
        adSpotInactiveListOne = createAdSpot(mediaActiveList.get(0), false, AD_SPOT_INACTIVE_COUNT);

        adSpotActiveListTwo = createAdSpot(mediaActiveList.get(1), true, AD_SPOT_ACTIVE_COUNT);
        adSpotInactiveListTwo = createAdSpot(mediaActiveList.get(1), false, AD_SPOT_INACTIVE_COUNT);

        adSpotActiveListInactiveMediaOne = createAdSpot(mediaInactiveList.get(0), true, AD_SPOT_ACTIVE_COUNT);
        adSpotInactiveListInactiveMediaOne = createAdSpot(mediaInactiveList.get(0), false, AD_SPOT_INACTIVE_COUNT);

        testStart()
                .given()
                .openDirectPath(Path.PROTECTIONS)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, protectionsPage.getNuxtProgress())
                .and("Click on 'Create Protection' button")
                .clickOnWebElement(protectionsPage.getCreateProtectionButton())
                .waitSideBarOpened()
                .testEnd();
    }

    @BeforeMethod
    private void expandMultipane() {
        testStart()
                .and("Expand 'Inventory' multipane")
                .clickOnWebElement(protectionMultipane.getPanelNameLabel())
                .waitSideBarOpened()
                .testEnd();
    }

    @Test(description = "Check Empty Inventory Items List", priority = 0)
    public void checkEmptyInventoryList() {

        testStart()
                .and(String.format("Select Publisher without Inventory '%s'", publisherEmpty.getName()))
                .selectFromDropdown(protectionSidebar.getPublisherInput(),
                        protectionSidebar.getPublisherItems(), publisherEmpty.getName())
                .then("Validate inventory items list should be empty")
                .validate(protectionMultipane.countSelectTableItems(), 0)
                .testEnd();
    }

    @Test(description = "Check Media Inventory Items List", priority = 1)
    public void checkInventoryList() {

        testStart()
                .and(String.format("Select Publisher without Inventory '%s'", publisherActive.getName()))
                .selectFromDropdown(protectionSidebar.getPublisherInput(),
                        protectionSidebar.getPublisherItems(), publisherActive.getName())
                .then("Validate inventory items list should be empty")
                .validateContainsText(protectionMultipane.getItemsQuantityString(), String.format("(%s MEDIA)", MEDIA_ACTIVE_COUNT))
                .validate(protectionMultipane.countSelectTableItems(), MEDIA_ACTIVE_COUNT)
                .testEnd();

        validateMediaList(mediaActiveList);
    }

    @Test(description = "Check Ad Spots Inventory Items List", priority = 2)
    public void checkAdSpotInventoryList() {

        testStart()
                .and(String.format("Select Publisher without Inventory '%s'", publisherActive.getName()))
                .selectFromDropdown(protectionSidebar.getPublisherInput(),
                        protectionSidebar.getPublisherItems(), publisherActive.getName())
                .testEnd();

        validateAdSpotsList(mediaActiveList.get(0), adSpotActiveListOne);
        validateAdSpotsList(mediaActiveList.get(1), adSpotActiveListTwo);
        validateAdSpotsCount(mediaActiveList.get(2).getName(), 0);
        validateAdSpotsCount(mediaActiveList.get(3).getName(), 0);
    }

    @Test(description = "Check Media Inventory Items List without Ad Spots should be empty", priority = 3)
    public void checkAdSpotInventoryListWithoutAdSpots() {

        testStart()
                .and(String.format("Select Publisher without Inventory '%s'", publisherActive.getName()))
                .selectFromDropdown(protectionSidebar.getPublisherInput(),
                        protectionSidebar.getPublisherItems(), publisherActive.getName())
                .testEnd();

        validateAdSpotsCount(mediaActiveList.get(2).getName(), 0);
        validateAdSpotsCount(mediaActiveList.get(3).getName(), 0);
        validateAdSpotsCount(mediaActiveList.get(4).getName(), 0);
    }

    @Test(description = "Check Media Inventory Inactive Items List", priority = 4)
    public void checkMediaInactiveInventoryList() {

        testStart()
                .and(String.format("Select Publisher without Inventory '%s'", publisherActive.getName()))
                .selectFromDropdown(protectionSidebar.getPublisherInput(),
                        protectionSidebar.getPublisherItems(), publisherActive.getName())
                .and("Set 'Show Inactive' toggle ON")
                .turnToggleOn(protectionMultipane.getShowInactive())
                .then("Validate item list includes inactive inventory")
                .validate(protectionMultipane.countSelectTableItems(), MEDIA_ACTIVE_COUNT + MEDIA_INACTIVE_COUNT)
                .testEnd();
        validateMediaList(mediaActiveList);
        validateInactiveMediaList(mediaInactiveList);
        testStart()
                .and("Set 'Show Inactive' toggle OFF")
                .turnToggleOff(protectionMultipane.getShowInactive())
                .then("Validate item list includes only active inventory")
                .validate(protectionMultipane.countSelectTableItems(), MEDIA_ACTIVE_COUNT)
                .testEnd();
        validateMediaList(mediaActiveList);
    }

    @Test(description = "Check AdSpots Inventory Inactive Items List (Media is Active)", priority = 5)
    public void checkAdSpotsInactiveInventoryListActiveMedia() {

        testStart()
                .and(String.format("Select Publisher without Inventory '%s'", publisherActive.getName()))
                .selectFromDropdown(protectionSidebar.getPublisherInput(),
                        protectionSidebar.getPublisherItems(), publisherActive.getName())
                .and("Set 'Show Inactive' toggle ON")
                .turnToggleOn(protectionMultipane.getShowInactive())
                .testEnd();
        validateAdSpotsCount(mediaActiveList.get(0).getName(), AD_SPOT_INACTIVE_COUNT + AD_SPOT_ACTIVE_COUNT);
        validateInactiveAdSpotsList(mediaActiveList.get(0).getName(), adSpotInactiveListOne);

        testStart()
                .and("Set 'Show Inactive' toggle OFF")
                .turnToggleOff(protectionMultipane.getShowInactive())
                .testEnd();

        validateAdSpotsCount(mediaActiveList.get(0).getName(), AD_SPOT_ACTIVE_COUNT);
    }

    @Test(description = "Check AdSpots Inventory Inactive Items List (Media is Inactive)", priority = 6)
    public void checkAdSpotsInactiveInventoryListInactiveMedia() {

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(protectionSidebar.getPublisherInput(),
                        protectionSidebar.getPublisherItems(), publisherActive.getName())
                .and("Set 'Show Inactive' toggle ON")
                .turnToggleOn(protectionMultipane.getShowInactive())
                .testEnd();
        validateAdSpotsCount(String.format("(Inactive) %s", mediaInactiveList.get(0).getName()), AD_SPOT_INACTIVE_COUNT + AD_SPOT_ACTIVE_COUNT);
        validateInactiveAdSpotsList(String.format("(Inactive) %s", mediaInactiveList.get(0).getName()), adSpotInactiveListInactiveMediaOne);
    }

    @Test(description = "Check Search Active Media", priority = 7)
    public void checkSearchActiveMedia() {

        testStart()
                .and(String.format("Select Publisher without Inventory '%s'", publisherActive.getName()))
                .selectFromDropdown(protectionSidebar.getPublisherInput(),
                        protectionSidebar.getPublisherItems(), publisherActive.getName())
                .and(String.format("Search inventory by Name = %s", mediaActiveList.get(0).getName()))
                .setValueWithClean(protectionMultipane.getSearchInput(), mediaActiveList.get(0).getName())
                .pressEnterKey(protectionMultipane.getSearchInput())
                .and("Set 'Show Inactive' toggle ON")
                .turnToggleOn(protectionMultipane.getShowInactive())
                .then("Validate item list includes inactive inventory")
                .validateContainsText(protectionMultipane.getItemsQuantityString(),"(1 MEDIA)")
                .validate(protectionMultipane.countSelectTableItems(), 1)
                .validate(protectionMultipane.getSelectTableItemByPositionInList(0).getName(), mediaActiveList.get(0).getName())
                .and("Set 'Show Inactive' toggle OFF")
                .turnToggleOff(protectionMultipane.getShowInactive())
                .then("Validate item list includes only active inventory")
                .validate(protectionMultipane.countSelectTableItems(), 1)
                .validate(protectionMultipane.getSelectTableItemByPositionInList(0).getName(), mediaActiveList.get(0).getName())
                .setValueWithClean(protectionMultipane.getSearchInput(), "")
                .testEnd();
    }

    @Test(description = "Check Search Inactive Media", priority = 8)
    public void checkSearchInactiveMedia() {

        testStart()
                .and(String.format("Select Publisher without Inventory '%s'", publisherActive.getName()))
                .selectFromDropdown(protectionSidebar.getPublisherInput(),
                        protectionSidebar.getPublisherItems(), publisherActive.getName())
                .and("Search inventory by Name")
                .setValueWithClean(protectionMultipane.getSearchInput(), mediaInactiveList.get(0).getName())
                .and("Set 'Show Inactive' toggle ON")
                .turnToggleOn(protectionMultipane.getShowInactive())
                .then("Validate item list includes inactive inventory")
                .validate(protectionMultipane.countSelectTableItems(), 1)
                .validate(protectionMultipane.getSelectTableItemByPositionInList(0).getName(),
                        String.format("(Inactive) %s", mediaInactiveList.get(0).getName()))
                .and("Set 'Show Inactive' toggle OFF")
                .turnToggleOff(protectionMultipane.getShowInactive())
                .then("Validate item list includes only active inventory")
                .validate(protectionMultipane.countSelectTableItems(), 0)
                .setValueWithClean(protectionMultipane.getSearchInput(), "")
                .testEnd();
    }

    //:TODO GS-3135

    @Test(description = "Check Search Active Ad Spot", priority = 9, enabled = false)
    public void checkSearchActiveAdSpot() {

        testStart()
                .and(String.format("Select Publisher without Inventory '%s'", publisherActive.getName()))
                .selectFromDropdown(protectionSidebar.getPublisherInput(),
                        protectionSidebar.getPublisherItems(), publisherActive.getName())
                .and(String.format("Search inventory by Name = %s", adSpotActiveListOne.get(0).getName()))
                .setValueWithClean(protectionMultipane.getSearchInput(), adSpotActiveListOne.get(0).getName())
                .pressEnterKey(protectionMultipane.getSearchInput())
                .validateContainsText(protectionMultipane.getItemsQuantityString(), "(1 AD SPOT)")
                .then("Validate item list")
                .validate(protectionMultipane.countSelectTableItems(), 1)
                .validate(protectionMultipane.getSelectTableItemByPositionInList(0).getName(),
                        String.format("Media > %s", mediaActiveList.get(0).getName()))
                .validateContainsText(protectionMultipane.getSelectTableItemByPositionInList(0).getName(),
                        adSpotActiveListOne.get(0).getName())
                .and("Set 'Show Inactive' toggle OFF")
                .turnToggleOff(protectionMultipane.getShowInactive())
                .then("Validate item list includes only active inventory")
                .validate(protectionMultipane.countSelectTableItems(), 1)
                .validate(protectionMultipane.getSelectTableItemByPositionInList(1).getName(), adSpotActiveListOne.get(0).getName())
                .setValueWithClean(protectionMultipane.getSearchInput(), "")
                .pressEnterKey(protectionMultipane.getSearchInput())
                .testEnd();
    }

    @Test(description = "Check Multipane Text 'Include All'", priority = 10)
    public void checkMultipaneText() {

        testStart()
                .and(String.format("Select Publisher without Inventory '%s'", publisherActive.getName()))
                .selectFromDropdown(protectionSidebar.getPublisherInput(),
                        protectionSidebar.getPublisherItems(), publisherActive.getName())
                .then("Validate text above items panel")
                .validate(protectionMultipane.getSelectionInfoExcludedLabel().getText(), "")
                .and("Include All media")
                .clickOnWebElement(protectionMultipane.getIncludeAllButton())
                .then("Validate 'Selected' items panel")
                .validate(protectionMultipane.countSelectTableItems(), protectionMultipane.countIncludedExcludedTableItems())
                .then("Validate text above items panel")
                .validate(protectionMultipane.getSelectionInfoExcludedLabel().getText(),
                        MultipaneConstants.MEDIA_ARE_INCLUDED.setQuantity(protectionMultipane.countSelectTableItems()))
                .testEnd();
    }

    @Test(description = "Check Multipane Text 'Clear All'", priority = 11)
    public void checkMultipaneTextClearAll() {

        testStart()
                .and(String.format("Select Publisher without Inventory '%s'", publisherActive.getName()))
                .selectFromDropdown(protectionSidebar.getPublisherInput(),
                        protectionSidebar.getPublisherItems(), publisherActive.getName())
                .then("Validate text above items panel")
                .validate(protectionMultipane.getSelectionInfoExcludedLabel().getText(), "")
                .and("Include All media")
                .clickOnWebElement(protectionMultipane.getIncludeAllButton())
                .clickOnWebElement(protectionMultipane.getClearAllButton())
                .then("Validate text above items panel")
                .validate(protectionMultipane.getSelectionInfoExcludedLabel().getText(),
                        MultipaneConstants.ALL_INVENTORY_IS_INCLUDED.setQuantity())
                .testEnd();
    }

    @Test(description = "Check Multipane Text 'N media is/are included'", priority = 12)
    public void checkMultipaneTextMediaIncluded() {

        testStart()
                .and(String.format("Select Publisher without Inventory '%s'", publisherActive.getName()))
                .selectFromDropdown(protectionSidebar.getPublisherInput(),
                        protectionSidebar.getPublisherItems(), publisherActive.getName())
                .validateContainsText(protectionMultipane.getItemsQuantityString(), String.format("(%s MEDIA)", MEDIA_ACTIVE_COUNT))
                .testEnd();
        selectMediaAndValidateSelectionInfoText(protectionMultipane.getSelectTableItemByPositionInList(1),
                MultipaneConstants.ONE_MEDIA_IS_INCLUDED.setQuantity(1));
        selectMediaAndValidateSelectionInfoText(protectionMultipane.getSelectTableItemByPositionInList(2),
                MultipaneConstants.MEDIA_ARE_INCLUDED.setQuantity(2));
        selectMediaAndValidateSelectionInfoText(protectionMultipane.getSelectTableItemByPositionInList(3),
                MultipaneConstants.MEDIA_ARE_INCLUDED.setQuantity(3));
        removeMediaAndValidateSelectionInfoText(protectionMultipane.getIncludedExcludedTableItemByPositionInList(0),
                MultipaneConstants.MEDIA_ARE_INCLUDED.setQuantity(2));
        removeMediaAndValidateSelectionInfoText(protectionMultipane.getIncludedExcludedTableItemByPositionInList(0),
                MultipaneConstants.ONE_MEDIA_IS_INCLUDED.setQuantity(1));
        removeMediaAndValidateSelectionInfoText(protectionMultipane.getIncludedExcludedTableItemByPositionInList(0),
                MultipaneConstants.ALL_INVENTORY_IS_INCLUDED.setQuantity());

    }

    //TODO: MultipanItems "//td[2]/div"
    @Test(description = "Check Multipane Text 'N media is/are included, N Ad spot is/are included'", priority = 13, enabled = false)
    public void checkMultipaneTextMediaIncludedAdSpotIncluded() {

        testStart()
                .and(String.format("Select Publisher without Inventory '%s'", publisherActive.getName()))
                .selectFromDropdown(protectionSidebar.getPublisherInput(),
                        protectionSidebar.getPublisherItems(), publisherActive.getName())
                .validateContainsText(protectionMultipane.getItemsQuantityString(), String.format("(%s MEDIA)", MEDIA_ACTIVE_COUNT))
                .testEnd();

        selectMediaAndValidateSelectionInfoText(protectionMultipane.getSelectTableItemByName(mediaActiveList.get(3).getName()),
                MultipaneConstants.ONE_MEDIA_IS_INCLUDED.setQuantity(1));
        selectMediaAndValidateSelectionInfoText(protectionMultipane.getSelectTableItemByName(mediaActiveList.get(2).getName()),
                MultipaneConstants.MEDIA_ARE_INCLUDED.setQuantity(2));
        selectAdSpotAndValidateSelectionInfoText(protectionMultipane.getSelectTableItemByName(mediaActiveList.get(0).getName()),
                adSpotActiveListOne.get(0).getName(),
                MultipaneConstants.MEDIA_ONE_AD_SPOT_ARE_INCLUDED.setQuantity(2, 1));
        removeMediaAndValidateSelectionInfoText(protectionMultipane.getIncludedExcludedTableItemByName(mediaActiveList.get(2).getName()),
                MultipaneConstants.MEDIA_ONE_AD_SPOT_ARE_INCLUDED.setQuantity(1, 1));

        removeMediaAndValidateSelectionInfoText(protectionMultipane.getIncludedExcludedTableItemByName(adSpotActiveListOne.get(0).getName()),
                MultipaneConstants.ONE_MEDIA_IS_INCLUDED.setQuantity(1));
    }

    @Test(description = "Check Multipane Text (exclude not all items)", priority = 14)
    private void checkMultipaneExcludeTextNotAllItems() {
        var adFormatMultipane = protectionSidebar.getAdFormatMultipane();


        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(protectionSidebar.getPublisherInput(),
                        protectionSidebar.getPublisherItems(), publisherActive.getName())
                .then("Validate inventory items list should be empty")
                .validate(protectionMultipane.countSelectTableItems(), 5)
                .testEnd();

        excludeOneByOneItems(mediaActiveList);
    }

    @Test(description = "Check Multipane Text (exclude not all items)", priority = 15, dependsOnMethods = "checkMultipaneExcludeTextNotAllItems")
    private void checkMultipaneExcludeTextRemoveNotAllItems() {
        var adFormatMultipane = protectionSidebar.getAdFormatMultipane();


        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(protectionSidebar.getPublisherInput(),
                        protectionSidebar.getPublisherItems(), publisherActive.getName())
                .then("Validate inventory items list should be empty")
                .validate(protectionMultipane.countSelectTableItems(), 5)
                .testEnd();

        removeOneByOneExcludedItems(mediaActiveList);
    }

    @Step("Exclude all items one by one")
    private void excludeOneByOneItems(List<Media> list) {
        var inventoryMultipane = protectionSidebar.getInventoryMultipane();
        var updated = new AtomicInteger(0);

        list.forEach(e ->
        {
            var selectedItem = inventoryMultipane.getSelectTableItemByPositionInList(updated.get());
            testStart()
                    .waiter(visible, selectedItem.getName())
                    .hoverMouseOnWebElement(selectedItem.getName())
                    .then()
                    .validate(not(visible), selectedItem.getExcludedIcon())
                    .validate(not(visible), selectedItem.getIncludedIcon())
                    .validate(not(visible), selectedItem.getActiveIcon())
                    .validate(not(visible), selectedItem.getInactiveIcon())
                    .validate(not(visible), selectedItem.getAssociatedWithPublisherIcon())
                    .validate(visible, selectedItem.getExcludeButton())
                    .and()
                    .clickOnWebElement(selectedItem.getExcludeButton())
                    .validate(inventoryMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 0) ?
                            MultipaneConstants.ONE_MEDIA_IS_EXCLUDED.setQuantity(updated.incrementAndGet()) :
                            MultipaneConstants.MEDIA_ARE_EXCLUDED.setQuantity(updated.incrementAndGet()))
                    .validate(exist, selectedItem.getName())
                    .testEnd();
        });
    }

    @Step("Remove all excluded items one by one")
    private void removeOneByOneExcludedItems(List<Media> list) {
        var updated = new AtomicInteger(list.size());
        var inventoryMultipane = protectionSidebar.getInventoryMultipane();

        list.forEach(item -> {
            var selectedItem = inventoryMultipane.getIncludedExcludedTableItemByName(item.getName());

            testStart()
                    .waiter(visible, selectedItem.getName())
                    .hoverMouseOnWebElement(selectedItem.getName())
                    .then()
                    .validateContainsText(inventoryMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 1) ?
                            MultipaneConstants.ONE_MEDIA_IS_EXCLUDED.setQuantity(updated.getAndDecrement()) :
                            MultipaneConstants.MEDIA_ARE_EXCLUDED.setQuantity(updated.getAndDecrement()))
                    .then()
                    .validate(visible, selectedItem.getRemoveButton())
                    .and(String.format("Remove selected item %s", selectedItem.getName()))
                    .clickOnWebElement(selectedItem.getRemoveButton())
                    .testEnd();
        });
    }

    @AfterMethod(alwaysRun = true)
    private void collapseMultipane() {
        testStart()
                .and("Collapse 'Inventory' multipane")
                .clickOnWebElement(protectionMultipane.getPanelNameLabel())
                .testEnd();
    }

    @AfterClass(alwaysRun = true)
    private void logout() {

        testStart()
                .clickOnWebElement(protectionSidebar.getCloseIcon())
                .waitSideBarClosed()
                .and("Logout")
                .logOut()
                .testEnd();

        deleteTestData();
    }

    @Step("Select media {0} and validate label {1}")
    private void selectMediaAndValidateSelectionInfoText(SelectTableItem media, String selectionInfoText) {

        testStart()
                .and(String.format("Include media %s", media.getName()))
                .hoverMouseOnWebElement(media.getName())
                .waiter(visible, media.getIncludeButton())
                .clickOnWebElement(media.getIncludeButton())
                .then("Validate text above items panel")
                .validate(protectionMultipane.getSelectionInfoExcludedLabel().getText(), selectionInfoText)
                .testEnd();
    }

    @Step("Remove media {0} and validate label {1}")
    private void removeMediaAndValidateSelectionInfoText(IncludedTableItem media, String selectionInfoText) {

        testStart()
                .and(String.format("Remove media %s", media.getName()))
                .clickOnWebElement(media.getRemoveButton())
                .then("Validate text above items panel")
                .validate(protectionMultipane.getSelectionInfoExcludedLabel().getText(), selectionInfoText)
                .testEnd();
    }

    @Step("Select ad spot {0} and validate label {1}")
    private void selectAdSpotAndValidateSelectionInfoText(SelectTableItem media, String adSpot, String selectionInfoText) {

        testStart()
                .and(String.format("Include ad spot %s", adSpot))
                .clickOnWebElement(media.getName())
                .waiter(visible, protectionMultipane.getSelectChildTableItemByName(adSpot).getName())
                .hoverMouseOnWebElement(protectionMultipane.getSelectChildTableItemByName(adSpot).getName())
                .waiter(visible, protectionMultipane.getSelectChildTableItemByName(adSpot).getIncludeButton())
                .clickOnWebElement(protectionMultipane.getSelectChildTableItemByName(adSpot).getIncludeButton())
                .then("Validate text above items panel")
                .validate(protectionMultipane.getSelectionInfoExcludedLabel().getText(), selectionInfoText)
                .testEnd();
    }

    @Step("Remove ad spot {0} and validate label {1}")
    private void removeAdSpotAndValidateSelectionInfoText(IncludedTableItem media, String adSpot, String selectionInfoText) {
        testStart()
                .and(String.format("Remove ad spot %s", adSpot))
                .clickOnWebElement(media.getName())
                .waiter(visible, protectionMultipane.getSelectChildTableItemByName(adSpot).getName())
                .hoverMouseOnWebElement(protectionMultipane.getSelectChildTableItemByName(adSpot).getName())
                .waiter(visible, protectionMultipane.getSelectChildTableItemByName(adSpot).getIncludeButton())
                .clickOnWebElement(protectionMultipane.getSelectChildTableItemByName(adSpot).getIncludeButton())
                .then("Validate text above items panel")
                .validate(protectionMultipane.getSelectionInfoExcludedLabel().getText(), selectionInfoText)
                .testEnd();
    }

    private Publisher createPublisher(String name, Boolean isEnabled) {

        return publisher()
                .createNewPublisher(name, isEnabled)
                .build()
                .getPublisherResponse();
    }

    private List<Media> createMedia(Publisher publisher, Boolean isEnabled, Integer count) {
        List<Media> list = new ArrayList<>();

        while (list.size() < count) {

            list.add(media()
                    .createNewMedia("autoMedia", publisher.getId(), isEnabled)
                    .build()
                    .getMediaResponse());
        }

        return list;
    }

    private List<AdSpot> createAdSpot(Media media, Boolean isEnabled, Integer count) {
        List<AdSpot> list = new ArrayList<>();

        while (list.size() < count) {
            list.add(adSpot()
                    .createNewAdSpot(captionWithSuffix("autoAdSpot"), media.getPublisherId(), media.getId(), isEnabled)
                    .build()
                    .getAdSpotResponse());
        }

        return list;
    }

    @Step("Validate Inventory List")
    private void validateMediaList(List<Media> list) {

        list.stream().forEach(item -> {
            testStart()
                    .validate(protectionMultipane.getSelectTableItemByName(item.getName()).getName(),
                            item.getName())
                    .testEnd();
        });
    }

    @Step("Validate Inactive Inventory List")
    private void validateInactiveMediaList(List<Media> list) {

        list.stream().forEach(item -> {
            testStart()
                    .validate(protectionMultipane.getSelectTableItemByName(String.format("(Inactive) %s", item.getName())).getName(),
                            String.format("(Inactive) %s", item.getName()))
                    .testEnd();
        });
    }

    @Step("Validate ad spot count for media {0}")
    private void validateAdSpotsCount(String mediaName, Integer count) {
        testStart()
                .clickOnWebElement(protectionMultipane.getSelectTableItemByName(mediaName).getName())
                .validate(protectionMultipane.countSelectChildTableItems(), count)
                .clickOnWebElement(protectionMultipane.getSelectTableItemByName(mediaName).getName())
                .testEnd();
    }

    @Step("Validate ad spot list for media {0}")
    private void validateAdSpotsList(Media media, List<AdSpot> adSpotList) {

        testStart()
                .clickOnWebElement(protectionMultipane.getSelectTableItemByName(media.getName()).getName())
                .testEnd();

        adSpotList.stream().forEach(item -> {
            testStart()
                    .validate(protectionMultipane.getSelectChildTableItemByName(item.getName()).getName(),
                            item.getName())
                    .testEnd();
        });

        testStart()
                .clickOnWebElement(protectionMultipane.getSelectTableItemByName(media.getName()).getName())
                .testEnd();
    }

    @Step("Validate inactive ad spot list for media {0}")
    private void validateInactiveAdSpotsList(String mediaName, List<AdSpot> adSpotList) {

        testStart()
                .clickOnWebElement(protectionMultipane.getSelectTableItemByName(mediaName).getName())
                .testEnd();

        adSpotList.forEach(item -> {
            String adSpotName = String.format("(Inactive) %s", item.getName());
            testStart()
                    .validate(protectionMultipane.getSelectChildTableItemByName(adSpotName).getName(), adSpotName)
                    .testEnd();
        });

        testStart()
                .clickOnWebElement(protectionMultipane.getSelectTableItemByName(mediaName).getName())
                .testEnd();
    }

    @Step("Delete test data")
    private void deleteTestData() {

        deleteAdSpots(adSpotActiveListTwo);
        deleteAdSpots(adSpotInactiveListOne);
        deleteAdSpots(adSpotInactiveListTwo);
        deleteAdSpots(adSpotActiveListInactiveMediaOne);
        deleteAdSpots(adSpotInactiveListInactiveMediaOne);

        deleteMedia(mediaActiveList);
        deleteMedia(mediaInactiveList);

        deletePublisher(publisherEmpty.getId());
        deletePublisher(publisherActive.getId());
        deletePublisher(publisherInactive.getId());
    }

    private void deletePublisher(Integer id) {

        if (publisher()
                .setCredentials(USER_FOR_DELETION)
                .deletePublisher(id)
                .build()
                .getResponseCode() == HttpStatus.SC_NO_CONTENT) {
            log.info(String.format("Deleted publisher %s", id));
        }
    }

    private void deleteMedia(List<Media> listMedia) {
        var deleted = new AtomicInteger(0);
        listMedia.forEach(
                media -> {
                    if (MediaPrecondition.media().
                            setCredentials(USER_FOR_DELETION).
                            deleteMedia(media.getId())
                            .build()
                            .getResponseCode() == HttpStatus.SC_NO_CONTENT) {
                        deleted.getAndIncrement();
                    }
                });
        log.info(String.format("Deleted media items %s", deleted));
    }

    private void deleteAdSpots(List<AdSpot> listAdSpots) {

        final var deleted = new AtomicInteger(0);
        listAdSpots.forEach(
                adSpot -> {
                    if (AdSpotPrecondition.adSpot()
                            .setCredentials(USER_FOR_DELETION)
                            .deleteAdSpot(adSpot.getId())
                            .build()
                            .getResponseCode() == HttpStatus.SC_NO_CONTENT) {
                        deleted.getAndIncrement();
                    }
                });
        log.info(String.format("Deleted ad spots items %s", deleted));
    }
}
