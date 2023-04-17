package rx.sales.privateauctions;

import api.dto.rx.inventory.adspot.AdSpot;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.sales.privateauctions.PrivateAuctionsPage;
import rx.BaseTest;
import widgets.common.adSizes.AdSizesList;
import widgets.common.adformats.AdFormatsList;
import widgets.common.detailsmenu.menu.TableItemDetailsMenu;
import widgets.common.detailsmenu.menu.sections.DetailsSection;
import widgets.common.devices.DeviceList;
import widgets.common.operatingsystems.OperatingSystemsList;
import widgets.common.table.ColumnNames;
import widgets.common.table.Statuses;
import widgets.sales.privateauctions.sidebar.CreatePrivateAuctionSidebar;
import zutils.StringUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static api.preconditionbuilders.AdSpotPrecondition.adSpot;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static configurations.User.USER_FOR_DELETION;
import static java.lang.String.format;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Private Auctions")
public class PrivateAuctionCreateEditTests extends BaseTest {

    private PrivateAuctionsPage privateAuctionsPage;
    private TableItemDetailsMenu auctionTableDetailsMenu;
    private CreatePrivateAuctionSidebar privateAuctionSidebar;

    private AdSpot adSpot;
    private String mediaName;
    private String auctionName;
    private String publisherName;

    public PrivateAuctionCreateEditTests() {

        privateAuctionsPage = new PrivateAuctionsPage();
        privateAuctionSidebar = new CreatePrivateAuctionSidebar();
        auctionTableDetailsMenu = new TableItemDetailsMenu();
    }

    @BeforeClass
    private void login() {
        adSpot = adSpot()
                .createNewAdSpot(captionWithSuffix("autoAuctionAdSpot"))
                .build()
                .getAdSpotResponse();

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
                .testEnd();
    }

    @Test
    public void createByDefault() {
        var calendar = privateAuctionSidebar.getDateRangeField();
        var name = captionWithSuffix("autoAuction");
        var tableData = privateAuctionsPage.getTable().getTableData();
        var tableOptions = privateAuctionsPage.getTable().getShowHideColumns();
        ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("UTC"));

        testStart()
                .and(String.format("Select Publisher %s", publisherName))
                .clickOnWebElement(privateAuctionSidebar.getTitle())
                .selectFromDropdown(privateAuctionSidebar.getPublisherNameDropdown(),
                        privateAuctionSidebar.getPublisherItems(), publisherName)
                .and()
                .setValueWithClean(privateAuctionSidebar.getNameInput(), name)
                .and()
                .clickOnWebElement(privateAuctionSidebar.getDateRangeInput())
                .clickOnWebElement(calendar.getNextMonthButton())
                .clickOnWebElement(calendar.getDayButtonByValue("14"))
                .turnToggleOn(privateAuctionSidebar.getAlwaysOnToggle())
                .clickOnWebElement(privateAuctionSidebar.getNameInput())
                .clickOnWebElement(privateAuctionSidebar.getSaveAndCloseButton())
                .then("Errors are not appear")
                .waitAndValidate(not(visible), privateAuctionSidebar.getErrorAlertByFieldName("Date Range"))
                .waitAndValidate(not(visible), privateAuctionSidebar.getErrorAlertByFieldName("Name"))
                .waitAndValidate(not(visible), privateAuctionSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), privateAuctionsPage.getToasterMessage().getPanelError())
                .and("Show all columns")
                .selectFromDropdown(privateAuctionsPage.getTable().getTablePagination().getPageMenu(),
                        privateAuctionsPage.getTable().getTablePagination().getRowNumbersList(), "10")
                .waitAndValidate(disappear, privateAuctionsPage.getNuxtProgress())
                .clickOnWebElement(tableOptions.getShowHideColumnsBtn())
                .waitAndValidate(visible, tableOptions.getMenuOptions())
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .then("Validate data in table")
                .validate(tableData.getCellByRowValue(ColumnNames.STATUS, ColumnNames.NAME, name), Statuses.ACTIVE.getStatus())
                .validate(tableData.getCellByRowValue(ColumnNames.PUBLISHER, ColumnNames.NAME, name), publisherName)
                .validate(tableData.getCellByRowValue(ColumnNames.ALWAYS_ON, ColumnNames.NAME, name), "Yes")
                .validate(tableData.getCellByRowValue(ColumnNames.OPTIMIZE, ColumnNames.NAME, name), "Yes")
                .validate(tableData.getCellByRowValue(ColumnNames.CREATED_BY, ColumnNames.NAME, name), TEST_USER.getMail())
                .validate(tableData.getCellByRowValue(ColumnNames.UPDATED_BY, ColumnNames.NAME, name), "")
                .validate(tableData.getCellByRowValue(ColumnNames.START_DATE, ColumnNames.NAME, name), StringUtils.getDateAsString(currentDate.plusMonths(1).getYear(), currentDate.plusMonths(1).getMonth(), 14))
                .validate(tableData.getCellByRowValue(ColumnNames.END_DATE, ColumnNames.NAME, name), "")
                .validate(tableData.getCellByRowValue(ColumnNames.CREATED_DATE, ColumnNames.NAME, name), StringUtils.getDateAsString(currentDate.getYear(), currentDate.getMonth(), currentDate.getDayOfMonth()))
                .validate(tableData.getCellByRowValue(ColumnNames.UPDATED_DATE, ColumnNames.NAME, name), StringUtils.getDateAsString(currentDate.getYear(), currentDate.getMonth(), currentDate.getDayOfMonth()))
                .testEnd();
    }

    @Test(description = "Create Auction with items included", dependsOnMethods = "createByDefault")
    public void createWithIncludedTargeting() {
        var calendar = privateAuctionSidebar.getDateRangeField();
        var tableData = privateAuctionsPage.getTable().getTableData();
        ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("UTC"));

        testStart()
                .and("Hit 'Create Private Auction' button")
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
                .clickOnWebElement(calendar.getDayButtonByValue("12"))
                .turnToggleOn(privateAuctionSidebar.getAlwaysOnToggle())
                .clickOnWebElement(privateAuctionSidebar.getNameInput())
                .and("Include device")
                .clickOnWebElement(privateAuctionSidebar.getDeviceMultipane().getPanelNameLabel())
                .clickOnWebElement(privateAuctionSidebar.getDeviceMultipane().getSelectTableItemByName(DeviceList.CONNECTED_DEVICE.getDevice()).getName())
                .clickOnWebElement(privateAuctionSidebar.getDeviceMultipane().getPanelNameLabel())
                .and("Include ad size")
                .clickOnWebElement(privateAuctionSidebar.getAdSizeMultipane().getPanelNameLabel())
                .clickOnWebElement(privateAuctionSidebar.getAdSizeMultipane().getSelectTableItemByName(AdSizesList.A120x60.getSize()).getName())
                .clickOnWebElement(privateAuctionSidebar.getAdSizeMultipane().getPanelNameLabel())
                .and("Include ad format")
                .clickOnWebElement(privateAuctionSidebar.getAdFormatMultipane().getPanelNameLabel())
                .clickOnWebElement(privateAuctionSidebar.getAdFormatMultipane().getSelectTableItemByName(AdFormatsList.BANNER.name()).getName())
                .clickOnWebElement(privateAuctionSidebar.getAdFormatMultipane().getPanelNameLabel())
                .and("Include Geo")
                .clickOnWebElement(privateAuctionSidebar.getGeoMultipane().getPanelNameLabel())
                .clickOnWebElement(privateAuctionSidebar.getGeoMultipane().getSelectTableItemByName("Angola").getName())
                .clickOnWebElement(privateAuctionSidebar.getGeoMultipane().getPanelNameLabel())
                .and("Include Operating System")
                .clickOnWebElement(privateAuctionSidebar.getOperatingSystemMultipane().getPanelNameLabel())
                .clickOnWebElement(privateAuctionSidebar.getOperatingSystemMultipane().getSelectTableItemByName(OperatingSystemsList.MACOSX.name()).getName())
                .clickOnWebElement(privateAuctionSidebar.getOperatingSystemMultipane().getPanelNameLabel())
                .and("Include Inventory")
                .clickOnWebElement(privateAuctionSidebar.getInventoryMultipane().getPanelNameLabel())
                .waiter(visible, privateAuctionSidebar.getInventoryMultipane().getSelectTableItemByName(mediaName).getName())
                .hoverMouseOnWebElement(privateAuctionSidebar.getInventoryMultipane().getSelectTableItemByName(mediaName).getName())
                .waiter(visible, privateAuctionSidebar.getInventoryMultipane().getSelectTableItemByName(mediaName).getIncludeButton())
                .clickOnWebElement(privateAuctionSidebar.getInventoryMultipane().getSelectTableItemByName(mediaName).getIncludeButton())
                .clickOnWebElement(privateAuctionSidebar.getInventoryMultipane().getPanelNameLabel())
                .and()
                .clickOnWebElement(privateAuctionSidebar.getSaveAndCloseButton())
                .then("Errors are not appear")
                .waitAndValidate(not(visible), privateAuctionSidebar.getErrorAlertByFieldName("Date Range"))
                .waitAndValidate(not(visible), privateAuctionSidebar.getErrorAlertByFieldName("Name"))
                .waitAndValidate(not(visible), privateAuctionSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), privateAuctionsPage.getToasterMessage().getPanelError())
                .and()
                .setValueWithClean(tableData.getSearch(), auctionName)
                .pressEnterKey(tableData.getSearch())
                .validateContainsText(privateAuctionsPage.getTable().getTablePagination().getPaginationPanel(), "1-1 of 1")
                .testEnd();
    }

    @Test(description = "Check Auction with targeting included", dependsOnMethods = "createWithIncludedTargeting")
    public void checkCreatedAuctionWithIncludedTargeting() {
        var tableData = privateAuctionsPage.getTable().getTableData();
        ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("UTC"));

        testStart()
                .and("Hit Edit button")
                .hoverMouseOnWebElement(privateAuctionsPage.getPageTitle())
                .clickOnTableCellLink(tableData, ColumnNames.NAME, auctionName)
                .and(String.format("Select Publisher %s", publisherName))
                .waitSideBarOpened()
                .validate(privateAuctionSidebar.getTitle(),format("Edit Private Auction: %s", auctionName))
                .then("Publisher should be disabled")
                .validate(disabled, privateAuctionSidebar.getPublisherNameInput())
                .validate(privateAuctionSidebar.getPublisherNameInput().getText(), publisherName)
                .validateAttribute(privateAuctionSidebar.getActiveToggle(), "aria-checked", "true")
                .validate(privateAuctionSidebar.getNameInput().getText(), auctionName)
                .validateAttribute(privateAuctionSidebar.getAlwaysOnToggle(), "aria-checked", "true")
                .validateAttribute(privateAuctionSidebar.getStartDateInput(), "value", format("%s-%s-12 GMT", currentDate.plusMonths(1).getYear(),
                        currentDate.plusMonths(1).getMonth().getValue() < 10 ? format("0%s", currentDate.plusMonths(1).getMonth().getValue()) : currentDate.plusMonths(1).getMonth().getValue()))
                .then("Check Included device")
                .clickOnWebElement(privateAuctionSidebar.getDeviceMultipane().getPanelNameLabel())
                .validate(privateAuctionSidebar.getDeviceMultipane().getIncludedExcludedTableItemByName(DeviceList.CONNECTED_DEVICE.getDevice()).getName())
                .clickOnWebElement(privateAuctionSidebar.getDeviceMultipane().getPanelNameLabel())
                .then("Check Included ad size")
                .clickOnWebElement(privateAuctionSidebar.getAdSizeMultipane().getPanelNameLabel())
                .validate(privateAuctionSidebar.getAdSizeMultipane().getIncludedExcludedTableItemByName(AdSizesList.A120x60.getSize()).getName())
                .clickOnWebElement(privateAuctionSidebar.getAdSizeMultipane().getPanelNameLabel())
                .then("Check Included ad format")
                .clickOnWebElement(privateAuctionSidebar.getAdFormatMultipane().getPanelNameLabel())
                .validate(privateAuctionSidebar.getAdFormatMultipane().getIncludedExcludedTableItemByName(AdFormatsList.BANNER.name()).getName())
                .clickOnWebElement(privateAuctionSidebar.getAdFormatMultipane().getPanelNameLabel())
                .then("Check Included Geo")
                .clickOnWebElement(privateAuctionSidebar.getGeoMultipane().getPanelNameLabel())
                .validate(privateAuctionSidebar.getGeoMultipane().getIncludedExcludedTableItemByName("Angola").getName())
                .clickOnWebElement(privateAuctionSidebar.getGeoMultipane().getPanelNameLabel())
                .then("Check Included Operating System")
                .clickOnWebElement(privateAuctionSidebar.getOperatingSystemMultipane().getPanelNameLabel())
                .validate(privateAuctionSidebar.getOperatingSystemMultipane().getIncludedExcludedTableItemByName(OperatingSystemsList.MACOSX.name()).getName())
                .clickOnWebElement(privateAuctionSidebar.getOperatingSystemMultipane().getPanelNameLabel())
                .then("Check Included Inventory")
                .clickOnWebElement(privateAuctionSidebar.getInventoryMultipane().getPanelNameLabel())
                .waiter(visible, privateAuctionSidebar.getInventoryMultipane().getSelectTableItemByName(mediaName).getName())
                .hoverMouseOnWebElement(privateAuctionSidebar.getInventoryMultipane().getSelectTableItemByName(mediaName).getName())
                .waiter(visible, privateAuctionSidebar.getInventoryMultipane().getIncludedExcludedTableItemByName(mediaName).getName())
                .validate(privateAuctionSidebar.getInventoryMultipane().getIncludedExcludedTableItemByName(mediaName).getName())
                .clickOnWebElement(privateAuctionSidebar.getInventoryMultipane().getPanelNameLabel())
                .testEnd();
    }

    @Test(description = "Create Auction with targeting included", dependsOnMethods = "checkCreatedAuctionWithIncludedTargeting")
    public void editWithIncludedTargeting() {
        var calendar = privateAuctionSidebar.getDateRangeField();
        var tableData = privateAuctionsPage.getTable().getTableData();
        var tableOptions = privateAuctionsPage.getTable().getShowHideColumns();
        String editedName = format("Edit %s", auctionName);
        ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("UTC"));

        testStart()
                .and("Edit name value")
                .setValueWithClean(privateAuctionSidebar.getNameInput(), editedName)
                .and()
                .turnToggleOff(privateAuctionSidebar.getAlwaysOnToggle())
                .turnToggleOff(privateAuctionSidebar.getActiveToggle())
                .unSelectCheckBox(privateAuctionSidebar.getOptimizeCheckbox())
                .clickOnWebElement(privateAuctionSidebar.getDateRangeInput())
                .clickOnWebElement(calendar.getDayButtonByValue("15"))
                .clickOnWebElement(privateAuctionSidebar.getNameInput())
                .and("Include device")
                .clickOnWebElement(privateAuctionSidebar.getDeviceMultipane().getPanelNameLabel())
                .clickOnWebElement(privateAuctionSidebar.getDeviceMultipane().getSelectTableItemByName(DeviceList.PHONE.getDevice()).getName())
                .clickOnWebElement(privateAuctionSidebar.getDeviceMultipane().getPanelNameLabel())
                .and("Include ad size")
                .clickOnWebElement(privateAuctionSidebar.getAdSizeMultipane().getPanelNameLabel())
                .clickOnWebElement(privateAuctionSidebar.getAdSizeMultipane().getSelectTableItemByName(AdSizesList.A216x36.getSize()).getName())
                .clickOnWebElement(privateAuctionSidebar.getAdSizeMultipane().getPanelNameLabel())
                .and("Include ad format")
                .clickOnWebElement(privateAuctionSidebar.getAdFormatMultipane().getPanelNameLabel())
                .clickOnWebElement(privateAuctionSidebar.getAdFormatMultipane().getSelectTableItemByName(AdFormatsList.NATIVE.name()).getName())
                .clickOnWebElement(privateAuctionSidebar.getAdFormatMultipane().getPanelNameLabel())
                .and("Include Geo")
                .clickOnWebElement(privateAuctionSidebar.getGeoMultipane().getPanelNameLabel())
                .clickOnWebElement(privateAuctionSidebar.getGeoMultipane().getSelectTableItemByName("Albania").getName())
                .clickOnWebElement(privateAuctionSidebar.getGeoMultipane().getPanelNameLabel())
                .and("Include Operating System")
                .clickOnWebElement(privateAuctionSidebar.getOperatingSystemMultipane().getPanelNameLabel())
                .clickOnWebElement(privateAuctionSidebar.getOperatingSystemMultipane().getSelectTableItemByName(OperatingSystemsList.IOS.name()).getName())
                .clickOnWebElement(privateAuctionSidebar.getOperatingSystemMultipane().getPanelNameLabel())
                .and("Include Inventory")
                .clickOnWebElement(privateAuctionSidebar.getInventoryMultipane().getPanelNameLabel())
                .clickOnWebElement(privateAuctionSidebar.getInventoryMultipane().getClearAllButton())
                .waiter(visible, privateAuctionSidebar.getInventoryMultipane().getSelectTableItemByName(mediaName).getName())
                .hoverMouseOnWebElement(privateAuctionSidebar.getInventoryMultipane().getSelectTableItemByName(mediaName).getName())
                .waiter(visible, privateAuctionSidebar.getInventoryMultipane().getSelectTableItemByName(mediaName).getIncludeButton())
                .clickOnWebElement(privateAuctionSidebar.getInventoryMultipane().getSelectTableItemByName(mediaName).getIncludeButton())
                .clickOnWebElement(privateAuctionSidebar.getInventoryMultipane().getPanelNameLabel())
                .and()
                .clickOnWebElement(privateAuctionSidebar.getSaveAndCloseButton())
                .then("Errors are not appear")
                .waitAndValidate(not(visible), privateAuctionSidebar.getErrorAlertByFieldName("Date Range"))
                .waitAndValidate(not(visible), privateAuctionSidebar.getErrorAlertByFieldName("Name"))
                .waitAndValidate(not(visible), privateAuctionSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), privateAuctionsPage.getToasterMessage().getPanelError())
                .and()
                .setValueWithClean(tableData.getSearch(), auctionName)
                .pressEnterKey(tableData.getSearch())
                .validateContainsText(privateAuctionsPage.getTable().getTablePagination().getPaginationPanel(), "1-1 of 1")
                .and("Show all columns")
                .selectFromDropdown(privateAuctionsPage.getTable().getTablePagination().getPageMenu(),
                        privateAuctionsPage.getTable().getTablePagination().getRowNumbersList(), "10")
                .waitAndValidate(disappear, privateAuctionsPage.getNuxtProgress())
                .clickOnWebElement(tableOptions.getShowHideColumnsBtn())
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .then("Validate data in table")
                .validate(tableData.getCellByRowValue(ColumnNames.STATUS, ColumnNames.NAME, editedName), Statuses.INACTIVE.getStatus())
                .validate(tableData.getCellByRowValue(ColumnNames.PUBLISHER, ColumnNames.NAME, editedName), publisherName)
                .validate(tableData.getCellByRowValue(ColumnNames.ALWAYS_ON, ColumnNames.NAME, editedName), "No")
                .validate(tableData.getCellByRowValue(ColumnNames.OPTIMIZE, ColumnNames.NAME, editedName), "No")
                .validate(tableData.getCellByRowValue(ColumnNames.CREATED_BY, ColumnNames.NAME, editedName), TEST_USER.getMail())
                .validate(tableData.getCellByRowValue(ColumnNames.UPDATED_BY, ColumnNames.NAME, editedName), TEST_USER.getMail())
                .validate(tableData.getCellByRowValue(ColumnNames.START_DATE, ColumnNames.NAME, editedName), StringUtils.getDateAsString(currentDate.plusMonths(1).getYear(), currentDate.plusMonths(1).getMonth(), 12))
                .validate(tableData.getCellByRowValue(ColumnNames.END_DATE, ColumnNames.NAME, editedName), StringUtils.getDateAsString(currentDate.plusMonths(1).getYear(), currentDate.plusMonths(1).getMonth(), 15))
                .validate(tableData.getCellByRowValue(ColumnNames.CREATED_DATE, ColumnNames.NAME, editedName), StringUtils.getDateAsString(currentDate.getYear(), currentDate.getMonth(), currentDate.getDayOfMonth()))
                .validate(tableData.getCellByRowValue(ColumnNames.UPDATED_DATE, ColumnNames.NAME, editedName), StringUtils.getDateAsString(currentDate.getYear(), currentDate.getMonth(), currentDate.getDayOfMonth()))
                .testEnd();
    }

    @Test(description = "Check Edited Auction with targeting included", dependsOnMethods = "editWithIncludedTargeting")
    public void checkEditedAuctionWithIncludedTargeting() {
        var tableData = privateAuctionsPage.getTable().getTableData();
        ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("UTC"));

        testStart()
                .and("Hit Edit button")
                .hoverMouseOnWebElement(privateAuctionsPage.getCreatePrivateAuctionsButton())
                .clickOnTableCellLink(tableData, ColumnNames.NAME, format("Edit %s", auctionName))
                .and(String.format("Select Publisher %s", publisherName))
                .waitSideBarOpened()
                .validate(privateAuctionSidebar.getTitle(),format("Edit Private Auction: %s", format("Edit %s", auctionName)))
                .then("Publisher should be disabled")
                .validate(disabled, privateAuctionSidebar.getPublisherNameInput())
                .validate(privateAuctionSidebar.getPublisherNameInput().getText(), publisherName)
                .validateAttribute(privateAuctionSidebar.getActiveToggle(), "aria-checked", "false")
                .validate(privateAuctionSidebar.getNameInput().getText(), auctionName)
                .validateAttribute(privateAuctionSidebar.getAlwaysOnToggle(), "aria-checked", "false")
                .validateAttribute(privateAuctionSidebar.getDateRangeInput(), "value", format("%s-%s-12 - %s-%s-15 GMT", currentDate.plusMonths(1).getYear(),
                        currentDate.plusMonths(1).getMonth().getValue() < 10 ? format("0%s", currentDate.plusMonths(1).getMonth().getValue()) : currentDate.plusMonths(1).getMonth().getValue(),
                        currentDate.plusMonths(1).getYear(),
                        currentDate.plusMonths(1).getMonth().getValue() < 10 ? format("0%s", currentDate.plusMonths(1).getMonth().getValue()) : currentDate.plusMonths(1).getMonth().getValue()))
                .then("Check Included device")
                .clickOnWebElement(privateAuctionSidebar.getDeviceMultipane().getPanelNameLabel())
                .validate(privateAuctionSidebar.getDeviceMultipane().getIncludedExcludedTableItemByName(DeviceList.CONNECTED_DEVICE.getDevice()).getName())
                .validate(privateAuctionSidebar.getDeviceMultipane().getIncludedExcludedTableItemByName(DeviceList.PHONE.getDevice()).getName())
                .clickOnWebElement(privateAuctionSidebar.getDeviceMultipane().getPanelNameLabel())
                .then("Check Included ad size")
                .clickOnWebElement(privateAuctionSidebar.getAdSizeMultipane().getPanelNameLabel())
                .validate(privateAuctionSidebar.getAdSizeMultipane().getIncludedExcludedTableItemByName(AdSizesList.A120x60.getSize()).getName())
                .validate(privateAuctionSidebar.getAdSizeMultipane().getIncludedExcludedTableItemByName(AdSizesList.A216x36.getSize()).getName())
                .clickOnWebElement(privateAuctionSidebar.getAdSizeMultipane().getPanelNameLabel())
                .then("Check Included ad format")
                .clickOnWebElement(privateAuctionSidebar.getAdFormatMultipane().getPanelNameLabel())
                .validate(privateAuctionSidebar.getAdFormatMultipane().getIncludedExcludedTableItemByName(AdFormatsList.BANNER.name()).getName())
                .validate(privateAuctionSidebar.getAdFormatMultipane().getIncludedExcludedTableItemByName(AdFormatsList.NATIVE.name()).getName())
                .clickOnWebElement(privateAuctionSidebar.getAdFormatMultipane().getPanelNameLabel())
                .then("Check Included Geo")
                .clickOnWebElement(privateAuctionSidebar.getGeoMultipane().getPanelNameLabel())
                .validate(privateAuctionSidebar.getGeoMultipane().getIncludedExcludedTableItemByName("Angola").getName())
                .validate(privateAuctionSidebar.getGeoMultipane().getIncludedExcludedTableItemByName("Albania").getName())
                .clickOnWebElement(privateAuctionSidebar.getGeoMultipane().getPanelNameLabel())
                .then("Check Included Operating System")
                .clickOnWebElement(privateAuctionSidebar.getOperatingSystemMultipane().getPanelNameLabel())
                .validate(privateAuctionSidebar.getOperatingSystemMultipane().getIncludedExcludedTableItemByName(OperatingSystemsList.MACOSX.name()).getName())
                .validate(privateAuctionSidebar.getOperatingSystemMultipane().getIncludedExcludedTableItemByName(OperatingSystemsList.IOS.name()).getName())
                .clickOnWebElement(privateAuctionSidebar.getOperatingSystemMultipane().getPanelNameLabel())
                .then("Check Included Inventory")
                .clickOnWebElement(privateAuctionSidebar.getInventoryMultipane().getPanelNameLabel())
                .waiter(visible, privateAuctionSidebar.getInventoryMultipane().getSelectTableItemByName(mediaName).getName())
                .hoverMouseOnWebElement(privateAuctionSidebar.getInventoryMultipane().getSelectTableItemByName(mediaName).getName())
                .waiter(visible, privateAuctionSidebar.getInventoryMultipane().getIncludedExcludedTableItemByName(mediaName).getName())
                .validate(privateAuctionSidebar.getInventoryMultipane().getIncludedExcludedTableItemByName(mediaName).getName())
                .clickOnWebElement(privateAuctionSidebar.getInventoryMultipane().getPanelNameLabel())
                .and("Close Sidebar")
                .clickOnWebElement(privateAuctionSidebar.getSaveAndCloseButton())
                .then("Errors are not appear")
                .waitAndValidate(not(visible), privateAuctionSidebar.getErrorAlertByFieldName("Date Range"))
                .waitAndValidate(not(visible), privateAuctionSidebar.getErrorAlertByFieldName("Name"))
                .waitAndValidate(not(visible), privateAuctionSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), privateAuctionsPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .testEnd();
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

    @AfterMethod(alwaysRun = true)
    private void deleteInventory() {

        adSpot()
                .setCredentials(USER_FOR_DELETION)
                .deleteAdSpot(adSpot.getId())
                .build();
    }
}
