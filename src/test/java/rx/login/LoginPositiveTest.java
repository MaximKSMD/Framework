package rx.login;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.dashbord.DashboardPage;
import rx.BaseTest;

import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature("Login")
public class LoginPositiveTest extends BaseTest {

    DashboardPage dashboardPage;

    public LoginPositiveTest(){
        dashboardPage = new DashboardPage();
    }

    @Test
    @Step("Login Positive Tests")
    public void loginPositiveTest() {
        testStart()
                .openUrl()
                .logIn(TEST_USER)
                .then()
                .validate(Condition.visible, dashboardPage.getLogo())
                .validate(TEST_USER.getMail())
        .testEnd();
    }

    @AfterClass
    @Step("LogOut")
    public void logOut() {
        testStart()
                .logOut()
       .testEnd();
    }
}
