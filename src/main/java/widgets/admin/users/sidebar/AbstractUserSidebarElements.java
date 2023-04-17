package widgets.admin.users.sidebar;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AbstractUserSidebarElements {

    USER_SIDEBAR("Create User popup", "//*[@id = 'dialog']"),
    EMAIL_INPUT("'EMail' Input", "//label[text()='Email']/../input"),
    PASSWORD_INPUT("'Password' Input", "//label[text()='Password']/../input"),
    USERNAME_INPUT("'Username' Input", "//label[text()='Username']/../input"),
    ERROR_ALERT_BY_FIELD_NAME("Error Alert under Field '%s'",
            "//label[text()='%s']/../../..//child::div[@class='v-messages__message']"),
    TOOLTIP_ICON_BY_FIELD_NAME("Tooltip '%s' Icon",
            "//label[text()='%s']/../div/span[@class='v-tooltip v-tooltip--bottom']/../i"),
    SAVE_BUTTON("'Save User' Button",  "//button/span[contains(text(),'Save User')]"),
    USER_SIDEBAR_TITLE("'Create User' popup title", "//div[@class='v-toolbar__title']"),
    PUBLISHER_NAME_INPUT( "'Publisher Name'", "//label[contains(text(),'Publisher')]/../div"),
    PUBLISHER_DROPDOWN_ITEMS( "'Publisher's Items' Input",
            "//div[contains(@class,'menuable__content__activ')]//div[@class='v-list-item__title']"),
    ACTIVE_TOGGLE("'Active' Toggle",  "//label[text()='Active']/..//input[@role='switch']/../input"),
    ADMIN_RADIOBUTTON( "'Admin' Radio Button", "//label/span[contains(text(),'Admin')]/../..//input"),
    USER_SIDEBAR_CLOSE("'Create User' popup close", "//header/div/button/span[@class='v-btn__content']/i"),
    CROSS_PUBLISHER_RADIOBUTTON("'Cross-publisher' Radio Button", "//label/span[contains(text(),'Cross-Publisher')]/../..//input"),
    SINGLE_PUBLISHER_RADIOBUTTON( "'Single publisher' Radio Button", "//label/span[contains(text(),'Single-Publisher')]/../..//input");

    private String alias;
    private String selector;
}
