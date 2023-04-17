package rx.yield.openpricing;

import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.adsize.AdSize;
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
import java.util.concurrent.atomic.AtomicInteger;

import static api.preconditionbuilders.AdSizePrecondition.adSize;
import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static configurations.User.USER_FOR_DELETION;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Open Pricing")
public class OpenPricingAdSizeMultipaneTests extends BaseTest {

    private Publisher publisherEmpty;
    private Publisher publisherActive;
    private Publisher publisherInactive;
    private List<AdSize> adSizeList;

    private OpenPricingPage openPricingPage;
    private CreateOpenPricingSidebar openPricingSideBar;

    public OpenPricingAdSizeMultipaneTests() {

        openPricingPage = new OpenPricingPage();
        openPricingSideBar = new CreateOpenPricingSidebar();
    }

    @BeforeClass
    private void login() {

        publisherEmpty = createPublisher(captionWithSuffix("00autoPubOpenPricing_empty"), true);
        publisherActive = createPublisher(captionWithSuffix("00autoPubPOpenPricing_active"), true);
        publisherInactive = createPublisher(captionWithSuffix("00autoPubOpenPricing_inactive"), false);

        adSizeList = getAdSizes();

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

    @Test(description = "Check ad size Items List is filled and disabled by default")
    private void checkDefaultAdSizeList() {
        var adSizeMultipane = openPricingSideBar.getAdSizeMultipane();

        testStart()
                .and("Expand 'ad size' multipane")
                .clickOnWebElement(adSizeMultipane.getPanelNameLabel())
                .then("Validate ad size items list should not be empty")
                .validate(adSizeMultipane.countSelectTableItems(), adSizeList.size())
                .waitAndValidate(exist, adSizeMultipane.getSearchInput())
                .validateContainsText(adSizeMultipane.getIncludeAllButton(), ("INCLUDE ALL"))
                .testEnd();
    }

    @Test(description = "Check ad size Items List")
    private void checkAdSizeList() {
        var adSizeMultipane = openPricingSideBar.getAdSizeMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSideBar.getPublisherInput(),
                        openPricingSideBar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'ad size' multipane")
                .clickOnWebElement(adSizeMultipane.getPanelNameLabel())
                .then("Validate ad size items list should not be empty")
                .validate(adSizeMultipane.countSelectTableItems(), adSizeList.size())
                .waitAndValidate(exist, adSizeMultipane.getSearchInput())

                .testEnd();

        adSizeList.forEach(e -> {
            testStart()
                    .waitAndValidate(exist, adSizeMultipane.getSearchInput())
                    .validate(visible, adSizeConverter(e))
                    .testEnd();
        });
    }

    @Test(description = "Check Search ad size", priority = 2)
    private void checkSearchAdSize() {
        var adSizeMultipane = openPricingSideBar.getAdSizeMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSideBar.getPublisherInput(),
                        openPricingSideBar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'ad size' multipane")
                .clickOnWebElement(adSizeMultipane.getPanelNameLabel())
                .and(String.format("Search ad size by Name = %s", adSizeList.get(0).getName()))
                .setValueWithClean(adSizeMultipane.getSearchInput(), adSizeConverter(adSizeList.get(0)))
                .pressEnterKey(adSizeMultipane.getSearchInput())
                .then("Validate item list includes ad size")
                .waitAndValidate(visible, adSizeMultipane.getSearchInput())
                .validateContainsText(adSizeMultipane.getItemsQuantityString(), "(1)")
                .waitAndValidate(exist, adSizeMultipane.getSearchInput())
                .validate(adSizeMultipane.countSelectTableItems(), 1)
                .waitAndValidate(exist, adSizeMultipane.getSearchInput())
                .validate(adSizeMultipane.getSelectTableItemByPositionInList(0).getName(), adSizeConverter(adSizeList.get(0)))
                .and("Clear Search field")
                .setValueWithClean(adSizeMultipane.getSearchInput(), "")
                .pressEnterKey(adSizeMultipane.getSearchInput())
                .waitAndValidate(visible, adSizeMultipane.getSearchInput())
                .waitAndValidate(visible, adSizeMultipane.getSearchInput())
                .validate(adSizeMultipane.countSelectTableItems(), adSizeList.size())
                .testEnd();

        adSizeList.forEach(e -> {
            testStart()
                    .validate(visible, adSizeConverter(e))
                    .testEnd();
        });
    }


    @Test(description = "Check Multipane Text 'Include All'", priority = 3)
    private void checkMultipaneText() {
        var adSizeMultipane = openPricingSideBar.getAdSizeMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSideBar.getPublisherInput(),
                        openPricingSideBar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'ad size' multipane")
                .clickOnWebElement(adSizeMultipane.getPanelNameLabel())
                .then("Validate text above items panel")
                .validate(adSizeMultipane.getSelectionInfoExcludedLabel().getText(),
                        "")
                .and("Include All media")
                .clickOnWebElement(adSizeMultipane.getIncludeAllButton())
                .then("Validate 'Selected' items panel")
                .waitAndValidate(exist, adSizeMultipane.getSearchInput())
                .validate(adSizeMultipane.countSelectTableItems(), adSizeMultipane.countIncludedExcludedTableItems())
                .then("Validate text above items panel")
                .waitAndValidate(visible, adSizeMultipane.getSearchInput())
                .validate(adSizeMultipane.getSelectionInfoExcludedLabel().getText(),
                        MultipaneConstants.AD_SIZES_ARE_INCLUDED.setQuantity(adSizeList.size()))
                .testEnd();
    }

    @Test(description = "Check Multipane Text 'Clear All'", priority = 4)
    private void checkMultipaneTextClearAll() {
        var adSizeMultipane = openPricingSideBar.getAdSizeMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSideBar.getPublisherInput(),
                        openPricingSideBar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'ad size' multipane")
                .clickOnWebElement(adSizeMultipane.getPanelNameLabel())
                .and("Include All ad size")
                .clickOnWebElement(adSizeMultipane.getIncludeAllButton())
                .waitAndValidate(visible, adSizeMultipane.getSearchInput())
                .validate(adSizeMultipane.countIncludedExcludedTableItems(), adSizeList.size())
                .validate(adSizeMultipane.countSelectTableItems(), adSizeList.size())
                .and("Clear All ad size")
                .clickOnWebElement(adSizeMultipane.getClearAllButton())
                .then("Validate text above items panel")
                .waitAndValidate(exist, adSizeMultipane.getSearchInput())
                .validate(adSizeMultipane.getSelectionInfoExcludedLabel().getText(), MultipaneConstants.ALL_AD_SIZES_ARE_INCLUDED.setQuantity())
                .validate(adSizeMultipane.countSelectTableItems(), adSizeList.size())
                .validate(adSizeMultipane.countIncludedExcludedTableItems(), 0)
                .testEnd();
    }

    @Test(description = "Check Multipane Text (include not all items)", priority = 4)
    private void checkMultipaneTextNotAllItems() {
        var adSizeMultipane = openPricingSideBar.getAdSizeMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSideBar.getPublisherInput(),
                        openPricingSideBar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'ad size' multipane")
                .clickOnWebElement(adSizeMultipane.getPanelNameLabel())
                .testEnd();

        includeOneByOneItems(adSizeList);
    }

    @Test(description = "Check Multipane Text (exclude not all items)", priority = 5)
    private void checkMultipaneTextExcludeNotAllItems() {
        var adSizeMultipane = openPricingSideBar.getAdSizeMultipane();


        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSideBar.getPublisherInput(),
                        openPricingSideBar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'ad size' multipane")
                .clickOnWebElement(adSizeMultipane.getPanelNameLabel())
                .and("Include All ad size")
                .clickOnWebElement(adSizeMultipane.getIncludeAllButton())
                .testEnd();

        excludeOneByOneItems(adSizeList);
    }


    @AfterClass(alwaysRun = true)
    private void logout() {

        testStart()
                .clickOnWebElement(openPricingSideBar.getCloseIcon())
                .waitSideBarClosed()
                .and("Logout")
                .logOut()
                .testEnd();

        deleteTestData();
    }

    private Publisher createPublisher(String name, Boolean isEnabled) {

        return publisher()
                .createNewPublisher(name, isEnabled)
                .build()
                .getPublisherResponse();
    }

    private List<AdSize> getAdSizes() {

        return adSize()
                .getAllAdSizesList()
                .build()
                .getAdSizesGetAllResponse()
                .getItems();
    }

    @Step("Include all items one by one")
    private void includeOneByOneItems(List<AdSize> list) {
        var adSizeMultipane = openPricingSideBar.getAdSizeMultipane();
        var updated = new AtomicInteger(0);

        list.forEach(e ->
        {
            var selectedItem = adSizeMultipane.getSelectTableItemByPositionInList(updated.get());
            testStart()
                    .waiter(visible, selectedItem.getName())
                    .hoverMouseOnWebElement(selectedItem.getName())
                    .then()
                    .validate(not(exist), selectedItem.getExcludedIcon())
                    .validate(not(visible), selectedItem.getIncludedIcon())
                    .validate(not(visible), selectedItem.getActiveIcon())
                    .validate(not(visible), selectedItem.getInactiveIcon())
                    .validate(not(visible), selectedItem.getAssociatedWithPublisherIcon())
                    .validate(visible, selectedItem.getIncludeButton())
                    .and()
                    .clickOnWebElement(selectedItem.getIncludeButton())
                    .validate(adSizeMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 0) ?
                            MultipaneConstants.ONE_AD_SIZE_IS_INCLUDED.setQuantity(updated.incrementAndGet()) :
                            MultipaneConstants.AD_SIZES_ARE_INCLUDED.setQuantity(updated.incrementAndGet()))
                    .validate(exist, selectedItem.getName())
                    .testEnd();
        });
    }

    @Step("Exclude all items one by one")
    private void excludeOneByOneItems(List<AdSize> list) {
        var updated = new AtomicInteger(list.size());
        var adSizeMultipane = openPricingSideBar.getAdSizeMultipane();

        list.forEach(item -> {
            var selectedItem = adSizeMultipane.getIncludedExcludedTableItemByName(adSizeConverter(item));

            testStart()
                    .waiter(visible, selectedItem.getName())
                    .hoverMouseOnWebElement(selectedItem.getName())
                    .then()
                    .validateContainsText(adSizeMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 1) ?
                            MultipaneConstants.ONE_AD_SIZE_IS_INCLUDED.setQuantity(updated.getAndDecrement()) :
                            MultipaneConstants.AD_SIZES_ARE_INCLUDED.setQuantity(updated.getAndDecrement()))
                    .then()
                    .validate(visible, selectedItem.getRemoveButton())
                    .and(String.format("Remove selected item %s", selectedItem.getName()))
                    .clickOnWebElement(selectedItem.getRemoveButton())
                    .testEnd();
        });
    }

    @AfterMethod(alwaysRun = true)
    private void collapseMultipane() {
        var adSizeMultipane = openPricingSideBar.getAdSizeMultipane();

        testStart()
                .and("Collapse 'ad size' multipane")
                .clickOnWebElement(adSizeMultipane.getPanelNameLabel())
                .testEnd();
    }

    private void deleteTestData() {

        deletePublisher(publisherEmpty.getId());
        deletePublisher(publisherActive.getId());
        deletePublisher(publisherInactive.getId());
    }

    private void deletePublisher(Integer id) {

        publisher()
                .setCredentials(USER_FOR_DELETION)
                .deletePublisher(id)
                .build();
    }

    private String adSizeConverter(AdSize item) {
        return item.getName().contains("In-Feed Native") ? item.getName() : item.getName()
                .substring(item.getName().indexOf("(") + 1, item.getName().indexOf(")"));
    }
}
