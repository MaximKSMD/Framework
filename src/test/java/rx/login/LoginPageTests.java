package rx.login;

import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.admin.user.UserDto;
import api.dto.rx.admin.user.UserRole;
import api.preconditionbuilders.UsersPrecondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.login.LoginPage;
import pages.Path;
import pages.dashbord.DashboardPage;
import pages.forgotpassword.ForgotPasswordPage;
import pages.profile.ProfilePage;
import rx.BaseTest;

import java.util.ArrayList;
import java.util.List;

import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static api.preconditionbuilders.UsersPrecondition.user;
import static configurations.User.*;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature("Login")
public class LoginPageTests extends BaseTest {

    private List<UserDto> listUsers = new ArrayList<>();
    private Publisher publisher;
    private ForgotPasswordPage forgotPasswordPage;
    private ProfilePage profilePage;
    private UserDto admin;
    private UserDto singleUser;
    private UserDto crossUser;

    DashboardPage dashboardPage;
    LoginPage loginPage;

    public LoginPageTests(){
        dashboardPage = new DashboardPage();
        forgotPasswordPage = new ForgotPasswordPage();
        profilePage = new ProfilePage();
        loginPage = new LoginPage();
    }

    @BeforeClass
    public void createTestData(){

        publisher = publisher()
                .createNewPublisher(captionWithSuffix("00autoPub1"))
                .build()
                .getPublisherResponse();

        singleUser = UsersPrecondition.user()
                .createSinglePublisherUser(publisher.getId())
                .build()
                .getUserResponse();

        admin = createUser(UserRole.ADMIN);
        crossUser = createUser(UserRole.CROSS_PUBLISHER);
        listUsers.add(singleUser);
        listUsers.add(crossUser);
        listUsers.add(admin);
    }

    @Test(description = "Verify Login Form", priority = 1)
    public void createUserFormValidationTest() {
        testStart()
                .openUrl()
                .then("Validate login page form fields and buttons")
                .validate(Condition.visible, loginPage.getLoginInput())
                .validate(Condition.visible, loginPage.getPasswordInput())
                .validate(Condition.visible, loginPage.getLogInButton())
                .validate(Condition.visible, loginPage.getSignupButton())
                .validate(Condition.visible, loginPage.getForgotPasswordButton())
                .when("Click on Forgot Password link")
                .clickOnWebElement(loginPage.getForgotPasswordButton())
                .then("Validate Forgot Password page form fields and buttons")
                .validateContainsText(forgotPasswordPage.getForgotPasswordPageTitle(), "Forgot Password?")
                .validate(Condition.visible, forgotPasswordPage.getForgotPasswordCancelButton())
                .when("Click Cancel button in ForgotPassword page, return to the login page")
                .clickOnWebElement(forgotPasswordPage.getForgotPasswordCancelButton())
                .clickBrowserRefreshButton()
                .setValueWithClean(loginPage.getLoginInput(),TEST_USER.getMail())
                .setValueWithClean(loginPage.getPasswordInput(),TEST_USER.getPassword())
                .pressEnterKey(loginPage.getPasswordInput())
                .testEnd();
    }

    @Test(description = "Verify Login Page with invalid Email", priority = 2)
    public void loginInvalidUserTest() {
        loginMethod(WRONG_EMAIL_USER.getMail(), WRONG_EMAIL_USER.getPassword());
        testStart()
                .then("Validate Failed button")
                .validate(Condition.visible, loginPage.getLoginResultButton())
                .validateContainsText(loginPage.getLoginResultButton(), "FAILED!")
                .when()
                .clickBrowserRefreshButton()
                .testEnd();
        loginMethod(TEST_USER.getMail(), TEST_USER.getPassword());
    }

    @Test(description = "Verify Login Page with invalid Email", priority = 2)
    public void loginInvalidPasswordTest() {
        loginMethod(WRONG_PASSWORD_USER.getMail(), WRONG_PASSWORD_USER.getPassword());
        testStart()
                .then("Validate Failed button")
                .validate(Condition.visible, loginPage.getLoginResultButton())
                .validateContainsText(loginPage.getLoginResultButton(), "FAILED!")
                .when()
                .clickBrowserRefreshButton()
                .testEnd();
        loginMethod(TEST_USER.getMail(), TEST_USER.getPassword());
    }

    @Test(description = "Verify Login Page with valid Single Publisher User", priority = 3)
    public void loginSinglePubUserTest() {
        loginMethod(singleUser.getMail(), "Password1");
        verifyLoginPage(singleUser, UserRole.SINGLE_PUBLISHER.getDefinition());
    }

    @Test(description = "Verify Login Page with valid Cross Publisher User", priority = 4)
    public void loginCrossPubUserTest() {
        loginMethod(crossUser.getMail(), "Password1");
        verifyLoginPage(crossUser, UserRole.CROSS_PUBLISHER.getDefinition());
    }

    @Test(description = "Verify Login Page with valid Admin Publisher User", alwaysRun = true, priority = 5)
    public void loginAdminPubUserTest() {
        loginMethod(admin.getMail(), "Password1");
        verifyLoginPage(admin, UserRole.ADMIN.getDefinition());
    }


//    @Test(description = "Verify Success button after login", alwaysRun = true, priority = 6)
//    public void verifySuccessButtonTest() {
//        loginMethod(TEST_USER.getMail(), TEST_USER.getPassword());
//        testStart()
//                .validateContainsText(loginPage.getLoginResultButton(), "SUCCESS!")
//                .testEnd();
//    }
    private void verifyLoginPage(UserDto user, String roleDefinition) {
        testStart()
                .then()
                .validate(Condition.visible, dashboardPage.getLogo())
                .validate(user.getMail())
                .when()
                .openDirectPath(Path.PROFILE)
                .then("Verify user Settings card")
                .validate(profilePage.getPageTitle(), user.getName())
                .validate(profilePage.getEmailLabel(), user.getMail())
                .validate(profilePage.getRoleLabel(), roleDefinition)
                .testEnd();
    }

    @AfterMethod(alwaysRun = true)
    private void logout(){
        testStart()
                .and("Logout")
                .logOut()
                .testEnd();
    }

    @Step("Create user")
    private UserDto createUser(UserRole role){

        return UsersPrecondition.user()
                       .createNewUser(role)
                        .build()
                        .getUserResponse();
    }

    @AfterTest(alwaysRun = true)
    private void deleteEntities() {
        if (listUsers != null) {
            for (UserDto users : listUsers) {
                deleteUser(users.getId());
            }
        }
    }

    private void deleteUser(Integer id) {
        user()
                .setCredentials(USER_FOR_DELETION)
                .deleteUser(id)
                .build();
    }

    private void loginMethod(String userName, String password) {
        testStart()
                .openUrl()
                .setValueWithClean(loginPage.getLoginInput(), userName)
                .setValueWithClean(loginPage.getPasswordInput(),password)
                .pressEnterKey(loginPage.getPasswordInput())
                .testEnd();
    }

}
