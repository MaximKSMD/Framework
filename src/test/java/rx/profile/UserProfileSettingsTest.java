package rx.profile;

import api.dto.rx.admin.user.UserDto;
import api.dto.rx.admin.user.UserRole;
import api.preconditionbuilders.UsersPrecondition;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Path;
import pages.profile.ProfilePage;
import rx.BaseTest;

import java.util.NoSuchElementException;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature("Profile")
public class UserProfileSettingsTest extends BaseTest {

    private UserDto user;
    private ProfilePage profilePage;

    private static final String EMPTY_STRING = "";
    private static final String BASE_API_URL = "https://qa-s-api.rmp.rakuten.com";

    public UserProfileSettingsTest() {
        profilePage = new ProfilePage();
    }

    @BeforeClass
    public void loginWithUserUnderTesting() {
        login();
        user = getUserData();
    }

    @Step("Login to system")
    private void login() {
        testStart()
                .given("Login to Profile Page")
                .openUrl()
                .logIn(TEST_USER)
                .waitAndValidate(visible, profilePage.getPageTitle())
                .testEnd();
    }

    @Test
    public void checkProfileUserSettingsTest() {

        testStart()
                .given("User is located on profile page")
                .openDirectPath(Path.PROFILE)
                .then("Verify user Settings card")
                .validate(profilePage.getPageTitle(), user.getName())
                .validate(profilePage.getEmailLabel(), user.getMail())
                .validate(profilePage.getRoleLabel(), UserRole.ADMIN.getDefinition())
                .then("Verify API settings card")
                .validateAttribute(profilePage.getApiBaseUrlInput(), "value", BASE_API_URL)
                .then("Verify Update Profile fields are empty")
                .validate(profilePage.getCurrentPasswordInput(), EMPTY_STRING)
                .validate(profilePage.getNewPasswordInput(), EMPTY_STRING)
                .then("Verify page buttons and Page Logo")
                .validate(enabled, profilePage.getLogoutButton())
                .validate(enabled, profilePage.getLogo())
                .testEnd();
    }

    @Step("Login to system")
    private UserDto getUserData() {

        return UsersPrecondition.user()
                .getAllUsers()
                .build()
                .getUserGetAllResponse().getItems().stream()
                .filter(userDto -> userDto.getMail().equals(TEST_USER.getMail()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("A user with mail '%s' wasn't found", TEST_USER.getMail())));
    }

    @AfterClass
    @Step("Delete Demand Source")
    private void logout() {
        testStart()
                .logOut()
                .testEnd();
    }
}
