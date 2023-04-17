package widgets.inventory.adSpots.sidebar;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdSpotSideBarElements {

    CLOSE_ICON("'Close Ad Spot' Icon", "//i[contains(@class,'mdi-close')]/../.."),
    TAB_HEADER_BY_CAPTION("'%s' Tab Header", "//a[@role='tab' and contains(text(),'%s')]"),
    SAVE_AD_SPOT_BUTTON("'Save Ad Spot' Button", "//button/span[contains(text(),'Save Ad Spot')]"),
    AD_SPOT_SIDEBAR_TITLE("'Create/Edit AdSpot' sidebar title", "//div[@class='v-toolbar__title']/div"),
    SAVE_AND_VIEW_AD_TAG_BUTTON("'Save And View Ad Tags' Button", "//button/span[contains(text(),'Save and View Ad Tags')]");

    private final String alias;
    private final String selector;
}
