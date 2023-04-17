package widgets.admin.publisher.sidebar;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PublisherSidebarElements {

    NAME("'Publisher Name' Input", "//label[text()='Name']/../input"),
    CLOSE_ICON("'Close Publisher' Icon", "//header/div/button/span/i"),
    CATEGORIES_LABEL("'Categories' Label", "//label[text()='Categories']"),
    DOMAIN("'Publisher Domain' Input", "//label[text()='Domain']/../input"),
    CATEGORIES("'Categories' Field", "//label[text()='Categories']/../div"),
    MAIL("'Publisher Mail' Input", "//label[text()='Ad Ops Email']/../input"),
    CURRENCY("'Publisher Currency' Input", "//label[text()='Currency']/../input"),
    CURRENCY_DROPDOWN("'Currency' Dropdown", "//label[text()='Currency']/../div"),
    CATEGORIES_INPUT("'Categories' Input", "//label[text()='Categories']/../div[1]"),
    AD_OPS_PERSON("'Ad Ops Person' Input", "//label[text()='Ad Ops Person']/../input"),
    EXTERNAL_ID_LABEL("'Publisher External ID' label", "//label[text()='External ID']"),
    EXTERNAL_ID("'Publisher External ID' input", "//label[text()='External ID']/../input"),
    SAVE_BUTTON("'Save Publisher' Button", "//button/span[contains(text(),'Save Publisher')]"),
    ACTIVE_TOGGLE("'Active' Toggle", "//label[text()='Active']/..//input[@role='switch']/../input"),
    CURRENCY_DROPDOWN_ITEMS("'Currency' Dropdown Items'", "//div[contains(@class,'menuable__content__activ')]//div[contains(@id,'list-item')]"),
    ERROR_ALERT_BY_FIELD_NAME("Error Alert under Field '%s'", "//label[text()='%s']/../../..//child::div[contains(@class,'v-messages__message')]");

    private String alias;
    private String selector;
}
