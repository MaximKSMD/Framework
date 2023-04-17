package rx.inventory.adspot;

import api.dto.rx.inventory.adspot.AdSpot;
import api.preconditionbuilders.AdSpotPrecondition;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.*;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.inventory.adspots.AdSpotsPage;
import rx.BaseTest;
import widgets.common.table.ColumnNames;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
@Epic("Waiting for separate QA env")
@Link("https://rakutenadvertising.atlassian.net/browse/GS-3280")
@Feature("Ad Spots")
public class AdSpotSortingTableTests extends BaseTest {

    private int totalAdSpots;
    private List<String> sortIdsByAsc;
    private List<String> sortIdsByDesc;
    private List<String> sortNamesByAsc;
    private List<String> sortNamesByDesc;

    private List<String> sortPublisherNameByDesc;
    private List<String> sortPublisherNameByAsc;
    private List<String> sortRelatedMediasByDesc;
    private List<String> sortRelatedMediaByAsc;
    private List<String> sortStatusByDesc;
    private List<String> sortStatusByAsc;
    private AdSpotsPage adSpotsPage;

    private static final String ASC = "ascending";
    private static final String DESC = "descending";

    public AdSpotSortingTableTests() {
        adSpotsPage = new AdSpotsPage();
    }

    @BeforeClass
    private void loginAndCreateExpectedResults() {

        if (getTotalAdSpots() < 60) {
            generateAdSpots();
        }

        totalAdSpots = getTotalAdSpots();

        //expected results for Media Name column
        sortNamesByDesc = getNamesByDesc();
        sortNamesByAsc = getNamesByAsc();

        //Expected result for ID column
        sortIdsByAsc = getIdsByAsc();
        sortIdsByDesc = getIdsByDesc();

        //Expected result for  Publisher Name column
        sortPublisherNameByAsc = getPublisherNameByAsc();
        sortPublisherNameByDesc = getPublisherNameByDesc();

        //Expected result for Related Media column
        sortRelatedMediaByAsc = getRelatedMediaByAsc();
        sortRelatedMediasByDesc = getRelatedMediaByDesc();

        //Expected result for Active/Inactive column
        sortStatusByAsc = getStatusByAsc();
        sortStatusByDesc = getStatusByDesc();
    }

    @Test(testName = "Sorting 'ID' column by descending")
    public void adSpotSortingByIdDesc() {
        sortByDescColumnByName(ColumnNames.ID);
        validateSortData(ColumnNames.ID, DESC, sortIdsByDesc);
    }

    @Test(testName = "Sorting 'ID' column by ascending")
    public void adSpotSortingByIdAsc() {

        sortByAscColumnByName(ColumnNames.ID);
        validateSortData(ColumnNames.ID, ASC, sortIdsByAsc);
    }

    @Test(testName = "Sorting 'Ad Spot Name' column by descending")
    public void adSpotSortingByNameDesc() {
        sortByDescColumnByName(ColumnNames.AD_SPOT_NAME);
        validateSortData(ColumnNames.AD_SPOT_NAME, DESC, sortNamesByDesc);
    }

    @Test(testName = "Sorting 'Ad Spot Name' column by ascending")
    public void adSpotSortingByAdSpotNameAsc() {
        sortByAscColumnByName(ColumnNames.AD_SPOT_NAME);
        validateSortData(ColumnNames.AD_SPOT_NAME, ASC, sortNamesByAsc);
    }

    @Test(testName = "Sorting 'Publisher' column by ascending")
    public void adSpotSortingByPublisherNameAsc() {
        sortByAscColumnByName(ColumnNames.PUBLISHER);
        validateSortData(ColumnNames.PUBLISHER, ASC, sortPublisherNameByAsc);
    }

    @Test(testName = "Sorting 'Publisher' column by descending")
    public void adSpotSortingByPublisherNameDesc() {
        sortByDescColumnByName(ColumnNames.PUBLISHER);
        validateSortData(ColumnNames.PUBLISHER, DESC, sortPublisherNameByDesc);
    }

    @Test(testName = "Sorting 'Related Media' column by descending")
    public void adSpotSortingByRelatedMediaDesc() {
        sortByDescColumnByName(ColumnNames.RELATED_MEDIA);
        validateSortData(ColumnNames.RELATED_MEDIA, DESC, sortRelatedMediasByDesc);
    }

    @Test(testName = "Sorting 'Related Media' column by ascending")
    public void adSpotSortingByRelatedMediaAsc() {
        sortByAscColumnByName(ColumnNames.RELATED_MEDIA);
        validateSortData(ColumnNames.RELATED_MEDIA, ASC, sortRelatedMediaByAsc);
    }

    //Todo make tests enabled  after fix of attached Bug
    @Test(testName = "Sorting 'Active/Inactive' column by descending", enabled = false)
    @Issue("https://rakutenadvertising.atlassian.net/browse/GS-3273")
    public void adSpotSortingByStatusDesc() {
        sortByDescColumnByName(ColumnNames.STATUS);
        validateSortData(ColumnNames.ID, DESC, sortStatusByDesc);
    }

    //Todo make tests enabled  after fix of attached Bug
    @Test(testName = "Sorting 'Active/Inactive' column by ascending", enabled = false)
    @Issue("https://rakutenadvertising.atlassian.net/browse/GS-3273")
    public void adSpotSortingByStatusAsc() {
        sortByAscColumnByName(ColumnNames.STATUS);
        validateSortData(ColumnNames.ID, ASC, sortStatusByAsc);
    }

    @BeforeMethod
    private void login() {
        var table = adSpotsPage.getAdSpotsTable().getShowHideColumns();
        var tableData = adSpotsPage.getAdSpotsTable().getTableData();
        testStart()
                .given()
                .openDirectPath(Path.AD_SPOT)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, adSpotsPage.getNuxtProgress())
                .testEnd();
    }

    @AfterMethod(alwaysRun = true)
    private void logOut() {
        testStart()
                .given()
                .logOut()
                .testEnd();
    }

    @Step("Sort column {0} by DESC")
    private void sortByDescColumnByName(ColumnNames columnName) {
        var tableData = adSpotsPage.getAdSpotsTable().getTableData();

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
                .waitAndValidate(disappear, adSpotsPage.getNuxtProgress())
                .testEnd();

    }

    @Step("Sort column {0} by ASC")
    private void sortByAscColumnByName(ColumnNames columnName) {
        var tableData = adSpotsPage.getAdSpotsTable().getTableData();
        testStart()
                .given()
                .and(String.format("Sort column '%s'", columnName))
                .clickOnWebElement(tableData.getColumnHeader(columnName.getName()))
                .then("Ensure that sort by ascending: validate column attribute value")
                .validateAttribute(tableData.getColumnHeader(columnName.getName()),
                        "aria-sort", ASC)
                .waitAndValidate(disappear, adSpotsPage.getNuxtProgress())
                .testEnd();

    }

    @Step("Validate data in column {0} sorted by {1}")
    private void validateSortData(ColumnNames columnName, String sortType, List<String> expectedResultList) {
        var tableData = adSpotsPage.getAdSpotsTable().getTableData();
        var tablePagination = adSpotsPage.getAdSpotsTable().getTablePagination();
        //Todo Add checking of total qauntity in pagination test when
        // https://rakutenadvertising.atlassian.net/browse/GS-3280 will be ready
        testStart()
                .given()
                .waitAndValidate(disappear, adSpotsPage.getNuxtProgress())
                .and("Select 50 row per page")
                .scrollIntoView(tablePagination.getPageMenu())
                .selectFromDropdown(tablePagination.getPageMenu(),
                        tablePagination.getRowNumbersList(), "50")
                .waitLoading(visible, adSpotsPage.getTableProgressBar())
                .waitLoading(disappear, adSpotsPage.getTableProgressBar())
                .then(String.format("Validate that text in table footer '1-50 of %s'", totalAdSpots))
                .validateContainsText(tablePagination.getPaginationPanel(), "1-50 of")
                .then(String.format("Validate data in column '%s' should be sorted by %s", columnName.getName(), sortType))
                .validateList(tableData.getCustomCells(columnName), expectedResultList.subList(0, 50))
                .and("Check next page")
                .clickOnWebElement(tablePagination.getNext())
                .waitLoading(visible, adSpotsPage.getTableProgressBar())
                .waitLoading(disappear, adSpotsPage.getTableProgressBar())
                .then(String.format("Validate that text in table footer '51-%s of %s'",
                        Math.min(100, totalAdSpots), totalAdSpots))
                .validateContainsText(tablePagination.getPaginationPanel(),
                        String.format("51-%s of", Math.min(100, totalAdSpots)))
                .then(String.format("Validate data in column '%s' should be sorted by %s", columnName.getName(), sortType))
                .validateList(tableData.getCustomCells(columnName),
                        expectedResultList.subList(50, Math.min(100, totalAdSpots)))
                .testEnd();
    }


    private int getTotalAdSpots() {

        return AdSpotPrecondition.adSpot()
                .getAllAdSpotsList()
                .build()
                .getAdSpotsGetAllResponse()
                .getTotal();
    }

    private List<String> getNamesByDesc() {

        return getAllItemsByParams("name-desc").stream()
                .map(AdSpot::getName)
                .collect(Collectors.toList());
    }

    private List<String> getNamesByAsc() {

        return getAllItemsByParams("name-asc").stream()
                .map(AdSpot::getName)
                .collect(Collectors.toList());
    }

    private List<String> getIdsByAsc() {

        return getAllItemsByParams("id-asc").stream()
                .map(AdSpot::getId)
                .map(x -> x.toString())
                .collect(Collectors.toList());
    }

    private List<String> getIdsByDesc() {

        return getAllItemsByParams("id-desc").stream()
                .map(AdSpot::getId)
                .map(x -> x.toString())
                .collect(Collectors.toList());
    }

    private List<String> getPublisherNameByAsc() {

        return getAllItemsByParams("publisher_name-asc").stream()
                .map(AdSpot::getPublisherName)
                .collect(Collectors.toList());
    }

    private List<String> getPublisherNameByDesc() {

        return getAllItemsByParams("publisher_name-desc").stream()
                .map(AdSpot::getPublisherName)
                .collect(Collectors.toList());
    }

    private List<String> getRelatedMediaByDesc() {

        return getAllItemsByParams("media_name-desc").stream()
                .map(AdSpot::getMediaName)
                .collect(Collectors.toList());
    }

    private List<String> getRelatedMediaByAsc() {

        return getAllItemsByParams("media_name-asc").stream()
                .map(AdSpot::getMediaName)
                .collect(Collectors.toList());
    }

    private List<String> getStatusByDesc() {

        return getAllItemsByParams("enabled-desc").stream()
                .map(AdSpot::getId)
                .map(x -> x.toString())
                .collect(Collectors.toList());
    }

    private List<String> getStatusByAsc() {

        return getAllItemsByParams("enabled-asc").stream()
                .map(AdSpot::getId)
                .map(x -> x.toString())
                .collect(Collectors.toList());
    }

    private List<AdSpot> getAllItemsByParams(String strParams) {

        return AdSpotPrecondition.adSpot()
                .getAdSpotsWithFilter(Map.of("sort", strParams))
                .build()
                .getAdSpotsGetAllResponse()
                .getItems();
    }

    private void generateAdSpots() {
        while (getTotalAdSpots() < 60) {
            AdSpotPrecondition.adSpot()
                    .createNewAdSpot(captionWithSuffix("auto"))
                    .build();
        }
    }
}
