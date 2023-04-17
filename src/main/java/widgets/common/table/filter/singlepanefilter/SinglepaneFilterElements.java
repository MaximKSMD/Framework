package widgets.common.table.filter.singlepanefilter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SinglepaneFilterElements {

    SEARCH_INPUT("'Search' Input in Singlepane", "//input[@placeholder='Search']"),
    CLEAR_ALL_BUTTON( "'Clear All' button in Multipane", "//span/div[contains(text(),'Clear All')]"),
    SEARCH_ICON("'Clear Search' Input Icon in Singlepane", "//div/i[contains(@class,'mdi-magnify')]"),
    INCLUDE_ALL_BUTTON( "'Include All' button in Singlepane", "//span/div[contains(text(),'Include All')]"),
    TOTAL_ITEMS_QUANTITY_LABEL("'Total Items Quantity' String in Singlepane", "//span/div[contains(text(),'Include All')]/../div[@class='item-count']"),
    INCLUDED_ITEMS_QUANTITY_LABEL("'Included Items Quantity' String in Singlepane", "//span/div[contains(text(),'Clear All')]/../div[@class='item-count']"),

    FILTER_ITEMS("Select Filter Elements","//table[contains(@class,'select-table')]/tbody");

    private String alias;
    private String selector;
}
