package pages.yield.dynamicprice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DynamicPricePageElements {

    DYNAMIC_PRICE_PAGE_TITLE( "'Dynamic Price' Page Title", "//h1");

    private String alias;
    private String selector;
}
