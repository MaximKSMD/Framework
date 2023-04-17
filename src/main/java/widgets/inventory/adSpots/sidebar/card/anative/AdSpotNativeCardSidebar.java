package widgets.inventory.adSpots.sidebar.card.anative;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import widgets.inventory.adSpots.sidebar.card.anative.floorprice.FloorPriceField;

import static com.codeborne.selenide.Selenide.$x;
import static widgets.inventory.adSpots.sidebar.card.anative.AdSpotNativeCardSidebarElements.*;

/**
 * Keep Selectors of UI elements in {@link AdSpotNativeCardSidebarElements}
 */
@Getter
public class AdSpotNativeCardSidebar {

    private SelenideElement nativePanel = $x(NATIVE_PANEL.getSelector())
            .as(NATIVE_PANEL.getAlias());
    private SelenideElement tooltipNativeFloorPrice = $x(TOOLTIP_NATIVE_FLOOR_PRICE_ICON.getSelector())
            .as(TOOLTIP_NATIVE_FLOOR_PRICE_ICON.getAlias());
    private SelenideElement nativeCardHeader = $x(NATIVE_CARD.getSelector()).as(NATIVE_CARD.getAlias());
    private SelenideElement enabledToggle = $x(ENABLED_TOGGLE.getSelector())
            .as(ENABLED_TOGGLE.getAlias());
    private FloorPriceField floorPriceField = new FloorPriceField();

    public SelenideElement getErrorAlertByFieldName(String fieldName){

        return $x(String.format(ERROR_ALERT_BY_FIELD_NAME.getSelector(),fieldName))
                .as(String.format(ERROR_ALERT_BY_FIELD_NAME.getAlias(),fieldName));
    }
}
