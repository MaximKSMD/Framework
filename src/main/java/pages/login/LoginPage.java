package pages.login;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$x;
import static pages.login.LoginPageElements.*;

/**
 * Keep Selectors of UI elements in {@link LoginPageElements}
 */
@Getter
public class LoginPage {

    private SelenideElement loginInput = $x(LOGIN_INPUT.getSelector()).as(LOGIN_INPUT.getAlias());
    private SelenideElement passwordInput = $x(PASSWORD_INPUT.getSelector()).as(PASSWORD_INPUT.getAlias());
    private SelenideElement logInButton = $x(LOGIN_BUTTON.getSelector()).as(LOGIN_BUTTON.getAlias());
    private SelenideElement signupButton = $x(SIGNUP_BUTTON.getSelector()).as(SIGNUP_BUTTON.getAlias());
    private SelenideElement forgotPasswordButton = $x(FORGOT_PASSWORD.getSelector()).as(FORGOT_PASSWORD.getAlias());
    private SelenideElement loginResultButton = $x(LOGIN_RESULT_BUTTON.getSelector()).as(LOGIN_RESULT_BUTTON.getAlias());
}
