package rx.yield.openpricing;

import api.dto.rx.adformat.AdFormat;
import api.dto.rx.admin.publisher.Publisher;
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

import static api.preconditionbuilders.AdFormatPrecondition.adFormat;
import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Open Pricing")
public class OpenPricingAdFormatMultipaneTests extends BaseTest {

    private Publisher publisherActive;
    private List<AdFormat> adFormatList;
    private OpenPricingPage openPricingPage;
    private CreateOpenPricingSidebar openPricingSidebar;

    public OpenPricingAdFormatMultipaneTests() {

        openPricingPage = new OpenPricingPage();
        openPricingSidebar = new CreateOpenPricingSidebar();
    }

    @BeforeClass
    private void login() {
        publisherActive = getAllPublishersByParams(Map.of("sort","created_at-desc"));

        adFormatList = getAdFormats();

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

    @Test(description = "Check Ad Format Items List is filled and disabled by default", priority = 1)
    private void checkDefaultAdFormatList() {
        var adFormatMultipane = openPricingSidebar.getAdFormatMultipane();

        testStart()
                .then("Validate Ad Format items list should not be empty")
                .validate(adFormatMultipane.countSelectTableItems(), adFormatList.size())
                .waitAndValidate(exist, adFormatMultipane.getSearchInput())
                .validateContainsText(adFormatMultipane.getIncludeAllButton(), "INCLUDE ALL")
                .testEnd();
    }

    @Test(description = "Check Ad Format Items List" , priority = 2)
    private void checkAdFormatList() {
        var adFormatMultipane = openPricingSidebar.getAdFormatMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherInput(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'Ad Format' multipane")
                .clickOnWebElement(adFormatMultipane.getPanelNameLabel())
                .scrollIntoView(openPricingSidebar.getSaveButton())
                .then("Validate Ad Format items list should not be empty")
                .validate(adFormatMultipane.countSelectTableItems(), adFormatList.size())
                .waitAndValidate(exist, adFormatMultipane.getSearchInput())
                .testEnd();

        adFormatList.forEach(e -> {
            testStart()
                    .validate(visible, adFormatMultipane.getSelectTableItemByName(e.getName()).getName())
                    .testEnd();
        });
    }

    @Test(description = "Check Search Ad Format", priority = 3)
    private void checkSearchAdFormat() {
        var adFormatMultipane = openPricingSidebar.getAdFormatMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherInput(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'Ad Format' multipane")
                .clickOnWebElement(adFormatMultipane.getPanelNameLabel())
                .and(String.format("Search Ad Format by Name = %s", adFormatList.get(0).getName()))
                .setValueWithClean(adFormatMultipane.getSearchInput(), adFormatList.get(0).getName())
                .pressEnterKey(adFormatMultipane.getSearchInput())
                .then("Validate item list includes Ad Format")
                .waitAndValidate(visible, adFormatMultipane.getSearchInput())
                .validateContainsText(adFormatMultipane.getItemsQuantityString(), "(1)")
                .waitAndValidate(exist, adFormatMultipane.getSearchInput())
                .validate(adFormatMultipane.countSelectTableItems(), 1)
                .waitAndValidate(exist, adFormatMultipane.getSearchInput())
                .validate(adFormatMultipane.getSelectTableItemByPositionInList(0).getName(), adFormatList.get(0).getName())
                .and("Clear Search field")
                .setValueWithClean(adFormatMultipane.getSearchInput(), "")
                .pressEnterKey(adFormatMultipane.getSearchInput())
                .waitAndValidate(visible, adFormatMultipane.getSearchInput())
                .validate(adFormatMultipane.countSelectTableItems(), adFormatList.size())
                .testEnd();

        adFormatList.forEach(e -> {
            testStart()
                    .validate(visible, adFormatMultipane.getSelectTableItemByName(e.getName()).getName())
                    .testEnd();
        });
    }

    @Test(description = "Check Multipane Text 'Include All'", priority = 4)
    private void checkMultipaneText() {
        var adFormatMultipane = openPricingSidebar.getAdFormatMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherInput(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'Ad Format' multipane")
                .clickOnWebElement(adFormatMultipane.getPanelNameLabel())
                .then("Validate text above items panel")
                .validate(adFormatMultipane.getSelectionInfoExcludedLabel().getText(),
                        "")
                .and("Include All media")
                .clickOnWebElement(adFormatMultipane.getIncludeAllButton())
                .then("Validate 'Selected' items panel")
                .waitAndValidate(exist, adFormatMultipane.getSearchInput())
                .validate(adFormatMultipane.countSelectTableItems(), adFormatMultipane.countIncludedExcludedTableItems())
                .then("Validate text above items panel")
                .waitAndValidate(visible, adFormatMultipane.getSearchInput())
                .validate(adFormatMultipane.getSelectionInfoExcludedLabel().getText(),
                        MultipaneConstants.AD_FORMATS_ARE_INCLUDED.setQuantity(adFormatList.size()))
                .testEnd();
    }

    @Test(description = "Check Multipane Text 'Clear All'", priority = 5)
    private void checkMultipaneTextClearAll() {
        var adFormatMultipane = openPricingSidebar.getAdFormatMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherInput(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'Ad Format' multipane")
                .clickOnWebElement(adFormatMultipane.getPanelNameLabel())
                .and("Include All Ad Format")
                .clickOnWebElement(adFormatMultipane.getIncludeAllButton())
                .waitAndValidate(visible, adFormatMultipane.getSearchInput())
                .validate(adFormatMultipane.countIncludedExcludedTableItems(), adFormatList.size())
                .validate(adFormatMultipane.countSelectTableItems(), adFormatList.size())
                .and("Clear All Ad Format")
                .clickOnWebElement(adFormatMultipane.getClearAllButton())
                .then("Validate text above items panel")
                .waitAndValidate(exist, adFormatMultipane.getSearchInput())
                .validate(adFormatMultipane.getSelectionInfoExcludedLabel().getText(), MultipaneConstants.ALL_AD_FORMATS_ARE_INCLUDED.setQuantity())
                .validate(adFormatMultipane.countSelectTableItems(), adFormatList.size())
                .validate(adFormatMultipane.countIncludedExcludedTableItems(), 0)
                .testEnd();
    }

    @Test(description = "Check Multipane Text (include not all items)", priority = 6)
    private void checkMultipaneTextNotAllItems() {
        var adFormatMultipane = openPricingSidebar.getAdFormatMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherInput(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'Ad Format' multipane")
                .clickOnWebElement(adFormatMultipane.getPanelNameLabel())
                .testEnd();

        includeOneByOneItems(adFormatList);
    }

    @Test(description = "Check Multipane Text (remove not all items)", priority = 7)
    private void checkMultipaneTextRemoveNotAllItems() {
        var adFormatMultipane = openPricingSidebar.getAdFormatMultipane();


        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherInput(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'Ad Format' multipane")
                .clickOnWebElement(adFormatMultipane.getPanelNameLabel())
                .and("Include All Ad Format")
                .clickOnWebElement(adFormatMultipane.getIncludeAllButton())
                .testEnd();

        removeOneByOneIncludedItems(adFormatList);
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

    private List<AdFormat> getAdFormats() {

        return adFormat()
                .getAllAdFormatsList()
                .build()
                .getAdFormatGetAllResponse()
                .getItems();
    }

    @Step("Include all items one by one")
    private void includeOneByOneItems(List<AdFormat> list) {
        var adFormatMultipane = openPricingSidebar.getAdFormatMultipane();
        var updated = new AtomicInteger(0);

        list.forEach(e ->
        {
            var selectedItem = adFormatMultipane.getSelectTableItemByPositionInList(updated.get());
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
                    .validate(adFormatMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 0) ?
                            MultipaneConstants.ONE_AD_FORMAT_IS_INCLUDED.setQuantity(updated.incrementAndGet()) :
                            MultipaneConstants.AD_FORMATS_ARE_INCLUDED.setQuantity(updated.incrementAndGet()))
                    .validate(exist, selectedItem.getName())
                    .testEnd();
        });
    }

    @Step("Remove all included items one by one")
    private void removeOneByOneIncludedItems(List<AdFormat> list) {
        var updated = new AtomicInteger(list.size());
        var adFormatMultipane = openPricingSidebar.getAdFormatMultipane();

        list.forEach(item -> {
            var selectedItem = adFormatMultipane.getIncludedExcludedTableItemByName(item.getName());

            testStart()
                    .waiter(visible, selectedItem.getName())
                    .hoverMouseOnWebElement(selectedItem.getName())
                    .then()
                    .validateContainsText(adFormatMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 1) ?
                            MultipaneConstants.ONE_AD_FORMAT_IS_INCLUDED.setQuantity(updated.getAndDecrement()) :
                            MultipaneConstants.AD_FORMATS_ARE_INCLUDED.setQuantity(updated.getAndDecrement()))
                    .then()
                    .validate(visible, selectedItem.getRemoveButton())
                    .and(String.format("Remove selected item %s", selectedItem.getName()))
                    .clickOnWebElement(selectedItem.getRemoveButton())
                    .testEnd();
        });
    }

    @AfterMethod(alwaysRun = true)
    private void collapseMultipane() {
        var adFormatMultipane = openPricingSidebar.getAdFormatMultipane();

        testStart()
                .and("Collapse 'Ad Format' multipane")
                .clickOnWebElement(adFormatMultipane.getPanelNameLabel())
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
