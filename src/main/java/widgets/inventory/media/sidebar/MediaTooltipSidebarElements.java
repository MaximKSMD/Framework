package widgets.inventory.media.sidebar;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MediaTooltipSidebarElements {

    TOOLTIP_PLACEHOLDER("Tooltip Placeholder","//span"),
    TOOLTIP_CATEGORIES_ICON("Tooltip Categories Icon",
            "//*[text()='Categories']/../div/*[contains(@class,'tooltip')]/../i"),
    TOOLTIP_BUNDLE("Bundle Tooltip Icon",
            "//label[text()='Bundle']/../../div/span[contains(@class,'v-tooltip')]/../i"),
    TOOLTIP_SITE_URL("Site URL Tooltip Icon",
            "//label[text()='Site URL']/../../div/span[contains(@class,'v-tooltip')]/../i"),

    TOOLTIP_APP_STORE_URL("App Store URL Tooltip Icon",
            "//label[text()='App Store URL']/../../div/span[contains(@class,'v-tooltip')]/../i");

    private String alias;
    private String selector;
}
