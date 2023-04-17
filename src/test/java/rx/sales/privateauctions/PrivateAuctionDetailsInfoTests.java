package rx.sales.privateauctions;

import api.dto.rx.inventory.adspot.AdSpot;
import api.dto.rx.inventory.media.Media;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.sales.privateauctions.PrivateAuctionsPage;
import rx.BaseTest;
import rx.enums.MultipaneConstants;
import widgets.common.adSizes.AdSizesList;
import widgets.common.adformats.AdFormatsList;
import widgets.common.detailsmenu.DetailsSectionName;
import widgets.common.detailsmenu.menu.sections.DetailsSection;
import widgets.common.devices.DeviceList;
import widgets.common.operatingsystems.OperatingSystemsList;
import widgets.common.table.ColumnNames;
import widgets.sales.privateauctions.sidebar.CreatePrivateAuctionSidebar;

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
@Feature(value = "Private Auctions")
public class PrivateAuctionDetailsInfoTests extends BaseTest {

    private PrivateAuctionsPage privateAuctionsPage;
    private CreatePrivateAuctionSidebar privateAuctionSidebar;

    private AdSpot adSpot;
    private Media mediaOnly;
    private String mediaName;
    private String auctionName;
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

    public PrivateAuctionDetailsInfoTests() {

        privateAuctionsPage = new PrivateAuctionsPage();
        privateAuctionSidebar = new CreatePrivateAuctionSidebar();
    }

    @BeforeClass
    private void loginAndCreatePrivateAuction() {
        var calendar = privateAuctionSidebar.getDateRangeField();

        adSpot = adSpot()
                .createNewAdSpot(captionWithSuffix("autoAuctionAdSpot"))
                .build()
                .getAdSpotResponse();

        mediaOnly = media()
                .createNewMedia("media", adSpot.getPublisherId(), true)
                .build()
                .getMediaResponse();

        mediaName = adSpot.getMediaName();
        publisherName = adSpot.getPublisherName();
        auctionName = captionWithSuffix("autoAuction");

        testStart()
                .given()
                .openDirectPath(Path.PRIVATE_AUCTIONS)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, privateAuctionsPage.getNuxtProgress())
                .and("Click on 'Create Private Auction' button")
                .clickOnWebElement(privateAuctionsPage.getCreatePrivateAuctionsButton())
                .waitSideBarOpened()
                .and(String.format("Select Publisher %s", publisherName))
                .clickOnWebElement(privateAuctionSidebar.getTitle())
                .selectFromDropdown(privateAuctionSidebar.getPublisherNameDropdown(),
                        privateAuctionSidebar.getPublisherItems(), publisherName)
                .and()
                .setValueWithClean(privateAuctionSidebar.getNameInput(), auctionName)
                .and()
                .clickOnWebElement(privateAuctionSidebar.getDateRangeInput())
                .clickOnWebElement(calendar.getNextMonthButton())
                .clickOnWebElement(calendar.getDayButtonByValue("14"))
                .turnToggleOn(privateAuctionSidebar.getAlwaysOnToggle())
                .clickOnWebElement(privateAuctionSidebar.getNameInput())
                .clickOnWebElement(privateAuctionSidebar.getSaveAndCloseButton())
                .waitSideBarClosed()
                .then("Errors are not appear")
                .waitAndValidate(not(visible), privateAuctionsPage.getToasterMessage().getPanelError())
                .testEnd();
    }

    @Test(description = "Included items by default", priority = 1)
    public void checkDetailsByDefaultIncludeAll() {
        var table = privateAuctionsPage.getTable();
        var tableData = privateAuctionsPage.getTable().getTableData();

        var geoMultipane = privateAuctionSidebar.getGeoMultipane();
        var deviceMultipane = privateAuctionSidebar.getDeviceMultipane();
        var adSizeMultipane = privateAuctionSidebar.getAdSizeMultipane();
        var adFormatMultipane = privateAuctionSidebar.getAdFormatMultipane();
        var osMultipane = privateAuctionSidebar.getOperatingSystemMultipane();
        var inventoryMultipane = privateAuctionSidebar.getInventoryMultipane();

        testStart()
                .and(format("Search auction by name %s", auctionName))
                .setValueWithClean(tableData.getSearch(), auctionName)
                .validateContainsText(table.getTablePagination().getPaginationPanel(), ("1-1 of 1"))
                .and("Open edit sidebar")
                .clickOnTableCellLink(tableData, ColumnNames.NAME, auctionName)
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
                .and("Save auction and close")
                .clickOnWebElement(privateAuctionSidebar.getSaveAndCloseButton())
                .waitSideBarClosed()
                .and("Hovering over 'Details' column in Private Auction Table")
                .hoverMouseOnWebElement(tableData.getCellByPositionInTable(ColumnNames.DETAILS, 0))
                .testEnd();
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.GEO), MultipaneConstants.ALL_GEOS_ARE_INCLUDED.setQuantity(0));
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.DEVICE), MultipaneConstants.ALL_DEVICES_ARE_INCLUDED.setQuantity(0));
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.AD_SIZE), MultipaneConstants.ALL_AD_SIZES_ARE_INCLUDED.setQuantity(0));
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.INVENTORY), MultipaneConstants.ALL_INVENTORY_IS_INCLUDED.setQuantity(0));
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.AD_FORMAT), MultipaneConstants.ALL_AD_FORMATS_ARE_INCLUDED.setQuantity(0));
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.OPERATING_SYSTEM), MultipaneConstants.ALL_OPERATING_SYSTEMS_ARE_INCLUDED.setQuantity(0));
    }

    @Test(description = "Include All and check Details", priority = 2)
    public void checkDetailsByClickOnIncludeAll() {
        var tableData = privateAuctionsPage.getTable().getTableData();

        var geoMultipane = privateAuctionSidebar.getGeoMultipane();
        var deviceMultipane = privateAuctionSidebar.getDeviceMultipane();
        var adSizeMultipane = privateAuctionSidebar.getAdSizeMultipane();
        var adFormatMultipane = privateAuctionSidebar.getAdFormatMultipane();
        var osMultipane = privateAuctionSidebar.getOperatingSystemMultipane();
        var inventoryMultipane = privateAuctionSidebar.getInventoryMultipane();

        testStart()
                .and("Open edit sidebar")
                .hoverMouseOnWebElement(tableData.getCellByPositionInTable(ColumnNames.ID, 0))
                .clickOnTableCellLink(tableData, ColumnNames.NAME, auctionName)
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
                .and("Save auction and close")
                .clickOnWebElement(privateAuctionSidebar.getSaveAndCloseButton())
                .waitSideBarClosed()
                .then("Errors are not appear")
                .waitAndValidate(not(visible), privateAuctionsPage.getToasterMessage().getPanelError())
                .and("Hovering over 'Details' column in Private Auction Table")
                .hoverMouseOnWebElement(tableData.getCellByPositionInTable(ColumnNames.DETAILS, 0))
                .testEnd();

        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.GEO), MultipaneConstants.ALL_GEOS_ARE_INCLUDED.setQuantity(0));
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.DEVICE), MultipaneConstants.ALL_DEVICES_ARE_INCLUDED.setQuantity(0));
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.AD_SIZE), MultipaneConstants.ALL_AD_SIZES_ARE_INCLUDED.setQuantity(0));
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.INVENTORY), MultipaneConstants.ALL_INVENTORY_IS_INCLUDED.setQuantity(0));
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.AD_FORMAT), MultipaneConstants.ALL_AD_FORMATS_ARE_INCLUDED.setQuantity(0));
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.OPERATING_SYSTEM), MultipaneConstants.ALL_OPERATING_SYSTEMS_ARE_INCLUDED.setQuantity(0));
    }

    @Epic("GS-3713")
    @Test(description = "Include part items and check Details", priority = 3)
    public void checkDetailsWithNotAllIncluded() {
        var tableData = privateAuctionsPage.getTable().getTableData();

        var geoMultipane = privateAuctionSidebar.getGeoMultipane();
        var deviceMultipane = privateAuctionSidebar.getDeviceMultipane();
        var adSizeMultipane = privateAuctionSidebar.getAdSizeMultipane();
        var adFormatMultipane = privateAuctionSidebar.getAdFormatMultipane();
        var osMultipane = privateAuctionSidebar.getOperatingSystemMultipane();
        var inventoryMultipane = privateAuctionSidebar.getInventoryMultipane();

        testStart()
                .and("Open edit sidebar")
                .hoverMouseOnWebElement(tableData.getCellByPositionInTable(ColumnNames.ID, 0))
                .clickOnTableCellLink(tableData, ColumnNames.NAME, auctionName)
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
                .and("Save auction and close")
                .clickOnWebElement(privateAuctionSidebar.getSaveAndCloseButton())
                .waitSideBarClosed()
                .then("Errors are not appear")
                .waitAndValidate(not(visible), privateAuctionsPage.getToasterMessage().getPanelError())
                .and("Hovering over 'Details' column in Private Auction Table")
                .hoverMouseOnWebElement(tableData.getCellByPositionInTable(ColumnNames.DETAILS, 0))
                .testEnd();

        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.GEO), GEO_1, GEO_2);
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.DEVICE), DEVICE_2, DEVICE_1);
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.OPERATING_SYSTEM), OS_1, OS_2);
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.INVENTORY), mediaOnly.getName());
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.AD_FORMAT), AD_FORMAT_2, AD_FORMAT_1);
      //  verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.AD_SIZE), AdSizesList.A300x600.getFullName());
        verifySelectionInDetailsMenuForTableItem(new DetailsSection(DetailsSectionName.AD_SIZE), AD_SIZE_FULL_NAME_1,AD_SIZE_FULL_NAME_2);
    }

    @Epic("GS-3641")
    @Test(description = "Check Inventory Details", priority = 4)
    public void checkDetailsInventoryAdSpotOnly() {
        var tableData = privateAuctionsPage.getTable().getTableData();

        var inventoryMultipane = privateAuctionSidebar.getInventoryMultipane();

        testStart()
                .and("Open edit sidebar")
                .hoverMouseOnWebElement(tableData.getCellByPositionInTable(ColumnNames.ID, 0))
                .clickOnTableCellLink(tableData, ColumnNames.NAME, auctionName)
                .waitSideBarOpened()
                .and("Expand Inventory Pane and click on Include All")
                .clickOnWebElement(inventoryMultipane.getPanelNameLabel())
                .clickOnWebElement(inventoryMultipane.getClearAllButton())
                .clickOnWebElement(inventoryMultipane.getSelectTableItemByName(mediaName).getExpandInnerItemButton())
                .clickOnWebElement(inventoryMultipane.getSelectChildTableItemByName(adSpot.getName()).getName())
                .and("Save auction and close")
                .clickOnWebElement(privateAuctionSidebar.getSaveAndCloseButton())
                .waitSideBarClosed()
                .then("Errors are not appear")
                .waitAndValidate(not(visible), privateAuctionSidebar.getErrorAlertByFieldName("Date Range"))
                .waitAndValidate(not(visible), privateAuctionSidebar.getErrorAlertByFieldName("Name"))
                .waitAndValidate(not(visible), privateAuctionSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), privateAuctionsPage.getToasterMessage().getPanelError())
                .and("Hovering over 'Details' column in Private Auction Table")
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
