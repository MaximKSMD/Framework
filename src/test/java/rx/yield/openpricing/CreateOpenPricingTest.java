package rx.yield.openpricing;

import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.device.Device;
import api.dto.rx.inventory.media.Media;
import api.dto.rx.os.OperatingSystem;
import api.dto.rx.yield.openpricing.OpenPricing;
import api.preconditionbuilders.DevicePrecondition;
import api.preconditionbuilders.OpenPricingPrecondition;
import api.preconditionbuilders.OperatingSystemPrecondition;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Path;
import pages.yield.openpricing.OpenPricingPage;
import rx.BaseTest;
import widgets.common.detailsmenu.menu.TableItemDetailsMenu;
import widgets.common.detailsmenu.menu.sections.DetailsSection;
import widgets.common.menusidebar.MenuSidebarOptions;
import widgets.common.multipane.Multipane;
import widgets.common.multipane.MultipaneNameImpl;
import widgets.common.table.ColumnNames;
import widgets.common.table.TableData;
import widgets.yield.openPricing.sidebar.CreateOpenPricingSidebar;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static api.preconditionbuilders.MediaPrecondition.media;
import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static configurations.User.USER_FOR_DELETION;
import static java.lang.String.format;
import static managers.TestManager.testStart;
import static rx.enums.MultipaneConstants.*;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Open Pricing")
public class CreateOpenPricingTest extends BaseTest {

    private OpenPricing newlyCreatedPricing;
    private OpenPricingPage openPricingPage;
    private TableItemDetailsMenu pricingTableDetailsMenu;
    private CreateOpenPricingSidebar createOpenPricingSidebar;

    Publisher publisher;
    Media media1;
    Media media2;

    private static final String EMPTY_STRING = "";
    private static final String PRICING_NAME = captionWithSuffix("autoPricing");

    private final static String FLOOR_PRICE = "22.09";

    public CreateOpenPricingTest() {
        openPricingPage = new OpenPricingPage();
        pricingTableDetailsMenu = new TableItemDetailsMenu();
        createOpenPricingSidebar = new CreateOpenPricingSidebar();
    }

    @BeforeClass
    @Step("Login ToOpenPricing Page")
    public void loginToCreateOpenPricingSidebar() {

        publisher = publisher()
                .createNewPublisher(captionWithSuffix("00000autoPubPricing"))
                .build()
                .getPublisherResponse();

        media1 = media()
                .createNewMedia("autoMedia", publisher.getId(), true)
                .build()
                .getMediaResponse();

        media2 = media()
                .createNewMedia("autoMedia", publisher.getId(), true)
                .build()
                .getMediaResponse();

        testStart()
                .given("Logging Directly to Open Pricing Page")
                .openDirectPath(Path.OPEN_PRICING)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                .waitAndValidate(disappear, openPricingPage.getTableProgressBar())
                .when("Opening 'Create New Open Pricing sidebar'")
                .openDirectPath(Path.CREATE_OPEN_PRICING)
                .waitSideBarOpened()
                .and("Enter data to all fields of sidebar")
                .selectFromDropdown(createOpenPricingSidebar.getPublisherNameDropdown(),
                        createOpenPricingSidebar.getPublisherNameDropdownItems(), publisher.getName())
                .setValue(createOpenPricingSidebar.getNameInput(), PRICING_NAME)
                .setValue(createOpenPricingSidebar.getFloorPriceField().getFloorPriceInput(), FLOOR_PRICE)
                .testEnd();
    }

    @Test(priority = 0)
    @Step("Add one device to new Pricing")
    public void addOneDeviceToPricingTest() {
        verifyItemSelectionInMultipane(createOpenPricingSidebar.getDeviceMultipane(), ONE_DEVICE_IS_INCLUDED.setQuantity(1));
    }

    @Test(priority = 1)
    @Step("Add one inventory to new Pricing")
    public void addOneInventoryToPricingTest() {
        verifyItemSelectionInMultipane(createOpenPricingSidebar.getInventoryMultipane(), ONE_MEDIA_IS_INCLUDED.setQuantity(1));
    }

    @Test(priority = 2)
    @Step("Add one operating system to new Pricing")
    public void addOneOperatingSystemToPricingTest() {
        verifyItemSelectionInMultipane(createOpenPricingSidebar.getOperatingSystemMultipane(), ONE_OPERATING_SYSTEM_IS_INCLUDED.setQuantity(1));
    }

    @Test(priority = 3)
    @Step("Add one geo to new Pricing")
    public void addOneGeoToPricingTest() {
        verifyItemSelectionInMultipane(createOpenPricingSidebar.getGeoMultipane(), ONE_GEO_IS_INCLUDED.setQuantity(1));
    }

    @Test(priority = 4)
    @Step("Add one adSize to new Pricing")
    public void addOneAdSizeToPricingTest() {
        verifyItemSelectionInMultipane(createOpenPricingSidebar.getAdSizeMultipane(), ONE_AD_SIZE_IS_INCLUDED.setQuantity(1));
    }

    @Test(priority = 5)
    @Step("Add one adFormat to new Pricing")
    public void addOneAdFormatToPricingTest() {
        verifyItemSelectionInMultipane(createOpenPricingSidebar.getAdFormatMultipane(), ONE_AD_FORMAT_IS_INCLUDED.setQuantity(1));
    }

    @Test(priority = 6)
    @Step("Add one demand Source to new Pricing")
    public void addOneDemandSourceToPricingTest() {
        verifyItemSelectionInMultipane(createOpenPricingSidebar.getDemandSourcesMultipane(), ONE_DEMAND_SOURCE_IS_INCLUDED.setQuantity(1));
    }

    @Test(priority = 7)
    @Step("Click on 'Save' button  open Pricing")
    public void saveOpenPricingTest() {
        testStart()
                .and("Checking functionality of 'Save' button")
                .clickOnWebElement(createOpenPricingSidebar.getSaveButton())
                .validate(not(visible), createOpenPricingSidebar.getSaveButton())
                .validate(visible, "Updated!")
                .waitSideBarClosed()
                .testEnd();
    }

    @Test(priority = 8, dependsOnMethods = "saveOpenPricingTest")
    @Step("Verify new Pricing in table")
    public void checkOpenPricingTableTest() {
        var tableData = openPricingPage.getOpenPricingTable().getTableData();

        testStart()
                .then("Checking data of newly created pricing in Table")
                .validate(tableData.getCustomCells(ColumnNames.NAME).get(0), PRICING_NAME)
                .validate(tableData.getCustomCells(ColumnNames.PUBLISHER).get(0), publisher.getName())
                .validate(tableData.getCustomCells(ColumnNames.FLOOR_PRICE).get(0), String.format("%s %s", FLOOR_PRICE, publisher.getCurrency()))
                .validate(tableData.getCustomCells(ColumnNames.STATUS).get(0), "Active")
                .testEnd();
    }

    @Test(priority = 9, dependsOnMethods = "saveOpenPricingTest")
    @Step("Verify 'createdBy' column in  Pricing in table")
    public void checkCreatedByTest() {
        var pricingTable = openPricingPage.getOpenPricingTable();
        var tableData = pricingTable.getTableData();

        testStart()
                .and("Adding 'createdBy' column in to Pricing  Table")
                .clickOnWebElement(pricingTable.getShowHideColumns().getShowHideColumnsBtn())
                .selectCheckBox(pricingTable.getShowHideColumns().getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .clickOnWebElement(openPricingPage.getMenuSidebar().getSidebarOptionByValue(MenuSidebarOptions.ADMIN))
                .then("Check that user under testing is presented in table")
                .validate(tableData.getCustomCells(ColumnNames.CREATED_BY).get(0), TEST_USER.getMail())
                .testEnd();
    }

    @Test(priority = 10, dependsOnMethods = "saveOpenPricingTest", enabled = false)
    @Step("Verify 'updatedBy' column  Pricing in table")
    public void checkUpdatedByTest() {
        var pricingTable = openPricingPage.getOpenPricingTable();
        var tableData = pricingTable.getTableData();

        testStart()
                .and("Adding 'updatedBy' column in to Pricing  Table")
                .clickOnWebElement(pricingTable.getShowHideColumns().getShowHideColumnsBtn())
                .selectCheckBox(pricingTable.getShowHideColumns().getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .clickOnWebElement(openPricingPage.getMenuSidebar().getSidebarOptionByValue(MenuSidebarOptions.ADMIN))
                .then("Check that user under testing is not presented in table")
                .validate(tableData.getCustomCells(ColumnNames.UPDATED_BY).get(0), EMPTY_STRING)
                .testEnd();
    }

    @Test(priority = 11, dependsOnMethods = "saveOpenPricingTest")
    @Step("Get info about newly created Open Pricing by API")
    public void getNewlyCreatedPricingByApi() {
        newlyCreatedPricing = OpenPricingPrecondition.openPricing()
                .getOpenPricingList()
                .build()
                .getOpenPricingGetAllResponse().getItems().stream()
                .filter(op -> op.getName().equals(PRICING_NAME))
                .findFirst()
                .orElseThrow(() ->
                        new NoSuchElementException(format("The Open Pricing with name '%s' wasn't found", PRICING_NAME)));
    }

    @Test(priority = 12, dependsOnMethods = {"saveOpenPricingTest", "getNewlyCreatedPricingByApi"})
    @Step("Verify 'Inventory' Items in Details' menu in Pricing table")
    public void checkInventoryMenuDetailsTest() {
        var tableData = openPricingPage.getOpenPricingTable().getTableData();
        var inventoryDetailsSection = pricingTableDetailsMenu.getInventoryDetailsSection();

        hoverMouseCursorOnDetailsIcon(tableData);

        var expectedMedia = getMediaById(newlyCreatedPricing.getRule().getMedia().getMedia().get(0)).getName();
        verifySelectionInDetailsMenuForTableItem(inventoryDetailsSection, expectedMedia);
    }

    @Test(priority = 13, dependsOnMethods = {"saveOpenPricingTest", "getNewlyCreatedPricingByApi"})
    @Step("Verify 'Device' Items in Details' menu in Pricing table")
    public void checkDeviceMenuDetailsTest() {
        var tableData = openPricingPage.getOpenPricingTable().getTableData();
        var deviceDetailsSection = pricingTableDetailsMenu.getDeviceDetailsSection();

        hoverMouseCursorOnDetailsIcon(tableData);

        var expectedDeice = getDeviceFromDeviceList(0).getName();
        verifySelectionInDetailsMenuForTableItem(deviceDetailsSection, expectedDeice);
    }

    @Test(priority = 14, dependsOnMethods = {"saveOpenPricingTest", "getNewlyCreatedPricingByApi"})
    @Step("Verify 'Os' Items in Details' menu in Pricing table")
    public void checkOsMenuDetailsTest() {
        var tableData = openPricingPage.getOpenPricingTable().getTableData();
        var operatingSystemsDetailsSection = pricingTableDetailsMenu.getOperatingSystemDetailsSection();

        hoverMouseCursorOnDetailsIcon(tableData);

        var expectedOperatingSystem = getOperatingSystemFromDeviceList(0).getName();
        verifySelectionInDetailsMenuForTableItem(operatingSystemsDetailsSection, expectedOperatingSystem);
    }

    private void hoverMouseCursorOnDetailsIcon(TableData tableData) {
        testStart()
                .and("Hovering mouse cursor on 'Details' column in Pricing Table")
                .hoverMouseOnWebElement(tableData.getCellByPositionInTable(ColumnNames.DETAILS, 0))
                .testEnd();
    }

    private void verifySelectionInDetailsMenuForTableItem(DetailsSection detailsSection, String... expectedItemNames) {

        AtomicInteger currentItemPosition = new AtomicInteger();

        Stream.of(expectedItemNames).forEach(item -> {
            testStart()
                    .then(format("Check that %s with name '%s' is presented in 'Details' Menu on %s position",
                            detailsSection.getDetailsSection().getName(), item, currentItemPosition))
                    .validate(visible, detailsSection.getMenuItemByPositionInList(currentItemPosition.get()).getName())
                    .validate(detailsSection.getMenuItemByPositionInList(currentItemPosition.get()).getName(),
                            expectedItemNames[currentItemPosition.get()])
                    .validate(visible, detailsSection.getMenuItemByPositionInList(currentItemPosition.get()).getIncludedIcon())
                    .validate(not(visible), detailsSection.getMenuItemByPositionInList(currentItemPosition.get()).getExcludedIcon())
                    .testEnd();
            currentItemPosition.getAndIncrement();
        });
    }

    private void verifyItemSelectionInMultipane(Multipane multipane, String expectedPanelNameLabel) {

        testStart()
                .clickOnWebElement(multipane.getPanelNameLabel())
                .testEnd();

        var firstItemToSelect = multipane.getSelectTableItemByPositionInList(0);
        var demandSourceRelatedOnlyCondition =
                multipane.getMultipaneName().equals(MultipaneNameImpl.DEMAND_SOURCES) ? visible : not(visible);

        testStart()
                .then(format("Validate initial state of Items to select in '%s' Multipane", multipane.getMultipaneName()))
                .scrollIntoView(firstItemToSelect.getName())
                .hoverMouseOnWebElement(firstItemToSelect.getName())
                .validate(not(visible), firstItemToSelect.getExcludedIcon())
                .validate(not(visible), firstItemToSelect.getIncludedIcon())
                .validate(demandSourceRelatedOnlyCondition, firstItemToSelect.getActiveIcon())
                .validate(not(visible), firstItemToSelect.getInactiveIcon())
                .validate(demandSourceRelatedOnlyCondition, firstItemToSelect.getAssociatedWithPublisherIcon())
                .validate(visible, firstItemToSelect.getIncludeButton())
                .validate(not(visible), firstItemToSelect.getExcludeButton())
                .and()
                .clickOnWebElement(firstItemToSelect.getIncludeButton())
                .then(format("Verify available %s' list", multipane.getMultipaneName()))
                .validate(visible, firstItemToSelect.getIncludedIcon())
                .validate(not(visible), firstItemToSelect.getExcludedIcon())
                .validate(demandSourceRelatedOnlyCondition, firstItemToSelect.getActiveIcon())
                .validate(not(visible), firstItemToSelect.getInactiveIcon())
                .validate(demandSourceRelatedOnlyCondition, firstItemToSelect.getAssociatedWithPublisherIcon())
                .validate(not(visible), firstItemToSelect.getIncludeButton())
                .validate(not(visible), firstItemToSelect.getExcludeButton())
                .testEnd();

        var firstSelectedItem = multipane.getIncludedExcludedTableItemByPositionInList(0);

        testStart()
                .then(format("Verify added %s' List", multipane.getMultipaneName()))
                .validate(visible, firstSelectedItem.getName())
                //Todo enable after refactor
                //.validate(not(visible), firstSelectedItem.getType())
                .validate(not(visible), firstSelectedItem.getParentLabel())
                .validate(visible, firstSelectedItem.getRemoveButton())
                .validate(enabled, multipane.getClearAllButton())
                .and(format("Collapse '%s' Multipane", multipane.getMultipaneName()))
                .clickOnWebElement(multipane.getPanelNameLabel())
                .then(format("Validate '%s' Multipane Panel Strings", multipane.getMultipaneName()))
                .validateContainsText(multipane.getPanelNameLabel(), expectedPanelNameLabel)
                .testEnd();
    }

    private Media getMediaById(int mediaId) {

        return media()
                .getMediaById(mediaId)
                .build()
                .getMediaResponse();
    }

    private Device getDeviceFromDeviceList(int devicePosition) {

        return DevicePrecondition.device()
                .getDeviceLList()
                .build()
                .getDeviceGetAllResponse()
                .getItems().stream()
                .sorted(Comparator.comparing(Device::getName))
                .collect(Collectors.toList())
                .get(devicePosition);
    }

    private OperatingSystem getOperatingSystemFromDeviceList(int operatingSystemPosition) {

        return OperatingSystemPrecondition.operatingSystem()
                .getOperatingSystemList()
                .build()
                .getOperatingSystemGetAllResponse()
                .getItems().stream()
                .sorted(Comparator.comparing(OperatingSystem::getName))
                .collect(Collectors.toList())
                .get(operatingSystemPosition);
    }

    @AfterClass(alwaysRun = true)
    private void deleteEntities() {

        publisher()
                .setCredentials(USER_FOR_DELETION)
                .deletePublisher(publisher.getId())
                .build();

        media()
                .setCredentials(USER_FOR_DELETION)
                .deleteMedia(media1.getId())
                .build();

        media()
                .setCredentials(USER_FOR_DELETION)
                .deleteMedia(media2.getId())
                .build();
    }
}
