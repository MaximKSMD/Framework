package rx.yield.openpricing;

import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.common.Currency;
import api.dto.rx.demandsource.DemandSource;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static api.preconditionbuilders.DemandSourcePrecondition.demandSource;
import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static configurations.User.USER_FOR_DELETION;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Open Pricing")
public class OpenPricingDspMultipaneTests extends BaseTest {

    private Publisher publisherActive;
    private List<DemandSource> dspList;
    private static final Currency CURRENCY = Currency.JPY;
    private OpenPricingPage openPricingPage;
    private CreateOpenPricingSidebar openPricingSidebar;

    public OpenPricingDspMultipaneTests() {

        openPricingPage = new OpenPricingPage();
        openPricingSidebar = new CreateOpenPricingSidebar();
    }

    @BeforeClass
    private void login() {
        publisherActive = createPublisher();
        dspList = getPublisherMatchingDsps(Map.of("publisher_id", publisherActive.getId()));

        testStart()
                .given()
                .openDirectPath(Path.OPEN_PRICING)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                .and("Click 'Create OpenPricing' button")
                .clickOnWebElement(openPricingPage.getCreateOpenPricingButton())
                .waitSideBarOpened()
                .testEnd();
    }

    @Test(description = "Check Dsp Items List is filled and disabled by default", priority = 0)
    private void checkDefaultDspList() {
        var openPricingMultipane = openPricingSidebar.getDemandSourcesMultipane();

        testStart()
                .and("Expand 'Dsp' multipane")
                .clickOnWebElement(openPricingMultipane.getPanelNameLabel())
                .then("Validate Dsp items list should not be empty")
                .validate(openPricingMultipane.countSelectTableItems(), 0)
                .validate(disabled, openPricingMultipane.getSearchInput())
                .validateContainsText(openPricingMultipane.getIncludeAllButton(),String.format("INCLUDE ALL\n(%s)",0))
                .testEnd();
    }

    @Test(description = "Check Dsp Items List", priority = 1)
    private void checkDspList() {
        var openPricingMultipane = openPricingSidebar.getDemandSourcesMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherInput(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'Dsp' multipane")
                .clickOnWebElement(openPricingMultipane.getPanelNameLabel())
                .then("Validate Dsp items list should not be empty")
                .validate(openPricingMultipane.countSelectTableItems(), dspList.size())
                .validate(openPricingMultipane.getSelectTableItemByName(dspList.get(0).getName()).getActiveIcon())
                .validate(openPricingMultipane.getSelectTableItemByName(dspList.get(0).getName()).getAssociatedWithPublisherIcon())
                .testEnd();

        dspList.stream().limit(10).collect(Collectors.toList()).forEach(e -> {
            testStart()
                    .validate(visible, openPricingMultipane.getSelectTableItemByName(e.getName()).getName())
                    .testEnd();
        });
    }

    @Test(description = "Check Search Dsp", priority = 2)
    private void checkSearchDsp() {
        var openPricingMultipane = openPricingSidebar.getDemandSourcesMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherInput(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'Dsp' multipane")
                .clickOnWebElement(openPricingMultipane.getPanelNameLabel())
                .and(String.format("Search Dsp by Name = %s", dspList.get(0).getName()))
                .setValueWithClean(openPricingMultipane.getSearchInput(), dspList.get(0).getName())
                .pressEnterKey(openPricingMultipane.getSearchInput())
                .then("Validate Active/Associated Publisher link icon")
                .validate(openPricingMultipane.getSelectTableItemByName(dspList.get(0).getName()).getActiveIcon())
                .validate(openPricingMultipane.getSelectTableItemByName(dspList.get(0).getName()).getAssociatedWithPublisherIcon())
                .and("Validate item list includes Dsp")
                .validateContainsText(openPricingMultipane.getItemsQuantityString(),"(1)")
                .validate(openPricingMultipane.countSelectTableItems(), 1)
                .validate(openPricingMultipane.getSelectTableItemByPositionInList(0).getName(), dspList.get(0).getName())
                .and("Clear Search field")
                .setValueWithClean(openPricingMultipane.getSearchInput(), "")
                .and("Click Enter button")
                .pressEnterKey(openPricingMultipane.getSearchInput())
                .and("Validate size of the filtered items list")
                .validate(openPricingMultipane.countSelectTableItems(), dspList.size())
                .testEnd();

        dspList.stream().limit(10).collect(Collectors.toList()).forEach(e -> {
            testStart()
                    .validate(visible, openPricingMultipane.getSelectTableItemByName(e.getName()).getName())
                    .testEnd();
        });

    }

    @Test(description = "Check Multipane Text 'Include All'", priority = 3)
    private void checkMultipaneText() {
        var openPricingMultipane = openPricingSidebar.getDemandSourcesMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherInput(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'Dsp' multipane")
                .clickOnWebElement(openPricingMultipane.getPanelNameLabel())
                .then("Validate Active/Associated Publisher link icon")
                .validate(openPricingMultipane.getSelectTableItemByName(dspList.get(0).getName()).getActiveIcon())
                .validate(openPricingMultipane.getSelectTableItemByName(dspList.get(0).getName()).getAssociatedWithPublisherIcon())
                .and("Validate text above items panel")
                .validate(openPricingMultipane.getSelectionInfoExcludedLabel().getText(), "")
                .and("Include All media")
                .clickOnWebElement(openPricingMultipane.getIncludeAllButton())
                .then("Validate 'Selected' items panel")
                .validate(openPricingMultipane.countSelectTableItems(), openPricingMultipane.countIncludedExcludedTableItems())
                .then("Validate text above items panel")
                .validate(openPricingMultipane.getSelectionInfoExcludedLabel().getText(),
                        MultipaneConstants.DEMAND_SOURCES_ARE_INCLUDED.setQuantity(dspList.size()))
                .testEnd();
    }

    @Test(description = "Check Multipane Text 'Clear All'", priority = 4)
    private void checkMultipaneTextClearAll() {
        var openPricingMultipane = openPricingSidebar.getDemandSourcesMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherInput(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'Dsp' multipane")
                .clickOnWebElement(openPricingMultipane.getPanelNameLabel())
                .and("Include All Dsps")
                .clickOnWebElement(openPricingMultipane.getIncludeAllButton())
                .validate(openPricingMultipane.countIncludedExcludedTableItems(), dspList.size())
                .validate(openPricingMultipane.countSelectTableItems(), dspList.size())
                .and("Clear All Dsps")
                .clickOnWebElement(openPricingMultipane.getClearAllButton())
                .then("Validate text above items panel")
                .validate(openPricingMultipane.getSelectionInfoExcludedLabel().getText(), MultipaneConstants.ALL_DEMAND_SOURCES_ARE_INCLUDED.setQuantity())
                .then("Validate Active/Associated Publisher link icon")
                .validate(openPricingMultipane.getSelectTableItemByName(dspList.get(0).getName()).getActiveIcon())
                .validate(openPricingMultipane.getSelectTableItemByName(dspList.get(0).getName()).getAssociatedWithPublisherIcon())
                .and("Validate text above items panel")
                .validate(openPricingMultipane.getSelectionInfoExcludedLabel().getText(), MultipaneConstants.ALL_DEMAND_SOURCES_ARE_INCLUDED.setQuantity())
                .validate(openPricingMultipane.countSelectTableItems(), dspList.size())
                .validate(openPricingMultipane.countIncludedExcludedTableItems(), 0)
                .testEnd();
    }

    @Test(description = "Check Multipane Text (include not all items)", priority = 4)
    private void checkMultipaneTextNotAllItems() {
        var openPricingMultipane = openPricingSidebar.getDemandSourcesMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherInput(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'Dsp' multipane")
                .clickOnWebElement(openPricingMultipane.getPanelNameLabel())
                .then("Validate Active/Associated Publisher link icon")
                .validate(openPricingMultipane.getSelectTableItemByName(dspList.get(0).getName()).getActiveIcon())
                .validate(openPricingMultipane.getSelectTableItemByName(dspList.get(0).getName()).getAssociatedWithPublisherIcon())
                .testEnd();

        includeOneByOneItems(dspList);
    }

    @Test(description = "Check Multipane Text (exclude not all items)", priority = 5)
    private void checkMultipaneTextExcludeNotAllItems() {
        var openPricingMultipane = openPricingSidebar.getDemandSourcesMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherInput(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherInput(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'Dsp' multipane")
                .clickOnWebElement(openPricingMultipane.getPanelNameLabel())
                .then("Validate Active/Associated Publisher link icon")
                .validate(openPricingMultipane.getSelectTableItemByName(dspList.get(0).getName()).getActiveIcon())
                .validate(openPricingMultipane.getSelectTableItemByName(dspList.get(0).getName()).getAssociatedWithPublisherIcon())
                .and("Clear All Dsps")
                .clickOnWebElement(openPricingMultipane.getClearAllButton())
                .validate(openPricingMultipane.getSelectionInfoExcludedLabel().getText(), MultipaneConstants.ALL_DEMAND_SOURCES_ARE_INCLUDED.setQuantity())
                .validate(openPricingMultipane.countSelectTableItems(), dspList.size())
                .validate(openPricingMultipane.countIncludedExcludedTableItems(), 0)
                .and("Include all dsp items")
                .validate(openPricingMultipane.countSelectTableItems(), dspList.size())
                .clickOnWebElement(openPricingMultipane.getIncludeAllButton())
                .testEnd();

        excludeOneByOneItems(dspList);
    }

    private Publisher createPublisher() {

        return publisher()
                .createNewPublisher(captionWithSuffix("000000autoPub1"), true, CURRENCY,
                        Collections.EMPTY_LIST, List.of(1, 2, 3, 4, 5, 6, 7, 8))
                .build()
                .getPublisherResponse();
    }

    @AfterClass(alwaysRun = true)
    private void logout() {

        testStart()
                .clickOnWebElement(openPricingSidebar.getCloseIcon())
                .waitSideBarClosed()
                .and("Logout")
                .logOut()
                .testEnd();

        deletePublisher(publisherActive.getId());
    }

    private List<DemandSource> getPublisherMatchingDsps(Map<String, Object> queryParams) {

        return demandSource()
                .getDSPsWithFilter(queryParams)
                .build()
                .getDemandSourceGetAllResponse()
                .getItems();
    }

    @Step("Include all items one by one")
    private void includeOneByOneItems(List<DemandSource> list) {
        var openPricingMultipane = openPricingSidebar.getDemandSourcesMultipane();
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
                        .validate(visible, selectedItem.getActiveIcon())
                        .validate(not(visible), selectedItem.getInactiveIcon())
                        .validate(visible, selectedItem.getAssociatedWithPublisherIcon())
                        .validate(visible, selectedItem.getIncludeButton())
                        .and()
                        .clickOnWebElement(selectedItem.getIncludeButton())
                        .validate(openPricingMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 0) ?
                                MultipaneConstants.ONE_DEMAND_SOURCE_IS_INCLUDED.setQuantity(updated.incrementAndGet()) :
                                MultipaneConstants.DEMAND_SOURCES_ARE_INCLUDED.setQuantity(updated.incrementAndGet()))
                        .validate(visible, selectedItem.getName())
                        .testEnd();
            });
    }

    @Step("Exclude all items one by one")
    private void excludeOneByOneItems(List<DemandSource> list) {
        var updated = new AtomicInteger(list.size());
        var openPricingMultipane = openPricingSidebar.getDemandSourcesMultipane();

        list.forEach(item -> {
            var selectedItem = openPricingMultipane.getIncludedExcludedTableItemByName(item.getName());

            testStart()
                    .waiter(visible, selectedItem.getName())
                    .hoverMouseOnWebElement(selectedItem.getName())
                    .then()
                    .validateContainsText(openPricingMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 1) ?
                            MultipaneConstants.ONE_DEMAND_SOURCE_IS_INCLUDED.setQuantity(updated.getAndDecrement()) :
                            MultipaneConstants.DEMAND_SOURCES_ARE_INCLUDED.setQuantity(updated.getAndDecrement()))
                    .then()
                    .validate(visible, selectedItem.getRemoveButton())
                    .and(String.format("Remove selected item %s", selectedItem.getName()))
                    .clickOnWebElement(selectedItem.getRemoveButton())
                    .testEnd();
        });
    }

    @AfterMethod(alwaysRun = true)
    private void collapseMultipane(){
        var openPricingMultipane = openPricingSidebar.getDemandSourcesMultipane();

        testStart()
                .and("Collapse 'Dsp' multipane")
                .clickOnWebElement(openPricingMultipane.getPanelNameLabel())
                .testEnd();
    }

    private void deletePublisher(Integer id) {
        publisher()
                .setCredentials(USER_FOR_DELETION)
                .deletePublisher(id)
                .build();
    }
}