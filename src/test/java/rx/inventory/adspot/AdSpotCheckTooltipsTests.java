package rx.inventory.adspot;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.inventory.adspots.AdSpotsPage;
import rx.BaseTest;
import widgets.common.tooltip.AdSpotTooltipText;
import widgets.inventory.adSpots.sidebar.EditAdSpotSidebar;

import static com.codeborne.selenide.Condition.disappear;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature("Ad Spots")
public class AdSpotCheckTooltipsTests extends BaseTest {

    private AdSpotsPage adSpotsPage;
    private EditAdSpotSidebar editAdSpotSidebar;

    public AdSpotCheckTooltipsTests() {
        adSpotsPage = new AdSpotsPage();
        editAdSpotSidebar = new EditAdSpotSidebar();
    }

    @BeforeClass
    private void login() {
        var videoCard = editAdSpotSidebar.getGeneralTab().getVideoCard();
        var nativeCard = editAdSpotSidebar.getGeneralTab().getNativeCard();
        var bannerCard = editAdSpotSidebar.getGeneralTab().getBannerCard();

        testStart()
                .given()
                .openDirectPath(Path.AD_SPOT)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, adSpotsPage.getNuxtProgress())
                .clickOnWebElement(adSpotsPage.getCreateAdSpotButton())
                .waitSideBarOpened()
                .clickOnWebElement(bannerCard.getBannerCardHeader())
                .clickOnWebElement(nativeCard.getNativeCardHeader())
                .scrollIntoView(videoCard.getVideoCardHeader())
                .clickOnWebElement(videoCard.getVideoCardHeader())
                .testEnd();
    }

    @Test(description = "'Categories' Tooltip Text")
    public void categoriesTooltip() {
        verifyTooltip(editAdSpotSidebar.getGeneralTab().getCategoriesTooltipIcon(),
                AdSpotTooltipText.CATEGORIES.getText());
    }

    @Test(description = "'Content for Children' Tooltip Text")
    public void contentForChildrenTooltip() {
        verifyTooltip(editAdSpotSidebar.getGeneralTab().getContentForChildrenTooltipIcon(),
                AdSpotTooltipText.CONTENT_FOR_CHILDREN.getText());
    }

    @Test(description = "'Default Ad Sizes' Tooltip Text")
    public void defaultAdSizesTooltip() {
        verifyTooltip(editAdSpotSidebar.getGeneralTab().getDefaultAdSizesTooltipIcon(),
                AdSpotTooltipText.DEFAULT_AD_SIZES.getText());
    }

    @Test(description = "'Default Floor Price' Tooltip Text")
    public void defaultFloorPriceTooltip() {
        verifyTooltip(editAdSpotSidebar.getGeneralTab().getDefaultFloorPriceTooltipIcon(),
                AdSpotTooltipText.DEFAULT_FLOOR_PRICE.getText());
    }

    @Test(description = "'Native Card. 'Floor Price' Tooltip Text'", priority = 5)
    public void nativeFloorPriceTooltip() {
        verifyTooltip(editAdSpotSidebar.getGeneralTab().getNativeCard().getTooltipNativeFloorPrice(),
                AdSpotTooltipText.NATIVE_FLOOR_PRICE.getText());
    }

    @Test(description = "Banner Card. 'Ad Sizes' Tooltip Text", priority = 4)
    public void bannerAdSizesTooltip() {
        verifyTooltip(editAdSpotSidebar.getGeneralTab().getBannerCard().getBannerAdSizesTooltipIcon(),
                AdSpotTooltipText.BANNER_AD_SIZE.getText());
    }

    @Test(description = "Banner Card. 'Floor Price' Tooltip Text", priority = 1)
    public void bannerFloorPriceTooltip() {
        verifyTooltip(editAdSpotSidebar.getGeneralTab().getBannerCard().getBannerFloorPriceTooltipIcon(),
                AdSpotTooltipText.BANNER_FLOOR_PRICE.getText());
    }

    @Test(description = "Video Card. 'Floor Price' Tooltip Text", priority = 3)
    public void videoFloorPriceTooltip() {
        verifyTooltip(editAdSpotSidebar.getGeneralTab().getVideoCard().getVideoFloorPriceTooltipIcon(),
                AdSpotTooltipText.VIDEO_FLOOR_PRICE.getText());
    }

    @Test(description = "Video Card. 'Ad Sizes' Tooltip Text", priority = 2)
    public void videoAdSizesTooltip() {
        verifyTooltip(editAdSpotSidebar.getGeneralTab().getVideoCard().getVideoAdSizesTooltipIcon(),
                AdSpotTooltipText.VIDEO_AD_SIZE.getText());
    }

    @Step("Verify Tooltip Text")
    public void verifyTooltip(SelenideElement field, String expectedText) {
        testStart()
                .scrollIntoView(field)
                .validateTooltip(field, expectedText)
                .testEnd();
    }

    @AfterClass(alwaysRun = true)
    private void logout() {
        testStart()
                .clickOnWebElement(editAdSpotSidebar.getCloseIcon())
                .waitSideBarClosed()
                .logOut()
                .testEnd();
    }

}
