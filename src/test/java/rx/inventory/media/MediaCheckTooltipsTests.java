package rx.inventory.media;

import api.dto.rx.admin.publisher.Publisher;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;
import pages.Path;
import pages.inventory.media.*;
import rx.BaseTest;
import widgets.common.tooltip.MediaTooltipText;
import widgets.inventory.media.sidebar.*;

import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.disappear;
import static configurations.User.TEST_USER;
import static configurations.User.USER_FOR_DELETION;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature("Media")
public class MediaCheckTooltipsTests extends BaseTest {

    private MediaPage mediaPage;
    private EditMediaSidebar editMediaSidebar;
    private MediaTooltipSidebar mediaTooltipSidebar;
    private Publisher publisher;

    public MediaCheckTooltipsTests() {
        mediaPage = new MediaPage();
        editMediaSidebar = new EditMediaSidebar();
        mediaTooltipSidebar = new MediaTooltipSidebar();
    }

    @BeforeClass
    private void init() {
        publisher = publisher()
                .createNewPublisher(captionWithSuffix("0002autoPub"))
                .build()
                .getPublisherResponse();
    }

    @BeforeMethod
    private void login() {
        testStart()
                .given()
                .openDirectPath(Path.MEDIA)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, mediaPage.getNuxtProgress())
                .clickBrowserRefreshButton()
                .openDirectPath(Path.MEDIA)
                .clickOnWebElement(mediaPage.getCreateMediaButton())
                .waitSideBarOpened()
                .selectFromDropdown(editMediaSidebar.getPublisherInput(),
                        editMediaSidebar.getPublisherDropdownItems(), publisher.getName())
                .testEnd();
    }

    @Test(description = "'Categories' Tooltip Text",alwaysRun = true)
    private void categoriesTooltip() {
        verifyTooltip(mediaTooltipSidebar.getTooltipCategories(), PlatformType.ANDROID.getName(),
                MediaTooltipText.CATEGORIES.getText());
        verifyTooltip(mediaTooltipSidebar.getTooltipCategories(), PlatformType.IOS.getName(),
                MediaTooltipText.CATEGORIES.getText());
        verifyTooltip(mediaTooltipSidebar.getTooltipCategories(), PlatformType.MOBILE_WEB.getName(),
                MediaTooltipText.CATEGORIES.getText());
        verifyTooltip(mediaTooltipSidebar.getTooltipCategories(), PlatformType.PC_WEB.getName(),
                MediaTooltipText.CATEGORIES.getText());
        verifyTooltip(mediaTooltipSidebar.getTooltipCategories(), PlatformType.IOS_WEB_VIEW.getName(),
                MediaTooltipText.CATEGORIES.getText());
        verifyTooltip(mediaTooltipSidebar.getTooltipCategories(), PlatformType.ANDROID_WEB_VIEW.getName(),
                MediaTooltipText.CATEGORIES.getText());
        verifyTooltip(mediaTooltipSidebar.getTooltipCategories(), PlatformType.CTV.getName(),
                MediaTooltipText.CATEGORIES.getText());
    }

    @Test(description = "'Site URL' Tooltip Text",alwaysRun = true)
    private void siteURLTooltip() {
        verifyTooltip(mediaTooltipSidebar.getTooltipSiteURL(), PlatformType.MOBILE_WEB.getName(),
                MediaTooltipText.SITE_URL.getText());
        verifyTooltip(mediaTooltipSidebar.getTooltipSiteURL(), PlatformType.PC_WEB.getName(),
                MediaTooltipText.SITE_URL.getText());
    }

    @Test(description = "'Bundle' Tooltip Text",alwaysRun = true)
    private void bundleTooltip(){
        verifyTooltip(mediaTooltipSidebar.getTooltipBundle(), PlatformType.ANDROID.getName(),
                MediaTooltipText.BUNDLE.getText());
        verifyTooltip(mediaTooltipSidebar.getTooltipBundle(), PlatformType.IOS.getName(),
                MediaTooltipText.BUNDLE.getText());
        verifyTooltip(mediaTooltipSidebar.getTooltipBundle(), PlatformType.IOS_WEB_VIEW.getName(),
                MediaTooltipText.BUNDLE.getText());
        verifyTooltip(mediaTooltipSidebar.getTooltipBundle(), PlatformType.ANDROID_WEB_VIEW.getName(),
                MediaTooltipText.BUNDLE.getText());
        verifyTooltip(mediaTooltipSidebar.getTooltipBundle(), PlatformType.CTV.getName(),
                MediaTooltipText.BUNDLE.getText());
    }

    @Test(description = "'App Store URL' Tooltip Text",alwaysRun = true)
    private void appStoreURLTooltip() {
        verifyTooltip(mediaTooltipSidebar.getTooltipAppStoreURL(), PlatformType.ANDROID.getName(),
                MediaTooltipText.APP_STORE_URL.getText());
        verifyTooltip(mediaTooltipSidebar.getTooltipAppStoreURL(), PlatformType.IOS.getName(),
                MediaTooltipText.APP_STORE_URL.getText());
        verifyTooltip(mediaTooltipSidebar.getTooltipAppStoreURL(), PlatformType.IOS_WEB_VIEW.getName(),
                MediaTooltipText.APP_STORE_URL.getText());
        verifyTooltip(mediaTooltipSidebar.getTooltipAppStoreURL(), PlatformType.ANDROID_WEB_VIEW.getName(),
                MediaTooltipText.APP_STORE_URL.getText());
    }

    @Step("Verify Tooltip Text")
    private void verifyTooltip(SelenideElement field, String platformType, String expectedText) {
        testStart()
                .selectFromDropdown(editMediaSidebar.getPlatformDropdown(),
                        editMediaSidebar.getPlatformDropdownItems(), platformType)
                .scrollIntoView(field)
                .validateTooltip(field, expectedText)
                .testEnd();
    }

    @AfterMethod(alwaysRun = true)
    private void logout() {
        testStart()
                .clickOnWebElement(editMediaSidebar.getCloseIcon())
                .waitSideBarClosed()
                .logOut()
                .testEnd();
    }

    @AfterClass(alwaysRun = true)
    private void deletePublisher() {
        if (publisher()
                .setCredentials(USER_FOR_DELETION)
                .deletePublisher(publisher.getId())
                .build()
                .getResponseCode() == HttpStatus.SC_NO_CONTENT)
            log.info(String.format("Deleted publisher %s", publisher.getId()));
    }

}
