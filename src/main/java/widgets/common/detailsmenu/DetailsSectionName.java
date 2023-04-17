package widgets.common.detailsmenu;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DetailsSectionName {

    GEO("Geo"),
    DEVICE("Device"),
    AD_SIZE("Ad Size"),
    AD_FORMAT("Ad Format"),
    INVENTORY("Inventory"),
    DEMAND_SOURCES("Demand Sources"),
    OPERATING_SYSTEM("Operating System"),

//---Ad Spots
    NATIVE("Native"),
    BANNER("Banner"),
    VIDEO("Video"),
    VIDEO_AD_SIZE("Video Ad Size"),
    BANNER_AD_SIZE("Banner Ad Size"),
    VIDEO_FLOOR_PRICE("Video Floor Price"),
    VIDEO_MIN_DURATION("Video Min Duration"),
    VIDEO_MAX_DURATION("Video Max Duration"),
    NATIVE_FLOOR_PRICE("Native Floor Price"),
    BANNER_FLOOR_PRICE("Banner Floor Price"),
    VIDEO_PLACEMENT_TYPE("Video Placement Type"),
    VIDEO_PLAYBACK_METHOD("Video Playback Method");

    private final String name;
}
