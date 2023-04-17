package widgets.common.table.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FilterOptionsElements {

    FILTER_OPTIONS_ITEMS( "'Filter Options' Elements", "//div[(@role='menu')and(contains(@class,'menuable__content__active'))]/div/div"),
    FILTER_OPTION_BY_NAME("'%s' Filter Option", "//div[@role='menuitem']//div[text()='%s']"),
    FILTER_OPTIONS_MENU( "Filter Options Menu", "//div[(@role='menu')and(contains(@class,'menuable__content__active'))]/div"),
    COLUMNS_FILTER_BUTTON( "'Column Filters' button", "//button/span[contains(text(),'Column Filters')]/..");

    private String alias;
    private String selector;
}
