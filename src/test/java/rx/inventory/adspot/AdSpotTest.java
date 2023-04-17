package rx.inventory.adspot;

import api.dto.rx.inventory.adspot.AdSpot;
import api.dto.rx.inventory.adspot.AdSpotRequest;
import api.dto.rx.inventory.adspot.Banner;
import api.dto.rx.inventory.media.Media;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Path;
import pages.inventory.adspots.AdSpotsPage;
import rx.BaseTest;
import widgets.common.table.ColumnNames;
import widgets.inventory.adSpots.sidebar.EditAdSpotSidebar;

import java.util.List;

import static api.preconditionbuilders.AdSpotPrecondition.adSpot;
import static api.preconditionbuilders.MediaPrecondition.media;
import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static configurations.User.TEST_USER;
import static configurations.User.USER_FOR_DELETION;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature("Ad Spots")
public class AdSpotTest extends BaseTest {

    private AdSpot adSpot;
    private AdSpotsPage adspotsPage;
    private EditAdSpotSidebar editAdSpotSidebar;

    public AdSpotTest() {
        adspotsPage = new AdSpotsPage();
        editAdSpotSidebar = new EditAdSpotSidebar();
    }

    @BeforeClass
    public void createNewAdSpot() {
        //Creating ad spot to edit Using API
        adSpot = adSpot()
                .createNewAdSpot()
                .build()
                .getAdSpotResponse();
    }


    @Test
    public void createCustomAdSpotTest() {
        var tableData = adspotsPage.getAdSpotsTable().getTableData();
        String adSpotName = captionWithSuffix("autoAdSpot");

        AdSpot adSpot = adSpot()
                .createNewAdSpot(createCustomAdSpot(adSpotName))
                .build()
                .getAdSpotResponse();

        //Opening Browser and check the ad spot created
        testStart()
                .given()
                .openDirectPath(Path.AD_SPOT)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, adspotsPage.getNuxtProgress())
                .setValueWithClean(tableData.getSearch(), adSpotName)
                .waitLoading(visible, adspotsPage.getTableProgressBar())
                .waitLoading(disappear, adspotsPage.getTableProgressBar())
                .validateListContainsTextOnly(tableData.getCustomCells(ColumnNames.AD_SPOT_NAME),
                        adSpot.getName())
                .and()
                .clickOnTableCellLink(tableData, ColumnNames.AD_SPOT_NAME, adSpot.getName())
                .waitSideBarOpened()
                .validateAttribute(editAdSpotSidebar.getGeneralTab().getNameInput(), "value", adSpotName)
                .validate(editAdSpotSidebar.getGeneralTab().getPublisherInput(), adSpot.getPublisherName())
                .validate(editAdSpotSidebar.getGeneralTab().getRelatedMediaInput(), adSpot.getMediaName())
                .validateAttribute(editAdSpotSidebar.getGeneralTab().getDefaultFloorPrice(), "value", adSpot.getFloorPrice().toString())
                .clickOnWebElement(editAdSpotSidebar.getSaveButton())
                .waitSideBarClosed()
                .and()
                .testEnd();
    }

    private AdSpotRequest createCustomAdSpot(String name) {

        Media media = media()
                .createNewMedia(captionWithSuffix("autoMedia"))
                .build()
                .getMediaResponse();

        return AdSpotRequest.builder()
                .name(name)
                .enabled(true)
                .publisherId(media.getPublisherId())
                .publisherName(media.getPublisherName())
                .floorPrice(9.99)
                .positionId(1)
                .mediaId(media.getId())
                .coppa(true)
                .categoryIds(List.of(1, 2))
                .sizeIds(List.of(10))
                .banner(Banner.builder()
                        .enabled(true)
                        .floorPrice(8.88)
                        .sizeIds(List.of(3))
                        .build())
                .build();
    }

    @AfterClass(alwaysRun = true)
    private void cleanData(){

        adSpot()
                .setCredentials(USER_FOR_DELETION)
                .deleteAdSpot(adSpot.getId())
                .build();

        media()
                .setCredentials(USER_FOR_DELETION)
                .deleteMedia(adSpot.getMediaId())
                .build();

        publisher()
                .setCredentials(USER_FOR_DELETION)
                .deletePublisher(adSpot.getPublisherId());
    }
}
