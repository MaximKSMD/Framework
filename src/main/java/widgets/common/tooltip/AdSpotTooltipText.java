package widgets.common.tooltip;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdSpotTooltipText {

    CATEGORIES("'Category' Tooltip Text",
            "Category/ies set in the Ad Spot level are indicated in its bid requests as the site.pagecat attribute " +
                    "for ad spots associated with web media types, and as the app.pagecat for ones associated with mobile app media types."),

    CONTENT_FOR_CHILDREN("'Content For Children' Tooltip Text",
            "When ad spot is set as children's content, bid requests sent from it will be indicated with the flag indicating that " +
                    "the request is subject to the COPPA regulations established by the USA FTC."),

    DEFAULT_AD_SIZES("'Default Ad Sizes' Tooltip Text",
            "Override values passed in real-time via prebid.js ad request when applicable. When unchecked the size values passed in " +
                    "real-time will be added to the allowed sizes. This setting will apply to all ad formats enabled on the ad spot, " +
                    "regardless if thier size were overriden."),

    DEFAULT_FLOOR_PRICE("'Default Floor Price' Tooltip Text",
            "Ad spots' default floor price is used unless overridden in its specific formats' floor price sections."),

    BANNER_AD_SIZE("'Banner - Ad Size' Tooltip Text",
            "Values passed in real-time via prebid.js ad request, when applicable, will override the sizes set in " +
                    "the ad spot settings. When unchecked the size values passed in real-time will be added to the allowed sizes. " +
                    "Settings applied on the ad spots' ad format level will override the settings in the default ad spot size and will" +
                    " apply only for this specific ad format."),

    BANNER_FLOOR_PRICE("'Banner - Floor Price' Tooltip Text",
            "Floor prices set in the ad spot's ad format level will override the ad spot's default floor price, only " +
                    "for the specific ad format they're set. Pricing Rules have a higher priority and will override these " +
                    "settings under their conditions."),

    NATIVE_FLOOR_PRICE("'Native - Floor Price' Tooltip Text",
            "Floor prices set in the ad spot's ad format level will override the ad spot's default floor price, only for " +
                    "the specific ad format they're set. Pricing Rules have a higher priority and will override these settings under " +
                    "their conditions."),

    VIDEO_AD_SIZE("'Video - Ad Size' Tooltip Text",
            "Values passed in real-time via prebid.js ad request, when applicable, will override the sizes set in " +
                    "the ad spot settings. When unchecked the size values passed in real-time will be added to the allowed sizes. " +
                    "Settings applied on the ad spots' ad format level will override the settings in the default ad spot size and will" +
                    " apply only for this specific ad format."),

    VIDEO_FLOOR_PRICE("'Video - Floor Price' Tooltip Text",
            "Floor prices set in the ad spot's ad format level will override the ad spot's default floor price, only for " +
                    "the specific ad format they're set. Pricing Rules have a higher priority and will override these settings under " +
                    "their conditions.");


    private String alias;
    private String text;
}
