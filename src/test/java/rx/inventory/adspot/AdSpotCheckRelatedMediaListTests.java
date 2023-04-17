package rx.inventory.adspot;

import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.inventory.media.Media;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;
import pages.Path;
import pages.inventory.adspots.AdSpotsPage;
import rx.BaseTest;
import widgets.inventory.adSpots.sidebar.CreateAdSpotSidebar;

import java.util.List;

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
public class AdSpotCheckRelatedMediaListTests extends BaseTest {

    private Media mediaActive;
    private Media mediaInActive;
    private Publisher publisher;
    private AdSpotsPage adSpotPage;
    private CreateAdSpotSidebar createAdSpotSidebar;

    private static final String MEDIA_NAME_ACTIVE = captionWithSuffix("4autoMediaActive");
    private static final String MEDIA_NAME_INACTIVE = captionWithSuffix("4autoMediaInActive");

    public AdSpotCheckRelatedMediaListTests() {
        adSpotPage = new AdSpotsPage();
        createAdSpotSidebar = new CreateAdSpotSidebar();
    }

    @BeforeClass
    private void loginAndPrepareTestData() {
        var tableData = adSpotPage.getAdSpotsTable().getTableData();

        createPublisherAndMedia();

        testStart()
                .given()
                .openDirectPath(Path.AD_SPOT)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, adSpotPage.getTableProgressBar())
                .clickOnWebElement(adSpotPage.getCreateAdSpotButton())
                .waitSideBarOpened()
                .testEnd();
    }

    @Test(description = "Check Related Media list")
    private void checkRelatedMediaList() {

        testStart()
                .and(String.format("Select Publisher '%s'", publisher.getName()))

                .selectFromDropdown(createAdSpotSidebar.getGeneralTab().getPublisherInput(),
                        createAdSpotSidebar.getGeneralTab().getPublisherItems(), publisher.getName())
                .clickOnWebElement(createAdSpotSidebar.getGeneralTab().getRelatedMedia())
                .then("Ensure inactive media is absent in the Related Media list")
                .validateList(createAdSpotSidebar.getGeneralTab().getRelatedMediaItems(),
                        List.of(mediaActive.getName(), mediaInActive.getName()))
                .testEnd();
    }

    private void createPublisherAndMedia() {

        publisher = publisher()
                .createNewPublisher(captionWithSuffix("000000autoPub"))
                .build()
                .getPublisherResponse();

        mediaActive = media()
                .createNewMedia(MEDIA_NAME_ACTIVE, publisher.getId(), true)
                .build()
                .getMediaResponse();

        mediaInActive = media()
                .createNewMedia(MEDIA_NAME_INACTIVE, publisher.getId(), false)
                .build()
                .getMediaResponse();
    }

    @AfterMethod(alwaysRun = true)
    private void clickSaveAndWaitSidebarClosed() {

        testStart()
                .and("Click Х")
                .clickOnWebElement(createAdSpotSidebar.getCloseIcon())
                .waitAndValidate(not(visible), createAdSpotSidebar.getGeneralTab().getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), adSpotPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .testEnd();
    }

    @AfterClass(alwaysRun = true)
    private void logoutAndDeleteTestData() {

        testStart()
                .and("Logout")
                .logOut()
                .testEnd();

        deletePublisher(publisher.getId());
        deleteMedia(mediaActive.getId());
        deleteMedia(mediaInActive.getId());
    }

    private void deleteMedia(Integer id) {

        if (media()
                .setCredentials(USER_FOR_DELETION)
                .deleteMedia(id)
                .build()
                .getResponseCode() == HttpStatus.SC_NO_CONTENT)
            log.info(String.format("Deleted media %s", id));
    }

    private void deletePublisher(Integer id) {

        if (publisher()
                .setCredentials(USER_FOR_DELETION)
                .deletePublisher(id)
                .build()
                .getResponseCode() == HttpStatus.SC_NO_CONTENT)
            log.info(String.format("Deleted publisher %s", id));
    }
}
