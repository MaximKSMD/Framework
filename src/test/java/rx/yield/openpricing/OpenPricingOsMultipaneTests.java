package rx.yield.openpricing;

import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.os.OperatingSystem;
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

import static api.preconditionbuilders.OperatingSystemPrecondition.operatingSystem;
import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static configurations.User.USER_FOR_DELETION;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Open Pricing")
public class OpenPricingOsMultipaneTests extends BaseTest {

    private Publisher publisherEmpty;
    private Publisher publisherActive;
    private Publisher publisherInactive;
    private List<OperatingSystem> operatingSystemList;

    private OpenPricingPage openPricingPage;
    private CreateOpenPricingSidebar openPricingSideBar;

    public OpenPricingOsMultipaneTests() {

        openPricingPage = new OpenPricingPage();
        openPricingSideBar = new CreateOpenPricingSidebar();
    }

    @BeforeClass
    private void login() {

        publisherEmpty = createPublisher(captionWithSuffix("00autoPubOpenPricing_empty"), true);
        publisherActive = createPublisher(captionWithSuffix("00autoPubPOpenPricing_active"), true);
        publisherInactive = createPublisher(captionWithSuffix("00autoPubOpenPricing_inactive"), false);

        operatingSystemList = getOperatingSystems();

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

    @Test(description = "Check OS Items List is filled and disabled by default", priority = 0)
    private void checkDefaultOsList() {
        var operatingSystemMultipane = openPricingSideBar.getOperatingSystemMultipane();

        testStart()
                .and("Expand 'OS' multipane")
                .clickOnWebElement(operatingSystemMultipane.getPanelNameLabel())
                .then("Validate OS items list should not be empty")
                .validate(operatingSystemMultipane.countSelectTableItems(), operatingSystemList.size())
                .validate(disabled, operatingSystemMultipane.getSearchInput())
                .validateContainsText(operatingSystemMultipane.getIncludeAllButton(), String.format("INCLUDE ALL\n(%s)", 0))
                .testEnd();
    }

    @Test(description = "Check OS Items List", priority = 1)
    private void checkOsList() {
        var operatingSystemMultipane = openPricingSideBar.getOperatingSystemMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSideBar.getPublisherInput(),
                        openPricingSideBar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'OS' multipane")
                .clickOnWebElement(operatingSystemMultipane.getPanelNameLabel())
                .then("Validate OS items list should not be empty")
                .validate(operatingSystemMultipane.countSelectTableItems(), operatingSystemList.size())
                .testEnd();

        operatingSystemList.forEach(e -> {
            testStart()
                    .validate(visible, operatingSystemMultipane.getSelectTableItemByName(e.getName()).getName())
                    .testEnd();
        });
    }

    @Test(description = "Check Search OS", priority = 2)
    private void checkSearchOS() {
        var operatingSystemMultipane = openPricingSideBar.getOperatingSystemMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSideBar.getPublisherInput(),
                        openPricingSideBar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'OS' multipane")
                .clickOnWebElement(operatingSystemMultipane.getPanelNameLabel())
                .and(String.format("Search OS by Name = %s", operatingSystemList.get(0).getName()))
                .setValueWithClean(operatingSystemMultipane.getSearchInput(), operatingSystemList.get(0).getName())
                .pressEnterKey(operatingSystemMultipane.getSearchInput())
                .then("Validate item list includes OS")
                .validateContainsText(operatingSystemMultipane.getItemsQuantityString(), "(1)")
                .validate(operatingSystemMultipane.countSelectTableItems(), 1)
                .validate(operatingSystemMultipane.getSelectTableItemByPositionInList(0).getName(), operatingSystemList.get(0).getName())
                .and("Clear Search field")
                .setValueWithClean(operatingSystemMultipane.getSearchInput(), "")
                .pressEnterKey(operatingSystemMultipane.getSearchInput())
                .waitAndValidate(visible, operatingSystemMultipane.getSearchInput())
                .validate(operatingSystemMultipane.countSelectTableItems(), operatingSystemList.size())
                .testEnd();

        operatingSystemList.forEach(e -> {
            testStart()
                    .validate(visible, operatingSystemMultipane.getSelectTableItemByName(e.getName()).getName())
                    .testEnd();
        });
    }

    @Test(description = "Check Multipane Text 'Include All'", priority = 3)
    private void checkMultipaneText() {
        var operatingSystemMultipane = openPricingSideBar.getOperatingSystemMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSideBar.getPublisherInput(),
                        openPricingSideBar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'OS' multipane")
                .clickOnWebElement(operatingSystemMultipane.getPanelNameLabel())
                .then("Validate text above items panel")
                .validate(operatingSystemMultipane.getSelectionInfoExcludedLabel().getText(),
                        "")
                .and("Include All media")
                .clickOnWebElement(operatingSystemMultipane.getIncludeAllButton())
                .then("Validate 'Selected' items panel")
                .validate(operatingSystemMultipane.countSelectTableItems(), operatingSystemMultipane.countIncludedExcludedTableItems())
                .then("Validate text above items panel")
                .validate(operatingSystemMultipane.getSelectionInfoExcludedLabel().getText(),
                        MultipaneConstants.OPERATING_SYSTEMS_ARE_INCLUDED.setQuantity(operatingSystemList.size()))
                .testEnd();
    }

    @Test(description = "Check Multipane Text 'Clear All'", priority = 4)
    private void checkMultipaneTextClearAll() {
        var operatingSystemMultipane = openPricingSideBar.getOperatingSystemMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSideBar.getPublisherInput(),
                        openPricingSideBar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'OS' multipane")
                .clickOnWebElement(operatingSystemMultipane.getPanelNameLabel())
                .and("Include All OSs")
                .clickOnWebElement(operatingSystemMultipane.getIncludeAllButton())
                .validate(operatingSystemMultipane.countIncludedExcludedTableItems(), operatingSystemList.size())
                .validate(operatingSystemMultipane.countSelectTableItems(), operatingSystemList.size())
                .and("Clear All OS")
                .clickOnWebElement(operatingSystemMultipane.getClearAllButton())
                .then("Validate text above items panel")
                .validate(operatingSystemMultipane.getSelectionInfoExcludedLabel().getText(), MultipaneConstants.ALL_OPERATING_SYSTEMS_ARE_INCLUDED.setQuantity())
                .validate(operatingSystemMultipane.countSelectTableItems(), operatingSystemList.size())
                .validate(operatingSystemMultipane.countIncludedExcludedTableItems(), 0)
                .testEnd();
    }

    @Test(description = "Check Multipane Text (include not all items)", priority = 4)
    private void checkMultipaneTextNotAllItems() {
        var operatingSystemMultipane = openPricingSideBar.getOperatingSystemMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSideBar.getPublisherInput(),
                        openPricingSideBar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'OS' multipane")
                .clickOnWebElement(operatingSystemMultipane.getPanelNameLabel())
                .testEnd();

        includeOneByOneItems(operatingSystemList);
    }

    @Test(description = "Check Multipane Text (exclude not all items)", priority = 5)
    private void checkMultipaneTextExcludeNotAllItems() {
        var operatingSystemMultipane = openPricingSideBar.getOperatingSystemMultipane();


        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSideBar.getPublisherInput(),
                        openPricingSideBar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'OS' multipane")
                .clickOnWebElement(operatingSystemMultipane.getPanelNameLabel())
                .and("Include All OS")
                .clickOnWebElement(operatingSystemMultipane.getIncludeAllButton())
                .testEnd();

        excludeOneByOneItems(operatingSystemList);
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

    private List<OperatingSystem> getOperatingSystems() {

        return operatingSystem()
                .getOperatingSystemList()
                .build()
                .getOperatingSystemGetAllResponse()
                .getItems();
    }

    @Step("Include all items one by one")
    private void includeOneByOneItems(List<OperatingSystem> list) {
        var operatingSystemMultipane = openPricingSideBar.getOperatingSystemMultipane();
        var updated = new AtomicInteger(0);

        list.forEach(e ->
        {
            var selectedItem = operatingSystemMultipane.getSelectTableItemByPositionInList(updated.get());
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
                    .validate(operatingSystemMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 0) ?
                            MultipaneConstants.ONE_OPERATING_SYSTEM_IS_INCLUDED.setQuantity(updated.incrementAndGet()) :
                            MultipaneConstants.OPERATING_SYSTEMS_ARE_INCLUDED.setQuantity(updated.incrementAndGet()))
                    .validate(exist, selectedItem.getName())
                    .testEnd();
        });
    }

    @Step("Exclude all items one by one")
    private void excludeOneByOneItems(List<OperatingSystem> list) {
        var updated = new AtomicInteger(list.size());
        var operatingSystemMultipane = openPricingSideBar.getOperatingSystemMultipane();


        list.forEach(item -> {
            var selectedItem = operatingSystemMultipane.getIncludedExcludedTableItemByName(item.getName());

            testStart()
                    .waiter(visible, selectedItem.getName())
                    .hoverMouseOnWebElement(selectedItem.getName())
                    .then()
                    .validateContainsText(operatingSystemMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 1) ?
                            MultipaneConstants.ONE_OPERATING_SYSTEM_IS_INCLUDED.setQuantity(updated.getAndDecrement()) :
                            MultipaneConstants.OPERATING_SYSTEMS_ARE_INCLUDED.setQuantity(updated.getAndDecrement()))
                    .then()
                    .validate(visible, selectedItem.getRemoveButton())
                    .and(String.format("Remove selected item %s", selectedItem.getName()))
                    .clickOnWebElement(selectedItem.getRemoveButton())
                    .testEnd();
        });
    }

    @AfterMethod(alwaysRun = true)
    private void collapseMultipane() {
        var operatingSystemMultipane = openPricingSideBar.getOperatingSystemMultipane();

        testStart()
                .and("Collapse 'OS' multipane")
                .clickOnWebElement(operatingSystemMultipane.getPanelNameLabel())
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
}
