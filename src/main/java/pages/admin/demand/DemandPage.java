package pages.admin.demand;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import pages.BasePage;
import widgets.common.table.Table;

import static com.codeborne.selenide.Selenide.$x;
import static pages.admin.demand.DemandPageElements.DEMAND_PAGE_TITLE;

/**
 * Keep Selectors of UI elements in {@link DemandPageElements}
 */
@Getter
public class DemandPage extends BasePage {

    private SelenideElement pageTitle = $x(DEMAND_PAGE_TITLE.getSelector()).as(DEMAND_PAGE_TITLE.getAlias());
    private Table demandTable = new Table();
}
