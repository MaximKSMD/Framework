package rx.yield.openpricing;

import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.device.Device;
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

import static api.preconditionbuilders.DevicePrecondition.device;
import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Open Pricing")
public class OpenPricingDeviceMultipaneTests extends BaseTest {

    private Publisher publisherActive;
    private List<Device> deviceList;
    private OpenPricingPage openPricingPage;
    private CreateOpenPricingSidebar openPricingSidebar;

    public OpenPricingDeviceMultipaneTests() {

        openPricingPage = new OpenPricingPage();
        openPricingSidebar = new CreateOpenPricingSidebar();
    }

    @BeforeClass
    private void login() {
        publisherActive = getAllPublishersByParams(Map.of("sort","created_at-desc"));

        deviceList = getDevices();

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

    @Test(description = "Check Device Items List is filled and disabled by default", priority = 0)
    private void checkDefaultDeviceList() {
        var openPricingMultipane = openPricingSidebar.getDeviceMultipane();

        testStart()
                .then("Validate device items list should not be empty")
                .validate(openPricingMultipane.countSelectTableItems(), deviceList.size())
                .validate(disabled, openPricingMultipane.getSearchInput())
                .validateContainsText(openPricingMultipane.getIncludeAllButton(),String.format("INCLUDE ALL\n(%s)",0))
                .testEnd();
    }

    @Test(description = "Check Device Items List", priority = 1)
    private void checkDeviceList() {
        var openPricingMultipane = openPricingSidebar.getDeviceMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherInput(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'Device' multipane")
                .clickOnWebElement(openPricingMultipane.getPanelNameLabel())
                .then("Validate device items list should not be empty")
                .validate(openPricingMultipane.countSelectTableItems(), deviceList.size())
                .testEnd();

        deviceList.forEach(e -> {
            testStart()
                    .validate(visible, openPricingMultipane.getSelectTableItemByName(e.getName()).getName())
                    .testEnd();
        });
    }

    @Test(description = "Check Search Device", priority = 2)
    private void checkSearchDevice() {
        var openPricingMultipane = openPricingSidebar.getDeviceMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherInput(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'Device' multipane")
                .clickOnWebElement(openPricingMultipane.getPanelNameLabel())
                .and(String.format("Search device by Name = %s", deviceList.get(0).getName()))
                .setValueWithClean(openPricingMultipane.getSearchInput(), deviceList.get(0).getName())
                .pressEnterKey(openPricingMultipane.getSearchInput())
                .then("Validate item list includes device")
                .validateContainsText(openPricingMultipane.getItemsQuantityString(),"(1)")
                .validate(openPricingMultipane.countSelectTableItems(), 1)
                .validate(openPricingMultipane.getSelectTableItemByPositionInList(0).getName(), deviceList.get(0).getName())
                .and("Clear Search field")
                .setValueWithClean(openPricingMultipane.getSearchInput(), "")
                .pressEnterKey(openPricingMultipane.getSearchInput())
                .validate(openPricingMultipane.countSelectTableItems(), deviceList.size())
                .testEnd();

        deviceList.forEach(e -> {
            testStart()
                    .validate(visible, openPricingMultipane.getSelectTableItemByName(e.getName()).getName())
                    .testEnd();
        });
    }

    @Test(description = "Check Multipane Text 'Include All'", priority = 3)
    private void checkMultipaneText() {
        var openPricingMultipane = openPricingSidebar.getDeviceMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherInput(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'Device' multipane")
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
                        MultipaneConstants.DEVICES_ARE_INCLUDED.setQuantity(deviceList.size()))
                .testEnd();
    }

    @Test(description = "Check Multipane Text 'Clear All'", priority = 4)
    private void checkMultipaneTextClearAll() {
        var openPricingMultipane = openPricingSidebar.getDeviceMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherInput(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'Device' multipane")
                .clickOnWebElement(openPricingMultipane.getPanelNameLabel())
                .and("Include All devices")
                .clickOnWebElement(openPricingMultipane.getIncludeAllButton())
                .validate(openPricingMultipane.countIncludedExcludedTableItems(), deviceList.size())
                .validate(openPricingMultipane.countSelectTableItems(), deviceList.size())
                .and("Clear All devices")
                .clickOnWebElement(openPricingMultipane.getClearAllButton())
                .then("Validate text above items panel")
                .validate(openPricingMultipane.getSelectionInfoExcludedLabel().getText(), MultipaneConstants.ALL_DEVICES_ARE_INCLUDED.setQuantity())
                .validate(openPricingMultipane.countSelectTableItems(), deviceList.size())
                .validate(openPricingMultipane.countIncludedExcludedTableItems(), 0)
                .testEnd();
    }

    @Test(description = "Check Multipane Text (include not all items)", priority = 4)
    private void checkMultipaneTextNotAllItems() {
        var openPricingMultipane = openPricingSidebar.getDeviceMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherInput(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'Device' multipane")
                .clickOnWebElement(openPricingMultipane.getPanelNameLabel())
                .testEnd();

        includeOneByOneItems(deviceList);
    }

    @Test(description = "Check Multipane Text (exclude not all items)", priority = 5)
    private void checkMultipaneTextExcludeNotAllItems() {
        var openPricingMultipane = openPricingSidebar.getDeviceMultipane();


        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherInput(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisherActive.getName())
                .and("Expand 'Device' multipane")
                .clickOnWebElement(openPricingMultipane.getPanelNameLabel())
                .and("Include All devices")
                .clickOnWebElement(openPricingMultipane.getIncludeAllButton())
                .testEnd();

        excludeOneByOneItems(deviceList);
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

    private List<Device> getDevices() {

        return device()
                .getDeviceLList()
                .build()
                .getDeviceGetAllResponse()
                .getItems();
    }

    @Step("Include all items one by one")
    private void includeOneByOneItems(List<Device> list) {
        var openPricingMultipane = openPricingSidebar.getDeviceMultipane();
        var updated = new AtomicInteger(0);

        list.stream().forEach(e ->
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
                            MultipaneConstants.ONE_DEVICE_IS_INCLUDED.setQuantity(updated.incrementAndGet()) :
                            MultipaneConstants.DEVICES_ARE_INCLUDED.setQuantity(updated.incrementAndGet()))
                    .validate(visible, selectedItem.getName())
                    .testEnd();
        });
    }

    @Step("Exclude all items one by one")
    private void excludeOneByOneItems(List<Device> list) {
        var updated = new AtomicInteger(list.size());
        var openPricingMultipane = openPricingSidebar.getDeviceMultipane();


        list.forEach(item -> {
            var selectedItem = openPricingMultipane.getIncludedExcludedTableItemByName(item.getName());

            testStart()
                    .waiter(visible, selectedItem.getName())
                    .hoverMouseOnWebElement(selectedItem.getName())
                    .then()
                    .validateContainsText(openPricingMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 1) ?
                            MultipaneConstants.ONE_DEVICE_IS_INCLUDED.setQuantity(updated.getAndDecrement()) :
                            MultipaneConstants.DEVICES_ARE_INCLUDED.setQuantity(updated.getAndDecrement()))
                    .then()
                    .validate(visible, selectedItem.getRemoveButton())
                    .and(String.format("Remove selected item %s", selectedItem.getName()))
                    .clickOnWebElement(selectedItem.getRemoveButton())
                    .testEnd();
        });
    }

    @AfterMethod(alwaysRun = true)
    private void collapseMultipane(){
        var openPricingMultipane = openPricingSidebar.getDeviceMultipane();

        testStart()
                .and("Collapse 'Device' multipane")
                .clickOnWebElement(openPricingMultipane.getPanelNameLabel())
                .testEnd();
    }

    private Publisher getAllPublishersByParams(Map<String, Object> strParams) {

        return publisher()
                .getPublishersListWithFilter(strParams)
                .build()
                .getPublisherGetAllResponse()
                .getItems().stream().filter(e -> e.getIsEnabled().equals(true))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);

    }
}
