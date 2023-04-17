package rx.protections;

import api.dto.rx.admin.publisher.Publisher;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.protections.ProtectionsPage;
import rx.BaseTest;
import rx.enums.MultipaneConstants;
import widgets.common.adSizes.AdSizesList;
import widgets.protections.sidebar.CreateProtectionSidebar;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Protections")
public class ProtectionsAdSizeMultipaneTests extends BaseTest {
    private List<AdSizesList> adSizeList;
    private ProtectionsPage protectionsPage;
    private CreateProtectionSidebar protectionSidebar;

    public ProtectionsAdSizeMultipaneTests() {
        protectionsPage = new ProtectionsPage();
        protectionSidebar = new CreateProtectionSidebar();
    }

    @BeforeClass
    private void login() {
        adSizeList = Arrays.asList(AdSizesList.values());

        testStart()
                .given()
                .openDirectPath(Path.PROTECTIONS)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, protectionsPage.getNuxtProgress())
                .and("Click 'Create Protection' button")
                .clickOnWebElement(protectionsPage.getCreateProtectionButton())
                .waitSideBarOpened()
                .testEnd();
    }

    @Test(description = "Check AdSize Items List is filled and disabled by default", priority = 0)
    private void checkDefaultAdSizeList() {
        var protectionMultipane = protectionSidebar.getAdSizeMultipane();

        testStart()
                .and("Scroll into 'Ad Size' multipane")
                .scrollIntoView(protectionSidebar.getSaveButton())
                .then("Validate Ad Size items list should not be empty")
                .validate(protectionMultipane.countSelectTableItems(), adSizeList.size())
                .validate(disabled, protectionMultipane.getSearchInput())
                .validateContainsText(protectionMultipane.getIncludeAllButton(),String.format("INCLUDE ALL\n(%s)",0))
                .testEnd();
    }

    @Test(description = "Check AdSize Items List", priority = 1)
    private void checkAdSizeList() {
        var protectionMultipane = protectionSidebar.getAdSizeMultipane();

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(protectionSidebar.getPublisherNameDropdown(),
                        protectionSidebar.getPublisherItems(), 1)
                .and("Expand 'Ad Size' multipane")
                .clickOnWebElement(protectionMultipane.getPanelNameLabel())
                .then("Validate Ad Size items list should not be empty")
                .validate(protectionMultipane.countSelectTableItems(), adSizeList.size())
                .testEnd();

        adSizeList.stream().limit(10).collect(Collectors.toList()).forEach(e -> {
            testStart()
                    .validate(visible, protectionMultipane.getSelectTableItemByName(e.getSize()).getName())
                    .testEnd();
        });
    }

    @Test(description = "Check Search AdSize", priority = 2)
    private void checkSearchAdSize() {
        var protectionMultipane = protectionSidebar.getAdSizeMultipane();

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(protectionSidebar.getPublisherNameDropdown(),
                        protectionSidebar.getPublisherItems(), 1)
                .and("Expand 'Ad Size' multipane")
                .clickOnWebElement(protectionMultipane.getPanelNameLabel())
                .and(String.format("Search Ad Size by Name = %s", adSizeList.get(0).getSize()))
                .setValueWithClean(protectionMultipane.getSearchInput(), adSizeList.get(0).getSize())
                .pressEnterKey(protectionMultipane.getSearchInput())
                .then("Validate item list includes Ad Size")
                .validateContainsText(protectionMultipane.getItemsQuantityString(),"(1)")
                .validate(protectionMultipane.countSelectTableItems(), 1)
                .validate(protectionMultipane.getSelectTableItemByPositionInList(0).getName(), adSizeList.get(0).getSize())
                .and("Clear Search field")
                .setValueWithClean(protectionMultipane.getSearchInput(), "")
                .pressEnterKey(protectionMultipane.getSearchInput())
                .and("Validate size of the filtered items list")
                .validate(protectionMultipane.countSelectTableItems(), adSizeList.size())
                .testEnd();

        adSizeList.stream().limit(10).collect(Collectors.toList()).forEach(e -> {
            testStart()
                    .validate(visible, protectionMultipane.getSelectTableItemByName(e.getSize()).getName())
                    .testEnd();
        });
    }

    @Test(description = "Check Multipane Text 'Include All'", priority = 3)
    private void checkMultipaneText() {
        var protectionMultipane = protectionSidebar.getAdSizeMultipane();

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(protectionSidebar.getPublisherNameDropdown(),
                        protectionSidebar.getPublisherItems(), 1)
                .and("Expand 'Ad Size' multipane")
                .clickOnWebElement(protectionMultipane.getPanelNameLabel())
                .then("Validate text above items panel")
                .validate(protectionMultipane.getSelectionInfoExcludedLabel().getText(), "")
                .and("Include All media")
                .clickOnWebElement(protectionMultipane.getIncludeAllButton())
                .then("Validate 'Selected' items panel")
                .validate(protectionMultipane.countSelectTableItems(), protectionMultipane.countIncludedExcludedTableItems())
                .then("Validate text above items panel")
                .validate(protectionMultipane.getSelectionInfoExcludedLabel().getText(),
                        MultipaneConstants.AD_SIZES_ARE_INCLUDED.setQuantity(adSizeList.size()))
                .testEnd();
    }

    @Test(description = "Check Multipane Text 'Clear All'", priority = 4)
    private void checkMultipaneTextClearAll() {
        var protectionMultipane = protectionSidebar.getAdSizeMultipane();

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(protectionSidebar.getPublisherNameDropdown(),
                        protectionSidebar.getPublisherItems(), 1)
                .and("Expand 'Ad Size' multipane")
                .clickOnWebElement(protectionMultipane.getPanelNameLabel())
                .and("Include All Ad Sizes")
                .clickOnWebElement(protectionMultipane.getIncludeAllButton())
                .validate(protectionMultipane.countIncludedExcludedTableItems(), adSizeList.size())
                .validate(protectionMultipane.countSelectTableItems(), adSizeList.size())
                .and("Clear All Ad Sizes")
                .clickOnWebElement(protectionMultipane.getClearAllButton())
                .then("Validate text above items panel")
                .validate(protectionMultipane.getSelectionInfoExcludedLabel().getText(), MultipaneConstants.ALL_AD_SIZES_ARE_INCLUDED_DETAILS.setQuantity())
                .validate(protectionMultipane.countSelectTableItems(), adSizeList.size())
                .validate(protectionMultipane.countIncludedExcludedTableItems(), 0)
                .testEnd();
    }

    @Test(description = "Check Multipane Text (include not all items)", priority = 4)
    private void checkMultipaneTextNotAllItems() {
        var protectionMultipane = protectionSidebar.getAdSizeMultipane();

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(protectionSidebar.getPublisherNameDropdown(),
                        protectionSidebar.getPublisherItems(), 1)
                .and("Expand 'Ad Size' multipane")
                .clickOnWebElement(protectionMultipane.getPanelNameLabel())
                .testEnd();

        includeOneByOneItems(adSizeList);
    }

    @Test(description = "Check Multipane Text (exclude not all items)", priority = 5)
    private void checkMultipaneTextExcludeNotAllItems() {
        var protectionMultipane = protectionSidebar.getAdSizeMultipane();

        var filterdAdSizes = adSizeList.stream()
                .filter(e -> e.getSize().contains("1")).collect(Collectors.toList());

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(protectionSidebar.getPublisherNameDropdown(),
                        protectionSidebar.getPublisherItems(), 1)
                .and("Expand 'Adsize' multipane")
                .clickOnWebElement(protectionMultipane.getPanelNameLabel())
                .and("Clear All AdSizes")
                .clickOnWebElement(protectionMultipane.getClearAllButton())
                .validate(protectionMultipane.getSelectionInfoExcludedLabel().getText(), MultipaneConstants.ALL_AD_SIZES_ARE_INCLUDED_DETAILS.setQuantity())
                .validate(protectionMultipane.countSelectTableItems(), adSizeList.size())
                .validate(protectionMultipane.countIncludedExcludedTableItems(), 0)
                .and(String.format("Search Ad Size by Name = 1"))
                .setValueWithClean(protectionMultipane.getSearchInput(), "1")
                .pressEnterKey(protectionMultipane.getSearchInput())
                .and("Include all filtered items")
                .validate(protectionMultipane.countSelectTableItems(), filterdAdSizes.size())
                .clickOnWebElement(protectionMultipane.getIncludeAllButton())
                .testEnd();

        excludeIncludedItemsOneByOne(filterdAdSizes);
    }

    @Test(description = "Check Multipane Text (include not all items)", priority = 6)
    private void checkMultipaneTextExcludeAdsizes() {
        var adSizeMultipane = protectionSidebar.getAdSizeMultipane();

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(protectionSidebar.getPublisherNameDropdown(),
                        protectionSidebar.getPublisherItems(), 1)
                .and("Expand 'Ad Size' multipane")
                .clickOnWebElement(adSizeMultipane.getPanelNameLabel())
                .testEnd();

        excludeOneByOneItems(adSizeList.stream()
                .filter(e -> e.getSize().contains("1"))
                .collect(Collectors.toList()));
    }

    @Test(description = "Check Multipane Text (exclude not all items)", priority = 7, dependsOnMethods = "checkMultipaneTextExcludeAdsizes")
    private void checkMultipaneRemoveExcludedAdSizes() {
        var adSizeMultipane = protectionSidebar.getAdSizeMultipane();

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(protectionSidebar.getPublisherNameDropdown(),
                        protectionSidebar.getPublisherItems(), 1)
                .and("Expand 'Ad Size' multipane")
                .clickOnWebElement(adSizeMultipane.getPanelNameLabel())
                .testEnd();

        removeOneByOneExcludedItems(adSizeList.stream()
                .filter(e -> e.getSize().contains("1"))
                .collect(Collectors.toList()));
    }

    @AfterClass(alwaysRun = true)
    private void logout() {

        testStart()
                .clickOnWebElement(protectionSidebar.getCloseIcon())
                .waitSideBarClosed()
                .and("Logout")
                .logOut()
                .testEnd();
    }

    @Step("Include all items one by one")
    private void includeOneByOneItems(List<AdSizesList> list) {
        var protectionMultipane = protectionSidebar.getAdSizeMultipane();
        var updated = new AtomicInteger(0);

        list.forEach(e ->
        {
            var selectedItem = protectionMultipane.getSelectTableItemByPositionInList(updated.get());
            testStart()
                    .waiter(visible, selectedItem.getName())
                    .hoverMouseOnWebElement(selectedItem.getName())
                    .then()
                    .validate(not(visible), selectedItem.getExcludedIcon())
                    .validate(not(visible), selectedItem.getIncludedIcon())
                    .validate(not(visible), selectedItem.getActiveIcon())
                    .validate(not(visible), selectedItem.getInactiveIcon())
                    .validate(not(visible), selectedItem.getAssociatedWithPublisherIcon())
                    .validate(visible, selectedItem.getIncludeButton())
                    .and()
                    .clickOnWebElement(selectedItem.getIncludeButton())
                    .validate(protectionMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 0) ?
                            MultipaneConstants.ONE_AD_SIZE_IS_INCLUDED.setQuantity(updated.incrementAndGet()) :
                            MultipaneConstants.AD_SIZES_ARE_INCLUDED.setQuantity(updated.incrementAndGet()))
                    .validate(visible, selectedItem.getName())
                    .testEnd();
        });
    }

    @Step("Exclude all items one by one")
    private void excludeIncludedItemsOneByOne(List<AdSizesList> list) {
        var updated = new AtomicInteger(list.size());
        var protectionMultipane = protectionSidebar.getAdSizeMultipane();

        list.forEach(item -> {
            var selectedItem = protectionMultipane.getIncludedExcludedTableItemByName(item.getSize());

            testStart()
                    .waiter(visible, selectedItem.getName())
                    .hoverMouseOnWebElement(selectedItem.getName())
                    .then()
                    .validateContainsText(protectionMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 1) ?
                            MultipaneConstants.ONE_AD_SIZE_IS_INCLUDED.setQuantity(updated.getAndDecrement()) :
                            MultipaneConstants.AD_SIZES_ARE_INCLUDED.setQuantity(updated.getAndDecrement()))
                    .then()
                    .validate(visible, selectedItem.getRemoveButton())
                    .and(String.format("Remove selected item %s", selectedItem.getName()))
                    .clickOnWebElement(selectedItem.getRemoveButton())
                    .testEnd();
        });
    }

    @Step("Exclude all items one by one")
    private void excludeOneByOneItems(List<AdSizesList> list) {
        var adSizeMultipane = protectionSidebar.getAdSizeMultipane();
        var updated = new AtomicInteger(0);

        list.forEach(e ->
        {
            var selectedItem = adSizeMultipane.getSelectTableItemByPositionInList(updated.get());
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
                    .validate(adSizeMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 0) ?
                            MultipaneConstants.ONE_AD_SIZE_IS_EXCLUDED.setQuantity(updated.incrementAndGet()) :
                            MultipaneConstants.AD_SIZES_ARE_EXCLUDED.setQuantity(updated.incrementAndGet()))
                    .validate(exist, selectedItem.getName())
                    .testEnd();
        });
    }

    @Step("Remove all excluded items one by one")
    private void removeOneByOneExcludedItems(List<AdSizesList> list) {
        var updated = new AtomicInteger(list.size());
        var adSizeMultipane = protectionSidebar.getAdSizeMultipane();

        list.forEach(item -> {
            var selectedItem = adSizeMultipane.getIncludedExcludedTableItemByName(item.getSize());

            testStart()
                    .waiter(visible, selectedItem.getName())
                    .hoverMouseOnWebElement(selectedItem.getName())
                    .then()
                    .validateContainsText(adSizeMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 1) ?
                            MultipaneConstants.ONE_AD_SIZE_IS_EXCLUDED.setQuantity(updated.getAndDecrement()) :
                            MultipaneConstants.AD_SIZES_ARE_EXCLUDED.setQuantity(updated.getAndDecrement()))
                    .then()
                    .validate(visible, selectedItem.getRemoveButton())
                    .and(String.format("Remove selected item %s", selectedItem.getName()))
                    .clickOnWebElement(selectedItem.getRemoveButton())
                    .testEnd();
        });
    }

    @AfterMethod(alwaysRun = true)
    private void collapseMultipane(){
        var protectionMultipane = protectionSidebar.getAdSizeMultipane();

        testStart()
                .and("Collapse 'AdSize' multipane")
                .clickOnWebElement(protectionMultipane.getPanelNameLabel())
                .testEnd();
    }

    private Publisher getAllPublishersByParams(Map<String, Object> strParams) {

        return publisher()
                .getPublishersListWithFilter(strParams)
                .build()
                .getPublisherGetAllResponse()
                .getItems().stream()
                .findFirst()
                .orElseThrow(NoSuchElementException::new);

    }
}