package pages.sales.deals;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import pages.BasePage;
import widgets.common.table.Table;

import static com.codeborne.selenide.Selenide.$x;
import static pages.sales.deals.DealsPageElements.*;

/**
 * Keep Selectors of UI elements in {@link DealsPageElements}
 */
@Getter
public class DealsPage extends BasePage {

    private SelenideElement pageTitle = $x(DEAL_PAGE_TITLE.getSelector()).as(DEAL_PAGE_TITLE.getAlias());
    private SelenideElement createNewDealButton = $x(CREATE_DEAL_BUTTON.getSelector()).as(CREATE_DEAL_BUTTON.getAlias());
    private SelenideElement activateDealsButton = $x(ACTIVATE_DEAL_BUTTON.getSelector()).as(ACTIVATE_DEAL_BUTTON.getAlias());
    private SelenideElement deactivateDealsButton = $x(DEACTIVATE_DEAL_BUTTON.getSelector()).as(DEACTIVATE_DEAL_BUTTON.getAlias());

    private Table dealsTable = new Table();

}
