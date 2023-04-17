package rx.login;

import api.dto.rx.version.VersionDto;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.dashbord.DashboardPage;
import rx.BaseTest;

import static api.preconditionbuilders.VersionPrecondition.version;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
public class EnvironmentVersionTest extends BaseTest {
    private DashboardPage dashboardPage;
    private VersionDto version;
    public EnvironmentVersionTest(){
        dashboardPage = new DashboardPage();
    }

    @BeforeClass
    public void login() {
        version = getApiVersion();

        testStart()
                .openUrl()
                .logIn(TEST_USER)
                .then()
                .validate(Condition.visible, dashboardPage.getLogo())
                .validate(TEST_USER.getMail())
        .testEnd();

    }

    @Test(description = "Verify API Hash tag", priority = 1)
    public void verifyApiHashTagTest() {

        testStart()
                .doubleClickOnWebElement(dashboardPage.getVersionText())
                .validateContainsText(dashboardPage.getVersionText(), version.getGitHash().substring(0,7))
                .testEnd();
    }

    @Test(description = "Verify warning circle icon is not present", priority = 2)
    public void verifyUIAndAPIVersionsIconTest() {

        testStart()
                .validate(Condition.hidden, dashboardPage.getCircleIcon())
                .testEnd();
    }

    @Test(description = "Verify UI and API version tags are in sync", priority = 3)
    public void verifyUIAndAPIVersionsTextTest() {
        var apiVersion = dashboardPage.getVersionText().getText().split("\\n")[1].split("//")[0].substring(0,4);

        testStart()
                .validate(apiVersion, version.getVersion().substring(2,7))
                .testEnd();
    }

    @Test(description = "Verify API version in UI", priority = 4)
    public void verifyAPIVersionsTextTest() {

        testStart()
                .validateContainsText(dashboardPage.getVersionText(), version.getVersion())
                .testEnd();
    }

    private VersionDto getApiVersion() {

        return version()
                .getAPIVersion()
                .build()
                .getVersionResponse();
    }

    @AfterClass
    @Step("LogOut")
    public void logOut() {
        testStart()
                .logOut()
       .testEnd();
    }
}
