package widgets.common.detailsmenu.menu;

import lombok.Getter;
import widgets.common.detailsmenu.menu.sections.DetailsSection;

import static widgets.common.detailsmenu.DetailsSectionName.*;

@Getter
public class TableItemDetailsMenu {

    private DetailsSection geoDetailsSection = new DetailsSection(GEO);
    private DetailsSection deviceDetailsSection = new DetailsSection(DEVICE);
    private DetailsSection adSizeDetailsSection = new DetailsSection(AD_SIZE);
    private DetailsSection adFormatDetailsSection = new DetailsSection(AD_FORMAT);
    private DetailsSection inventoryDetailsSection = new DetailsSection(INVENTORY);
    private DetailsSection demandSourcesDetailsSection = new DetailsSection(DEMAND_SOURCES);
    private DetailsSection operatingSystemDetailsSection = new DetailsSection(OPERATING_SYSTEM);
    private DetailsSection nativeDetailsSection = new DetailsSection(NATIVE);
    private DetailsSection nativeFloorPriceDetailsSection = new DetailsSection(NATIVE_FLOOR_PRICE);
    private DetailsSection bannerDetailsSection = new DetailsSection(BANNER);
    private DetailsSection bannerFloorPriceDetailsSection = new DetailsSection(BANNER_FLOOR_PRICE);
    private DetailsSection bannerAdSizeDetailsSection = new DetailsSection(BANNER_AD_SIZE);
    private DetailsSection videoDetailsSection = new DetailsSection(VIDEO);
    private DetailsSection videoAdSizeDetailsSection = new DetailsSection(VIDEO_AD_SIZE);
    private DetailsSection videoFloorPriceDetailsSection = new DetailsSection(VIDEO_FLOOR_PRICE);
    private DetailsSection videoPlacementTypeDetailsSection = new DetailsSection(VIDEO_PLACEMENT_TYPE);
    private DetailsSection videoMaxDurationDetailsSection = new DetailsSection(VIDEO_MAX_DURATION);
    private DetailsSection videoMinDurationDetailsSection = new DetailsSection(VIDEO_MIN_DURATION);
    private DetailsSection videoPlaybackMethodDetailsSection = new DetailsSection(VIDEO_PLAYBACK_METHOD);
}
