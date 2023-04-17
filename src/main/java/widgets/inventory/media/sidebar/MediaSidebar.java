package widgets.inventory.media.sidebar;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import widgets.common.categories.CategoriesListPanel;
import widgets.common.validationalert.ValidationBottomAlert;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static widgets.inventory.media.sidebar.MediaSidebarElements.*;

/**
 * Keep Selectors of UI elements in {@link MediaSidebarElements}
 */
@Getter
public abstract class MediaSidebar {

    private SelenideElement bundleInput = $x(BUNDLE.getSelector()).as(BUNDLE.getAlias());
    private SelenideElement siteURL = $x(SITE_URL.getSelector()).as(SITE_URL.getAlias());
    private SelenideElement nameInput = $x(MEDIA_NAME.getSelector()).as(MEDIA_NAME.getAlias());
    private SelenideElement closeIcon = $x(CLOSE_ICON.getSelector()).as(CLOSE_ICON.getAlias());
    private SelenideElement categories = $x(CATEGORIES.getSelector()).as(CATEGORIES.getAlias());
    private SelenideElement saveButton = $x(SAVE_BUTTON.getSelector()).as(SAVE_BUTTON.getAlias());
    private SelenideElement appStoreURL = $x(APP_STORE_URL.getSelector()).as(APP_STORE_URL.getAlias());
    private SelenideElement activeToggle = $x(ACTIVE_TOGGLE.getSelector()).as(ACTIVE_TOGGLE.getAlias());
    private SelenideElement platformInput = $x(PLATFORM_INPUT.getSelector()).as(PLATFORM_INPUT.getAlias());
    private SelenideElement publisherInput = $x(PUBLISHER_NAME.getSelector()).as(PUBLISHER_NAME.getAlias());
    private SelenideElement categoriesInput = $x(CATEGORIES_INPUT.getSelector()).as(CATEGORIES_INPUT.getAlias());
    private SelenideElement platformDropdown = $x(PLATFORM_DROPDOWN.getSelector()).as(PLATFORM_DROPDOWN.getAlias());
    private SelenideElement publisherNameInput = $x(PUBLISHER_NAME_INPUT.getSelector()).as(PUBLISHER_NAME_INPUT.getAlias());
    private ElementsCollection platformDropdownItems = $$x(PLATFORM_DROPDOWN_ITEMS.getSelector()).as(PLATFORM_DROPDOWN_ITEMS.getAlias());
    private ElementsCollection publisherDropdownItems = $$x(PUBLISHER_DROPDOWN_ITEMS.getSelector()).as(PUBLISHER_DROPDOWN_ITEMS.getAlias());


    private ValidationBottomAlert errorAlert = new ValidationBottomAlert();
    private CategoriesListPanel categoriesPanel = new CategoriesListPanel();

    public SelenideElement getTooltipIconByFieldName(String fieldName) {

        return $x(String.format(TOOLTIP_ICON_BY_FIELD_NAME.getSelector(), fieldName))
                .as(String.format(TOOLTIP_ICON_BY_FIELD_NAME.getAlias(), fieldName));
    }

    public SelenideElement getErrorAlertByFieldName(String fieldName) {

        return $x(String.format(ERROR_ALERT_BY_FIELD_NAME.getSelector(), fieldName))
                .as(String.format(ERROR_ALERT_BY_FIELD_NAME.getAlias(), fieldName));
    }
}
