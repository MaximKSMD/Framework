package pages.login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginPageElements {

    SIGNUP_BUTTON("'Sign Up' Button", "//div/button[2]"),
    LOGIN_BUTTON("'Login' Button", "//button[@type='submit']"),
    PASSWORD_INPUT( "'Password' Input", "//input[@type='password']"),
    LOGIN_INPUT( "'Login' Input", "//label[text()='E-mail']/../input"),
    FORGOT_PASSWORD("'Forgot Password' Link", "//a[@href='/reset-password']/button"),
    LOGIN_RESULT_BUTTON("'SUCCESS/FAILED' Button", "//div[@class='v-card__actions']/button[@type='button']/span");

    private String alias;
    private String selector;
}
