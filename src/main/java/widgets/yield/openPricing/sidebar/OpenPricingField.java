package widgets.yield.openPricing.sidebar;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OpenPricingField {

    NAME("Name"),
    FLOOR_PRICE("Floor Price"),
    PUBLISHER_NAME("Publisher Name");

    private String name;
}
