package pages.forgotpassword;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ForgotPasswordPageElements {

    FORGOT_PASSWORD_PAGE_TITLE("'Forgot Password' Page Title", "//div[@class='v-card__title title']"),
    FORGOT_PASSWORD_INPUT( "'Forgot Password' Input", "//*[@id = 'input-24']"),
    FORGOT_PASSWORD_BUTTON("'Forgot Password' Button", "//button[@type='submit']"),
    FORGOT_PASSWORD_CANCEL_BUTTON("'Forgot Password' Cancel Button", "//div/button[2]");
    private String alias;
    private String selector;
}
