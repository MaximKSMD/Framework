package widgets.inventory.adSpots.sidebar.card.banner;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import widgets.common.adSizes.AdSizesListPanel;
import widgets.inventory.adSpots.sidebar.card.banner.floorprice.FloorPriceField;

import static com.codeborne.selenide.Selenide.$x;
import static widgets.inventory.adSpots.sidebar.card.banner.AdSpotBannerCardSidebarElements.*;

/**
 * Keep Selectors of UI elements in {@link AdSpotBannerCardSidebarElements}
 */
@Getter
public class AdSpotBannerCardSidebar {

    private SelenideElement bannerPanel = $x(BANNER_PANEL.getSelector())
            .as(BANNER_PANEL.getAlias());
    private SelenideElement adSizes = $x(BANNER_AD_SIZES.getSelector())
            .as(BANNER_AD_SIZES.getAlias());
    private SelenideElement enabledToggle = $x(ENABLED_TOGGLE.getSelector())
            .as(ENABLED_TOGGLE.getAlias());
    private SelenideElement adSizesInput = $x(BANNER_AD_SIZES_INPUT.getSelector())
            .as(BANNER_AD_SIZES_INPUT.getAlias());
    private SelenideElement bannerAdSizesTooltipIcon = $x(TOOLTIP_BANNER_AD_SIZES_ICON.getSelector())
            .as(TOOLTIP_BANNER_AD_SIZES_ICON.getAlias());
    private SelenideElement bannerCardHeader = $x(BANNER_CARD.getSelector()).as(BANNER_CARD.getAlias());
    private SelenideElement bannerFloorPriceTooltipIcon = $x(TOOLTIP_BANNER_FLOOR_PRICE_ICON.getSelector())
            .as(TOOLTIP_BANNER_FLOOR_PRICE_ICON.getAlias());

    private AdSizesListPanel adSizesPanel = new AdSizesListPanel();
    private FloorPriceField floorPriceField = new FloorPriceField();

    public SelenideElement getErrorAlertByFieldName(String fieldName){

        return $x(String.format(ERROR_ALERT_BY_FIELD_NAME.getSelector(),fieldName))
                .as(String.format(ERROR_ALERT_BY_FIELD_NAME.getAlias(),fieldName));
    }
}
