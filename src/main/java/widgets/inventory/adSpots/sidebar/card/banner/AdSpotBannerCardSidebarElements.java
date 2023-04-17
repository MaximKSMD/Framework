package widgets.inventory.adSpots.sidebar.card.banner;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdSpotBannerCardSidebarElements {

    BANNER_CARD("'Banner' Card", "//h3[text()='Banner']/.."),
    BANNER_PANEL("'Banner' Panel with Elements", "//h3[text()='Banner']/../.."),
    ENABLED_TOGGLE("'Banner' Active Toggle", "//h3[text()='Banner']/..//input[@role='switch']"),
    TOOLTIP_BANNER_AD_SIZES_ICON("Banner Card  Ad Sizes Tooltip '%s' Icon",
            "//h3[text()='Banner']/../..//*[text()='Ad Sizes']/../div/*[contains(@class,'v-tooltip')]/../i"),
    ERROR_ALERT_BY_FIELD_NAME("Error Alert under Field '%s'",
            "//h3[text()='Banner']/../..//label[text()='%s']/../../../div[2]/div/div/div"),
    TOOLTIP_BANNER_FLOOR_PRICE_ICON("Banner Card  Floor Price Tooltip '%s' Icon",
            "//h3[text()='Banner']/../..//*[text()='Floor Price']/../../div/*[contains(@class,'v-tooltip')]/../i"),
    BANNER_AD_SIZES("'Banner' Ad Sizes Button", "//h3[text()='Banner']/../..//label[text()='Ad Sizes']/../div[1]"),
    BANNER_AD_SIZES_INPUT("'Banner' Ad Sizes Input", "//h3[text()='Banner']/../..//label[text()='Ad Sizes']/../div/input");

    private String alias;
    private String selector;
}
