package rx.yield.openpricing;

import api.dto.rx.inventory.adspot.AdSpot;
import api.dto.rx.inventory.media.Media;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Path;
import pages.yield.openpricing.OpenPricingPage;
import rx.BaseTest;
import rx.enums.MultipaneConstants;
import widgets.common.adSizes.AdSizesList;
import widgets.common.adformats.AdFormatsList;
import widgets.common.detailsmenu.DetailsSectionName;
import widgets.common.detailsmenu.menu.sections.DetailsSection;
import widgets.common.devices.DeviceList;
import widgets.common.operatingsystems.OperatingSystemsList;
import widgets.common.table.ColumnNames;
import widgets.yield.openPricing.sidebar.CreateOpenPricingSidebar;
import widgets.yield.openPricing.sidebar.EditOpenPricingSidebar;

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
@Feature(value = "Open Pricing")
public class OpenPricingDetailsInfoTests extends BaseTest {

    private OpenPricingPage openPricingPage;
    private EditOpenPricingSidebar editOpenPricingSidebar;
    private CreateOpenPricingSidebar createOpenPricingSidebar;

    private AdSpot adSpot;
    private Media mediaOnly;
    private String mediaName;
    private String pricingName;
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
    private static final String AD_SIZE_FULL_NAME_1 = AdSizesList.A120x60.getFullName();
    private static final String AD_SIZE_FULL_NAME_2 = AdSizesList.A750x1134.getFullName();

    public OpenPricingDetailsInfoTests() {
        openPricingPage = new OpenPricingPage();
        editOpenPricingSidebar = new EditOpenPricingSidebar();
        createOpenPricingSidebar = new CreateOpenPricingSidebar();
    }

    @BeforeClass
    private void loginAndCreateOpenPricing() {
        adSpot = adSpot()
                .createNewAdSpot(captionWithSuffix("autoPricingAdSpot"))
                .build()
                .getAdSpotResponse();

        mediaOnly = media()
                .createNewMedia("media", adSpot.getPublisherId(), true)
                .build()
                .getMediaResponse();

        mediaName = adSpot.getMediaName();
        publisherName = adSpot.getPublisherName();
        pricingName = captionWithSuffix("autoPricing");

        testStart()
                .given()
                .openDirectPath(Path.OPEN_PRICING)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                .and("Click on 'Create Open Pricing' button")
                .clickOnWebElement(openPricingPage.getCreateOpenPricingButton())
                .waitSideBarOpened()
                .and(String.format("Select Publisher %s", publisherName))
                .when("Opening 'Create New Open Pricing sidebar'")
                .openDirectPath(Path.CREATE_OPEN_PRICING)
                .waitSideBarOpened()
                .and("Enter data to all fields of sidebar")
                .selectFromDropdown(createOpenPricingSidebar.getPublisherNameDropdown(),
                        createOpenPricingSidebar.getPublisherNameDropdownItems(), publisherName)
                .setValue(createOpenPricingSidebar.getNameInput(), pricingName)
                .setValue(createOpenPricingSidebar.getFloorPriceField().getFloorPriceInput(), "2.25")
                .clickOnWebElement(createOpenPricingSidebar.getSaveButton())
                .waitSideBarClosed()
                .then("Errors are not appear")
                .waitAndValidate(not(visible), openPricingPage.getToasterMessage().getPanelError())
                .testEnd();
    }

    @Test(description = "Included items by default", priority = 1)
    public void checkDetailsByDefaultIncludeAll() {
        var table = openPricingPage.getOpenPricingTable();
        var tableData = openPricingPage.getOpenPricingTable().getTableData();

        var geoMultipane = editOpenPricingSidebar.getGeoMultipane();
        var deviceMultipane = editOpenPricingSidebar.getDeviceMultipane();
        var adSizeMultipane = editOpenPricingSidebar.getAdSizeMultipane();
        var dspMultipane = editOpenPricingSidebar.getDemandSourcesMultipane();
        var adFormatMultipane = editOpenPricingSidebar.getAdFormatMultipane();
        var osMultipane = editOpenPricingSidebar.getOperatingSystemMultipane();
        var inventoryMultipane = editOpenPricingSidebar.getInventoryMultipane();

        testStart()
                .and(format("Search auction by name %s", pricingName))
                .setValueWithClean(tableData.getSearch(), pricingName)
                .validateContainsText(table.getTablePagination().getPaginationPanel(), ("1-1 of 1"))
                .and("Open edit sidebar")
                .clickOnTableCellLink(tableData, ColumnNames.NAME, pricingName)
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
                .clickOnWebElement(editOpenPricingSidebar.getSaveButton())
                .waitSideBarClosed()
                .and("Hovering over 'Details' column in Open Pricing Table")
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

    @Test(description = "Include All and check Details", priority = 2)
    public void checkDetailsByClickOnIncludeAll() {
        var tableData = openPricingPage.getOpenPricingTable().getTableData();

        var geoMultipane = editOpenPricingSidebar.getGeoMultipane();
        var deviceMultipane = editOpenPricingSidebar.getDeviceMultipane();
        var adSizeMultipane = editOpenPricingSidebar.getAdSizeMultipane();
        var adFormatMultipane = editOpenPricingSidebar.getAdFormatMultipane();
        var dspMultipane = editOpenPricingSidebar.getDemandSourcesMultipane();
        var osMultipane = editOpenPricingSidebar.getOperatingSystemMultipane();
        var inventoryMultipane = editOpenPricingSidebar.getInventoryMultipane();

        testStart()
                .and("Open edit sidebar")
                .hoverMouseOnWebElement(tableData.getCellByPositionInTable(ColumnNames.ID, 0))
                .clickOnTableCellLink(tableData, ColumnNames.NAME, pricingName)
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
                .clickOnWebElement(editOpenPricingSidebar.getSaveButton())
                .waitSideBarClosed()
                .then("Errors are not appear")
                .waitAndValidate(not(visible), openPricingPage.getToasterMessage().getPanelError())
                .and("Hovering over 'Details' column in Open Pricing Table")
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

    @Test(description = "Include part items and check Details", priority = 3)
    public void checkDetailsWithNotAllIncluded() {
        var tableData = openPricingPage.getOpenPricingTable().getTableData();

        var geoMultipane = editOpenPricingSidebar.getGeoMultipane();
        var deviceMultipane = editOpenPricingSidebar.getDeviceMultipane();
        var adSizeMultipane = editOpenPricingSidebar.getAdSizeMultipane();
        var dspMultipane = editOpenPricingSidebar.getDemandSourcesMultipane();
        var adFormatMultipane = editOpenPricingSidebar.getAdFormatMultipane();
        var osMultipane = editOpenPricingSidebar.getOperatingSystemMultipane();
        var inventoryMultipane = editOpenPricingSidebar.getInventoryMultipane();

        testStart()
                .and("Open edit sidebar")
                .hoverMouseOnWebElement(tableData.getCellByPositionInTable(ColumnNames.ID, 0))
                .clickOnTableCellLink(tableData, ColumnNames.NAME, pricingName)
                .waitSideBarOpened()
                .and("Expand Inventory Pane and click on Include All")
                .clickOnWebElement(inventoryMultipane.getPanelNameLabel())
                .clickOnWebElement(inventoryMultipane.getClearAllButton())
                .clickOnWebElement(inventoryMultipane.getSelectTableItemByName(mediaOnly.getName()).getName())
                .and("Expand Device Pane and click on Include All")
                .clickOnWebElement(deviceMultipane.getPanelNameLabel())
                .clickOnWebElement(deviceMultipane.getClearAllButton())
                .clickOnWebElement(deviceMultipane.getSelectTableItemByName(DEVICE_1).getName())
                .clickOnWebElement(deviceMultipane.getSelectTableItemByName(DEVICE_2).getName())
                .and("Expand Operating System Pane and click on Include All")
                .clickOnWebElement(osMultipane.getPanelNameLabel())
                .clickOnWebElement(osMultipane.getClearAllButton())
                .clickOnWebElement(osMultipane.getSelectTableItemByName(OS_1).getName())
                .clickOnWebElement(osMultipane.getSelectTableItemByName(OS_2).getName())
                .and("Expand Ad Format Pane and click on Include All")
                .clickOnWebElement(adFormatMultipane.getPanelNameLabel())
                .clickOnWebElement(adFormatMultipane.getClearAllButton())
                .clickOnWebElement(adFormatMultipane.getSelectTableItemByName(AD_FORMAT_1).getName())
                .clickOnWebElement(adFormatMultipane.getSelectTableItemByName(AD_FORMAT_2).getName())
                .and("Expand Ad Size Pane and click on Include All")
                .clickOnWebElement(adSizeMultipane.getPanelNameLabel())
                .clickOnWebElement(adSizeMultipane.getClearAllButton())
                .clickOnWebElement(adSizeMultipane.getSelectTableItemByName(AD_SIZE_1).getName())
                .clickOnWebElement(adSizeMultipane.getSelectTableItemByName(AD_SIZE_2).getName())
                .and("Expand Geo Pane and click on Include All")
                .clickOnWebElement(geoMultipane.getPanelNameLabel())
                .clickOnWebElement(geoMultipane.getClearAllButton())
                .clickOnWebElement(geoMultipane.getSelectTableItemByName(GEO_1).getName())
                .clickOnWebElement(geoMultipane.getSelectTableItemByName(GEO_2).getName())
                .and("Expand Demand Source Pane and click on Include All")
                .clickOnWebElement(dspMultipane.getPanelNameLabel())
                .clickOnWebElement(dspMultipane.getClearAllButton())
                .turnToggleOn(dspMultipane.getShowInactive())
                .clickOnWebElement(dspMultipane.getSelectTableItemByName(DSP_1).getName())
                .and("Save auction and close")
                .clickOnWebElement(editOpenPricingSidebar.getSaveButton())
                .waitSideBarClosed()
                .then("Errors are not appear")
                .waitAndValidate(not(visible), openPricingPage.getToasterMessage().getPanelError())
                .and("Hovering over 'Details' column in Private Auction Table")
                .hoverMouseOnWebElement(tableData.getCellByPositionInTable(ColumnNames.DETAILS, 0))
                .testEnd();

        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.GEO), GEO_1, GEO_2);
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.DEMAND_SOURCES), DSP_1);
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.DEVICE), DEVICE_2, DEVICE_1);
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.OPERATING_SYSTEM), OS_1, OS_2);
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.INVENTORY), mediaOnly.getName());
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.AD_FORMAT), AD_FORMAT_2, AD_FORMAT_1);
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.AD_SIZE), AD_SIZE_FULL_NAME_1, AD_SIZE_FULL_NAME_2);
    }

    @Test(description = "Check Inventory Details", priority = 4)
    public void checkDetailsInventoryAdSpotOnly() {
        var tableData = openPricingPage.getOpenPricingTable().getTableData();

        var inventoryMultipane = editOpenPricingSidebar.getInventoryMultipane();

        testStart()
                .and("Open edit sidebar")
                .hoverMouseOnWebElement(tableData.getCellByPositionInTable(ColumnNames.ID, 0))
                .clickOnTableCellLink(tableData, ColumnNames.NAME, pricingName)
                .waitSideBarOpened()
                .and("Expand Inventory Pane and click on Include All")
                .clickOnWebElement(inventoryMultipane.getPanelNameLabel())
                .clickOnWebElement(inventoryMultipane.getClearAllButton())
                .clickOnWebElement(inventoryMultipane.getSelectTableItemByName(mediaName).getExpandInnerItemButton())
                .clickOnWebElement(inventoryMultipane.getSelectChildTableItemByName(adSpot.getName()).getName())
                .and("Save auction and close")
                .clickOnWebElement(editOpenPricingSidebar.getSaveButton())
                .waitSideBarClosed()
                .then("Errors are not appear")
                .waitAndValidate(not(visible), openPricingPage.getToasterMessage().getPanelError())
                .and("Hovering over 'Details' column in Open Pricing Table")
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
