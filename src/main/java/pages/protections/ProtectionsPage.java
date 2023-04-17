package pages.protections;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import pages.BasePage;
import widgets.common.table.Table;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static pages.protections.ProtectionsPageElements.*;


/**
 * Keep Selectors of UI elements in {@link ProtectionsPageElements}
 */
@Getter
public class ProtectionsPage extends BasePage {

    private Table protectionsTable = new Table();
    private ElementsCollection protectionItems = $$x(PROTECTION_ITEMS.getSelector()).as(PROTECTION_ITEMS.getAlias());
    private SelenideElement protectionPageTitle = $x(PROTECTIONS_PAGE_TITLE.getSelector()).as(PROTECTIONS_PAGE_TITLE.getAlias());
    private SelenideElement editProtectionButton = $x(EDIT_PROTECTION_BUTTON.getSelector()).as(EDIT_PROTECTION_BUTTON.getAlias());
    private SelenideElement createProtectionButton = $x(CREATE_PROTECTION_BUTTON.getSelector()).as(CREATE_PROTECTION_BUTTON.getAlias());
    private SelenideElement activateProtectionButton = $x(ACTIVATE_PROTECTION_BUTTON.getSelector()).as(ACTIVATE_PROTECTION_BUTTON.getAlias());
    private SelenideElement deactivateProtectionButton = $x(DEACTIVATE_PROTECTION_BUTTON.getSelector()).as(DEACTIVATE_PROTECTION_BUTTON.getAlias());

}
