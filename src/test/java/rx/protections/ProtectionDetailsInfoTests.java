package rx.protections;

import api.dto.rx.inventory.adspot.AdSpot;
import api.dto.rx.inventory.media.Media;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.protections.ProtectionsPage;
import rx.BaseTest;
import rx.enums.MultipaneConstants;
import widgets.common.adSizes.AdSizesList;
import widgets.common.adformats.AdFormatsList;
import widgets.common.detailsmenu.DetailsSectionName;
import widgets.common.detailsmenu.menu.sections.DetailsSection;
import widgets.common.devices.DeviceList;
import widgets.common.operatingsystems.OperatingSystemsList;
import widgets.common.table.ColumnNames;
import widgets.protections.sidebar.CreateProtectionSidebar;
import widgets.protections.sidebar.EditProtectionsSidebar;
import widgets.protections.sidebar.ProtectionTypesList;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static api.preconditionbuilders.AdSpotPrecondition.adSpot;
import static api.preconditionbuilders.MediaPrecondition.media;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static configurations.User.USER_FOR_DELETION;
import static java.lang.String.format;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Protections")
public class ProtectionDetailsInfoTests extends BaseTest {

    private ProtectionsPage protectionsPage;
    private EditProtectionsSidebar editProtectionsSidebar;
    private CreateProtectionSidebar createProtectionSidebar;

    private AdSpot adSpot;
    private Media mediaOnly;
    private String mediaName;
    private String protectionName;
    private String publisherName;

    private static final String GEO_1 = "Angola";
    private static final String GEO_2 = "Benin";
    private static final String DSP_1 = "LoopMe";
    private static final String DEVICE_2 = DeviceList.PHONE.getDevice();
    private static final String OS_1 = OperatingSystemsList.IOS.getName();
    private static final String AD_SIZE_1 = AdSizesList.A120x60.getSize();
    private static final String AD_SIZE_2 = AdSizesList.A750x1134.getSize();
    private static final String AD_FORMAT_1 = AdFormatsList.NATIVE.getName();
    private static final String AD_FORMAT_2 = AdFormatsList.BANNER.getName();
    private static final String OS_2 = OperatingSystemsList.ANDROID.getName();
    private static final String DEVICE_1 = DeviceList.CONNECTED_DEVICE.getDevice();

    public ProtectionDetailsInfoTests() {
        protectionsPage = new ProtectionsPage();
        editProtectionsSidebar = new EditProtectionsSidebar();
        createProtectionSidebar = new CreateProtectionSidebar();
    }

    @BeforeClass
    private void loginAndCreateProtection() {
        adSpot = adSpot()
                .createNewAdSpot(captionWithSuffix("autoProtectionAdSpot"))
                .build()
                .getAdSpotResponse();

        mediaOnly = media()
                .createNewMedia("media", adSpot.getPublisherId(), true)
                .build()
                .getMediaResponse();

        mediaName = adSpot.getMediaName();
        publisherName = adSpot.getPublisherName();
        protectionName = captionWithSuffix("autoProtection");

        testStart()
                .given()
                .openDirectPath(Path.PROTECTIONS)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, protectionsPage.getNuxtProgress())
                .and("Click on 'Create Protection' button")
                .clickOnWebElement(protectionsPage.getCreateProtectionButton())
                .waitSideBarOpened()
                .and(String.format("Select Publisher %s", publisherName))
                .and("Enter data to all fields of sidebar")
                .selectFromDropdown(createProtectionSidebar.getPublisherNameDropdown(),
                        createProtectionSidebar.getPublisherItems(), publisherName)
                .setValue(createProtectionSidebar.getNameInput(), protectionName)
                .selectFromDropdown(createProtectionSidebar.getProtectionTypeDropdown(),
                        createProtectionSidebar.getProtectionTypeItems(), ProtectionTypesList.SUPPLY_BLOCKS.getType())
                .clickOnWebElement(createProtectionSidebar.getSaveButton())
                .waitSideBarClosed()
                .then("Errors are not appear")
                .waitAndValidate(not(visible), protectionsPage.getToasterMessage().getPanelError())
                .testEnd();
    }

    @Test(description = "Included items by default", priority = 1)
    public void checkDetailsByDefaultIncludeAll() {
        var table = protectionsPage.getProtectionsTable();
        var tableData = protectionsPage.getProtectionsTable().getTableData();

        var geoMultipane = editProtectionsSidebar.getGeoMultipane();
        var deviceMultipane = editProtectionsSidebar.getDeviceMultipane();
        var adSizeMultipane = editProtectionsSidebar.getAdSizeMultipane();
        var dspMultipane = editProtectionsSidebar.getDemandSourcesMultipane();
        var adFormatMultipane = editProtectionsSidebar.getAdFormatMultipane();
        var osMultipane = editProtectionsSidebar.getOperatingSystemMultipane();
        var inventoryMultipane = editProtectionsSidebar.getInventoryMultipane();

        testStart()
                .and(format("Search protection by name %s", protectionName))
                .setValueWithClean(tableData.getSearch(), protectionName)
                .validateContainsText(table.getTablePagination().getPaginationPanel(), ("1-1 of 1"))
                .and("Open edit sidebar")
                .clickOnTableCellLink(tableData, ColumnNames.NAME, protectionName)
                .waitSideBarOpened()
                .and("Expand Inventory Pane and click on Clear All")
                .clickOnWebElement(inventoryMultipane.getPanelNameLabel())
                .clickOnWebElement(inventoryMultipane.getIncludeAllButton())
                .clickOnWebElement(inventoryMultipane.getClearAllButton())
                .and("Expand Device Pane and click on Clear All")
                .clickOnWebElement(deviceMultipane.getPanelNameLabel())
                .clickOnWebElement(deviceMultipane.getIncludeAllButton())
                .clickOnWebElement(deviceMultipane.getClearAllButton())
                .and("Expand Operating System Pane and click on Clear All")
                .clickOnWebElement(osMultipane.getPanelNameLabel())
                .clickOnWebElement(osMultipane.getIncludeAllButton())
                .clickOnWebElement(osMultipane.getClearAllButton())
                .and("Expand Ad Format Pane and click on Include All")
                .clickOnWebElement(adFormatMultipane.getPanelNameLabel())
                .clickOnWebElement(adFormatMultipane.getIncludeAllButton())
                .clickOnWebElement(adFormatMultipane.getClearAllButton())
                .and("Expand Ad Size Pane and click on Include All")
                .clickOnWebElement(adSizeMultipane.getPanelNameLabel())
                .clickOnWebElement(adSizeMultipane.getIncludeAllButton())
                .clickOnWebElement(adSizeMultipane.getClearAllButton())
                .and("Expand Geo Pane and click on Include All")
                .clickOnWebElement(geoMultipane.getPanelNameLabel())
                .clickOnWebElement(geoMultipane.getIncludeAllButton())
                .clickOnWebElement(geoMultipane.getClearAllButton())
                .and("Expand Demand Source Pane and click on Include All")
                .clickOnWebElement(dspMultipane.getPanelNameLabel())
                .clickOnWebElement(dspMultipane.getIncludeAllButton())
                .clickOnWebElement(dspMultipane.getClearAllButton())
                .and("Save auction and close")
                .clickOnWebElement(editProtectionsSidebar.getSaveButton())
                .waitSideBarClosed()
                .and("Hovering over 'Details' column in Protection Table")
                .hoverMouseOnWebElement(tableData.getCellByPositionInTable(ColumnNames.DETAILS, 0))
                .testEnd();
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.GEO), MultipaneConstants.ALL_GEOS_ARE_INCLUDED.setQuantity(0));
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.DEVICE), MultipaneConstants.ALL_DEVICES_ARE_INCLUDED.setQuantity(0));
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.AD_SIZE), MultipaneConstants.ALL_AD_SIZES_ARE_INCLUDED.setQuantity(0));
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.INVENTORY), MultipaneConstants.ALL_INVENTORY_IS_INCLUDED.setQuantity(0));
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.AD_FORMAT), MultipaneConstants.ALL_AD_FORMATS_ARE_INCLUDED.setQuantity(0));
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.DEMAND_SOURCES), MultipaneConstants.ALL_DEMAND_SOURCES_ARE_INCLUDED.setQuantity(0));
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.OPERATING_SYSTEM), MultipaneConstants.ALL_OPERATING_SYSTEMS_ARE_INCLUDED.setQuantity(0));
    }

    @Test(description = "Include All and check Details", priority = 2, dependsOnMethods = "checkDetailsByDefaultIncludeAll")
    public void checkDetailsByClickOnIncludeAll() {
        var tableData = protectionsPage.getProtectionsTable().getTableData();

        var geoMultipane = editProtectionsSidebar.getGeoMultipane();
        var deviceMultipane = editProtectionsSidebar.getDeviceMultipane();
        var adSizeMultipane = editProtectionsSidebar.getAdSizeMultipane();
        var adFormatMultipane = editProtectionsSidebar.getAdFormatMultipane();
        var dspMultipane = editProtectionsSidebar.getDemandSourcesMultipane();
        var osMultipane = editProtectionsSidebar.getOperatingSystemMultipane();
        var inventoryMultipane = editProtectionsSidebar.getInventoryMultipane();

        testStart()
                .and("Open edit sidebar")
                .hoverMouseOnWebElement(tableData.getCellByPositionInTable(ColumnNames.ID, 0))
                .clickOnTableCellLink(tableData, ColumnNames.NAME, protectionName)
                .waitSideBarOpened()
                .and("Expand Inventory Pane and click on Include All")
                .clickOnWebElement(inventoryMultipane.getPanelNameLabel())
                .clickOnWebElement(inventoryMultipane.getIncludeAllButton())
                .and("Expand Device Pane and click on Include All")
                .clickOnWebElement(deviceMultipane.getPanelNameLabel())
                .clickOnWebElement(deviceMultipane.getIncludeAllButton())
                .and("Expand Operating System Pane and click on Include All")
                .clickOnWebElement(osMultipane.getPanelNameLabel())
                .clickOnWebElement(osMultipane.getIncludeAllButton())
                .and("Expand Ad Format Pane and click on Include All")
                .clickOnWebElement(adFormatMultipane.getPanelNameLabel())
                .clickOnWebElement(adFormatMultipane.getIncludeAllButton())
                .and("Expand Ad Size Pane and click on Include All")
                .clickOnWebElement(adSizeMultipane.getPanelNameLabel())
                .clickOnWebElement(adSizeMultipane.getIncludeAllButton())
                .and("Expand Geo Pane and click on Include All")
                .clickOnWebElement(geoMultipane.getPanelNameLabel())
                .clickOnWebElement(geoMultipane.getIncludeAllButton())
                .and("Expand Demand Source Pane and click on Include All")
                .clickOnWebElement(dspMultipane.getPanelNameLabel())
                .turnToggleOn(dspMultipane.getShowInactive())
                .clickOnWebElement(dspMultipane.getIncludeAllButton())
                .and("Save auction and close")
                .clickOnWebElement(editProtectionsSidebar.getSaveButton())
                .waitSideBarClosed()
                .then("Errors are not appear")
                .waitAndValidate(not(visible), protectionsPage.getToasterMessage().getPanelError())
                .and("Hovering over 'Details' column in Protection Table")
                .hoverMouseOnWebElement(tableData.getCellByPositionInTable(ColumnNames.DETAILS, 0))
                .testEnd();

        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.GEO), MultipaneConstants.ALL_GEOS_ARE_INCLUDED.setQuantity(0));
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.DEVICE), MultipaneConstants.ALL_DEVICES_ARE_INCLUDED.setQuantity(0));
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.AD_SIZE), MultipaneConstants.ALL_AD_SIZES_ARE_INCLUDED.setQuantity(0));
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.INVENTORY), MultipaneConstants.ALL_INVENTORY_IS_INCLUDED.setQuantity(0));
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.AD_FORMAT), MultipaneConstants.ALL_AD_FORMATS_ARE_INCLUDED.setQuantity(0));
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.DEMAND_SOURCES), MultipaneConstants.ALL_DEMAND_SOURCES_ARE_INCLUDED.setQuantity(0));
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.OPERATING_SYSTEM), MultipaneConstants.ALL_OPERATING_SYSTEMS_ARE_INCLUDED.setQuantity(0));
    }

    // TODO: Multipane Issue GS-3706
    @Ignore
    @Test(description = "Include part items and check Details", priority = 3, dependsOnMethods = "checkDetailsByClickOnIncludeAll")
    public void checkDetailsWithNotAllIncluded() {
        var tableData = protectionsPage.getProtectionsTable().getTableData();

        var geoMultipane = editProtectionsSidebar.getGeoMultipane();
        var deviceMultipane = editProtectionsSidebar.getDeviceMultipane();
        var adSizeMultipane = editProtectionsSidebar.getAdSizeMultipane();
        var dspMultipane = editProtectionsSidebar.getDemandSourcesMultipane();
        var adFormatMultipane = editProtectionsSidebar.getAdFormatMultipane();
        var osMultipane = editProtectionsSidebar.getOperatingSystemMultipane();
        var inventoryMultipane = editProtectionsSidebar.getInventoryMultipane();

        testStart()
                .and("Open edit sidebar")
                .hoverMouseOnWebElement(tableData.getCellByPositionInTable(ColumnNames.ID, 0))
                .clickOnTableCellLink(tableData, ColumnNames.NAME, protectionName)
                .waitSideBarOpened()
                .and("Expand Inventory Pane and click on Include All")
                .clickOnWebElement(inventoryMultipane.getPanelNameLabel())
                .clickOnWebElement(inventoryMultipane.getClearAllButton())
                .and(String.format("Include media %s", mediaOnly.getName()))
                .hoverMouseOnWebElement(inventoryMultipane.getSelectTableItemByName(mediaOnly.getName()).getName())
                .waiter(visible, inventoryMultipane.getSelectTableItemByName(mediaOnly.getName()).getIncludeButton())
                .clickOnWebElement(inventoryMultipane.getSelectTableItemByName(mediaOnly.getName()).getIncludeButton())
                .clickOnWebElement(inventoryMultipane.getSelectTableItemByName(mediaOnly.getName()).getIncludedIcon())
                .and("Expand Device Pane and click on Include All")
                .clickOnWebElement(deviceMultipane.getPanelNameLabel())
                .clickOnWebElement(deviceMultipane.getClearAllButton())
                .hoverMouseOnWebElement(deviceMultipane.getSelectTableItemByName(DEVICE_1).getName())
                .waiter(visible, deviceMultipane.getSelectTableItemByName(DEVICE_1).getIncludeButton())
                .clickOnWebElement(deviceMultipane.getSelectTableItemByName(DEVICE_1).getIncludedIcon())
                .and("Expand Operating System Pane and click on Include All")
                .clickOnWebElement(osMultipane.getPanelNameLabel())
                .clickOnWebElement(osMultipane.getClearAllButton())
                .hoverMouseOnWebElement(osMultipane.getSelectTableItemByName(OS_1).getName())
                .waiter(visible, osMultipane.getSelectTableItemByName(OS_1).getIncludeButton())
                .clickOnWebElement(osMultipane.getSelectTableItemByName(OS_1).getIncludedIcon())
                .clickOnWebElement(osMultipane.getSelectTableItemByName(OS_1).getIncludedIcon())
                .and("Expand Ad Format Pane and click on Include All")
                .clickOnWebElement(adFormatMultipane.getPanelNameLabel())
                .clickOnWebElement(adFormatMultipane.getClearAllButton())
                .clickOnWebElement(adFormatMultipane.getSelectTableItemByName(AD_FORMAT_1).getIncludedIcon())
                .and("Expand Ad Size Pane and click on Include All")
                .clickOnWebElement(adSizeMultipane.getPanelNameLabel())
                .clickOnWebElement(adSizeMultipane.getClearAllButton())
                .clickOnWebElement(adSizeMultipane.getSelectTableItemByName(AD_SIZE_1).getIncludedIcon())
                .and("Expand Geo Pane and click on Include All")
                .clickOnWebElement(geoMultipane.getPanelNameLabel())
                .clickOnWebElement(geoMultipane.getClearAllButton())
                .clickOnWebElement(geoMultipane.getSelectTableItemByName(GEO_1).getIncludedIcon())
                .and("Expand Demand Source Pane and click on Include All")
                .clickOnWebElement(dspMultipane.getPanelNameLabel())
                .clickOnWebElement(dspMultipane.getClearAllButton())
                .turnToggleOn(dspMultipane.getShowInactive())
                .clickOnWebElement(dspMultipane.getSelectTableItemByName(DSP_1).getIncludedIcon())
                .and("Save auction and close")
                .clickOnWebElement(editProtectionsSidebar.getSaveButton())
                .waitSideBarClosed()
                .then("Errors are not appear")
                .waitAndValidate(not(visible), protectionsPage.getToasterMessage().getPanelError())
                .and("Hovering over 'Details' column in Protections Table")
                .hoverMouseOnWebElement(tableData.getCellByPositionInTable(ColumnNames.DETAILS, 0))
                .testEnd();

        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.DEVICE), DEVICE_1);
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.AD_SIZE), AD_SIZE_1);
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.AD_FORMAT), AD_FORMAT_1);
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.OPERATING_SYSTEM), OS_1);
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.GEO), GEO_1);
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.DEMAND_SOURCES), DSP_1);
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.INVENTORY), mediaOnly.getName());
    }

    //TODO; inventory multipane need to improve for protections GS-3706
    @Ignore
    @Test(description = "Check Inventory Details", priority = 4)
    public void checkDetailsInventoryAdSpotOnly() {
        var tableData = protectionsPage.getProtectionsTable().getTableData();

        var inventoryMultipane = editProtectionsSidebar.getInventoryMultipane();

        testStart()
                .and("Open edit sidebar")
                .hoverMouseOnWebElement(tableData.getCellByPositionInTable(ColumnNames.ID, 0))
                .clickOnTableCellLink(tableData, ColumnNames.NAME, protectionName)
                .waitSideBarOpened()
                .and("Expand Inventory Pane and click on Include All")
                .clickOnWebElement(inventoryMultipane.getPanelNameLabel())
                .clickOnWebElement(inventoryMultipane.getClearAllButton())
                .clickOnWebElement(inventoryMultipane.getSelectTableItemByName(mediaName).getExpandInnerItemButton())
                .clickOnWebElement(inventoryMultipane.getSelectChildTableItemByName(adSpot.getName()).getName())
                .and("Save auction and close")
                .clickOnWebElement(editProtectionsSidebar.getSaveButton())
                .waitSideBarClosed()
                .then("Errors are not appear")
                .waitAndValidate(not(visible), protectionsPage.getToasterMessage().getPanelError())
                .and("Hovering mouse cursor on 'Details' column in Protections Table")
                .hoverMouseOnWebElement(tableData.getCellByPositionInTable(ColumnNames.DETAILS, 0))
                .testEnd();

        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.INVENTORY), adSpot.getName());
    }

    private void verifySelectionInDetailsMenuForTableItem(DetailsSection detailsSection, String... expectedItemNames) {

        AtomicInteger currentItemPosition = new AtomicInteger();

        Stream.of(expectedItemNames).forEach(item -> {

            var currentActualItem = detailsSection.getMenuItemByPositionInList(currentItemPosition.get());

            testStart()
                    .then(format("Check that %s with name '%s' is presented in 'Details' Menu on %s position",
                            detailsSection.getDetailsSection().getName(), item, currentItemPosition))
                    .validate(visible, currentActualItem.getName())
                    .validate(currentActualItem.getName(),
                            expectedItemNames[currentItemPosition.get()])
                    .validate(visible, currentActualItem.getIncludedIcon())
                    .validate(not(visible), currentActualItem.getExcludedIcon())
                    .testEnd();
            currentItemPosition.getAndIncrement();
        });
    }

    @AfterClass(alwaysRun = true)
    private void deleteInventory() {

        adSpot()
                .setCredentials(USER_FOR_DELETION)
                .deleteAdSpot(adSpot.getId())
                .build();

        media()
                .setCredentials(USER_FOR_DELETION)
                .deleteMedia(mediaOnly.getId())
                .build();

        media()
                .setCredentials(USER_FOR_DELETION)
                .deleteMedia(adSpot.getMediaId())
                .build();
    }
}
