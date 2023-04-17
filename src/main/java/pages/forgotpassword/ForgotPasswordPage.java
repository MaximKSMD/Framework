package pages.forgotpassword;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import pages.BasePage;

import static com.codeborne.selenide.Selenide.$x;
import static pages.forgotpassword.ForgotPasswordPageElements.*;

/**
 * Keep Selectors of UI elements in {@link ForgotPasswordPageElements}
 */
@Getter
public class ForgotPasswordPage extends BasePage {

    private SelenideElement forgotPasswordPageTitle = $x(FORGOT_PASSWORD_PAGE_TITLE.getSelector()).as(FORGOT_PASSWORD_PAGE_TITLE.getAlias());
    private SelenideElement forgotPasswordInput = $x(FORGOT_PASSWORD_INPUT.getSelector()).as(FORGOT_PASSWORD_INPUT.getAlias());
    private SelenideElement forgotPasswordSubmitButton = $x(FORGOT_PASSWORD_BUTTON.getSelector()).as(FORGOT_PASSWORD_BUTTON.getAlias());
    private SelenideElement forgotPasswordCancelButton = $x(FORGOT_PASSWORD_CANCEL_BUTTON.getSelector()).as(FORGOT_PASSWORD_CANCEL_BUTTON.getAlias());
}
