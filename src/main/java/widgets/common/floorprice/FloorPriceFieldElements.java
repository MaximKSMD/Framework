
package widgets.common.floorprice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FloorPriceFieldElements {

    //ToDo Add Selectors
    FLOOR_PRICE_UP_BUTTON("'Floor Price Up' button","//todo add Selector"),
    FLOOR_PRICE_DOWN_BUTTON("'Floor Price Down' button","//todo add Selector"),
    FLOOR_PRICE_INPUT("'Floor Price' Input Field", "//label[text()='Floor Price']/../input"),
    FLOOR_PRICE_PREFIX("''Floor Price' Field Prefix", "//label[text()='Floor Price']/../div");

    private String alias;
    private String selector;
}
