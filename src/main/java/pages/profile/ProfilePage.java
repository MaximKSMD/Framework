package pages.profile;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import groovyjarjarantlr4.v4.runtime.atn.PredicateEvalInfo;
import lombok.Getter;
import pages.BasePage;

import static com.codeborne.selenide.Selenide.$x;
import static pages.profile.ProfilePageElements.*;

/**
 * Keep Selectors of UI elements in {@link ProfilePageElements}
 */
@Getter
public class ProfilePage extends BasePage {

    private SelenideElement roleLabel = $x(ROLE_LABEL.getSelector()).as(ROLE_LABEL.getAlias());
    private SelenideElement emailLabel =$x(EMAIL_LABEL.getSelector()).as(EMAIL_LABEL.getAlias());
    private SelenideElement submitButton = $x(SUBMIT_BUTTON.getSelector()).as(SUBMIT_BUTTON.getAlias());
    private SelenideElement logoutButton = $x(LOGOUT_BUTTON.getSelector()).as(LOGOUT_BUTTON.getAlias());
    private SelenideElement pageTitle = $x(PROFILE_PAGE_TITLE.getSelector()).as(PROFILE_PAGE_TITLE.getAlias());
    private SelenideElement apiBaseUrlInput = $x(API_BASE_URL_INPUT.getSelector()).as(API_BASE_URL_INPUT.getAlias());
    private SelenideElement newPasswordInput = $x(NEW_PASSWORD_INPUT.getSelector()).as(NEW_PASSWORD_INPUT.getAlias());
    private SelenideElement currentPasswordInput  = $x(CURRENT_PASSWORD_INPUT.getSelector()).as(CURRENT_PASSWORD_INPUT.getAlias());
    private SelenideElement updatePasswordButton  = $x(UPDATE_PASSWORD_BUTTON.getSelector()).as(UPDATE_PASSWORD_BUTTON.getAlias());

}
