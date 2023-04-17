package widgets.inventory.adSpots.sidebar.card.anative;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdSpotNativeCardSidebarElements {

    NATIVE_CARD("'Native' Card", "//h3[text()='Native']/.."),
    NATIVE_PANEL("'Native' Panel with Elements", "//h3[text()='Native']/../.."),
    ENABLED_TOGGLE("'Native' Active Toggle", "//h3[text()='Native']/..//input[@role='switch']"),
    ERROR_ALERT_BY_FIELD_NAME("Error Alert under Field '%s'",
            "//h3[text()='Native']/../..//label[text()='%s']/../../../div[2]/div/div/div"),
    TOOLTIP_NATIVE_FLOOR_PRICE_ICON("Native Card Floor Price Tooltip '%s' Icon",
            "//h3[text()='Native']/../..//*[text()='Floor Price']/../../div/span[contains(@class,'v-tooltip')]/../i");

    private String alias;
    private String selector;
}
