package pages.yield.openpricing;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.AccessLevel;
import lombok.Getter;
import pages.BasePage;
import widgets.common.table.Table;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static pages.yield.openpricing.OpenPricingPageElements.*;

/**
 * Keep Selectors of UI elements in {@link OpenPricingPageElements}
 */
@Getter
public class OpenPricingPage extends BasePage {

    private SelenideElement pageTitle = $x(OPEN_PRICE_PAGE_TITLE.getSelector()).as(OPEN_PRICE_PAGE_TITLE.getAlias());
    private ElementsCollection openPricingItems = $$x(OPEN_PRICING_ITEMS.getSelector()).as(OPEN_PRICING_ITEMS.getAlias());
    private SelenideElement activateButton = $x(ACTIVATE_OPEN_PRICING_BUTTON.getSelector()).as(ACTIVATE_OPEN_PRICING_BUTTON.getAlias());
    private SelenideElement createOpenPricingButton = $x(CREATE_OPEN_PRICING_BUTTON.getSelector()).as(CREATE_OPEN_PRICING_BUTTON.getAlias());
    private SelenideElement uploadOpenPricingButton = $x(UPLOAD_OPEN_PRICING_BUTTON.getSelector()).as(UPLOAD_OPEN_PRICING_BUTTON.getAlias());
    private SelenideElement deactivateButton = $x(DEACTIVATE_OPEN_PRICING_BUTTON.getSelector()).as(DEACTIVATE_OPEN_PRICING_BUTTON.getAlias());
    private SelenideElement updateOpenPricingLink = $x(UPDATE_EXISTING_OPEN_PRICING_ITEM.getSelector()).as(UPDATE_EXISTING_OPEN_PRICING_ITEM.getAlias());
    private Table openPricingTable = new Table();
}
