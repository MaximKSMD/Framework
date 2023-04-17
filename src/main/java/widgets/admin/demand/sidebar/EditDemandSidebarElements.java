package widgets.admin.demand.sidebar;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EditDemandSidebarElements {

    PMP_SUPPORT_TOGGLE("'PMP Support' Toggle", "//label[text()='PMP Support']/..//input[@role='switch']"),
    SYNC_REQUIRED_TOGGLE("'Sync Required' Toggle", "//label[text()='Sync Required']/..//input[@role='switch']"),
    IDFA_REQUIRED_TOGGLE("'IDFA Required' Toggle", "//label[text()='IDFA Required']/..//input[@role='switch']"),
    TOKEN_GENERATION_TOGGLE("'Token Generation' Toggle", "//label[text()='Token Generation']/..//input[@role='switch']"),
    NON_PROGRAMMATIC_TOGGLE("'Non-Programmatic' Toggle", "//label[text()='Non-Programmatic']/..//input[@role='switch']"),

    ACTIVE_RADIO_BUTTON("'Active' Radio Button", "//label[text()='Active']/..//input"),
    INACTIVE_RADIO_BUTTON("'Inactive' Radio Button", "//label[text()='Inactive']/..//input"),
    ONBOARDING_RADIO_BUTTON("'Active' Radio Button", "//label[text()='Onboarding']/..//input"),
    EDDPOINT_URI_INPUT("'Publisher Domain' Input", "//label[text()='Endpoint URI']/../input"),
    SYNC_URL("'Sync Url' Input", "//label[text()='Sync Url']/../input"),
    FORMAT_LABEL("'Format' Label", "//header[text()='Format']"),
    CLOSE_ICON("'Close  Sidebar' Icon", "//header/div/button/span/i"),
    BIDDER( "'Bidder' Input", "//label[text()='Name']/../input"),
    CHECKBOX("'Format' Checkbox", "//label[normalize-space()='%s']/../../div//i"),
    CHECKBOX_LABEL("'Format' Checkbox", "//label[normalize-space()='%s']/../../div//input"),
    DROPDOWN_ITEMS("Dropdown Items", "//div[contains(@class,'menuable__content__activ')]//div[@class='v-list-item__title']"),
    CURRENCY_DROPDOWN("'Currency' Dropdown", "//label[text()='Currency']/../input"),
    DATA_CENTER_DROPDOWN( "'Data Center' Dropdown", "//label[text()='Data Center']/../input"),
    SAVE_BUTTON("'Save' Button",  "//button/span[contains(text(),'Save')]");

    private String alias;
    private String selector;
}
