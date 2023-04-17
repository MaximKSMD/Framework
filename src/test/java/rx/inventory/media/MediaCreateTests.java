package rx.inventory.media;

import api.dto.rx.admin.publisher.Publisher;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.inventory.media.*;
import rx.BaseTest;
import widgets.common.categories.CategoriesList;
import widgets.common.table.ColumnNames;
import widgets.common.table.Statuses;
import widgets.inventory.media.sidebar.CreateMediaSidebar;
import widgets.inventory.media.sidebar.PlatformType;

import java.util.List;

import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature("Media")
public class MediaCreateTests extends BaseTest {
    
    private MediaPage mediaPage;
    private Publisher publisher;
    private CreateMediaSidebar createMediaSidebar;

    public MediaCreateTests() {
        mediaPage = new MediaPage();
        createMediaSidebar = new CreateMediaSidebar();
    }

    @BeforeClass
    private void initAndLogin() {
        publisher = publisher()
                .createNewPublisher(captionWithSuffix("0002autoPub"))
                .build()
                .getPublisherResponse();

        testStart()
                .given()
                .openDirectPath(Path.MEDIA)
                .clickBrowserRefreshButton()
                .logIn(TEST_USER)
                .waitAndValidate(disappear, mediaPage.getNuxtProgress())
                .clickBrowserRefreshButton()
                .testEnd();
    }

    @Test(description = "Create Media with 'IOS' platform type")
    public void createMediaIOSPlatformType() {
        var mediaName = captionWithSuffix("autoMediaIOS");
        var appStoreURL = "https://play.google.com/store/apps/";
        var bundle = "com.viber.voip";
        createAndCheckCreatedMedia(mediaName, appStoreURL, bundle, PlatformType.IOS.getName());
    }

    @Test(description = "Create Media with 'IOS Web View' platform type")
    private void createMediaIOSWebViewPlatformType() {
        var mediaName = captionWithSuffix("autoMediaIOSWebView");
        var appStoreURL = "https://play.google.com/store/apps/";
        var bundle = "com.viber.voip";
        createAndCheckCreatedMedia(mediaName, appStoreURL, bundle, PlatformType.IOS_WEB_VIEW.getName());
    }

    @Test(description = "Create Media with 'Android' platform type")
    private void createMediaAndroidPlatformType() {
        var mediaName = captionWithSuffix("autoMediaAndroid");
        var appStoreURL = "https://play.google.com/store/apps/";
        var bundle = "com.viber.voip";
        createAndCheckCreatedMedia(mediaName, appStoreURL, bundle, PlatformType.ANDROID.getName());
    }

    @Test(description = "Create Media with 'Android Web View' platform type")
    private void createMediaAndroidWebViewPlatformType() {
        var mediaName = captionWithSuffix("autoMediaAndroidWebView");
        var appStoreURL = "https://play.google.com/store/apps/";
        var bundle = "com.viber.voip";
        createAndCheckCreatedMedia(mediaName, appStoreURL, bundle, PlatformType.ANDROID_WEB_VIEW.getName());
    }

    @Test(description = "Create Media with 'PC Web' platform type")
    private void createMediaPCWebViewPlatformType() {
        var mediaName = captionWithSuffix("autoMediaPCWeb");
        var appStoreURL = "https://checkmedia.com";
        createAndCheckCreatedMedia(mediaName, appStoreURL, "", PlatformType.PC_WEB.getName());
    }

    @Test(description = "Create Media with 'Mobile Web' platform type")
    private void createMediaMobileWebViewPlatformType() {
        var mediaName = captionWithSuffix("autoMediaMobileWeb");
        var appStoreURL = "https://checkmedia.com";
        createAndCheckCreatedMedia(mediaName, appStoreURL, "", PlatformType.MOBILE_WEB.getName());
    }

    @Test(description = "Create Media with CTV' platform type")
    private void createMediaCTVPlatformType() {
        var mediaName = captionWithSuffix("autoMediaCTV");
        var appStoreURL = "https://checkmedia.com";
        var bundle = "com.viber.voip";
        createAndCheckCreatedMedia(mediaName, appStoreURL, bundle, PlatformType.CTV.getName());
    }


    @Step("Create Media {0} with platform type {1}")
    private void createAndCheckCreatedMedia(String mediaName, String url, String bundle, String platformType) {
        var tableData = mediaPage.getMediaTable().getTableData();
        var tableOptions = mediaPage.getMediaTable().getShowHideColumns();
        var tablePagination = mediaPage.getMediaTable().getTablePagination();
        var categories = createMediaSidebar.getCategoriesPanel();

        testStart()
                .clickOnWebElement(mediaPage.getCreateMediaButton())
                .waitSideBarOpened()
                .selectFromDropdown(createMediaSidebar.getPublisherInput(),
                        createMediaSidebar.getPublisherDropdownItems(), publisher.getName())
                .and("Fill Name")
                .setValueWithClean(createMediaSidebar.getNameInput(), mediaName)
                .selectFromDropdown(createMediaSidebar.getPlatformDropdown(),
                        createMediaSidebar.getPlatformDropdownItems(), platformType)
                .clickOnWebElement(createMediaSidebar.getCategories())
                .clickOnWebElement(categories.getCategoryCheckbox(CategoriesList.ART_ENTERTAIMENTS))
                .clickOnWebElement(categories.getCategoryCheckbox(CategoriesList.AUTOMOTIVE))
                .testEnd();

        if (!platformType.equals("PC Web") && (!platformType.equals("Mobile Web"))) {
            testStart()
                    .setValueWithClean(createMediaSidebar.getAppStoreURL(), url)
                    .setValueWithClean(createMediaSidebar.getBundleInput(), bundle)
                    .testEnd();
        } else {
            testStart()
                    .setValueWithClean(createMediaSidebar.getSiteURL(), url)
                    .testEnd();
        }

        testStart()
                .clickOnWebElement(createMediaSidebar.getSaveButton())
                .and("Error message is absent")
                .waitAndValidate(not(visible), createMediaSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), mediaPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .and("Toaster Error message is absent")
                .waitAndValidate(not(visible), mediaPage.getToasterMessage().getPanelError())
                .and("Search new media")
                .setValueWithClean(tableData.getSearch(), mediaName)
                .pressEnterKey(tableData.getSearch())
                .then("Validate that text in table footer '1-1 of 1")
                .validateContainsText(tablePagination.getPaginationPanel(), "1-1 of 1")
                .and("Open Sidebar and check data")
                .clickOnTableCellLink(tableData, ColumnNames.MEDIA_NAME, mediaName)
                .waitSideBarOpened()
                .then("Check all fields")
                .validate(createMediaSidebar.getPublisherInput(), publisher.getName())
                .validateAttribute(createMediaSidebar.getNameInput(), "value", mediaName)
                .validate(createMediaSidebar.getPlatformDropdown(), platformType)
                .validateList(categories.getCategoriesSelectedItems(),
                        List.of(CategoriesList.ART_ENTERTAIMENTS.getName(), CategoriesList.AUTOMOTIVE.getName()))
                .testEnd();

        if (!platformType.equals("PC Web") && (!platformType.equals("Mobile Web"))) {
            testStart()
                    .validateAttribute(createMediaSidebar.getAppStoreURL(), "value", url)
                    .validateAttribute(createMediaSidebar.getBundleInput(), "value", bundle)
                    .testEnd();
        } else {
            testStart()
                    .validateAttribute(createMediaSidebar.getSiteURL(), "value", url)
                    .testEnd();
        }
        testStart()

                .and("Click Save")
                .clickOnWebElement(createMediaSidebar.getSaveButton())
                .waitAndValidate(not(exist), createMediaSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(exist), mediaPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .and("Toaster Error message is absent")
                .waitAndValidate(not(exist), mediaPage.getToasterMessage().getPanelError())
                .and("Show column 'Site/App Store URL'")
                .clickOnWebElement(tableOptions.getShowHideColumnsBtn())
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.SITE_APP_STORE_URL))
                .then("Validate data in table")
                //:TODO need to add all columns
                .validate(tableData.getCellByRowValue(ColumnNames.STATUS, ColumnNames.MEDIA_NAME, mediaName), Statuses.ACTIVE.getStatus())
                .validate(tableData.getCellByRowValue(ColumnNames.PUBLISHER, ColumnNames.MEDIA_NAME, mediaName), publisher.getName())
                .validate(tableData.getCellByRowValue(ColumnNames.PLATFORM, ColumnNames.MEDIA_NAME, mediaName), platformType)
                .validate(tableData.getCellByRowValue(ColumnNames.SITE_APP_STORE_URL, ColumnNames.MEDIA_NAME, mediaName), url)
                .clickOnWebElement(tableData.getClear())
                .then("Validate that text in table footer '1-20 of X")
                .validateContainsText(tablePagination.getPaginationPanel(), "1-20 of ")
                .testEnd();
    }


    @AfterClass
    private void logout() {
        testStart()
                .logOut()
                .testEnd();

    }
}
