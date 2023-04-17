package rx.inventory.adspot;

import api.dto.rx.inventory.adspot.AdSpot;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.inventory.adspots.AdSpotsPage;
import rx.BaseTest;
import widgets.common.table.ColumnNames;
import widgets.common.table.Statuses;
import widgets.errormessages.ErrorMessages;

import java.util.List;
import java.util.Map;

import static api.preconditionbuilders.AdSpotPrecondition.adSpot;
import static api.preconditionbuilders.MediaPrecondition.media;
import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static configurations.User.USER_FOR_DELETION;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature("Ad Spots")
public class AdSpotActivateDeactivateTableTests extends BaseTest {

    private AdSpot adSpotActive1;
    private AdSpot adSpotInActive1;
    private AdSpot adSpotActiveBulkA1;
    private AdSpot adSpotActiveBulkA2;
    private AdSpot adSpotActiveBulkA3;
    private AdSpot adSpotActiveBulkD1;
    private AdSpot adSpotActiveBulkD2;
    private AdSpot adSpotActiveBulkD3;

    private AdSpot adSpotInActiveBulkA1;
    private AdSpot adSpotInActiveBulkA2;
    private AdSpot adSpotInActiveBulkA3;
    private AdSpot adSpotInActiveBulkD1;
    private AdSpot adSpotInActiveBulkD2;
    private AdSpot adSpotInActiveBulkD3;

    private AdSpot adSpotActiveWithInactivePublisher;
    private AdSpot AdSpotInActiveWithInactivePublisher;
    private AdSpot adSpotActiveWithInactiveMedia;
    private AdSpot adSpotInActiveWithInactiveMedia;

    private AdSpotsPage adSpotsPage;

    private static final String PREFIX_AD_SPOT = "autoAdSpotSingleActive";
    private static final String PREFIX_AD_SPOT_INACTIVE = "autoAdSpotSingleInactive";
    private static final String PREFIX_AD_SPOT_INACTIVE_PUBLISHER = "autoAdSpotInactivePub";
    private static final String PREFIX_AD_SPOT_INACTIVE_MEDIA = "autoAdSpotInactiveMedia";
    private static final String PREFIX_AD_SPOT_BULK_1 = captionWithSuffix("autoAdSpotBulk1");
    private static final String PREFIX_AD_SPOT_BULK_2 = captionWithSuffix("autoAdSpotBulk2");
    private static final String CLASS_ATTRIBUTE_FOR_UNCHECKED_CHECKBOX = "v-icon notranslate mdi mdi-checkbox-blank-outline theme--light";

    public AdSpotActivateDeactivateTableTests() {
        adSpotsPage = new AdSpotsPage();
    }

    @BeforeClass
    public void loginAndCreateExpectedResuts() {
        deleteAdSpots();

        //Creating media Using API
        adSpotActive1 = createAdSpot(PREFIX_AD_SPOT, true);
        adSpotInActive1 = createAdSpot(PREFIX_AD_SPOT_INACTIVE, false);
        adSpotActiveWithInactiveMedia = createAdSpot(PREFIX_AD_SPOT_INACTIVE_MEDIA, true);
        adSpotInActiveWithInactiveMedia = createAdSpot(PREFIX_AD_SPOT_INACTIVE_MEDIA, false);

        adSpotActiveBulkA1 = createAdSpot(PREFIX_AD_SPOT_BULK_1, true);
        adSpotActiveBulkA2 = createAdSpot(PREFIX_AD_SPOT_BULK_1, true);
        adSpotActiveBulkA3 = createAdSpot(PREFIX_AD_SPOT_BULK_1, true);
        adSpotInActiveBulkA1 = createAdSpot(PREFIX_AD_SPOT_BULK_1, false);
        adSpotInActiveBulkA2 = createAdSpot(PREFIX_AD_SPOT_BULK_1, false);
        adSpotInActiveBulkA3 = createAdSpot(PREFIX_AD_SPOT_BULK_1, false);

        adSpotActiveBulkD1 = createAdSpot(PREFIX_AD_SPOT_BULK_2, true);
        adSpotActiveBulkD2 = createAdSpot(PREFIX_AD_SPOT_BULK_2, true);
        adSpotActiveBulkD3 = createAdSpot(PREFIX_AD_SPOT_BULK_2, true);
        adSpotInActiveBulkD1 = createAdSpot(PREFIX_AD_SPOT_BULK_2, false);
        adSpotInActiveBulkD2 = createAdSpot(PREFIX_AD_SPOT_BULK_2, false);
        adSpotInActiveBulkD3 = createAdSpot(PREFIX_AD_SPOT_BULK_2, false);

        adSpotActiveWithInactivePublisher = createAdSpot(PREFIX_AD_SPOT_INACTIVE_PUBLISHER, true);
        deactivatePublisher(adSpotActiveWithInactivePublisher.getPublisherId());
        AdSpotInActiveWithInactivePublisher = createAdSpot(PREFIX_AD_SPOT_INACTIVE_PUBLISHER, false);
        deactivatePublisher(AdSpotInActiveWithInactivePublisher.getPublisherId());
        adSpotActiveWithInactiveMedia = createAdSpot(PREFIX_AD_SPOT_INACTIVE_MEDIA, true);
        deactivateMedia(adSpotActiveWithInactiveMedia.getMediaId());
    }

    @BeforeMethod
    private void login() {
        testStart()
                .given()
                .openDirectPath(Path.AD_SPOT)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, adSpotsPage.getNuxtProgress())
                .testEnd();
    }

    @Test(testName = "Deactivate single ad spot")
    public void deactivateSingleAdSpot() {
        var toasterPanel = adSpotsPage.getToasterMessage();
        var tableData = adSpotsPage.getAdSpotsTable().getTableData();
        var tablePagination = adSpotsPage.getAdSpotsTable().getTablePagination();

        testStart()
                .given()
                .and(String.format("Search ad spot by name '%s'", adSpotActive1.getName()))
                .setValueWithClean(tableData.getSearch(), adSpotActive1.getName())
                .pressEnterKey(tableData.getSearch())
                .validateContainsText(tablePagination.getPaginationPanel(), "1-1 of 1")
                .selectAllRowsByColumnCellValue(tableData, ColumnNames.AD_SPOT_NAME, adSpotActive1.getName())
                .and("Click 'Deactivate' button")
                .clickOnWebElement(adSpotsPage.getDeactivateAdSpotButton())
                .then("Validate media status should be changed on 'Inactive'")
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.AD_SPOT_NAME, adSpotActive1.getName()),
                        Statuses.INACTIVE.getStatus())
                .validate(not(exist), toasterPanel.getPanelError())
                .testEnd();
    }

    @Test(testName = "Deactivate single ad spot with inactive media")
    public void deactivateSingleAdSpotWithInactiveMedia() {
        var toasterPanel = adSpotsPage.getToasterMessage();
        var tableData = adSpotsPage.getAdSpotsTable().getTableData();
        var tablePagination = adSpotsPage.getAdSpotsTable().getTablePagination();

        testStart()
                .given()
                .and(String.format("Search ad spot by name '%s'", adSpotActiveWithInactiveMedia.getName()))
                .setValueWithClean(tableData.getSearch(), adSpotActiveWithInactiveMedia.getName())
                .pressEnterKey(tableData.getSearch())
                .validateContainsText(tablePagination.getPaginationPanel(), "1-1 of 1")
                .selectAllRowsByColumnCellValue(tableData, ColumnNames.AD_SPOT_NAME, adSpotActiveWithInactiveMedia.getName())
                .and("Click 'Deactivate' button")
                .clickOnWebElement(adSpotsPage.getDeactivateAdSpotButton())
                .then("Validate media status should be changed on 'Inactive'")
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.AD_SPOT_NAME, adSpotActiveWithInactiveMedia.getName()),
                        Statuses.INACTIVE.getStatus())
                .then("Error message is not appear")
                .validate(not(exist), toasterPanel.getPanelError())
                .testEnd();
    }

    @Test(testName = "Activate single ad spot with inactive media")
    public void activateSingleAdSpotWithInactiveMedia() {
        var toasterPanel = adSpotsPage.getToasterMessage();
        var tableData = adSpotsPage.getAdSpotsTable().getTableData();
        var tablePagination = adSpotsPage.getAdSpotsTable().getTablePagination();

        testStart()
                .given()
                .and(String.format("Search ad spot by name '%s'", adSpotInActiveWithInactiveMedia.getName()))
                .setValueWithClean(tableData.getSearch(), adSpotInActiveWithInactiveMedia.getName())
                .pressEnterKey(tableData.getSearch())
                .validateContainsText(tablePagination.getPaginationPanel(), "1-1 of 1")
                .selectAllRowsByColumnCellValue(tableData, ColumnNames.AD_SPOT_NAME, adSpotInActiveWithInactiveMedia.getName())
                .and("Click 'Activate' button")
                .clickOnWebElement(adSpotsPage.getActivateAdSpotButton())
                .then("Validate media status should be changed on 'Inactive'")
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.AD_SPOT_NAME, adSpotInActiveWithInactiveMedia.getName()),
                        Statuses.ACTIVE.getStatus())
                .then("Error message is not appear")
                .validate(not(exist), toasterPanel.getPanelError())
                .testEnd();
    }

    @Test(testName = "Deactivate single ad spot with inactive publisher")
    public void deactivateSingleAdSpotWithInactivePublisher() {
        var tableData = adSpotsPage.getAdSpotsTable().getTableData();
        var tablePagination = adSpotsPage.getAdSpotsTable().getTablePagination();
        var toasterPanel = adSpotsPage.getToasterMessage();

        testStart()
                .given()
                .and(String.format("Search ad spot by name '%s'", adSpotActiveWithInactivePublisher.getName()))
                .setValueWithClean(tableData.getSearch(), adSpotActiveWithInactivePublisher.getName())
                .pressEnterKey(tableData.getSearch())
                .validateContainsText(tablePagination.getPaginationPanel(), "1-1 of 1")
                .selectAllRowsByColumnCellValue(tableData, ColumnNames.AD_SPOT_NAME,
                        adSpotActiveWithInactivePublisher.getName())
                .and("Click 'Deactivate' button")
                .clickOnWebElement(adSpotsPage.getDeactivateAdSpotButton())
                .validate(visible, toasterPanel.getPanelError())
                .and("Click on the link 'View More' on toaster message")
                .clickOnWebElement(toasterPanel.getViewErrorDetails())
                .then("Validate error message")
                .validateContainsText(toasterPanel.getMessageStatus(),
                        ErrorMessages.BAD_REQUEST.getText())
                .validateContainsText(toasterPanel.getMessageError(),
                        ErrorMessages.PUBLISHER_IS_DISABLED.getText())
                .and("Close toaster message")
                .clickOnWebElement(toasterPanel.getRemoveIcon())
                .then("Validate ad spot status should not be changed on 'Inactive'")
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.AD_SPOT_NAME, adSpotActiveWithInactivePublisher.getName()),
                        Statuses.ACTIVE.getStatus())
                .testEnd();
    }

    @Test(testName = "Activate single ad spot with inactive publisher")
    public void activateSingleAdSpotWithInactivePublisher() {
        var tableData = adSpotsPage.getAdSpotsTable().getTableData();
        var tablePagination = adSpotsPage.getAdSpotsTable().getTablePagination();
        var toasterPanel = adSpotsPage.getToasterMessage();

        testStart()
                .given()
                .and(String.format("Search ad spot by name '%s'", AdSpotInActiveWithInactivePublisher.getName()))
                .setValueWithClean(tableData.getSearch(), AdSpotInActiveWithInactivePublisher.getName())
                .pressEnterKey(tableData.getSearch())
                .validateContainsText(tablePagination.getPaginationPanel(), "1-1 of 1")
                .selectAllRowsByColumnCellValue(tableData, ColumnNames.AD_SPOT_NAME,
                        AdSpotInActiveWithInactivePublisher.getName())
                .and("Click 'Deactivate' button")
                .clickOnWebElement(adSpotsPage.getActivateAdSpotButton())
                .validate(visible, toasterPanel.getPanelError())
                .and("Click on the link 'View More' on toaster message")
                .clickOnWebElement(toasterPanel.getViewErrorDetails())
                .then("Validate error message")
                .validateContainsText(toasterPanel.getMessageStatus(), ErrorMessages.BAD_REQUEST.getText())
                .validateContainsText(toasterPanel.getMessageError(), ErrorMessages.PUBLISHER_IS_DISABLED.getText())
                .and("Close toaster message")
                .clickOnWebElement(toasterPanel.getRemoveIcon())
                .then("Validate ad spot status should not be changed on 'Active'")
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.AD_SPOT_NAME,
                                AdSpotInActiveWithInactivePublisher.getName()),
                        Statuses.INACTIVE.getStatus())
                .testEnd();
    }

    @Test(testName = "Activate single ad spot")
    public void activateSingleAdSpot() {
        var toasterPanel = adSpotsPage.getToasterMessage();
        var tableData = adSpotsPage.getAdSpotsTable().getTableData();
        var tablePagination = adSpotsPage.getAdSpotsTable().getTablePagination();

        testStart()
                .given()
                .and(String.format("Search ad spot by name '%s'", adSpotInActive1.getName()))
                .setValueWithClean(tableData.getSearch(), adSpotInActive1.getName())
                .pressEnterKey(tableData.getSearch())
                .validateContainsText(tablePagination.getPaginationPanel(),
                        "1-1 of 1")
                .selectAllRowsByColumnCellValue(tableData, ColumnNames.AD_SPOT_NAME,
                        adSpotInActive1.getName())
                .and("Click 'Deactivate' button")
                .clickOnWebElement(adSpotsPage.getActivateAdSpotButton())
                .then("Validate ad spot status should be changed on 'Active'")
                .waitAndValidate(disappear, adSpotsPage.getTableProgressBar())
                .and(String.format("Search ad spot by name '%s'", adSpotInActive1))
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.AD_SPOT_NAME, adSpotInActive1.getName()),
                        Statuses.ACTIVE.getStatus())
                .then("Error message is not appear")
                .validate(not(exist), toasterPanel.getPanelError())
                .testEnd();
    }

    @Test(testName = "Activate bulk ad spot")
    public void activateBulkAdSpot() {
        var tableData = adSpotsPage.getAdSpotsTable().getTableData();
        var tablePagination = adSpotsPage.getAdSpotsTable().getTablePagination();

        testStart()
                .given()
                .and(String.format("Search media by ad spot '%s'", PREFIX_AD_SPOT_BULK_1))
                .setValueWithClean(tableData.getSearch(), PREFIX_AD_SPOT_BULK_1)
                .pressEnterKey(tableData.getSearch())
                .validateContainsText(tablePagination.getPaginationPanel(),
                        "1-6 of 6")
                .selectAllRowsByColumnCellValue(tableData, ColumnNames.NAME,
                        PREFIX_AD_SPOT_BULK_1)
                .and("Click 'Activate' button")
                .clickOnWebElement(adSpotsPage.getActivateAdSpotButton())
                .then("Validate ad spots status should be changed on 'Active'")
                .waitAndValidate(visible, tableData.getCheckbox(1)
                        .shouldHave(attributeMatching("class", CLASS_ATTRIBUTE_FOR_UNCHECKED_CHECKBOX)))
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.NAME, adSpotInActiveBulkA1.getName()),
                        Statuses.ACTIVE.getStatus())
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.NAME, adSpotInActiveBulkA2.getName()),
                        Statuses.ACTIVE.getStatus())
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.NAME, adSpotInActiveBulkA3.getName()),
                        Statuses.ACTIVE.getStatus())
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.NAME, adSpotActiveBulkA1.getName()),
                        Statuses.ACTIVE.getStatus())
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.NAME, adSpotActiveBulkA2.getName()),
                        Statuses.ACTIVE.getStatus())
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.NAME, adSpotActiveBulkA3.getName()),
                        Statuses.ACTIVE.getStatus())
                .validate(not(exist), adSpotsPage.getToasterMessage().getPanelError())
                .testEnd();
    }

    @Test(testName = "Deactivate bulk ad spot")
    public void deactivateBulkAdSpot() {
        var tableData = adSpotsPage.getAdSpotsTable().getTableData();
        var tablePagination = adSpotsPage.getAdSpotsTable().getTablePagination();

        testStart()
                .given()
                .and(String.format("Search ad spots by name '%s'", PREFIX_AD_SPOT_BULK_2))
                .setValueWithClean(tableData.getSearch(), PREFIX_AD_SPOT_BULK_2)
                .pressEnterKey(tableData.getSearch())
                .validateContainsText(tablePagination.getPaginationPanel(),
                        "1-6 of 6")
                .selectAllRowsByColumnCellValue(tableData, ColumnNames.AD_SPOT_NAME,
                        PREFIX_AD_SPOT_BULK_2)
                .and("Click 'Activate' button")
                .clickOnWebElement(adSpotsPage.getActivateAdSpotButton())
                .then("Validate ad spots status should be changed on 'Inactive'")
                .waitAndValidate(visible, tableData.getCheckbox(1)
                        .shouldHave(attributeMatching("class", CLASS_ATTRIBUTE_FOR_UNCHECKED_CHECKBOX)))
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.AD_SPOT_NAME, adSpotInActiveBulkD1.getName()),
                        Statuses.ACTIVE.getStatus())
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.AD_SPOT_NAME, adSpotInActiveBulkD2.getName()),
                        Statuses.ACTIVE.getStatus())
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.AD_SPOT_NAME, adSpotInActiveBulkD3.getName()),
                        Statuses.ACTIVE.getStatus())
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.AD_SPOT_NAME, adSpotActiveBulkD1.getName()),
                        Statuses.ACTIVE.getStatus())
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.AD_SPOT_NAME, adSpotActiveBulkD2.getName()),
                        Statuses.ACTIVE.getStatus())
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.AD_SPOT_NAME, adSpotActiveBulkD3.getName()),
                        Statuses.ACTIVE.getStatus())
                .validate(not(exist), adSpotsPage.getToasterMessage().getPanelError())
                .testEnd();
    }

    private List<AdSpot> getAllAdSpotsItemsByParams(String strParams) {

        return adSpot()
                .getAdSpotsWithFilter(Map.of("search", strParams))
                .build()
                .getAdSpotsGetAllResponse()
                .getItems();
    }

    @AfterMethod(alwaysRun = true)
    private void logOut() {
        testStart()
                .given()
                .logOut()
                .testEnd();
    }

    @AfterClass(alwaysRun = true)
    private void cleanData() {
        deleteAdSpots();
    }

    private void deleteAdSpots() {

        deleteAdSpot(getAllAdSpotsItemsByParams(PREFIX_AD_SPOT));
        deleteAdSpot(getAllAdSpotsItemsByParams(PREFIX_AD_SPOT_INACTIVE));
        deleteAdSpot(getAllAdSpotsItemsByParams(PREFIX_AD_SPOT_BULK_1));
        deleteAdSpot(getAllAdSpotsItemsByParams(PREFIX_AD_SPOT_BULK_2));
        deleteAdSpot(getAllAdSpotsItemsByParams(PREFIX_AD_SPOT_INACTIVE_MEDIA));
        deleteAdSpot(getAllAdSpotsItemsByParams(PREFIX_AD_SPOT_INACTIVE_PUBLISHER));
    }

    private void deleteAdSpot(List<AdSpot> adSpotList) {

        adSpotList.forEach(adSpot -> {
            adSpot()
                    .setCredentials(USER_FOR_DELETION)
                    .deleteAdSpot(adSpot.getId())
                    .build();
            deleteMedia(adSpot.getMediaId());
            deletePublisher(adSpot.getPublisherId());
        });
    }

    private void deactivatePublisher(Integer id) {
        publisher().changePublisherStatus(id, false);
    }

    private void deactivateMedia(Integer id) {
        media().changeMediaStatus(id, false).build();
    }

    private void deletePublisher(Integer id) {

        publisher()
                .setCredentials(USER_FOR_DELETION)
                .deletePublisher(id)
                .build();
    }

    private void deleteMedia(Integer id) {

        media()
                .setCredentials(USER_FOR_DELETION)
                .deleteMedia(id)
                .build();
    }

    private AdSpot createAdSpot(String name, Boolean isEnabled) {


        return adSpot()
                .createNewAdSpot(captionWithSuffix(name), captionWithSuffix("autoPub"), isEnabled)
                .build()
                .getAdSpotResponse();
    }
}
