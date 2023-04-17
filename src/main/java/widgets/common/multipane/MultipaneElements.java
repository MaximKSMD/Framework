package widgets.common.multipane;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MultipaneElements {

    PANEL_NAME_LABEL("'Panel Name' Label in Multipane", "/button"),
    SEARCH_INPUT("'Search' Input in Multipane", "//input[@placeholder='Search']"),
    EXCLUDED_ICON("'Excluded' Icon in Multipane", "//div[@class='excluded-banner']/i"),
    INCLUDED_ICON("'Included' Icon in Multipane", "//div[@class='included-banner']/i"),
    EXCLUDED_BANNER("'Excluded' Banner in Multipane", "//div[@class='excluded-banner']"),
    INCLUDED_BANNER("'Included' Banner in Multipane", "//div[@class='included-banner']"),
    SHOW_INACTIVE_TOGGLE("'Show Inactive' Toggle in Multipane",  "//input[@role='switch']"),
    ITEMS_QUANTITY_STRING("'Items Quantity' String in Multipane", "//div[@class='item-count']"),
    CLEAR_ALL_BUTTON( "'Clear All' button in Multipane", "//span[contains(text(),'Clear All')]"),
    CLEAR_SEARCH_ICON("'Clear Seach' Input Icon in Multipane", "//button[@aria-label='clear icon']"),
    SELECTION_INFO_LABEL("'Selection Info' Label in Multipane","//div[@class='selectionInfo']//span"),
    INCLUDE_ALL_BUTTON( "'Include All' button in Multipane", "//span/div[contains(text(),'Include All')]/.."),
    EXCLUDE_ALL_BUTTON( "'Exclude All' button in Multipane", "//span/div[contains(text(),'Exclude All')]/.."),
    SELECTION_INFO_EXCLUDED_LABEL("'Selection Info' Label in Multipane","//div[@class='selectionInfo']//span[1]"),
    SELECT_TABLE_ITEMS("Select Table Elements","//table[contains(@class,'select-table')]/tbody"),
    INCLUDED_EXCLUDED_TABLE_ITEMS("Included Excluded Table  Items", "//table[contains(@class,'included-table')]//tr"),
    SELECT_CHILD_TABLE_ITEMS( "Select Child Table Items", "//table[@class='select-table fixed']/tbody/tr[contains(@class,'children')]");

    private String alias;
    private String selector;
}
