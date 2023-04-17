package widgets.inventory.adSpots.sidebar.adtagcard.video.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecommendedParameter {

    OS("OS"),
    HEIGHT("GDPR"),
    COPPA("COPPA"),
    WIDTH("Device Type"),
    CACHE_BUSTER("Consent"),
    OS_VERSION("OS Version"),
    DEVICE_MAKE("Device Make"),
    DO_NOT_TRACK("Do Not Track"),
    DEVICE_MODEL("Device Model"),
    US_PRIVACY_CCPA("US Privacy (CCPA)"),
    LIMIT_AD_TRACKING("Limit ad tracking");

    private String name;
}
