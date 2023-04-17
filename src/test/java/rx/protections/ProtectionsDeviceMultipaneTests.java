package rx.protections;

import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.device.Device;
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

import static api.preconditionbuilders.DevicePrecondition.device;
import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Protections")
public class ProtectionsDeviceMultipaneTests extends BaseTest {

    private List<Device> deviceList;

    private ProtectionsPage protectionsPage;
    private CreateProtectionSidebar protectionSidebar;

    public ProtectionsDeviceMultipaneTests() {

        protectionsPage = new ProtectionsPage();
        protectionSidebar = new CreateProtectionSidebar();
    }

    @BeforeClass
    private void login() {
        deviceList = getDevices();

        testStart()
                .given()
                .openDirectPath(Path.PROTECTIONS)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, protectionsPage.getNuxtProgress())
                .and("Press 'Create Protection' button")
                .clickOnWebElement(protectionsPage.getCreateProtectionButton())
                .waitSideBarOpened()
                .validateContainsText(protectionSidebar.getTitle(),"Create Protections")
                .testEnd();
    }

    @Test(description = "Check Device Items List is filled and disabled by default", priority = 0)
    private void checkDefaultDeviceList() {
        var deviceMultipane = protectionSidebar.getDeviceMultipane();

        testStart()
                .and("Expand 'Device' multipane")
                .waitAndValidate(visible, deviceMultipane.getPanelNameLabel())
                .clickOnWebElement(deviceMultipane.getPanelNameLabel())
                .then("Validate device items list should not be empty")
                .validate(deviceMultipane.countSelectTableItems(), deviceList.size())
                .validate(disabled, deviceMultipane.getSearchInput())
                .validateContainsText(deviceMultipane.getIncludeAllButton(), String.format("INCLUDE ALL\n(%s)", 0))
                .and("Collapse 'Device' multipane")
                .clickOnWebElement(deviceMultipane.getPanelNameLabel())
                .testEnd();
    }

    @Test(description = "Check Device Items List", priority = 1)
    private void checkDeviceList() {
        var deviceMultipane = protectionSidebar.getDeviceMultipane();

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(protectionSidebar.getPublisherNameDropdown(),
                        protectionSidebar.getPublisherItems(), 1)
                .and("Expand 'Device' multipane")
                .clickOnWebElement(deviceMultipane.getPanelNameLabel())
                .then("Validate device items list should not be empty")
                .validate(deviceMultipane.countSelectTableItems(), deviceList.size())
                .testEnd();

        deviceList.forEach(e -> {
            testStart()
                    .validate(visible, deviceMultipane.getSelectTableItemByName(e.getName()).getName())
                    .testEnd();
        });

        testStart()
                .and("Collapse 'Device' multipane")
                .clickOnWebElement(deviceMultipane.getPanelNameLabel())
                .testEnd();
    }

    @Test(description = "Check Search Device", priority = 2)
    private void checkSearchDevice() {
        var deviceMultipane = protectionSidebar.getDeviceMultipane();

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(protectionSidebar.getPublisherNameDropdown(),
                        protectionSidebar.getPublisherItems(), 1)
                .and("Expand 'Device' multipane")
                .clickOnWebElement(deviceMultipane.getPanelNameLabel())
                .and(String.format("Search device by Name = %s", deviceList.get(0).getName()))
                .setValueWithClean(deviceMultipane.getSearchInput(), deviceList.get(0).getName())
                .pressEnterKey(deviceMultipane.getSearchInput())
                .then("Validate item list includes device")
                .validateContainsText(deviceMultipane.getItemsQuantityString(), "(1)")
                .validate(deviceMultipane.countSelectTableItems(), 1)
                .validate(deviceMultipane.getSelectTableItemByPositionInList(0).getName(), deviceList.get(0).getName())
                .and("Clear Search field")
                .setValueWithClean(deviceMultipane.getSearchInput(), "")
                .pressEnterKey(deviceMultipane.getSearchInput())
                .validate(deviceMultipane.countSelectTableItems(), deviceList.size())
                .testEnd();

        deviceList.forEach(e -> {
            testStart()
                    .validate(visible, deviceMultipane.getSelectTableItemByName(e.getName()).getName())
                    .testEnd();
        });

        testStart()
                .and("Collapse 'Device' multipane")
                .clickOnWebElement(deviceMultipane.getPanelNameLabel())
                .testEnd();
    }

    @Test(description = "Check Multipane Text 'Include All'", priority = 3)
    private void checkMultipaneText() {
        var deviceMultipane = protectionSidebar.getDeviceMultipane();

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(protectionSidebar.getPublisherNameDropdown(),
                        protectionSidebar.getPublisherItems(), 1)
                .and("Expand 'Device' multipane")
                .clickOnWebElement(deviceMultipane.getPanelNameLabel())
                .then("Validate text above items panel")
                .validate(deviceMultipane.getSelectionInfoExcludedLabel().getText(),
                        "")
                .and("Include All media")
                .clickOnWebElement(deviceMultipane.getIncludeAllButton())
                .then("Validate 'Selected' items panel")
                .validate(deviceMultipane.countSelectTableItems(), deviceMultipane.countIncludedExcludedTableItems())
                .then("Validate text above items panel")
                .validate(deviceMultipane.getSelectionInfoExcludedLabel().getText(),
                        MultipaneConstants.DEVICES_ARE_INCLUDED.setQuantity(deviceList.size()))
                .and("Collapse 'Device' multipane")
                .clickOnWebElement(deviceMultipane.getPanelNameLabel())
                .testEnd();
    }

    @Test(description = "Check Multipane Text 'Clear All'", priority = 4)
    private void checkMultipaneTextClearAll() {
        var deviceMultipane = protectionSidebar.getDeviceMultipane();

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(protectionSidebar.getPublisherNameDropdown(),
                        protectionSidebar.getPublisherItems(), 1)
                .and("Expand 'Device' multipane")
                .clickOnWebElement(deviceMultipane.getPanelNameLabel())
                .and("Include All devices")
                .clickOnWebElement(deviceMultipane.getIncludeAllButton())
                .validate(deviceMultipane.countIncludedExcludedTableItems(), deviceList.size())
                .validate(deviceMultipane.countSelectTableItems(), deviceList.size())
                .and("Clear All devices")
                .clickOnWebElement(deviceMultipane.getClearAllButton())
                .then("Validate text above items panel")
                .validate(deviceMultipane.getSelectionInfoExcludedLabel().getText(), MultipaneConstants.ALL_DEVICES_ARE_INCLUDED.setQuantity())
                .validate(deviceMultipane.countSelectTableItems(), deviceList.size())
                .validate(deviceMultipane.countIncludedExcludedTableItems(), 0)
                .and("Collapse 'Device' multipane")
                .clickOnWebElement(deviceMultipane.getPanelNameLabel())
                .testEnd();
    }

    @Test(description = "Check Multipane Text (include not all items)", priority = 4)
    private void checkMultipaneTextNotAllItems() {
        var deviceMultipane = protectionSidebar.getDeviceMultipane();

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(protectionSidebar.getPublisherNameDropdown(),
                        protectionSidebar.getPublisherItems(), 1)
                .and("Expand 'Device' multipane")
                .clickOnWebElement(deviceMultipane.getPanelNameLabel())
                .testEnd();

        includeOneByOneItems(deviceList);

        testStart()
                .and("Collapse 'Device' multipane")
                .clickOnWebElement(deviceMultipane.getPanelNameLabel())
                .testEnd();
    }

    @Test(description = "Check Multipane Text (exclude not all items)", priority = 5)
    private void checkMultipaneTextExcludeNotAllItems() {
        var deviceMultipane = protectionSidebar.getDeviceMultipane();

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(protectionSidebar.getPublisherNameDropdown(),
                        protectionSidebar.getPublisherItems(), 1)
                .and("Expand 'Device' multipane")
                .clickOnWebElement(deviceMultipane.getPanelNameLabel())
                .and("Include All devices")
                .clickOnWebElement(deviceMultipane.getIncludeAllButton())
                .testEnd();

        excludeIncludedItemsOneByOneItems(deviceList);

        testStart()
                .and("Collapse 'Device' multipane")
                .clickOnWebElement(deviceMultipane.getPanelNameLabel())
                .testEnd();
    }

    @Test(description = "Check Multipane Text (include not all items)", priority = 6)
    private void checkMultipaneTextExcludeDevices() {
        var deviceMultipane = protectionSidebar.getDeviceMultipane();

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(protectionSidebar.getPublisherNameDropdown(),
                        protectionSidebar.getPublisherItems(), 1)
                .and("Expand 'Device' multipane")
                .clickOnWebElement(deviceMultipane.getPanelNameLabel())
                .testEnd();

        excludeOneByOneItems(deviceList);

        testStart()
                .and("Collapse 'Device' multipane")
                .clickOnWebElement(deviceMultipane.getPanelNameLabel())
                .testEnd();
    }

    @Test(description = "Check Multipane Text (exclude not all items)", priority = 7, dependsOnMethods = "checkMultipaneTextExcludeDevices")
    private void checkMultipaneRemoveExcludedDevices() {
        var deviceMultipane = protectionSidebar.getDeviceMultipane();

        testStart()
                .and("Select Publisher")
                .selectFromDropdownByPosition(protectionSidebar.getPublisherNameDropdown(),
                        protectionSidebar.getPublisherItems(), 1)
                .and("Expand 'Device' multipane")
                .clickOnWebElement(deviceMultipane.getPanelNameLabel())
                .testEnd();

        removeOneByOneExcludedItems(deviceList);

        testStart()
                .and("Collapse 'Device' multipane")
                .clickOnWebElement(deviceMultipane.getPanelNameLabel())
                .testEnd();
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

    private List<Device> getDevices() {

        return device()
                .getDeviceLList()
                .build()
                .getDeviceGetAllResponse()
                .getItems();
    }

    @Step("Include all items one by one")
    private void includeOneByOneItems(List<Device> list) {
        var protectionMultipane = protectionSidebar.getDeviceMultipane();
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
                            MultipaneConstants.ONE_DEVICE_IS_INCLUDED.setQuantity(updated.incrementAndGet()) :
                            MultipaneConstants.DEVICES_ARE_INCLUDED.setQuantity(updated.incrementAndGet()))
                    .validate(visible, selectedItem.getName())
                    .testEnd();
        });
    }

    @Step("Exclude all items one by one")
    private void excludeIncludedItemsOneByOneItems(List<Device> list) {
        var updated = new AtomicInteger(list.size());
        var deviceMultipane = protectionSidebar.getDeviceMultipane();


        list.forEach(item -> {
            var selectedItem = deviceMultipane.getIncludedExcludedTableItemByName(item.getName());

            testStart()
                    .waiter(visible, selectedItem.getName())
                    .hoverMouseOnWebElement(selectedItem.getName())
                    .then()
                    .validateContainsText(deviceMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 1) ?
                            MultipaneConstants.ONE_DEVICE_IS_INCLUDED.setQuantity(updated.getAndDecrement()) :
                            MultipaneConstants.DEVICES_ARE_INCLUDED.setQuantity(updated.getAndDecrement()))
                    .then()
                    .validate(visible, selectedItem.getRemoveButton())
                    .and(String.format("Remove selected item %s", selectedItem.getName()))
                    .clickOnWebElement(selectedItem.getRemoveButton())
                    .testEnd();
        });
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

    @Step("Exclude all items one by one")
    private void excludeOneByOneItems(List<Device> list) {
        var deviceMultipane = protectionSidebar.getDeviceMultipane();
        var updated = new AtomicInteger(0);

        list.forEach(e ->
        {
            var selectedItem = deviceMultipane.getSelectTableItemByPositionInList(updated.get());
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
                    .validate(deviceMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 0) ?
                            MultipaneConstants.ONE_DEVICE_IS_EXCLUDED.setQuantity(updated.incrementAndGet()) :
                            MultipaneConstants.DEVICES_ARE_EXCLUDED.setQuantity(updated.incrementAndGet()))
                    .validate(exist, selectedItem.getName())
                    .testEnd();
        });
    }

    @Step("Remove all excluded items one by one")
    private void removeOneByOneExcludedItems(List<Device> list) {
        var updated = new AtomicInteger(list.size());
        var deviceMultipane = protectionSidebar.getDeviceMultipane();

        list.forEach(item -> {
            var selectedItem = deviceMultipane.getIncludedExcludedTableItemByName(item.getName());

            testStart()
                    .waiter(visible, selectedItem.getName())
                    .hoverMouseOnWebElement(selectedItem.getName())
                    .then()
                    .validateContainsText(deviceMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 1) ?
                            MultipaneConstants.ONE_DEVICE_IS_EXCLUDED.setQuantity(updated.getAndDecrement()) :
                            MultipaneConstants.DEVICES_ARE_EXCLUDED.setQuantity(updated.getAndDecrement()))
                    .then()
                    .validate(visible, selectedItem.getRemoveButton())
                    .and(String.format("Remove selected item %s", selectedItem.getName()))
                    .clickOnWebElement(selectedItem.getRemoveButton())
                    .testEnd();
        });
    }
}
