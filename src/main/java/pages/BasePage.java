package pages;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import widgets.common.ToasterMessage;
import widgets.common.menusidebar.MenuSidebar;

import static com.codeborne.selenide.Selenide.$x;
import static pages.BasePageElements.*;

/**
 * Keep Selectors of UI elements in {@link BasePageElements}
 */
@Getter
public class BasePage {

    private MenuSidebar menuSidebar = new MenuSidebar();
    private ToasterMessage toasterMessage = new ToasterMessage();
    private SelenideElement logo = $x(LOGO.getSelector()).as(LOGO.getAlias());
    private SelenideElement sidebar = $x(SIDEBAR.getSelector()).as(SIDEBAR.getAlias());
    private SelenideElement snackBar = $x(SNACKBAR.getSelector()).as(SNACKBAR.getAlias());
    private SelenideElement circleIcon = $x(CIRCLE_ICON.getSelector()).as(CIRCLE_ICON.getAlias());
    private SelenideElement versionText = $x(VERSION_TEXT.getSelector()).as(VERSION_TEXT.getAlias());
    private SelenideElement nuxtProgress = $x(NUXT_PROGRESSBAR.getSelector()).as(NUXT_PROGRESSBAR.getAlias());
    private SelenideElement tableProgressBar = $x(TABLE_PROGRESSBAR.getSelector()).as(TABLE_PROGRESSBAR.getAlias());
}

