package rx.protections;

import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.common.Currency;
import api.dto.rx.demandsource.DemandSource;
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
@Feature(value = "Protections")
public class ProtectionsDspMultipaneTests extends BaseTest {
    private Publisher publisherActive;
    private List<DemandSource> dspList;
    private static final Currency CURRENCY = Currency.JPY;
    private ProtectionsPage protectionsPage;
    private CreateProtectionSidebar createProtectionSidebar;

    public ProtectionsDspMultipaneTests() {

        protectionsPage = new ProtectionsPage();
        createProtectionSidebar = new CreateProtectionSidebar();
    }

    @BeforeClass
    private void login() {
        publisherActive = createPublisher();

        dspList = getPublisherMatchingDsps(Map.of("publisher_id", publisherActive.getId()));

        testStart()
                .given()
                .openDirectPath(Path.PROTECTIONS)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, protectionsPage.getNuxtProgress())
                .and("Press 'Create Protections' button")
                .clickOnWebElement(protectionsPage.getCreateProtectionButton())
                .waitSideBarOpened()
                .testEnd();
    }

    @Test(description = "Check Dsp Items List is filled and disabled by default", priority = 0)
    private void checkDefaultDspList() {
        var protectionsMultipane = createProtectionSidebar.getDemandSourcesMultipane();

        testStart()
                .scrollIntoView(createProtectionSidebar.getSaveButton())
                .then("Validate Dsp items list should not be empty")
                .validate(protectionsMultipane.countSelectTableItems(), 0)
                .validate(disabled, protectionsMultipane.getSearchInput())
                .validateContainsText(protectionsMultipane.getIncludeAllButton(),String.format("INCLUDE ALL\n(%s)",0))
                .testEnd();
    }

    @Test(description = "Check Dsp Items List", priority = 1)
    private void checkDspList() {
        var protectionsMultipane = createProtectionSidebar.getDemandSourcesMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(createProtectionSidebar.getPublisherInput(),
                        createProtectionSidebar.getPublisherItems(), publisherActive.getName())
                .and("Expand 'Dsp' multipane")
                .clickOnWebElement(protectionsMultipane.getPanelNameLabel())
                .then("Validate Dsp items list should not be empty")
                .validate(protectionsMultipane.countSelectTableItems(), dspList.size())
                .testEnd();

        dspList.stream().limit(10).collect(Collectors.toList()).forEach(e -> {
            testStart()
                    .validate(visible, protectionsMultipane.getSelectTableItemByName(e.getName()).getName())
                    .testEnd();
        });

    }

    @Test(description = "Check Search Dsp", priority = 2)
    private void checkSearchDsp() {
        var protectionsMultipane = createProtectionSidebar.getDemandSourcesMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(createProtectionSidebar.getPublisherInput(),
                        createProtectionSidebar.getPublisherItems(), publisherActive.getName())
                .and("Expand 'Dsp' multipane")
                .clickOnWebElement(protectionsMultipane.getPanelNameLabel())
                .and(String.format("Search Dsp by Name = %s", dspList.get(0).getName()))
                .setValueWithClean(protectionsMultipane.getSearchInput(), dspList.get(0).getName())
                .pressEnterKey(protectionsMultipane.getSearchInput())
                .then("Validate Active/Associated Publisher link icon")
                .validate(protectionsMultipane.getSelectTableItemByName(dspList.get(0).getName()).getActiveIcon())
                .validate(protectionsMultipane.getSelectTableItemByName(dspList.get(0).getName()).getAssociatedWithPublisherIcon())
                .and("Validate item list includes Dsp")
                .validateContainsText(protectionsMultipane.getItemsQuantityString(),"(1)")
                .validate(protectionsMultipane.countSelectTableItems(), 1)
                .validate(protectionsMultipane.getSelectTableItemByPositionInList(0).getName(), dspList.get(0).getName())
                .and("Clear Search field")
                .setValueWithClean(protectionsMultipane.getSearchInput(), "")
                .and("Click Enter button")
                .pressEnterKey(protectionsMultipane.getSearchInput())
                .and("Validate size of the filtered items list")
                .validate(protectionsMultipane.countSelectTableItems(), dspList.size())
                .testEnd();

        dspList.stream().limit(10).collect(Collectors.toList()).forEach(e -> {
            testStart()
                    .validate(visible, protectionsMultipane.getSelectTableItemByName(e.getName()).getName())
                    .testEnd();
        });

    }

    @Test(description = "Check Multipane Text 'Include All'", priority = 3)
    private void checkMultipaneText() {
        var protectionsMultipane = createProtectionSidebar.getDemandSourcesMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(createProtectionSidebar.getPublisherInput(),
                        createProtectionSidebar.getPublisherItems(), publisherActive.getName())
                .and("Expand 'Dsp' multipane")
                .clickOnWebElement(protectionsMultipane.getPanelNameLabel())
                .then("Validate Active/Associated Publisher link icon")
                .validate(protectionsMultipane.getSelectTableItemByName(dspList.get(0).getName()).getActiveIcon())
                .validate(protectionsMultipane.getSelectTableItemByName(dspList.get(0).getName()).getAssociatedWithPublisherIcon())
                .and("Validate text above items panel")
                .validate(protectionsMultipane.getSelectionInfoExcludedLabel().getText(), "")
                .and("Include All media")
                .clickOnWebElement(protectionsMultipane.getIncludeAllButton())
                .then("Validate 'Selected' items panel")
                .validate(protectionsMultipane.countSelectTableItems(), protectionsMultipane.countIncludedExcludedTableItems())
                .then("Validate text above items panel")
                .validate(protectionsMultipane.getSelectionInfoExcludedLabel().getText(),
                        MultipaneConstants.DEMAND_SOURCES_ARE_INCLUDED.setQuantity(dspList.size()))
                .testEnd();
    }

    @Test(description = "Check Multipane Text 'Clear All'", priority = 4)
    private void checkMultipaneTextClearAll() {
        var protectionsMultipane = createProtectionSidebar.getDemandSourcesMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(createProtectionSidebar.getPublisherInput(),
                        createProtectionSidebar.getPublisherItems(), publisherActive.getName())
                .and("Expand 'Dsp' multipane")
                .clickOnWebElement(protectionsMultipane.getPanelNameLabel())
                .then("Validate Active/Associated Publisher link icon")
                .validate(protectionsMultipane.getSelectTableItemByName(dspList.get(0).getName()).getActiveIcon())
                .validate(protectionsMultipane.getSelectTableItemByName(dspList.get(0).getName()).getAssociatedWithPublisherIcon())
                .when("Include All Dsps")
                .clickOnWebElement(protectionsMultipane.getIncludeAllButton())
                .validate(protectionsMultipane.countIncludedExcludedTableItems(), dspList.size())
                .validate(protectionsMultipane.countSelectTableItems(), dspList.size())
                .and("Clear All Dsps")
                .clickOnWebElement(protectionsMultipane.getClearAllButton())
                .then("Validate text above items panel")
                .validate(protectionsMultipane.getSelectionInfoExcludedLabel().getText(), MultipaneConstants.ALL_DEMAND_SOURCES_ARE_INCLUDED.setQuantity())
                .validate(protectionsMultipane.countSelectTableItems(), dspList.size())
                .validate(protectionsMultipane.countIncludedExcludedTableItems(), 0)
                .testEnd();
    }

    @Test(description = "Check Multipane Text (include not all items)", priority = 4)
    private void checkMultipaneTextNotAllItems() {
        var protectionsMultipane = createProtectionSidebar.getDemandSourcesMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(createProtectionSidebar.getPublisherInput(),
                        createProtectionSidebar.getPublisherItems(), publisherActive.getName())
                .and("Expand 'Dsp' multipane")
                .clickOnWebElement(protectionsMultipane.getPanelNameLabel())
                .then("Validate Active/Associated Publisher link icon")
                .validate(protectionsMultipane.getSelectTableItemByName(dspList.get(0).getName()).getActiveIcon())
                .validate(protectionsMultipane.getSelectTableItemByName(dspList.get(0).getName()).getAssociatedWithPublisherIcon())
                .testEnd();

        includeOneByOneItems(dspList);
    }

    @Test(description = "Check Multipane Text (exclude not all items)", priority = 5)
    private void checkMultipaneTextExcludeNotAllItems() {
        var protectionsMultipane = createProtectionSidebar.getDemandSourcesMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(createProtectionSidebar.getPublisherInput(),
                        createProtectionSidebar.getPublisherItems(), publisherActive.getName())
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(createProtectionSidebar.getPublisherInput(),
                        createProtectionSidebar.getPublisherItems(), publisherActive.getName())
                .and("Expand 'Dsp' multipane")
                .clickOnWebElement(protectionsMultipane.getPanelNameLabel())
                .then("Validate Active/Associated Publisher link icon")
                .validate(protectionsMultipane.getSelectTableItemByName(dspList.get(0).getName()).getActiveIcon())
                .validate(protectionsMultipane.getSelectTableItemByName(dspList.get(0).getName()).getAssociatedWithPublisherIcon())
                .when("Clear All Dsps")
                .clickOnWebElement(protectionsMultipane.getClearAllButton())
                .validate(protectionsMultipane.getSelectionInfoExcludedLabel().getText(), MultipaneConstants.ALL_DEMAND_SOURCES_ARE_INCLUDED.setQuantity())
                .validate(protectionsMultipane.countSelectTableItems(), dspList.size())
                .validate(protectionsMultipane.countIncludedExcludedTableItems(), 0)
                .and("Include all dsp items")
                .validate(protectionsMultipane.countSelectTableItems(), dspList.size())
                .clickOnWebElement(protectionsMultipane.getIncludeAllButton())
                .testEnd();

        excludeIncludedItemsOneByOne(dspList);
    }

    @Test(description = "Check Multipane Text (include not all items)", priority = 6)
    private void checkMultipaneTextExcludeDSP() {
        var dspMultipane = createProtectionSidebar.getDemandSourcesMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(createProtectionSidebar.getPublisherInput(),
                        createProtectionSidebar.getPublisherItems(), publisherActive.getName())
                .and("Expand 'Adsize' multipane")
                .clickOnWebElement(dspMultipane.getPanelNameLabel())
                .testEnd();

        excludeOneByOneItems(dspList);
    }

    @Test(description = "Check Multipane Text (exclude not all items)", priority = 7, dependsOnMethods = "checkMultipaneTextExcludeDSP")
    private void checkMultipaneRemoveExcludedDSP() {
        var dspMultipane = createProtectionSidebar.getDemandSourcesMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(createProtectionSidebar.getPublisherInput(),
                        createProtectionSidebar.getPublisherItems(), publisherActive.getName())
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(createProtectionSidebar.getPublisherInput(),
                        createProtectionSidebar.getPublisherItems(), publisherActive.getName())
                .and("Expand 'Adsize' multipane")
                .clickOnWebElement(dspMultipane.getPanelNameLabel())
                .testEnd();

        removeOneByOneExcludedItems(dspList);
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
                .clickOnWebElement(createProtectionSidebar.getCloseIcon())
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
        var protectionsMultipane = createProtectionSidebar.getDemandSourcesMultipane();
        var updated = new AtomicInteger(0);

        list.forEach(e ->
        {
                var selectedItem = protectionsMultipane.getSelectTableItemByPositionInList(updated.get());
                testStart()
                        .waiter(visible, selectedItem.getName())
                        .hoverMouseOnWebElement(selectedItem.getName())
                        .then()
                        .validate(not(visible), selectedItem.getExcludedIcon())
                        .validate(not(visible), selectedItem.getIncludedIcon())
                        .validate(visible, selectedItem.getActiveIcon())
                        .validate(not(visible), selectedItem.getInactiveIcon())
                        .validate(visible, selectedItem.getAssociatedWithPublisherIcon())
                        .and()
                        .clickOnWebElement(selectedItem.getIncludeButton())
                        .validate(protectionsMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 0) ?
                                MultipaneConstants.ONE_DEMAND_SOURCE_IS_INCLUDED.setQuantity(updated.incrementAndGet()) :
                                MultipaneConstants.DEMAND_SOURCES_ARE_INCLUDED.setQuantity(updated.incrementAndGet()))
                        .validate(visible, selectedItem.getName())
                        .testEnd();
            });
    }

    @Step("Exclude all items one by one")
    private void excludeIncludedItemsOneByOne(List<DemandSource> list) {
        var updated = new AtomicInteger(list.size());
        var protectionsMultipane = createProtectionSidebar.getDemandSourcesMultipane();

        list.forEach(item -> {
            var selectedItem = protectionsMultipane.getIncludedExcludedTableItemByName(item.getName());

            testStart()
                    .waiter(visible, selectedItem.getName())
                    .hoverMouseOnWebElement(selectedItem.getName())
                    .then()
                    .validateContainsText(protectionsMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 1) ?
                            MultipaneConstants.ONE_DEMAND_SOURCE_IS_INCLUDED.setQuantity(updated.getAndDecrement()) :
                            MultipaneConstants.DEMAND_SOURCES_ARE_INCLUDED.setQuantity(updated.getAndDecrement()))
                    .then()
                    .validate(visible, selectedItem.getRemoveButton())
                    .and(String.format("Remove selected item %s", selectedItem.getName()))
                    .clickOnWebElement(selectedItem.getRemoveButton())
                    .testEnd();
        });
    }

    @Step("Exclude all items one by one")
    private void excludeOneByOneItems(List<DemandSource> list) {
        var dspMultipane = createProtectionSidebar.getDemandSourcesMultipane();
        var updated = new AtomicInteger(0);

        list.forEach(e ->
        {
            var selectedItem = dspMultipane.getSelectTableItemByPositionInList(updated.get());
            testStart()
                    .waiter(visible, selectedItem.getName())
                    .hoverMouseOnWebElement(selectedItem.getName())
                    .then()
                    .validate(not(visible), selectedItem.getExcludedIcon())
                    .validate(not(visible), selectedItem.getIncludedIcon())
                    .validate(visible, selectedItem.getActiveIcon())
                    .validate(visible, selectedItem.getAssociatedWithPublisherIcon())
                    .validate(visible, selectedItem.getExcludeButton())
                    .and()
                    .clickOnWebElement(selectedItem.getExcludeButton())
                    .validate(dspMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 0) ?
                            MultipaneConstants.ONE_DEMAND_SOURCE_IS_EXCLUDED.setQuantity(updated.incrementAndGet()) :
                            MultipaneConstants.DEMAND_SOURCES_ARE_EXCLUDED.setQuantity(updated.incrementAndGet()))
                    .validate(exist, selectedItem.getName())
                    .testEnd();
        });
    }

    @Step("Remove all excluded items one by one")
    private void removeOneByOneExcludedItems(List<DemandSource> list) {
        var updated = new AtomicInteger(list.size());
        var dspMultipane = createProtectionSidebar.getDemandSourcesMultipane();

        list.forEach(item -> {
            var selectedItem = dspMultipane.getIncludedExcludedTableItemByName(item.getName());

            testStart()
                    .waiter(visible, selectedItem.getName())
                    .hoverMouseOnWebElement(selectedItem.getName())
                    .then()
                    .validateContainsText(dspMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 1) ?
                            MultipaneConstants.ONE_DEMAND_SOURCE_IS_EXCLUDED.setQuantity(updated.getAndDecrement()) :
                            MultipaneConstants.DEMAND_SOURCES_ARE_EXCLUDED.setQuantity(updated.getAndDecrement()))
                    .then()
                    .validate(visible, selectedItem.getRemoveButton())
                    .and(String.format("Remove selected item %s", selectedItem.getName()))
                    .clickOnWebElement(selectedItem.getRemoveButton())
                    .testEnd();
        });
    }

    @AfterMethod(alwaysRun = true)
    private void collapseMultipane(){
        var protectionsMultipane = createProtectionSidebar.getDemandSourcesMultipane();

        testStart()
                .and("Collapse 'Dsp' multipane")
                .clickOnWebElement(protectionsMultipane.getPanelNameLabel())
                .testEnd();
    }

    private void deletePublisher(Integer id) {
        publisher()
                .setCredentials(USER_FOR_DELETION)
                .deletePublisher(id)
                .build();
    }
}
