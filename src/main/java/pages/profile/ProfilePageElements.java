package pages.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProfilePageElements {

    PROFILE_PAGE_TITLE( "'Profile' Page Title", "//h1"),
    SUBMIT_BUTTON( "'Submit' Button", "//span[text()='submit']/.."),
    LOGOUT_BUTTON("'Logout' button", "//span[contains(text(),'Logout')]/.."),
    NEW_PASSWORD_INPUT("'New password' Input", "//label[text()='New password']/../input"),
    API_BASE_URL_INPUT("'API Base URL' Input", "//label[text()='API Base URL']/../input"),
    ROLE_LABEL("'Role' label" , "//h1/..//descendant::div[contains(@class,'_title')][2]"),
    EMAIL_LABEL("'Email' label" , "//h1/..//descendant::div[contains(@class,'_title')][1]"),
    CURRENT_PASSWORD_INPUT("'Current password' Input", "//label[text()='Current password']/../input"),
    UPDATE_PASSWORD_BUTTON( "'Update Password' Button", "//span[contains(text(),'Update password')]/.."),
    CONFIRM_NEW_PASSWORD_INPUT("'Confirm New password' Input", "//label[text()='Confirm new password']/../input");

    private String alias;
    private String selector;
}
