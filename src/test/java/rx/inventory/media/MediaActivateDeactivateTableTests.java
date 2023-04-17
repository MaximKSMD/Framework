package rx.inventory.media;

import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.inventory.media.Media;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.inventory.media.*;
import rx.BaseTest;
import widgets.common.table.ColumnNames;
import widgets.common.table.Statuses;
import widgets.errormessages.ErrorMessages;

import java.util.*;

import static api.preconditionbuilders.MediaPrecondition.media;
import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static configurations.User.USER_FOR_DELETION;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature("Media")
public class MediaActivateDeactivateTableTests extends BaseTest {

    private Media mediaActive1;
    private Media mediaInActive1;
    private Media mediaActiveBulkA1;
    private Media mediaActiveBulkA2;
    private Media mediaActiveBulkA3;
    private Media mediaActiveBulkD1;
    private Media mediaActiveBulkD2;
    private Media mediaActiveBulkD3;

    private Media mediaInActiveBulkA1;
    private Media mediaInActiveBulkA2;
    private Media mediaInActiveBulkA3;
    private Media mediaInActiveBulkD1;
    private Media mediaInActiveBulkD2;
    private Media mediaInActiveBulkD3;

    private Media mediaActiveWithInactivePublisher;
    private Media mediaInActiveWithInactivePublisher;

    private MediaPage mediaPage;

    private static final String PREFIX_MEDIA = captionWithSuffix("autoMediaSingle");
    private static final String PREFIX_MEDIA_BULK_1 = captionWithSuffix("autoMediaBulk1");
    private static final String PREFIX_MEDIA_BULK_2 = captionWithSuffix("autoMediaBulk2");
    private static final String PREFIX_MEDIA_INACTIVE_PUBLISHER = captionWithSuffix("autoMediaInactivePub");
    private static final String CLASS_ATTRIBUTE_FOR_UNCHECKED_CHECKBOX = "v-icon notranslate mdi mdi-checkbox-blank-outline theme--light";

    public MediaActivateDeactivateTableTests() {
        mediaPage = new MediaPage();
    }

    @BeforeClass
    public void loginAndCreateExpectedResuts() {
        deleteMedia();

        //Creating media Using API
        mediaActive1 = createMedia(PREFIX_MEDIA, true);
        mediaInActive1 = createMedia(PREFIX_MEDIA, false);

        mediaActiveBulkA1 = createMedia(PREFIX_MEDIA_BULK_1, true);
        mediaActiveBulkA2 = createMedia(PREFIX_MEDIA_BULK_1, true);
        mediaActiveBulkA3 = createMedia(PREFIX_MEDIA_BULK_1, true);
        mediaInActiveBulkA1 = createMedia(PREFIX_MEDIA_BULK_1, false);
        mediaInActiveBulkA2 = createMedia(PREFIX_MEDIA_BULK_1, false);
        mediaInActiveBulkA3 = createMedia(PREFIX_MEDIA_BULK_1, false);

        mediaActiveBulkD1 = createMedia(PREFIX_MEDIA_BULK_2, true);
        mediaActiveBulkD2 = createMedia(PREFIX_MEDIA_BULK_2, true);
        mediaActiveBulkD3 = createMedia(PREFIX_MEDIA_BULK_2, true);
        mediaInActiveBulkD1 = createMedia(PREFIX_MEDIA_BULK_2, false);
        mediaInActiveBulkD2 = createMedia(PREFIX_MEDIA_BULK_2, false);
        mediaInActiveBulkD3 = createMedia(PREFIX_MEDIA_BULK_2, false);

        mediaActiveWithInactivePublisher = createMedia(PREFIX_MEDIA_INACTIVE_PUBLISHER, true);
        deactivatePublisher(mediaActiveWithInactivePublisher.getPublisherId());
        mediaInActiveWithInactivePublisher = createMedia(PREFIX_MEDIA_INACTIVE_PUBLISHER, false);
        deactivatePublisher(mediaInActiveWithInactivePublisher.getPublisherId());
    }

    @BeforeMethod
    private void login() {
        testStart()
                .given()
                .openDirectPath(Path.MEDIA)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, mediaPage.getNuxtProgress())
                .testEnd();
    }

    @Test(testName = "Deactivate single media")
    public void deactivateSingleMedia() {
        var tableData = mediaPage.getMediaTable().getTableData();
        var tablePagination = mediaPage.getMediaTable().getTablePagination();

        testStart()
                .given()
                .and(String.format("Search media by name '%s'", mediaActive1.getName()))
                .setValueWithClean(tableData.getSearch(), mediaActive1.getName())
                .pressEnterKey(tableData.getSearch())
                .validateContainsText(tablePagination.getPaginationPanel(), "1-1 of 1")
                .selectAllRowsByColumnCellValue(tableData, ColumnNames.MEDIA_NAME, mediaActive1.getName())
                .and("Click 'Deactivate' button")
                .clickOnWebElement(mediaPage.getDeactivateMediaButton())
                .then("Validate media status should be changed on 'Inactive'")
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.MEDIA_NAME, mediaActive1.getName()),
                        Statuses.INACTIVE.getStatus())
                .testEnd();
    }

    @Test(testName = "Deactivate single media with inactive publisher")
    public void deactivateSingleMediaWithInactivePublisher() {
        var tableData = mediaPage.getMediaTable().getTableData();
        var tablePagination = mediaPage.getMediaTable().getTablePagination();
        var toasterPanel = mediaPage.getToasterMessage();

        testStart()
                .given()
                .and(String.format("Search media by name '%s'", mediaActiveWithInactivePublisher.getName()))
                .setValueWithClean(tableData.getSearch(), mediaActiveWithInactivePublisher.getName())
                .pressEnterKey(tableData.getSearch())
                .validateContainsText(tablePagination.getPaginationPanel(), "1-1 of 1")
                .selectAllRowsByColumnCellValue(tableData, ColumnNames.MEDIA_NAME,
                        mediaActiveWithInactivePublisher.getName())
                .and("Click 'Deactivate' button")
                .clickOnWebElement(mediaPage.getDeactivateMediaButton())
                .validate(not(visible), toasterPanel.getPanelError())
                .then("Validate media status should be changed on 'Active'")
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.MEDIA_NAME, mediaActiveWithInactivePublisher.getName()),
                        Statuses.ACTIVE.getStatus())
                .testEnd();
    }

    @Test(testName = "Activate single media with inactive publisher")
    public void activateSingleMediaWithInactivePublisher() {
        var tableData = mediaPage.getMediaTable().getTableData();
        var tablePagination = mediaPage.getMediaTable().getTablePagination();
        var toasterPanel = mediaPage.getToasterMessage();

        testStart()
                .given()
                .and(String.format("Search media by name '%s'", mediaInActiveWithInactivePublisher.getName()))
                .setValueWithClean(tableData.getSearch(), mediaInActiveWithInactivePublisher.getName())
                .pressEnterKey(tableData.getSearch())
                .validateContainsText(tablePagination.getPaginationPanel(), "1-1 of 1")
                .selectAllRowsByColumnCellValue(tableData, ColumnNames.MEDIA_NAME,
                        mediaInActiveWithInactivePublisher.getName())
                .and("Click 'Activate' button")
                .clickOnWebElement(mediaPage.getActivateMediaButton())
                .validate(not(visible), toasterPanel.getPanelError())
                .then("Validate media status should not be changed on 'Inactive'")
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.MEDIA_NAME,
                                mediaInActiveWithInactivePublisher.getName()),
                        Statuses.ACTIVE.getStatus())
                .testEnd();
    }

    @Test(testName = "Activate single media")
    public void activateSingleMedia() {
        var tableData = mediaPage.getMediaTable().getTableData();
        var tablePagination = mediaPage.getMediaTable().getTablePagination();

        testStart()
                .given()
                .and(String.format("Search media by name '%s'", mediaInActive1.getName()))
                .setValueWithClean(tableData.getSearch(), mediaInActive1.getName())
                .pressEnterKey(tableData.getSearch())
                .validateContainsText(tablePagination.getPaginationPanel(),
                        "1-1 of 1")
                .selectAllRowsByColumnCellValue(tableData, ColumnNames.MEDIA_NAME,
                        mediaInActive1.getName())
                .and("Click 'Deactivate' button")
                .clickOnWebElement(mediaPage.getActivateMediaButton())
                .then("Validate media status should be changed on 'Active'")
                .waitAndValidate(disappear, mediaPage.getTableProgressBar())
                .and(String.format("Search media by name '%s'", mediaInActive1))
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.MEDIA_NAME, mediaInActive1.getName()),
                        Statuses.ACTIVE.getStatus())
                .testEnd();
    }

    @Test(testName = "Activate bulk media")
    public void activateBulkMedia() {
        var tableData = mediaPage.getMediaTable().getTableData();
        var filterOptions = mediaPage.getMediaTable().getColumnFiltersBlock();
        var tablePagination = mediaPage.getMediaTable().getTablePagination();

        testStart()
                .given()
                .and(String.format("Search media by name '%s'", PREFIX_MEDIA_BULK_1))
                .setValueWithClean(tableData.getSearch(), PREFIX_MEDIA_BULK_1)
                .pressEnterKey(tableData.getSearch())
                .validateContainsText(tablePagination.getPaginationPanel(),
                        "1-6 of 6")
                .selectAllRowsByColumnCellValue(tableData, ColumnNames.MEDIA_NAME,
                        PREFIX_MEDIA_BULK_1)
                .and("Click 'Activate' button")
                .clickOnWebElement(mediaPage.getActivateMediaButton())
                .then("Validate media status should be changed on 'Active'")
                .waitAndValidate(visible, tableData.getCheckbox(1)
                        .shouldHave(attributeMatching("class", CLASS_ATTRIBUTE_FOR_UNCHECKED_CHECKBOX)))
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.MEDIA_NAME, mediaInActiveBulkA1.getName()),
                        Statuses.ACTIVE.getStatus())
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.MEDIA_NAME, mediaInActiveBulkA2.getName()),
                        Statuses.ACTIVE.getStatus())
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.MEDIA_NAME, mediaInActiveBulkA3.getName()),
                        Statuses.ACTIVE.getStatus())
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.MEDIA_NAME, mediaActiveBulkA1.getName()),
                        Statuses.ACTIVE.getStatus())
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.MEDIA_NAME, mediaActiveBulkA2.getName()),
                        Statuses.ACTIVE.getStatus())
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.MEDIA_NAME, mediaActiveBulkA3.getName()),
                        Statuses.ACTIVE.getStatus())
                .validate(not(exist), mediaPage.getToasterMessage().getPanelError())
                .testEnd();
    }

    @Test(testName = "Deactivate bulk media")
    public void deactivateBulkMedia() {
        var tableData = mediaPage.getMediaTable().getTableData();
        var tablePagination = mediaPage.getMediaTable().getTablePagination();

        testStart()
                .given()
                .and(String.format("Search media by name '%s'", PREFIX_MEDIA_BULK_2))
                .setValueWithClean(tableData.getSearch(), PREFIX_MEDIA_BULK_2)
                .pressEnterKey(tableData.getSearch())
                .validateContainsText(tablePagination.getPaginationPanel(),
                        "1-6 of 6")
                .selectAllRowsByColumnCellValue(tableData, ColumnNames.MEDIA_NAME,
                        PREFIX_MEDIA_BULK_2)
                .and("Click 'Activate' button")
                .clickOnWebElement(mediaPage.getActivateMediaButton())
                .then("Validate media status should be changed on 'Inactive'")
                .waitAndValidate(visible, tableData.getCheckbox(1)
                        .shouldHave(attributeMatching("class", CLASS_ATTRIBUTE_FOR_UNCHECKED_CHECKBOX)))
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.MEDIA_NAME, mediaInActiveBulkD1.getName()),
                        Statuses.ACTIVE.getStatus())
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.MEDIA_NAME, mediaInActiveBulkD2.getName()),
                        Statuses.ACTIVE.getStatus())
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.MEDIA_NAME, mediaInActiveBulkD3.getName()),
                        Statuses.ACTIVE.getStatus())
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.MEDIA_NAME, mediaActiveBulkD1.getName()),
                        Statuses.ACTIVE.getStatus())
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.MEDIA_NAME, mediaActiveBulkD2.getName()),
                        Statuses.ACTIVE.getStatus())
                .validateContainsText(tableData.getCellByRowValue(ColumnNames.STATUS,
                                ColumnNames.MEDIA_NAME, mediaActiveBulkD3.getName()),
                        Statuses.ACTIVE.getStatus())
                .validate(not(exist), mediaPage.getToasterMessage().getPanelError())
                .testEnd();
    }

    private int getTotalMedia() {

        return media()
                .getMediaWithFilter(Map.of("search", PREFIX_MEDIA))
                .build()
                .getMediaGetAllResponse()
                .getTotal();
    }

    private List<Media> getAllMediaItemsByParams(String strParams) {

        return media()
                .getMediaWithFilter(Map.of("search", strParams))
                .build()
                .getMediaGetAllResponse()
                .getItems();
    }

    private List<Publisher> getAllPublishersItemsByParams(String strParams) {

        return publisher()
                .getPublishersListWithFilter(Map.of("search", strParams))
                .build()
                .getPublisherGetAllResponse()
                .getItems();
    }

    @AfterMethod
    public void logOut() {
        testStart()
                .logOut()
                .testEnd();
    }

    @AfterClass(alwaysRun = true)
    public void cleanData() {
        deleteMedia();
        deletePublishers();
    }

    @Step("Deleting media")
    private void deleteMedia() {
        deleteMedia(getAllMediaItemsByParams(PREFIX_MEDIA));
        deleteMedia(getAllMediaItemsByParams(PREFIX_MEDIA_BULK_1));
        deleteMedia(getAllMediaItemsByParams(PREFIX_MEDIA_BULK_2));
        deleteMedia(getAllMediaItemsByParams(PREFIX_MEDIA_INACTIVE_PUBLISHER));
    }

    @Step("Deleting media from media list")
    private void deleteMedia(List<Media> mediaList) {

        for (Media media : mediaList) {
            media()
                    .setCredentials(USER_FOR_DELETION)
                    .deleteMedia(media.getId())
                    .build();
        }
    }

    @Step("Deleting publisher by id")
    private void deactivatePublisher(Integer id) {
        publisher()
                .changePublisherStatus(id, false)
                .build();
    }

    @Step("Deleting publisher from publisher list")
    private void deletePublishers() {
        List<Publisher> publishersList = getAllPublishersItemsByParams("autoPublisher");

        for (Publisher publisher : publishersList) {
            publisher()
                    .setCredentials(USER_FOR_DELETION)
                    .deletePublisher(publisher.getId())
                    .build();
        }
    }

    private Media createMedia(String name, Boolean isEnabled) {

        return media()
                .createNewMedia(name, isEnabled)
                .build()
                .getMediaResponse();
    }
}
