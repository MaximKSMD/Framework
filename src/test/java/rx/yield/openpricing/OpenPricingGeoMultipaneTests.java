package rx.yield.openpricing;

import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.geo.Geo;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.yield.openpricing.OpenPricingPage;
import rx.BaseTest;
import rx.enums.MultipaneConstants;
import widgets.yield.openPricing.sidebar.CreateOpenPricingSidebar;

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
@Feature(value = "Open Pricing")
public class OpenPricingGeoMultipaneTests extends BaseTest {

    private Publisher publisherActive;
    private List<Geo> geoList;
    private OpenPricingPage openPricingPage;
    private CreateOpenPricingSidebar openPricingSidebar;

    public OpenPricingGeoMultipaneTests() {

        openPricingPage = new OpenPricingPage();
        openPricingSidebar = new CreateOpenPricingSidebar();
    }

    @BeforeClass
    private void login() {
        publisherActive = getAllPublishersByParams(Map.of("sort", "created_at-desc"));
        geoList = getGeos();

        testStart()
                .given()
                .openDirectPath(Path.OPEN_PRICING)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                .and("Press 'Create OpenPricing' button")
                .clickOnWebElement(openPricingPage.getCreateOpenPricingButton())
                .waitSideBarOpened()
                .testEnd();
    }

    @Test(description = "Check Geo Items List is filled and disabled by default", priority = 0)
    private void checkDefaultGeoList() {
        var openPricingMultipane = openPricingSidebar.getGeoMultipane();

        testStart()
                .then("Validate Geo items list should not be empty")
                .validate(openPricingMultipane.countSelectTableItems(), geoList.size())
                .validate(disabled, openPricingMultipane.getSearchInput())
                .validateContainsText(openPricingMultipane.getIncludeAllButton(), String.format("INCLUDE ALL\n(%s)", 0))
                .testEnd();
    }

    @Test(description = "Check Geo Items List", priority = 1)
    private void checkGeoList() {
        var openPricingMultipane = openPricingSidebar.getGeoMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherInput(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'Geo' multipane")
                .clickOnWebElement(openPricingMultipane.getPanelNameLabel())
                .then("Validate Geo items list should not be empty")
                .validate(openPricingMultipane.countSelectTableItems(), geoList.size())
                .testEnd();

        geoList.stream().limit(10).collect(Collectors.toList()).forEach(e -> {
            testStart()
                    .validate(visible, openPricingMultipane.getSelectTableItemByName(e.getName()).getName())
                    .testEnd();
        });
    }

    @Test(description = "Check Search Geo", priority = 2)
    private void checkSearchGeo() {
        var openPricingMultipane = openPricingSidebar.getGeoMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherInput(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'Geo' multipane")
                .clickOnWebElement(openPricingMultipane.getPanelNameLabel())
                .and(String.format("Search Geo by Name = %s", geoList.get(0).getName()))
                .setValueWithClean(openPricingMultipane.getSearchInput(), geoList.get(0).getName())
                .pressEnterKey(openPricingMultipane.getSearchInput())
                .then("Validate item list includes Geo")
                .validateContainsText(openPricingMultipane.getItemsQuantityString(), "(1)")
                .validate(openPricingMultipane.countSelectTableItems(), 1)
                .validate(openPricingMultipane.getSelectTableItemByPositionInList(0).getName(), geoList.get(0).getName())
                .and("Clear Search field")
                .setValueWithClean(openPricingMultipane.getSearchInput(), "")
                .pressEnterKey(openPricingMultipane.getSearchInput())
                .and("Validate size of the filtered items list")
                .validate(openPricingMultipane.countSelectTableItems(), geoList.size())
                .testEnd();

        geoList.stream().limit(10).collect(Collectors.toList()).forEach(e -> {
            testStart()
                    .validate(visible, openPricingMultipane.getSelectTableItemByName(e.getName()).getName())
                    .testEnd();
        });
    }

    @Test(description = "Check Multipane Text 'Include All'", priority = 3)
    private void checkMultipaneText() {
        var openPricingMultipane = openPricingSidebar.getGeoMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherInput(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'Geo' multipane")
                .clickOnWebElement(openPricingMultipane.getPanelNameLabel())
                .then("Validate text above items panel")
                .validate(openPricingMultipane.getSelectionInfoExcludedLabel().getText(),
                        "")
                .and("Include All media")
                .clickOnWebElement(openPricingMultipane.getIncludeAllButton())
                .then("Validate 'Selected' items panel")
                .validate(openPricingMultipane.countSelectTableItems(), openPricingMultipane.countIncludedExcludedTableItems())
                .then("Validate text above items panel")
                .validate(openPricingMultipane.getSelectionInfoExcludedLabel().getText(),
                        MultipaneConstants.GEOS_ARE_INCLUDED.setQuantity(geoList.size()))
                .testEnd();
    }

    @Test(description = "Check Multipane Text 'Clear All'", priority = 4)
    private void checkMultipaneTextClearAll() {
        var openPricingMultipane = openPricingSidebar.getGeoMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherInput(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'Geo' multipane")
                .clickOnWebElement(openPricingMultipane.getPanelNameLabel())
                .and("Include All Geos")
                .clickOnWebElement(openPricingMultipane.getIncludeAllButton())
                .validate(openPricingMultipane.countIncludedExcludedTableItems(), geoList.size())
                .validate(openPricingMultipane.countSelectTableItems(), geoList.size())
                .and("Clear All Geos")
                .clickOnWebElement(openPricingMultipane.getClearAllButton())
                .then("Validate text above items panel")
                .validate(openPricingMultipane.getSelectionInfoExcludedLabel().getText(), MultipaneConstants.ALL_GEOS_ARE_INCLUDED.setQuantity())
                .validate(openPricingMultipane.countSelectTableItems(), geoList.size())
                .validate(openPricingMultipane.countIncludedExcludedTableItems(), 0)
                .testEnd();
    }

    @Test(description = "Check Multipane Text (include not all items)", priority = 4)
    private void checkMultipaneTextNotAllItems() {
        var openPricingMultipane = openPricingSidebar.getGeoMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherInput(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'Geo' multipane")
                .clickOnWebElement(openPricingMultipane.getPanelNameLabel())
                .testEnd();

        includeOneByOneItems(geoList.stream().limit(10).collect(Collectors.toList()));
    }

    @Test(description = "Check Multipane Text (exclude not all items)", priority = 5)
    private void checkMultipaneTextExcludeNotAllItems() {
        var openPricingMultipane = openPricingSidebar.getGeoMultipane();

        String substring = geoList.get(0).getName().substring(0, 2).toLowerCase();

        List<Geo> filterdGeos = geoList.stream()
                .filter(e -> e.getName().toLowerCase().contains(substring))
                .collect(Collectors.toList());

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherInput(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherInput(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'Geo' multipane")
                .clickOnWebElement(openPricingMultipane.getPanelNameLabel())
                .and("Clear All Geos")
                .clickOnWebElement(openPricingMultipane.getClearAllButton())
                .validate(openPricingMultipane.getSelectionInfoExcludedLabel().getText(), MultipaneConstants.ALL_GEOS_ARE_INCLUDED.setQuantity())
                .validate(openPricingMultipane.countSelectTableItems(), geoList.size())
                .validate(openPricingMultipane.countIncludedExcludedTableItems(), 0)
                .and(String.format("Search Geo by Name = %s", substring))
                .setValueWithClean(openPricingMultipane.getSearchInput(), substring)
                .pressEnterKey(openPricingMultipane.getSearchInput())
                .and("Include all filtered items")
                .validate(openPricingMultipane.countSelectTableItems(), filterdGeos.size())
                .clickOnWebElement(openPricingMultipane.getIncludeAllButton())
                .testEnd();

        excludeOneByOneItems(filterdGeos);
    }

    @AfterClass(alwaysRun = true)
    private void logout() {

        testStart()
                .clickOnWebElement(openPricingSidebar.getCloseIcon())
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

    @Step("Include all items one by one")
    private void includeOneByOneItems(List<Geo> list) {

        var openPricingMultipane = openPricingSidebar.getGeoMultipane();
        var updated = new AtomicInteger(0);

        list.forEach(e ->
        {
            var selectedItem = openPricingMultipane.getSelectTableItemByPositionInList(updated.get());
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
                    .validate(openPricingMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 0) ?
                            MultipaneConstants.ONE_GEO_IS_INCLUDED.setQuantity(updated.incrementAndGet()) :
                            MultipaneConstants.GEOS_ARE_INCLUDED.setQuantity(updated.incrementAndGet()))
                    .validate(visible, selectedItem.getName())
                    .testEnd();
        });
    }

    @Step("Exclude all items one by one")
    private void excludeOneByOneItems(List<Geo> list) {

        var updated = new AtomicInteger(list.size());
        var openPricingMultipane = openPricingSidebar.getGeoMultipane();


        list.forEach(item -> {
            var selectedItem = openPricingMultipane.getIncludedExcludedTableItemByName(item.getName());

            testStart()
                    .waiter(visible, selectedItem.getName())
                    .hoverMouseOnWebElement(selectedItem.getName())
                    .then()
                    .validateContainsText(openPricingMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 1) ?
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

        var openPricingMultipane = openPricingSidebar.getGeoMultipane();

        testStart()
                .and("Collapse 'Geo' multipane")
                .clickOnWebElement(openPricingMultipane.getPanelNameLabel())
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
