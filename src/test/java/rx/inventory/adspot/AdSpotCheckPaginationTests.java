package rx.inventory.adspot;

import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.inventory.adspot.AdSpot;
import api.dto.rx.inventory.media.Media;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.inventory.adspots.AdSpotsPage;
import rx.BaseTest;

import java.util.ArrayList;
import java.util.List;

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
@Epic("Waiting for separate QA env")
@Link("https://rakutenadvertising.atlassian.net/browse/GS-3280")
@Feature("Ad Spots")
public class AdSpotCheckPaginationTests extends BaseTest {
    private AdSpotsPage adSpotsPage;

    private int totalAdSpots;
    private List<AdSpot> listAdSpots;

    Publisher publisher;
    Media media;

    public AdSpotCheckPaginationTests() {
        adSpotsPage = new AdSpotsPage();
    }

    @BeforeClass
    private void init() {
        if (getTotalAdSpots() < 200) generateAdSpots();

        totalAdSpots = getTotalAdSpots();
    }

    @BeforeMethod(alwaysRun = true)
    private void login() {
        testStart()
                .given()
                .openDirectPath(Path.AD_SPOT)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, adSpotsPage.getNuxtProgress())
                .testEnd();
    }

    @Test(description = "Verify Pagination: 100 rows per page", priority = 1)
    public void checkPagination100() {
        verifyPagination(100);
    }

    @Test(description = "Verify Pagination: 50 rows per page", priority = 4)
    public void checkPagination50() {
        verifyPagination(50);
    }

    @Test(description = "Verify Pagination: 25 rows per page", priority = 5)
    public void checkPagination25() {
        verifyPagination(25);
    }

    @Test(description = "Verify Pagination: 20 rows per page", priority = 6)
    public void checkPagination20() {
        verifyPagination(20);
    }

    @Test(description = "Verify Pagination: 15 rows per page", priority = 3)
    public void checkPagination15() {
        verifyPagination(15);
    }

    @Test(description = "Verify Pagination: 10 rows per page", priority = 2)
    public void checkPagination10() {
        verifyPagination(10);
    }

    @Step("Verify pagination {0}")
    private void verifyPagination(int rowsPerPage) {
        var tablePagination = adSpotsPage.getAdSpotsTable().getTablePagination();
        var tableData = adSpotsPage.getAdSpotsTable().getTableData();
        // Todo Add checking of total qauntity in pagination test when
        //  https://rakutenadvertising.atlassian.net/browse/GS-3280 will be ready
        testStart()
                .and(String.format("Select %s rows per page", rowsPerPage))
                .scrollIntoView(tablePagination.getPageMenu())
                .selectFromDropdown(tablePagination.getPageMenu(),
                        tablePagination.getRowNumbersList(), String.valueOf(rowsPerPage))
                .waitLoading(disappear, adSpotsPage.getTableProgressBar())
                .then(String.format("Validate that text in table footer '1-%s of", rowsPerPage))
                .validateContainsText(tablePagination.getPaginationPanel(),
                        String.format(String.format("1-%s of", rowsPerPage)))
                .then(String.format("Rows in table page equals %s", rowsPerPage))
                .validateListSize(tableData.getRows(), rowsPerPage)
                .and("Click on Next page")
                .scrollIntoView(tablePagination.getNext())
                .clickOnWebElement(tablePagination.getNext())
                .then(String.format("Validate that text in table footer '%s-%s of", rowsPerPage + 1, Math.min(rowsPerPage * 2, totalAdSpots)))
                .validateContainsText(tablePagination.getPaginationPanel(),
                        String.format("%s-%s of", rowsPerPage + 1, Math.min(rowsPerPage * 2, totalAdSpots)))
                .then(String.format("Rows in table page equals %s", rowsPerPage))
                .validateListSize(tableData.getRows(),rowsPerPage)
                .and("Click on Previous page")
                .scrollIntoView(tablePagination.getPrevious())
                .clickOnWebElement(tablePagination.getPrevious())
                .then(String.format("Validate that text in table footer '1-%s of", rowsPerPage))
                .validateContainsText(tablePagination.getPaginationPanel(), String.format("1-%s of", rowsPerPage))
                .then(String.format("Rows in table page equals %s", rowsPerPage))
                .validateListSize(tableData.getRows(), rowsPerPage)
                .testEnd();
    }

    @AfterMethod(alwaysRun = true)
    public void logout(){
        testStart()
                .and("Logout")
                .logOut()
                .testEnd();
    }

    @AfterClass(alwaysRun = true)
    private void deleteEntities() {

        if (listAdSpots != null) {
            listAdSpots.forEach(adSpot -> deleteAdSpot(adSpot.getId()));
            deleteMedia(media.getId());
            deletePublisher(publisher.getId());
        }
    }

    private void deleteAdSpot(Integer id) {
        adSpot()
                .setCredentials(USER_FOR_DELETION)
                .deleteAdSpot(id);
    }

    private void deleteMedia(Integer id) {
        media()
                .setCredentials(USER_FOR_DELETION)
                .deleteMedia(id);
    }

    private void deletePublisher(Integer id) {
        publisher()
                .setCredentials(USER_FOR_DELETION)
                .deletePublisher(id);
    }

    private int getTotalAdSpots() {

        return adSpot()
                .getAllAdSpotsList()
                .build()
                .getAdSpotsGetAllResponse()
                .getTotal();
    }

    private void generateAdSpots() {

        publisher = publisher()
                .createNewPublisher(captionWithSuffix("autoPub"))
                .build()
                .getPublisherResponse();

        media = media()
                .createNewMedia(captionWithSuffix("autoMedia"), publisher.getId(), true)
                .build()
                .getMediaResponse();

        listAdSpots = new ArrayList<>();
        while (getTotalAdSpots() < 210) {
            AdSpot adSpot = adSpot()
                    .createNewAdSpot(captionWithSuffix("auto"), publisher.getId(), media.getId(), true)
                    .build()
                    .getAdSpotResponse();

            listAdSpots.add(adSpot);
        }
    }
}
