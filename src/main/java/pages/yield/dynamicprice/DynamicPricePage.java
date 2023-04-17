package pages.yield.dynamicprice;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import pages.BasePage;

import static com.codeborne.selenide.Selenide.$x;
import static pages.yield.dynamicprice.DynamicPricePageElements.DYNAMIC_PRICE_PAGE_TITLE;

/**
 * Keep Selectors of UI elements in {@link DynamicPricePageElements}
 */
@Getter
public class DynamicPricePage extends BasePage {

    private SelenideElement pageTitle = $x(DYNAMIC_PRICE_PAGE_TITLE.getSelector()).as(DYNAMIC_PRICE_PAGE_TITLE.getAlias());
}
