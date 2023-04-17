
package widgets.inventory.adSpots.sidebar.card.banner.floorprice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FloorPriceFieldElements {

    //ToDo Add Selectors
    FLOOR_PRICE_UP_BUTTON("'Floor Price Up' button","//todo add Selector"),
    FLOOR_PRICE_DOWN_BUTTON("'Floor Price Down' button","//todo add Selector"),
    FLOOR_PRICE_INPUT("'Floor Price' Input Field",
            "//h3[text()='Banner']/../..//label[text()='Floor Price']/../../div/input"),
    FLOOR_PRICE_PREFIX("''Floor Price' Field Currency",
            "//h3[text()='Banner']/../..//label[text()='Floor Price']/../../div/div");

    private String alias;
    private String selector;
}
