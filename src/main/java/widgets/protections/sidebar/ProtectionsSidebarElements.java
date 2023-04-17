package widgets.protections.sidebar;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProtectionsSidebarElements {

    NAME("'Name' Input", "//label[text()='Name']/../input"),
    TITLE("'Title' Sidebar", "//div[@class='v-toolbar__title']/div"),
    NOTES("'Notes' Text Area", "//label[text()='Notes']/../textarea"),
    PUBLISHER_NAME("'Publisher Name' Input", "//label[text()='Publisher Name']/../div"),
    PUBLISHER_ITEMS("'Publisher's Items' Input",
            "//div[contains(@class,'menuable__content__activ')]//div[@class='v-list-item__title']"),
    SAVE_BUTTON("'Save Protection' Button",  "//button/span[contains(text(),'Save Protection')]"),
    PUBLISHER_NAME_DROPDOWN( "'Publisher Name' Dropdown", "//label[text()='Publisher Name']/../div[2]"),

    PROTECTION_TYPE_DROPDOWN("'Protection Type' Dropdown", "//label[text()='Protection Type']/../div[2]"),
    PROTECTION_TYPE_DROPDOWN_ITEMS("'Protection Type ' Dropdown Items",
            "//div[contains(@class,'menuable__content__activ')]//div[@class='v-list-item__title']"),
    CLOSE_ICON("'Close Ad Spot' Icon", "//div[@class='v-toolbar__content']/button[contains(@class,'v-btn--round theme--dark')]"),
    MANAGED_BY_SYSTEM_ADMIN_ONLY( "'Managed by system admins only'' Checkbox", "//label[text()='Managed by system admins only']/..//input");

    private String alias;
    private String selector;
}
