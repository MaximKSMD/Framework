package widgets.inventory.media.sidebar;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$x;
import static widgets.inventory.media.sidebar.MediaTooltipSidebarElements.*;


/**
 * Keep Selectors of UI elements in {@link MediaTooltipSidebarElements}
 */
@Getter
public class MediaTooltipSidebar {

    private SelenideElement tooltipCategories = $x(TOOLTIP_CATEGORIES_ICON.getSelector())
            .as(TOOLTIP_CATEGORIES_ICON.getAlias());
    private SelenideElement tooltipSiteURL = $x(TOOLTIP_SITE_URL.getSelector())
            .as(TOOLTIP_SITE_URL.getAlias());
    private SelenideElement tooltipAppStoreURL = $x(TOOLTIP_APP_STORE_URL.getSelector())
            .as(TOOLTIP_APP_STORE_URL.getAlias());
    private SelenideElement tooltipPlaceholder = $x(TOOLTIP_PLACEHOLDER.getSelector())
            .as(TOOLTIP_PLACEHOLDER.getAlias());
    private SelenideElement tooltipBundle = $x(TOOLTIP_BUNDLE.getSelector())
            .as(TOOLTIP_BUNDLE.getAlias());
}
