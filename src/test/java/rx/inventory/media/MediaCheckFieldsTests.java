package rx.inventory.media;

import api.dto.rx.admin.publisher.Publisher;
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
import widgets.errormessages.ErrorMessages;
import widgets.inventory.media.sidebar.EditMediaSidebar;
import widgets.inventory.media.sidebar.PlatformType;

import java.util.List;

import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static configurations.User.USER_FOR_DELETION;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
@Link("https://rakutenadvertising.atlassian.net/browse/GS-3017")
@Feature("Media")
public class MediaCheckFieldsTests extends BaseTest {

    private MediaPage mediaPage;
    private Publisher publisher;
    private EditMediaSidebar editMediaSidebar;
    
    private static String URL = "https://play.google.com/store/apps/";
    private static String BUNDLE = "com.viber.voip";

    public MediaCheckFieldsTests() {
        mediaPage = new MediaPage();
        editMediaSidebar = new EditMediaSidebar();
    }

    @BeforeClass
    public void init() {

        publisher = publisher()
                .createNewPublisher(captionWithSuffix("00000002autoPub1"))
                .build()
                .getPublisherResponse();
    }

    @BeforeMethod
    public void login() {

        testStart()
                .given()
                .openDirectPath(Path.MEDIA)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, mediaPage.getNuxtProgress())
                .clickOnWebElement(mediaPage.getCreateMediaButton())
                .waitSideBarOpened()
                .testEnd();
    }

    @Test(description = "Check fields by default")
    public void checkDefaultFields() {
        testStart()
                .then("Validate fields by default")
                .validate(disabled, editMediaSidebar.getActiveToggle())
                .validateAttribute(editMediaSidebar.getActiveToggle(), "aria-checked", "true")
                .validate(visible, editMediaSidebar.getPublisherInput())
                .validate(editMediaSidebar.getPublisherInput(), "")
                .validate(disabled, editMediaSidebar.getPlatformInput())
                .validate(editMediaSidebar.getPlatformInput(), "")
                .validate(disabled, editMediaSidebar.getNameInput())
                .validate(editMediaSidebar.getNameInput(), "")
                .validate(disabled, editMediaSidebar.getSiteURL())
                .validate(editMediaSidebar.getSiteURL(), "")
                .validate(disabled, editMediaSidebar.getCategoriesInput())
                .testEnd();
    }

    @Test(description = "Check required fields")
    public void checkRequiredFields() {
        var errorsList = editMediaSidebar.getErrorAlert().getErrorsList();

        testStart()
                .then("Validate required fields")
                .and("Click 'Save' with all empty fields")
                .clickOnWebElement(editMediaSidebar.getSaveButton())
                .then("Validate errors for all required fields in Error Panel")
                .waitAndValidate(visible, editMediaSidebar.getErrorAlert().getErrorPanel())
                .validateListSize(errorsList, 4)
                .validateList(errorsList, List.of(
                        ErrorMessages.PUBLISHER_NAME_ERROR_ALERT.getText(),
                        ErrorMessages.NAME_ERROR_ALERT.getText(),
                        ErrorMessages.PLATFORM_ERROR_ALERT.getText(),
                        ErrorMessages.SITE_URL_REQUIRED_ERROR_ALERT.getText())
                )
                .then("Validate error under the 'Publisher' field")
                .waitAndValidate(visible, editMediaSidebar.getErrorAlertByFieldName("Publisher Name"))
                .validate(editMediaSidebar.getErrorAlertByFieldName("Publisher Name"), ErrorMessages.PUBLISHER_NAME_ERROR_ALERT.getText())
                .and(String.format("Select Publisher '%s'", publisher.getName()))
                .selectFromDropdown(editMediaSidebar.getPublisherInput(),
                        editMediaSidebar.getPublisherDropdownItems(), publisher.getName())
                .then("Validate error under the 'Publisher field' disappeared")
                .waitAndValidate(not(visible), editMediaSidebar.getErrorAlertByFieldName("Publisher Name"))
                .and("Click 'Save'")
                .clickOnWebElement(editMediaSidebar.getSaveButton())
                .then("Validate errors for 3 required fields in Error Panel (Media Name, Platform, Site URL)")
                .validateListSize(errorsList, 3)
                .validateList(errorsList, List.of(
                        ErrorMessages.NAME_ERROR_ALERT.getText(),
                        ErrorMessages.PLATFORM_ERROR_ALERT.getText(),
                        ErrorMessages.SITE_URL_REQUIRED_ERROR_ALERT.getText())
                )
                .then("Validate error under the 'Media Name' field")
                .waitAndValidate(visible, editMediaSidebar.getErrorAlertByFieldName("Name"))
                .validate(editMediaSidebar.getErrorAlertByFieldName("Name"), ErrorMessages.NAME_ERROR_ALERT.getText())
                .then("Validate error under the 'Platform Type' field")
                .waitAndValidate(visible, editMediaSidebar.getErrorAlertByFieldName("Platform"))
                .validate(editMediaSidebar.getErrorAlertByFieldName("Platform"), ErrorMessages.PLATFORM_ERROR_ALERT.getText())
                .then("Validate error under the 'Site URL' field")
                .waitAndValidate(visible, editMediaSidebar.getErrorAlertByFieldName("Site URL"))
                .validate(editMediaSidebar.getErrorAlertByFieldName("Site URL"), ErrorMessages.SITE_URL_REQUIRED_ERROR_ALERT.getText())
                .and(String.format("Fill Name with value %s", "mediaName"))
                .setValueWithClean(editMediaSidebar.getNameInput(), "mediaName")
                .then("Validate error under the 'Media Name' disappeared")
                .waitAndValidate(not(visible), editMediaSidebar.getErrorAlertByFieldName("Name"))
                .then("Validate errors for 3 required fields in Error Panel (Platform, Site URL)")
                .validateListSize(errorsList, 2)
                .validateList(errorsList, List.of(
                        ErrorMessages.PLATFORM_ERROR_ALERT.getText(),
                        ErrorMessages.SITE_URL_REQUIRED_ERROR_ALERT.getText())
                )
                .and("Select Platform 'Android'")
                .selectFromDropdown(editMediaSidebar.getPlatformDropdown(),
                        editMediaSidebar.getPlatformDropdownItems(), PlatformType.ANDROID.getName())
                .then("Validate error under the 'Platform' disappeared")
                .waitAndValidate(not(visible), editMediaSidebar.getErrorAlertByFieldName("Platform"))
                .then("Validate errors for 1 required field in Error Panel (App Store URL)")
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(
                        ErrorMessages.APP_STORE_URL_REQUIRED_ERROR_ALERT.getText())
                )
                .and("Select Platform 'Mobile Web'")
                .selectFromDropdown(editMediaSidebar.getPlatformDropdown(),
                        editMediaSidebar.getPlatformDropdownItems(), PlatformType.MOBILE_WEB.getName())
                .then("Validate errors for 1 required field in Error Panel (Site URL)")
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(
                        ErrorMessages.SITE_URL_REQUIRED_ERROR_ALERT.getText())
                )
                .and(String.format("Fill 'Site URL' field with wrong value '%s'", "wrong url"))
                .setValueWithClean(editMediaSidebar.getSiteURL(), "wrong url")
                .then("Validate errors in Error Panel that 'Site URL' value is wrong")
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(
                        ErrorMessages.SITE_URL_ERROR_ALERT.getText())
                )
                .then("Validate error under the field that 'Site URL' value is wrong")
                .waitAndValidate(visible, editMediaSidebar.getErrorAlertByFieldName("Site URL"))
                .validate(editMediaSidebar.getErrorAlertByFieldName("Site URL"), ErrorMessages.SITE_URL_ERROR_ALERT.getText())
                .and(String.format("Fill 'Site URL' field with valid value '%s'", URL))
                .setValueWithClean(editMediaSidebar.getSiteURL(), URL)
                .then("Validate errors disappeared")
                .waitAndValidate(not(visible), editMediaSidebar.getErrorAlertByFieldName("Site URL"))
                .validate(not(visible), editMediaSidebar.getErrorAlert().getErrorPanel())

                .testEnd();
    }

    @Test(description = "Check errors then switch Platform")
    private void switchPlatformTypeAndCheckError() {
        var errorsList = editMediaSidebar.getErrorAlert().getErrorsList();
        var appStoreURLErrorMsg = ErrorMessages.APP_STORE_URL_REQUIRED_ERROR_ALERT.getText();
        var siteURLErrorMsg = ErrorMessages.SITE_URL_REQUIRED_ERROR_ALERT.getText();

        testStart()
                .and(String.format("Select Publisher '%s'", publisher.getName()))
                .selectFromDropdown(editMediaSidebar.getPublisherInput(),
                        editMediaSidebar.getPublisherDropdownItems(), publisher.getName())
                .and(String.format("Fill Name with value '%s'", "mediaName"))
                .setValueWithClean(editMediaSidebar.getNameInput(), "mediaName")
                .testEnd();

        changePlatformTypeAndValidateErrors(PlatformType.ANDROID.getName(), appStoreURLErrorMsg, "App Store URL");
        changePlatformTypeAndValidateErrors(PlatformType.ANDROID_WEB_VIEW.getName(), appStoreURLErrorMsg, "App Store URL");
        changePlatformTypeAndValidateErrors(PlatformType.PC_WEB.getName(), siteURLErrorMsg, "Site URL");
        changePlatformTypeAndValidateErrors(PlatformType.IOS.getName(), appStoreURLErrorMsg, "App Store URL");
        changePlatformTypeAndValidateErrors(PlatformType.MOBILE_WEB.getName(), siteURLErrorMsg, "Site URL");
        changePlatformTypeAndValidateErrors(PlatformType.IOS_WEB_VIEW.getName(), appStoreURLErrorMsg, "App Store URL");
    }

    @Step("Change Platform {0}")
    private void changePlatformTypeAndValidateErrors(String platformType, String errorMsg, String fieldName) {
        var errorsList = editMediaSidebar.getErrorAlert().getErrorsList();

        testStart()
                .and(String.format("Select Platform '%s'", platformType))
                .selectFromDropdown(editMediaSidebar.getPlatformDropdown(),
                        editMediaSidebar.getPlatformDropdownItems(), platformType)
                .and("Click 'Save'")
                .clickOnWebElement(editMediaSidebar.getSaveButton())
                .then(String.format("Validate Error Message appeared in Error panel :'%s'", errorMsg))
                .validateList(errorsList, List.of(errorMsg))
                .then(String.format("Validate Error Message is '%s' appeared under the field '%s'",
                        errorMsg, errorMsg))
                .waitAndValidate(visible, editMediaSidebar.getErrorAlertByFieldName(fieldName))
                .validate(editMediaSidebar.getErrorAlertByFieldName(fieldName), errorMsg)
                .testEnd();
    }

    @AfterMethod
    private void logout() {
        testStart()
                .and("Close Media Sidebar")
                .clickOnWebElement(editMediaSidebar.getCloseIcon())
                .waitSideBarClosed()
                .and("Logout")
                .logOut()
                .testEnd();
    }

    @AfterClass
    private void deletePublisher() {
        if (publisher()
                .setCredentials(USER_FOR_DELETION)
                .deletePublisher(publisher.getId())
                .build()
                .getResponseCode() == HttpStatus.SC_NO_CONTENT)
            log.info(String.format("Deleted publisher %s", publisher.getId()));
    }

}
