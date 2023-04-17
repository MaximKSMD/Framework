package rx.inventory.media;


import api.dto.rx.inventory.media.Media;
import api.preconditionbuilders.MediaPrecondition;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.inventory.media.*;
import rx.BaseTest;
import widgets.common.table.ColumnNames;

import java.util.*;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.*;

@Slf4j
@Listeners({ScreenShooter.class})
@Epic("Waiting for separate QA env")
@Link("https://rakutenadvertising.atlassian.net/browse/GS-3280")
@Feature("Media")
public class MediaSortingTableTests extends BaseTest {

    private int totalMedia;
    private List<String> sortIdsByAsc;
    private List<String> sortIdsByDesc;
    private List<String> sortNamesByAsc;
    private List<String> sortNamesByDesc;


    private List<String> sortURLByAsc;
    private List<String> sortURLByDesc;

    private List<String> sortPublisherNameByDesc;
    private List<String> sortPublisherNameByAsc;

    private MediaPage mediaPage;

    private static final String ASC = "ascending";
    private static final String DESC = "descending";

    public MediaSortingTableTests() {
        mediaPage = new MediaPage();
    }

    @BeforeClass
    private void loginAndCreateExpectedResults() {

        if (getTotalMedia() < 60) generateMedia();

        totalMedia = getTotalMedia();

        //expected results for Media Name column
        sortNamesByDesc = getNamesByDesc();
        sortNamesByAsc = getNamesByAsc();

        //Expected result for ID column
        sortIdsByAsc = getIdsByAsc();
        sortIdsByDesc = getIdsByDesc();

        //Expected result for  Publisher Name column
        sortPublisherNameByAsc = getPublisherNameByAsc();
        sortPublisherNameByDesc = getPublisherNameByDesc();

        //Expected result for URL column
        sortURLByAsc = getUrlByAsc();
        sortURLByDesc = getUrlByDesc();
    }

    @Test(testName = "Sorting 'Media Name' column by descending")
    public void mediaSortingByMediaNameDesc() {
        sortByDescColumnByName(ColumnNames.MEDIA_NAME);
        validateSortData(ColumnNames.MEDIA_NAME, DESC, sortNamesByDesc);
    }

    @Test(testName = "Sorting 'Media Name' column by ascending")
    public void mediaSortingByMediaNameAsc() {
        sortByAscColumnByName(ColumnNames.MEDIA_NAME);
        validateSortData(ColumnNames.MEDIA_NAME, ASC, sortNamesByAsc);
    }

    @Test(testName = "Sorting 'Publisher' column by ascending")
    public void mediaSortingByPublisherNameAsc() {
        sortByAscColumnByName(ColumnNames.PUBLISHER);
        validateSortData(ColumnNames.PUBLISHER, ASC, sortPublisherNameByAsc);
    }

    @Test(testName = "Sorting 'Publisher' column by descending")
    public void mediaSortingByPublisherNameDesc() {
        sortByDescColumnByName(ColumnNames.PUBLISHER);
        validateSortData(ColumnNames.PUBLISHER, DESC, sortPublisherNameByDesc);
    }

    @Test(testName = "Sorting 'ID' column by descending")
    public void mediaSortingByIdDesc() {
        sortByDescColumnByName(ColumnNames.ID);
        validateSortData(ColumnNames.ID, DESC, sortIdsByDesc);
    }

    @Test(testName = "Sorting 'ID' column by ascending")
    public void mediaSortingByIdAsc() {

        sortByAscColumnByName(ColumnNames.ID);
        validateSortData(ColumnNames.ID, ASC, sortIdsByAsc);
    }

    @Test(testName = "Sorting 'Site/App Store URL' column by descending")
    public void mediaSortingByUrlDesc() {
        sortByDescColumnByName(ColumnNames.SITE_APP_STORE_URL);
        validateSortData(ColumnNames.SITE_APP_STORE_URL, DESC, sortURLByDesc);
    }

    @Test(testName = "Sorting 'Site/App Store URL' column by ascending")
    public void mediaSortingByUrlAsc() {
        sortByAscColumnByName(ColumnNames.SITE_APP_STORE_URL);
        validateSortData(ColumnNames.SITE_APP_STORE_URL, ASC, sortURLByAsc);
    }

    @BeforeMethod
    private void login() {
        var table = mediaPage.getMediaTable().getShowHideColumns();
        var tableData = mediaPage.getMediaTable().getTableData();

        testStart()
                .given()
                .openDirectPath(Path.MEDIA)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, mediaPage.getNuxtProgress())
                .clickOnWebElement(table.getShowHideColumnsBtn())
                .selectCheckBox(table.getMenuItemCheckbox(ColumnNames.SITE_APP_STORE_URL))
                .selectCheckBox(table.getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .validate(visible, tableData.getColumnHeader(ColumnNames.SITE_APP_STORE_URL.getName()))
                .clickOnWebElement(table.getShowHideColumnsBtn())
                .testEnd();
    }

    @AfterMethod
    private void logOut() {
        testStart()
                .given()
                .logOut()
                .testEnd();
    }

    @Step("Sort column {0} by DESC")
    private void sortByDescColumnByName(ColumnNames columnName) {
        var tableData = mediaPage.getMediaTable().getTableData();

        testStart()
                .given()
                .and(String.format("Sort column '%s'", columnName))
                .clickOnWebElement(tableData.getColumnHeader(columnName.getName()))
                .then("Ensure that sort by descending: validate column attribute value")
                .validateAttribute(tableData.getColumnHeader(columnName.getName()),
                        "aria-sort", ASC)
                .clickOnWebElement(tableData.getColumnHeader(columnName.getName()))
                .validateAttribute(tableData.getColumnHeader(columnName.getName()),
                        "aria-sort", DESC)
                .waitAndValidate(disappear, mediaPage.getNuxtProgress())
                .testEnd();

    }

    @Step("Sort column {0} by ASC")
    private void sortByAscColumnByName(ColumnNames columnName) {
        var tableData = mediaPage.getMediaTable().getTableData();
        testStart()
                .given()
                .and(String.format("Sort column '%s'", columnName))
                .clickOnWebElement(tableData.getColumnHeader(columnName.getName()))
                .then("Ensure that sort by ascending: validate column attribute value")
                .validateAttribute(tableData.getColumnHeader(columnName.getName()),
                        "aria-sort", ASC)
                .waitAndValidate(disappear, mediaPage.getNuxtProgress())
                .testEnd();

    }

    @Step("Validate data in column {0} sorted by {1}")
    private void validateSortData(ColumnNames columnName, String sortType, List<String> expectedResultList) {
        var tableData = mediaPage.getMediaTable().getTableData();
        var tablePagination = mediaPage.getMediaTable().getTablePagination();
        //Todo Add checking of total quantity in pagination test when
        // https://rakutenadvertising.atlassian.net/browse/GS-3280 will be ready
        testStart()
                .given()
                .waitAndValidate(disappear, mediaPage.getNuxtProgress())
                .and("Select 50 row per page")
                .selectFromDropdown(tablePagination.getPageMenu(),
                        tablePagination.getRowNumbersList(), "50")
                .waitLoading(visible, mediaPage.getTableProgressBar())
                .waitLoading(disappear, mediaPage.getTableProgressBar())
                .then("Validate that text in table footer '1-50 of")
                .validateContainsText(tablePagination.getPaginationPanel(), "1-50 of")
                .then(String.format("Validate data in column '%s' should be sorted by %s", columnName.getName(), sortType))
                .validateList(tableData.getCustomCells(columnName), expectedResultList.subList(0, 50))
                .and("Check next page")
                .clickOnWebElement(tablePagination.getNext())
                .waitLoading(visible, mediaPage.getTableProgressBar())
                .waitLoading(disappear, mediaPage.getTableProgressBar())
                .then(String.format("Validate that text in table footer '51-%s of %s'",
                        Math.min(100, totalMedia), totalMedia))
                .validateContainsText(tablePagination.getPaginationPanel(),
                        String.format("51-%s of", Math.min(100, totalMedia)))
                .then(String.format("Validate data in column '%s' should be sorted by %s", columnName.getName(), sortType))
                .validateList(tableData.getCustomCells(columnName),
                        expectedResultList.subList(50, Math.min(100, totalMedia)))

                .testEnd();
    }


    private int getTotalMedia() {

        return MediaPrecondition.media()
                .getAllMediaList()
                .build()
                .getMediaGetAllResponse()
                .getTotal();
    }

    private List<String> getNamesByDesc() {

        return getAllItemsByParams("name-desc").stream()
                .map(Media::getName)
                .collect(Collectors.toList());
    }

    private List<String> getNamesByAsc() {

        return getAllItemsByParams("name-asc").stream()
                .map(Media::getName)
                .collect(Collectors.toList());
    }

    private List<String> getIdsByAsc() {

        return getAllItemsByParams("id-asc").stream()
                .map(Media::getId)
                .map(x -> x.toString())
                .collect(Collectors.toList());
    }

    private List<String> getIdsByDesc() {

        return getAllItemsByParams("id-desc").stream()
                .map(Media::getId)
                .map(x -> x.toString())
                .collect(Collectors.toList());
    }

    private List<String> getPublisherNameByAsc() {

        return getAllItemsByParams("publisher_name-asc").stream()
                .map(Media::getPublisherName)
                .collect(Collectors.toList());
    }

    private List<String> getPublisherNameByDesc() {

        return getAllItemsByParams("publisher_name-desc").stream()
                .map(Media::getPublisherName)
                .collect(Collectors.toList());
    }

    private List<String> getUrlByAsc() {

        return getAllItemsByParams("url-asc").stream()
                .map(Media::getUrl)
                .collect(Collectors.toList());
    }

    private List<String> getUrlByDesc() {

        return getAllItemsByParams("url-desc").stream()
                .map(Media::getUrl)
                .collect(Collectors.toList());
    }

    private List<Media> getAllItemsByParams(String strParams) {

        return MediaPrecondition.media()
                .getMediaWithFilter(Map.of("sort", strParams))
                .build()
                .getMediaGetAllResponse()
                .getItems();
    }

    private void generateMedia() {
        while (getTotalMedia() < 60) {
            MediaPrecondition.media()
                    .createNewMedia(captionWithSuffix("auto"))
                    .build();
        }
    }
}
