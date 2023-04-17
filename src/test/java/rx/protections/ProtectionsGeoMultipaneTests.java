package rx.protections;

import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.geo.Geo;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.protections.ProtectionsPage;
import rx.BaseTest;
import rx.enums.MultipaneConstants;
import widgets.protections.sidebar.CreateProtectionSidebar;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static api.preconditionbuilders.GeoPrecondition.geo;
import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Protections")
public class ProtectionsGeoMultipaneTests extends BaseTest {
    private List<Geo> geoList;
    private ProtectionsPage protectionsPage;
    private CreateProtectionSidebar protectionSidebar;

    public ProtectionsGeoMultipaneTests() {
        protectionsPage = new ProtectionsPage();
        protectionSidebar = new CreateProtectionSidebar();
    }

    @BeforeClass
    private void login() {
        geoList = getGeos();

        testStart()
                .given()
                .openDirectPath(Path.PROTECTIONS)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, protectionsPage.getNuxtProgress())
                .and("Press 'Create Protection' button")
                .clickOnWebElement(protectionsPage.getCreateProtectionButton())
                .waitSideBarOpened()
                .testEnd();
    }

    @Test(description = "Check Geo Items List is filled and disabled by default", priority = 0)
    private void checkDefaultGeoList() {
        var protectionMultipane = protectionSidebar.getGeoMultipane();

        testStart()
                .then("Validate Geo items list should not be empty")
                .validate(protectionMultipane.countSelectTableItems(), geoList.size())
                .validate(disabled, protectionMultipane.getSearchInput())
                .validateContainsText(protectionMultipane.getIncludeAllButton(), String.format("INCLUDE ALL\n(%s)", 0))
                .testEnd();
    }

    @Test(description = "Check Geo Items List", priority = 1)
    private void checkGeoList() {
        var protectionMultipane = protectionSidebar.getGeoMultipane();

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(protectionSidebar.getPublisherNameDropdown(),
                        protectionSidebar.getPublisherItems(), 1)
                .and("Expand 'Geo' multipane")
                .clickOnWebElement(protectionMultipane.getPanelNameLabel())
                .then("Validate Geo items list should not be empty")
                .validate(protectionMultipane.countSelectTableItems(), geoList.size())
                .testEnd();

        geoList.stream().limit(10).collect(Collectors.toList()).forEach(e -> {
            testStart()
                    .validate(visible, protectionMultipane.getSelectTableItemByName(e.getName()).getName())
                    .testEnd();
        });
    }

    @Test(description = "Check Search Geo", priority = 2)
    private void checkSearchGeo() {
        var protectionMultipane = protectionSidebar.getGeoMultipane();

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(protectionSidebar.getPublisherNameDropdown(),
                        protectionSidebar.getPublisherItems(), 1)
                .and("Expand 'Geo' multipane")
                .clickOnWebElement(protectionMultipane.getPanelNameLabel())
                .and(String.format("Search Geo by Name = %s", geoList.get(0).getName()))
                .setValueWithClean(protectionMultipane.getSearchInput(), geoList.get(0).getName())
                .pressEnterKey(protectionMultipane.getSearchInput())
                .then("Validate item list includes Geo")
                .validateContainsText(protectionMultipane.getItemsQuantityString(), "(1)")
                .validate(protectionMultipane.countSelectTableItems(), 1)
                .validate(protectionMultipane.getSelectTableItemByPositionInList(0).getName(), geoList.get(0).getName())
                .and("Clear Search field")
                .setValueWithClean(protectionMultipane.getSearchInput(), "")
                .pressEnterKey(protectionMultipane.getSearchInput())
                .and("Validate size of the filtered items list")
                .validate(protectionMultipane.countSelectTableItems(), geoList.size())
                .testEnd();

        geoList.stream().limit(10).collect(Collectors.toList()).forEach(e -> {
            testStart()
                    .validate(visible, protectionMultipane.getSelectTableItemByName(e.getName()).getName())
                    .testEnd();
        });
    }

    @Test(description = "Check Multipane Text 'Include All'", priority = 3)
    private void checkMultipaneText() {
        var protectionMultipane = protectionSidebar.getGeoMultipane();

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(protectionSidebar.getPublisherNameDropdown(),
                        protectionSidebar.getPublisherItems(), 1)
                .and("Expand 'Geo' multipane")
                .clickOnWebElement(protectionMultipane.getPanelNameLabel())
                .then("Validate text above items panel")
                .validate(protectionMultipane.getSelectionInfoExcludedLabel().getText(),
                        "")
                .and("Include All media")
                .clickOnWebElement(protectionMultipane.getIncludeAllButton())
                .then("Validate 'Selected' items panel")
                .validate(protectionMultipane.countSelectTableItems(), protectionMultipane.countIncludedExcludedTableItems())
                .then("Validate text above items panel")
                .validate(protectionMultipane.getSelectionInfoExcludedLabel().getText(),
                        MultipaneConstants.GEOS_ARE_INCLUDED.setQuantity(geoList.size()))
                .testEnd();
    }

    @Test(description = "Check Multipane Text 'Clear All'", priority = 4)
    private void checkMultipaneTextClearAll() {
        var protectionMultipane = protectionSidebar.getGeoMultipane();

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(protectionSidebar.getPublisherNameDropdown(),
                        protectionSidebar.getPublisherItems(), 1)
                .and("Expand 'Geo' multipane")
                .clickOnWebElement(protectionMultipane.getPanelNameLabel())
                .and("Include All Geos")
                .clickOnWebElement(protectionMultipane.getIncludeAllButton())
                .validate(protectionMultipane.countIncludedExcludedTableItems(), geoList.size())
                .validate(protectionMultipane.countSelectTableItems(), geoList.size())
                .and("Clear All Geos")
                .clickOnWebElement(protectionMultipane.getClearAllButton())
                .then("Validate text above items panel")
                .validate(protectionMultipane.getSelectionInfoExcludedLabel().getText(), MultipaneConstants.ALL_GEOS_ARE_INCLUDED.setQuantity())
                .validate(protectionMultipane.countSelectTableItems(), geoList.size())
                .validate(protectionMultipane.countIncludedExcludedTableItems(), 0)
                .testEnd();
    }

    @Test(description = "Check Multipane Text (include not all items)", priority = 5)
    private void checkMultipaneTextNotAllItems() {
        var protectionMultipane = protectionSidebar.getGeoMultipane();

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(protectionSidebar.getPublisherNameDropdown(),
                        protectionSidebar.getPublisherItems(), 1)
                .and("Expand 'Geo' multipane")
                .clickOnWebElement(protectionMultipane.getPanelNameLabel())
                .testEnd();

        includeOneByOneItems(geoList.stream().limit(10).collect(Collectors.toList()));
    }

    @Test(description = "Check Multipane Text (exclude not all items)", priority = 6)
    private void checkMultipaneTextExcludeNotAllItems() {
        var protectionMultipane = protectionSidebar.getGeoMultipane();
        String substring = geoList.get(0).getName().substring(0, 2).toLowerCase();

        var filterdGeos = geoList.stream()
                .filter(e -> e.getName().toLowerCase().contains(substring))
                .collect(Collectors.toList());

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(protectionSidebar.getPublisherNameDropdown(),
                        protectionSidebar.getPublisherItems(), 1)
                .and("Expand 'Geo' multipane")
                .clickOnWebElement(protectionMultipane.getPanelNameLabel())
                .and("Clear All Geos")
                .clickOnWebElement(protectionMultipane.getClearAllButton())
                .validate(protectionMultipane.getSelectionInfoExcludedLabel().getText(), MultipaneConstants.ALL_GEOS_ARE_INCLUDED.setQuantity())
                .validate(protectionMultipane.countSelectTableItems(), geoList.size())
                .validate(protectionMultipane.countIncludedExcludedTableItems(), 0)
                .and(String.format("Search Geo by Name = %s", substring))
                .setValueWithClean(protectionMultipane.getSearchInput(), substring)
                .pressEnterKey(protectionMultipane.getSearchInput())
                .and("Include all filtered items")
                .validate(protectionMultipane.countSelectTableItems(), filterdGeos.size())
                .clickOnWebElement(protectionMultipane.getIncludeAllButton())
                .testEnd();

        excludeIncludedItemsOneByOne(filterdGeos);
    }

    @Test(description = "Check Multipane Text (include not all items)", priority = 7)
    private void checkMultipaneTextExcludeGeos() {
        var protectionMultipane = protectionSidebar.getGeoMultipane();
        String substring = geoList.get(0).getName().substring(0, 2).toLowerCase();

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(protectionSidebar.getPublisherNameDropdown(),
                        protectionSidebar.getPublisherItems(), 1)
                .and("Expand 'Geo' multipane")
                .clickOnWebElement(protectionMultipane.getPanelNameLabel())
                .testEnd();

        excludeOneByOneItems(geoList.stream()
                .filter(e -> e.getName().toLowerCase().contains(substring))
                .collect(Collectors.toList()));
    }

    @Test(description = "Check Multipane Text (exclude not all items)", priority = 8, dependsOnMethods = "checkMultipaneTextExcludeGeos")
    private void checkMultipaneRemoveExcludedGeos() {
        var protectionMultipane = protectionSidebar.getGeoMultipane();
        String substring = geoList.get(0).getName().substring(0, 2).toLowerCase();

        var filterdGeos = geoList.stream()
                .filter(e -> e.getName().toLowerCase().contains(substring))
                .collect(Collectors.toList());

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(protectionSidebar.getPublisherNameDropdown(),
                        protectionSidebar.getPublisherItems(), 1)
                .and("Expand 'Geo' multipane")
                .clickOnWebElement(protectionMultipane.getPanelNameLabel())
                .testEnd();

        removeOneByOneExcludedItems(geoList.stream()
                .filter(e -> e.getName().toLowerCase().contains(substring))
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

    private List<Geo> getGeos() {

        return geo()
                .getAllGeos()
                .build()
                .getGeoGetAllResponse()
                .getItems();
    }

    @Step("Exclude all items one by one")
    private void excludeOneByOneItems(List<Geo> list) {
        var geoMultipane = protectionSidebar.getGeoMultipane();
        var updated = new AtomicInteger(0);

        list.forEach(e ->
        {
            var selectedItem = geoMultipane.getSelectTableItemByPositionInList(updated.get());
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
                    .validate(geoMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 0) ?
                            MultipaneConstants.ONE_GEO_EXCLUDED.setQuantity(updated.incrementAndGet()) :
                            MultipaneConstants.GEOS_ARE_EXCLUDED.setQuantity(updated.incrementAndGet()))
                    .validate(exist, selectedItem.getName())
                    .testEnd();
        });
    }

    @Step("Remove all excluded items one by one")
    private void removeOneByOneExcludedItems(List<Geo> list) {
        var updated = new AtomicInteger(list.size());
        var geoMultipane = protectionSidebar.getGeoMultipane();

        list.forEach(item -> {
            var selectedItem = geoMultipane.getIncludedExcludedTableItemByName(item.getName());

            testStart()
                    .waiter(visible, selectedItem.getName())
                    .hoverMouseOnWebElement(selectedItem.getName())
                    .then()
                    .validateContainsText(geoMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 1) ?
                            MultipaneConstants.ONE_GEO_EXCLUDED.setQuantity(updated.getAndDecrement()) :
                            MultipaneConstants.GEOS_ARE_EXCLUDED.setQuantity(updated.getAndDecrement()))
                    .then()
                    .validate(visible, selectedItem.getRemoveButton())
                    .and(String.format("Remove selected item %s", selectedItem.getName()))
                    .clickOnWebElement(selectedItem.getRemoveButton())
                    .testEnd();
        });
    }

    @Step("Include all items one by one")
    private void includeOneByOneItems(List<Geo> list) {
        var protectionMultipane = protectionSidebar.getGeoMultipane();
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
                            MultipaneConstants.ONE_GEO_IS_INCLUDED.setQuantity(updated.incrementAndGet()) :
                            MultipaneConstants.GEOS_ARE_INCLUDED.setQuantity(updated.incrementAndGet()))
                    .validate(visible, selectedItem.getName())
                    .testEnd();
        });
    }

    @Step("Exclude all items one by one")
    private void excludeIncludedItemsOneByOne(List<Geo> list) {
        var updated = new AtomicInteger(list.size());
        var protectionMultipane = protectionSidebar.getGeoMultipane();

        list.forEach(item -> {
            var selectedItem = protectionMultipane.getIncludedExcludedTableItemByName(item.getName());

            testStart()
                    .waiter(visible, selectedItem.getName())
                    .hoverMouseOnWebElement(selectedItem.getName())
                    .then()
                    .validateContainsText(protectionMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 1) ?
                            MultipaneConstants.ONE_GEO_IS_INCLUDED.setQuantity(updated.getAndDecrement()) :
                            MultipaneConstants.GEOS_ARE_INCLUDED.setQuantity(updated.getAndDecrement()))
                    .then()
                    .validate(visible, selectedItem.getRemoveButton())
                    .and(String.format("Remove selected item %s", selectedItem.getName()))
                    .clickOnWebElement(selectedItem.getRemoveButton())
                    .testEnd();
        });
    }

    @AfterMethod(alwaysRun = true)
    private void collapseMultipane() {
        var protectionMultipane = protectionSidebar.getGeoMultipane();

        testStart()
                .and("Collapse 'Geo' multipane")
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