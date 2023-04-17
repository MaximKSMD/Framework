package widgets.inventory.adSpots.sidebar.card.video;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdSpotVideoCardSidebarElements {

    VIDEO_CARD("'Video' Card", "//h3[text()='Video']/.."),
    VIDEO_PANEL("'Video' Panel with Elements", "//h3[text()='Video']/../.."),
    VIDEO_PLACEMENT_TYPE_ITEMS("'Video' Placement Type Items",
            "//div[contains(@class,'menuable__content__activ')]//div[@class='v-list-item__title']"),
    VIDEO_PLAYBACK_METHODS_ITEMS("'Video' Playback Methods Items",
            "//div[contains(@class,'menuable__content__activ')]//div[@class='v-list-item__title']"),
    VIDEO_PLAYBACK_METHODS_SELECTED_ITEMS("'Video' Playback Methods Selected Items",
            "//label[text()='Video Playback Methods']/..//div[@class='v-select__selections']/div"),
    ENABLED_TOGGLE("'Video' Active Toggle", "//h3[text()='Video']/..//input[@role='switch']"),
    MIN_VIDEO_DURATION("Minimum Video Duration (seconds)",
            "//h3[text()='Video']/../..//label[text()='Minimum Video Duration (seconds)']/../../div/input[1]"),
    MAX_VIDEO_DURATION("Maximum Video Duration (seconds)",
            "//h3[text()='Video']/../..//label[text()='Maximum Video Duration (seconds)']/../../div/input[1]"),
    TOOLTIP_VIDEO_AD_SIZES_ICON("Video Card Ad Sizes Tooltip '%s' Icon",
            "//h3[text()='Video']/../..//*[text()='Ad Sizes']/../div/*[contains(@class,'v-tooltip')]/../i"),
    VIDEO_AD_SIZES("'Video' Ad Sizes", "//h3[text()='Video']/../..//label[text()='Ad Sizes']/../div[1]"),
    VIDEO_ERROR_ALERT_BY_FIELD_NAME("Error Alert under Field '%s'",
            "//h3[text()='Video']/../..//label[text()='%s']/../../../div[2]/div/div/div"),
    TOOLTIP_VIDEO_FLOOR_PRICE_ICON("Video Card Floor Price Tooltip '%s' Icon",
            "//h3[text()='Video']/../..//*[text()='Floor Price']/../../div/*[contains(@class,'v-tooltip')]/../i"),
    VIDEO_PLACEMENT_TYPE("'Video' Placement Type", "//label[text()='Video Placement Type']/../../div/div[2]"),
    VIDEO_PLAYBACK_METHODS("'Video' Playback Methods", "//label[text()='Video Playback Methods']/../../div/div[2]");

    private String alias;
    private String selector;
}
