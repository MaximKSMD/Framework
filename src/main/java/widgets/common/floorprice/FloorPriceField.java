package widgets.common.floorprice;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$x;
import static widgets.common.floorprice.FloorPriceFieldElements.*;


/**
 * Keep Selectors of UI elements in {@link FloorPriceFieldElements}
 */
@Getter
public class FloorPriceField {

    private SelenideElement floorPriceInput = $x(FLOOR_PRICE_INPUT.getSelector()).as(FLOOR_PRICE_INPUT.getAlias());
    private SelenideElement floorPricePrefix = $x(FLOOR_PRICE_PREFIX.getSelector()).as(FLOOR_PRICE_PREFIX.getAlias());
    private SelenideElement florPriceUpButton = $x(FLOOR_PRICE_UP_BUTTON.getSelector()).as(FLOOR_PRICE_UP_BUTTON.getAlias());
    private SelenideElement floorPriceDownButton = $x(FLOOR_PRICE_DOWN_BUTTON.getSelector()).as(FLOOR_PRICE_DOWN_BUTTON.getAlias());

}
