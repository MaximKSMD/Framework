package rx.component.multipane;

import api.dto.rx.inventory.adspot.AdSpot;
import api.dto.rx.inventory.media.Media;
import api.preconditionbuilders.AdSpotPrecondition;
import api.preconditionbuilders.MediaPrecondition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Path;
import rx.BaseTest;
import widgets.common.multipane.item.SelectChildTableItem;
import widgets.common.multipane.item.SelectTableItem;
import widgets.common.multipane.item.included.IncludedTableItem;
import widgets.protections.sidebar.CreateProtectionSidebar;
import widgets.sales.privateauctions.sidebar.CreatePrivateAuctionSidebar;
import widgets.yield.openPricing.sidebar.CreateOpenPricingSidebar;

import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static java.lang.String.format;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Components")
public class InventoryMultipaneTest extends BaseTest {

    private Media expectedMedia;
    private AdSpot expectedAdSpot;
    private CreateProtectionSidebar createProtectionSidebar;
    private CreateOpenPricingSidebar createOpenPricingSidebar;
    private CreatePrivateAuctionSidebar createPrivateAuctionSidebar;

    private static final String VIBER_PUBLISHER_NAME = "Viber";
    private static final String VIBER_INVENTORY_PARENT_TYPE = "Media";
    private static final String VIBER_INVENTORY_CHILD_TYPE = "Ad Spot";

    public InventoryMultipaneTest() {
        createProtectionSidebar = new CreateProtectionSidebar();
        createOpenPricingSidebar = new CreateOpenPricingSidebar();
        createPrivateAuctionSidebar = new CreatePrivateAuctionSidebar();
    }
    @BeforeClass
    @Step("Logging in SSP")
    public void getInventoryItemDataAndLogin() {
        expectedAdSpot = getExpectedAdSpot();
        expectedMedia = getExpectedMedia();

        testStart()
                .openUrl()
                .logIn(TEST_USER)
                .testEnd();
    }

    @Step("Expand Multipane Component for Inventory for Private Auction")
    @Test(priority = 0)
    public void expandMultipaneTestInPrivateAuctionSidebar() {
        openInventoryMultipaneInSidebar(Path.CREATE_PRIVATE_AUCTION);

        testStart()
                .and("Select Publisher")
                .waitAndValidate(appear, createPrivateAuctionSidebar.getPublisherNameDropdown())
                .selectFromDropdown(createPrivateAuctionSidebar.getPublisherNameDropdown(), createPrivateAuctionSidebar.getPublisherItems(), VIBER_PUBLISHER_NAME)
                .clickOnWebElement(createPrivateAuctionSidebar.getInventoryMultipane().getPanelNameLabel())
                .testEnd();
    }

    @Step("Check Parent item state in Multipane Component")
    @Test(priority = 1)
    public void checkParentItemStateInMultipaneTest() {
        //Check Inventory Selection
        validateParentInventoryToSelect(createPrivateAuctionSidebar.getInventoryMultipane()
                .getSelectTableItemByName(expectedMedia.getName()));
    }

    @Test(priority = 2)
    @Step("Check Child item state in Multipane Component")
    public void checkChildItemStateInMultipaneTest() {
        //Check Inventory Selection
        selectChildItemFromParent( createPrivateAuctionSidebar.getInventoryMultipane()
                .getSelectTableItemByName(expectedMedia.getName()));

        validateChildInventoryToSelect(createPrivateAuctionSidebar.getInventoryMultipane()
                .getSelectChildTableItemByPositionInList(0));
    }

    @Test(priority = 3)
    @Step("Search Inventory from Multipane Component")
    public void checkSearchItemTest() {
        validateDefaultSettingsOfSearchItem(createPrivateAuctionSidebar.getInventoryMultipane()
                .getSelectTableItemByPositionInList(0));
    }

    @Test(priority = 4)
    @Step("Select Child Item as Included from Search result list from Multipane Component")
    public void selectChildMultipaneItemAsIncludedTest() {
        validateItemToSelectAfterInclude(createPrivateAuctionSidebar.getInventoryMultipane()
                .getSelectTableItemByPositionInList(0));

        validateIncludedItem(createPrivateAuctionSidebar.getInventoryMultipane()
                .getIncludedExcludedTableItemByPositionInList(0), expectedAdSpot.getName(), VIBER_INVENTORY_CHILD_TYPE);
        validateParentLabelInIncludedItem(createPrivateAuctionSidebar.getInventoryMultipane()

                .getIncludedExcludedTableItemByPositionInList(0), expectedMedia.getName());
    }

    @Test(priority = 5)
    @Step("Remove Child Inventory Item from Included Items list from Multipane Component")
    public void removeChildMultipaneItemAsIncludedTest() {
        removeIncludedItemFromList(createPrivateAuctionSidebar.getInventoryMultipane()
                .getIncludedExcludedTableItemByPositionInList(0));

        validateEmptyIncludedExcludedList();

        selectChildItemFromParent(createPrivateAuctionSidebar.getInventoryMultipane()
                .getSelectTableItemByName(expectedMedia.getName()));

        validateChildInventoryToSelect(createPrivateAuctionSidebar.getInventoryMultipane()
                .getSelectChildTableItemByPositionInList(0));
    }

    @Test(priority = 6)
    @Step("Select Parent Item as Included from Search result list from Multipane Component")
    public void selectParentMultipaneItemAsIncludedTest() {
        validateItemToSelectAfterInclude(createPrivateAuctionSidebar.getInventoryMultipane()
                .getSelectTableItemByName(expectedMedia.getName()));

        validateIncludedItem(createPrivateAuctionSidebar.getInventoryMultipane()
                .getIncludedExcludedTableItemByPositionInList(0), expectedMedia.getName(), VIBER_INVENTORY_PARENT_TYPE);
    }

    @Test(priority = 7)
    @Step("Remove Parent Inventory Item from Included Items list from Multipane Component")
    public void removeParentMultipaneItemAsIncludedTest() {
        removeIncludedItemFromList(createPrivateAuctionSidebar.getInventoryMultipane()
                .getIncludedExcludedTableItemByPositionInList(0));

        validateEmptyIncludedExcludedList();

        selectChildItemFromParent( createPrivateAuctionSidebar.getInventoryMultipane()
                .getSelectTableItemByName(expectedMedia.getName()));
    }

    @Step("Expand Multipane Component for Inventory for Open Pricing")
    @Test(priority = 8)
    public void expandMultipaneTestInOpenPricingSidebar() {
        openInventoryMultipaneInSidebar(Path.CREATE_OPEN_PRICING);

        testStart()
                .and("Select Publisher")
                .waitAndValidate(appear, createOpenPricingSidebar.getPublisherNameDropdown())
                .selectFromDropdown(createOpenPricingSidebar.getPublisherNameDropdown(), createOpenPricingSidebar.getPublisherNameDropdownItems(), VIBER_PUBLISHER_NAME)
                .clickOnWebElement(createOpenPricingSidebar.getInventoryMultipane().getPanelNameLabel())
                .testEnd();
    }

    @Step("Check Parent item state in Multipane Component in OpenPricing sidebar")
    @Test(priority = 9)
    public void checkParentItemStateInOpenPricingMultipaneTest() {
        //Check Inventory Selection
        validateParentInventoryToSelect(createOpenPricingSidebar.getInventoryMultipane()
                .getSelectTableItemByName(expectedMedia.getName()));
    }

    @Test(priority = 10)
    @Step("Check Child item state in Multipane Component in OpenPricing sidebar")
    public void checkChildItemStateInOpenPricingMultipaneTest() {
        //Check Inventory Selection
        selectChildItemFromParent( createOpenPricingSidebar.getInventoryMultipane()
                .getSelectTableItemByName(expectedMedia.getName()));

        validateChildInventoryToSelect(createOpenPricingSidebar.getInventoryMultipane()
                .getSelectChildTableItemByPositionInList(0));
    }

    @Test(priority = 11)
    @Step("Search Inventory from Multipane Component in OpenPricing sidebar")
    public void checkSearchItemInOpenPricingMultipaneTest() {
        validateDefaultSettingsOfSearchItem(createOpenPricingSidebar.getInventoryMultipane()
                .getSelectTableItemByPositionInList(0));
    }

    @Test(priority = 12)
    @Step("Select Child Item as Included from Search result list from Multipane Component in OpenPricing sidebar")
    public void selectChildMultipaneItemAsIncludedInOpenPricingTest() {
        validateItemToSelectAfterInclude(createOpenPricingSidebar.getInventoryMultipane()
                .getSelectTableItemByPositionInList(0));

        validateIncludedItem(createOpenPricingSidebar.getInventoryMultipane()
                .getIncludedExcludedTableItemByPositionInList(0), expectedAdSpot.getName(), VIBER_INVENTORY_CHILD_TYPE);
        validateParentLabelInIncludedItem(createPrivateAuctionSidebar.getInventoryMultipane()

                .getIncludedExcludedTableItemByPositionInList(0), expectedMedia.getName());
    }

    @Step("Expand Multipane Component for Inventory for Protection sidebar")
    @Test(priority = 13)
    public void expandMultipaneTestInProtectionSidebar() {
        openInventoryMultipaneInSidebar(Path.CREATE_PROTECTION);

        testStart()
                .and("Select Publisher")
                .waitAndValidate(appear, createProtectionSidebar.getPublisherInput())
                .selectFromDropdown(createProtectionSidebar.getPublisherNameDropdown(),
                        createProtectionSidebar.getPublisherItems(), VIBER_PUBLISHER_NAME)
                .clickOnWebElement(createProtectionSidebar.getInventoryMultipane().getPanelNameLabel())
                .testEnd();
    }

    @Step("Check Parent item state in Multipane Component in Protection sidebar")
    @Test(priority = 14)
    public void checkParentItemStateInProtectionMultipaneTest() {
        //Check Inventory Selection
        validateParentInventoryToSelect(createProtectionSidebar.getInventoryMultipane()
                .getSelectTableItemByName(expectedMedia.getName()));
    }

    @Test(priority = 15)
    @Step("Check Child item state in Multipane Component in Protection sidebar")
    public void checkChildItemStateInOpenProtectionMultipaneTest() {
        //Check Inventory Selection
        selectChildItemFromParent(createProtectionSidebar.getInventoryMultipane()
                .getSelectTableItemByName(expectedMedia.getName()));

        validateChildInventoryToSelect(createProtectionSidebar.getInventoryMultipane()
                .getSelectChildTableItemByPositionInList(0));
    }

    @Test(priority = 16)
    @Step("Search Inventory from Multipane Component in Protection sidebar")
    public void checkSearchItemInProtectionMultipaneTest() {
        validateDefaultSettingsOfSearchItem(createProtectionSidebar.getInventoryMultipane()
                .getSelectTableItemByPositionInList(0));
    }

    @Test(priority = 17)
    @Step("Select Child Item as Included from Search result list from Multipane Component in Protection sidebar")
    public void selectChildMultipaneItemAsIncludedInProtectionTest() {
        validateItemToSelectAfterInclude(createProtectionSidebar.getInventoryMultipane()
                .getSelectTableItemByPositionInList(0));

        validateIncludedItem(createProtectionSidebar.getInventoryMultipane()
                .getIncludedExcludedTableItemByPositionInList(0), expectedAdSpot.getName(), VIBER_INVENTORY_CHILD_TYPE);
        validateParentLabelInIncludedItem(createPrivateAuctionSidebar.getInventoryMultipane()

                .getIncludedExcludedTableItemByPositionInList(0), expectedMedia.getName());
    }

    @Test(priority = 18)
    @Step("Remove Child Inventory Item from Excluded Items list from Multipane Component in Protection sidebar")
    public void removeChildMultipaneItemAsIncludedInProtectionTest() {
        removeIncludedItemFromList(createProtectionSidebar.getInventoryMultipane()
                .getIncludedExcludedTableItemByPositionInList(0));

        validateEmptyIncludedExcludedList();

        selectChildItemFromParent(createProtectionSidebar.getInventoryMultipane()
                .getSelectTableItemByName(expectedMedia.getName()));

        validateChildInventoryToSelect(createProtectionSidebar.getInventoryMultipane()
                .getSelectChildTableItemByPositionInList(0));
    }

    @Test(priority = 19)
    @Step("Search Inventory from Multipane Component")
    public void checkSearchItemInInventoryMultipaneInProtectionSidebarTest() {
        validateDefaultSettingsOfSearchItem(createPrivateAuctionSidebar.getInventoryMultipane()
                .getSelectTableItemByPositionInList(0));
    }

    @Test(priority = 20)
    @Step("Select Child Item as Excluded from Search result list from Multipane Component in Protection sidebar")
    public void selectChildMultipaneItemAsExcludedTest() {
        validateItemToSelectAfterExclude(createProtectionSidebar.getInventoryMultipane()
                .getSelectTableItemByPositionInList(0));


        validateExcludedItem(createProtectionSidebar.getInventoryMultipane()
                .getIncludedExcludedTableItemByPositionInList(0), expectedAdSpot.getName(), VIBER_INVENTORY_CHILD_TYPE);
        validateParentLabelInExcludedItem(createProtectionSidebar.getInventoryMultipane()

                .getIncludedExcludedTableItemByPositionInList(0), expectedMedia.getName());
    }

    @Test(priority = 21)
    @Step("Remove Child Inventory Item from Excluded Items list from Multipane Component in Protection sidebar")
    public void removeChildMultipaneItemAsExcludedTest() {
        removeIncludedItemFromList(createProtectionSidebar.getInventoryMultipane()
                .getIncludedExcludedTableItemByPositionInList(0));

        validateEmptyIncludedExcludedList();

        selectChildItemFromParent(createProtectionSidebar.getInventoryMultipane()
                .getSelectTableItemByName(expectedMedia.getName()));

        validateChildInventoryToSelect(createProtectionSidebar.getInventoryMultipane()
                .getSelectChildTableItemByPositionInList(0));
    }

    @Test(priority = 22)
    @Step("Select Parent Item as Excluded from Search result list from Multipane Component in Protection sidebar")
    public void selectParentMultipaneItemAsExcludedTest() {
        validateItemToSelectAfterExclude(createProtectionSidebar.getInventoryMultipane()
                .getSelectTableItemByName(expectedMedia.getName()));

        validateExcludedItem(createProtectionSidebar.getInventoryMultipane()
                .getIncludedExcludedTableItemByPositionInList(0), expectedMedia.getName(), VIBER_INVENTORY_PARENT_TYPE);
    }

    @Test(priority = 23)
    @Step("Remove Parent Inventory Item from Excluded Items list from Multipane Component")
    public void removeParentMultipaneItemAsExcludedTest() {
        removeIncludedItemFromList(createProtectionSidebar.getInventoryMultipane()
                .getIncludedExcludedTableItemByPositionInList(0));

        validateEmptyIncludedExcludedList();

        selectChildItemFromParent( createProtectionSidebar.getInventoryMultipane()
                .getSelectTableItemByName(expectedMedia.getName()));
    }

    private void openInventoryMultipaneInSidebar(Path path){

        //Expand Inventory Multipane
        testStart()
                .given(format("Open direct path %s", path.getPath()))
                .openDirectPath(path)
                .and()
                .waitSideBarOpened()
                .testEnd();
    }

    private void validateParentInventoryToSelect(SelectTableItem firstInventoryToSelect){
        //Check Inventory Selection
        testStart()
                .waiter(visible, firstInventoryToSelect.getName())
                .and("Hovering mouse on first Item in Inventory")
                .hoverMouseOnWebElement(firstInventoryToSelect.getName())
                .then("Validating that all those icons are not visible")
                .validate(not(visible), firstInventoryToSelect.getActiveIcon())
                .validate(not(visible), firstInventoryToSelect.getInactiveIcon())
                .validate(not(visible), firstInventoryToSelect.getExcludedIcon())
                .validate(not(visible), firstInventoryToSelect.getIncludedIcon())
                .validate(not(visible), firstInventoryToSelect.getNestedItemNameLabel())
                .validate(not(visible), firstInventoryToSelect.getAssociatedWithPublisherIcon())
                .then("Validating that all those icons are visible")
                .validate(visible, firstInventoryToSelect.getIncludeButton())
                .validate(visible, firstInventoryToSelect.getExpandInnerItemButton())
                .testEnd();
    }

    private void selectChildItemFromParent(SelectTableItem firstInventoryToSelect){
        testStart()
                .and("Click on 'Inner' button")
                .clickOnWebElement(firstInventoryToSelect.getExpandInnerItemButton())
                .testEnd();
    }

    private void removeIncludedItemFromList(IncludedTableItem firstIncludedInventoryToRemove){
        testStart()
                .and("Click on 'Remove' button")
                .clickOnWebElement(firstIncludedInventoryToRemove.getRemoveButton())
                .clickOnWebElementIfPresent(createPrivateAuctionSidebar.getInventoryMultipane().getClearSearchIcon())
                .testEnd();
    }

    private void validateChildInventoryToSelect(SelectChildTableItem innerInventoryToSelect){
        testStart()
                .hoverMouseOnWebElement(innerInventoryToSelect.getName())
                .then("Validating that all those icons are not visible")
                .validate(not(visible), innerInventoryToSelect.getExcludedIcon())
                .validate(not(visible), innerInventoryToSelect.getIncludedIcon())
                .validate(not(visible), innerInventoryToSelect.getActiveIcon())
                .validate(not(visible), innerInventoryToSelect.getInactiveIcon())
                .validate(not(visible), innerInventoryToSelect.getAssociatedWithPublisherIcon())
                .then("Validating that all those icons are visible")
                .validate(visible, innerInventoryToSelect.getIncludeButton())
                .then("Check inner item's values")
                .validate(innerInventoryToSelect.getName(), expectedAdSpot.getName())
                .validate(innerInventoryToSelect.getType(), VIBER_INVENTORY_CHILD_TYPE)
                .testEnd();
    }

    private void validateDefaultSettingsOfSearchItem(SelectTableItem firstInventoryToSelect){
        testStart()
                .and("Searching inner item using 'post' char")
                .setValue(createPrivateAuctionSidebar.getInventoryMultipane().getSearchInput(), "post")
                .and()
                .hoverMouseOnWebElement(firstInventoryToSelect.getName())
                .then("Validating that all those icons are not visible")
                .validate(not(visible), firstInventoryToSelect.getActiveIcon())
                .validate(not(visible), firstInventoryToSelect.getInactiveIcon())
                .validate(not(visible), firstInventoryToSelect.getAssociatedWithPublisherIcon())
                .then("Validating that all those icons are visible")
                .validate(visible, firstInventoryToSelect.getIncludeButton())
                .testEnd();
    }

    private void validateItemToSelectAfterInclude(SelectTableItem firstInventoryToSelect){
        //Check Inventory Selection
        testStart()
                .and("Select first Child item to include")
                .hoverMouseOnWebElement(firstInventoryToSelect.getName())
                .clickOnWebElement(firstInventoryToSelect.getIncludeButton())
                .then("Check item after click")
                .validate(visible, firstInventoryToSelect.getIncludedIcon())
                .validate(not(visible), firstInventoryToSelect.getExcludedIcon())
                .validate(not(visible), firstInventoryToSelect.getAssociatedWithPublisherIcon())
                .testEnd();
    }

    private void validateIncludedItem(IncludedTableItem firstIncludedInventory,
                                      String expectedItemName, String expectedItemType){
        testStart()
                .then("Check item after include")
                .validate(firstIncludedInventory.getName(),expectedItemName)
                .validateContainsText(firstIncludedInventory.getType(), expectedItemType)
                .validate(visible, firstIncludedInventory.getRemoveButton())
                .testEnd();
    }

    private void validateParentLabelInIncludedItem(IncludedTableItem firstIncludedInventory, String expectedItemName){
        testStart()
                .validateContainsText(firstIncludedInventory.getParentLabel(), expectedItemName)
                .testEnd();
    }

    private void validateItemToSelectAfterExclude(SelectTableItem firstInventoryToSelect){
        //Check Inventory Selection
        testStart()
                .and("Select first Child item to exclude")
                .hoverMouseOnWebElement(firstInventoryToSelect.getName())
                .clickOnWebElement(firstInventoryToSelect.getExcludeButton())
                .then("Check item after click")
                .validate(not(visible), firstInventoryToSelect.getIncludedIcon())
                .validate(visible, firstInventoryToSelect.getExcludedIcon())
                .validate(not(visible), firstInventoryToSelect.getAssociatedWithPublisherIcon())
                .validate(enabled, createPrivateAuctionSidebar.getInventoryMultipane().getClearAllButton())
                .testEnd();
    }

    private void validateExcludedItem(IncludedTableItem firstIncludedInventory,
                                      String expectedItemName, String expectedItemType){
        testStart()
                .then("Check item after exclude")
                .validate(firstIncludedInventory.getName(),expectedItemName)
                .validateContainsText(firstIncludedInventory.getType(), expectedItemType)
                .validate(visible, firstIncludedInventory.getRemoveButton())
                .testEnd();
    }

    private void validateParentLabelInExcludedItem(IncludedTableItem firstIncludedInventory, String expectedItemName){
        testStart()
                .validateContainsText(firstIncludedInventory.getParentLabel(), expectedItemName)
                .testEnd();
    }

    private void validateEmptyIncludedExcludedList(){
        testStart()
                .then("Check item after include/exclude")
                .validate(createPrivateAuctionSidebar.getInventoryMultipane().countIncludedExcludedTableItems(), 0)
                .validateContainsText(createPrivateAuctionSidebar.getInventoryMultipane().getItemsQuantityString(), "Media")
                .testEnd();
    }

    private AdSpot getExpectedAdSpot(){

        return AdSpotPrecondition.adSpot()
                .getAdSpotsWithFilter(Map.of("publisher_id", 4))
                .build()
                .getAdSpotsGetAllResponse()
                .getItems().stream()
                .filter(adSpot -> adSpot.getName().length() > 8)
                .min(Comparator.comparing(AdSpot::getName))
                .orElseThrow(() -> new NoSuchElementException(
                        format("There is no adSpot with id = %s found in adSpotList", expectedMedia.getId())));
    }
    private Media getExpectedMedia(){

        return MediaPrecondition.media()
                .getMediaWithFilter(Map.of("publisher_id", 4))
                .build()
                .getMediaGetAllResponse()
                .getItems().stream()
                .filter(media -> media.getId().equals(expectedAdSpot.getMediaId()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        format("There is no Media with related adSpotId = %s found in MediaList", expectedAdSpot.getMediaId())));
    }

   // @AfterClass
    public void logout(){
        testStart()
                .logOut()
                .testEnd();
    }
}
