package widgets.inventory.media.sidebar;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MediaSidebarElements {

    BUNDLE("'Bundle' Input", "//label[text()='Bundle']/../input"),
    MEDIA_NAME("'Media Name' Input", "//label[text()='Name']/../input"),
    SITE_URL( "'Site URL' Input", "//label[text()='Site URL']/../input"),
    CATEGORIES("'Categories' Field", "//label[text()='Categories']/../div"),
    ERROR_ALERT_BY_FIELD_NAME("Error Alert under Field '%s'",
            "//label[text()='%s']/../../..//child::div[@class='v-messages__message']"),
    PLATFORM_INPUT("'Platform' Input", "//label[text()='Platform']/../div/input"),
    PLATFORM_DROPDOWN("'Platform' Dropdown", "//label[text()='Platform']/../div"),
    CATEGORIES_INPUT("'Categories' Input", "//label[text()='Categories']/../div[1]/input"),
    TOOLTIP_ICON_BY_FIELD_NAME("Tooltip '%s' Icon",
            "//label[text()='%s']/../div/span[@class='v-tooltip v-tooltip--bottom']/../i"),
    PUBLISHER_NAME( "'Publisher Name'", "//label[contains(text(),'Publisher')]/../div"),
    APP_STORE_URL( "'App Store URL' Input", "//label[text()='App Store URL']/../input"),
    SAVE_BUTTON("'Save Media' Button",  "//button/span[contains(text(),'Save Media')]"),
    PUBLISHER_DROPDOWN_ITEMS( "'Publisher's Items' Input",
            "//div[contains(@class,'menuable__content__activ')]//div[@class='v-list-item__title']"),
    PLATFORM_DROPDOWN_ITEMS("'Platform' Dropdown Items'",
            "//div[contains(@class,'menuable__content__activ')]//div[contains(@class,'v-list-item')]"),
    ACTIVE_TOGGLE("'Active' Toggle",  "//label[text()='Active']/..//input[@role='switch']/../input"),
    PUBLISHER_NAME_INPUT( "'Publisher Name' Input", "//label[contains(text(),'Publisher')]/../div/input"),
    CLOSE_ICON("'Close Media' Icon", "//header/div/button/span/i");

    private String alias;
    private String selector;
}
