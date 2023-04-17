package widgets.protections.sidebar;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import widgets.common.floorprice.FloorPriceField;
import widgets.common.multipane.Multipane;
import widgets.common.multipane.MultipaneNameImpl;
import widgets.common.warningbanner.ChangePublisherBanner;
import widgets.protections.protectiontypemultipane.ProtectionTypeMultipane;
import widgets.protections.protectiontypemultipane.ProtectionTypeNameImpl;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static widgets.protections.sidebar.ProtectionsSidebarElements.*;

/**
 * Keep Selectors of UI elements in {@link ProtectionsSidebarElements}
 */
@Getter
public abstract class ProtectionsSidebar {

    private SelenideElement notes = $x(NOTES.getSelector()).as(NOTES.getAlias());
    private SelenideElement title = $x(TITLE.getSelector()).as(TITLE.getAlias());
    private SelenideElement nameInput = $x(NAME.getSelector()).as(NAME.getAlias());
    private SelenideElement saveButton = $x(SAVE_BUTTON.getSelector()).as(SAVE_BUTTON.getAlias());
    private SelenideElement publisherNameDropdown = $x(PUBLISHER_NAME_DROPDOWN.getSelector()).as(PUBLISHER_NAME_DROPDOWN.getAlias());
    private SelenideElement protectionTypeDropdown = $x(PROTECTION_TYPE_DROPDOWN.getSelector()).as(PROTECTION_TYPE_DROPDOWN.getAlias());
    private SelenideElement managedBySystemAdminOnly = $x(MANAGED_BY_SYSTEM_ADMIN_ONLY.getSelector()).as(MANAGED_BY_SYSTEM_ADMIN_ONLY.getAlias());

    private SelenideElement closeIcon = $x(CLOSE_ICON.getSelector()).as(CLOSE_ICON.getAlias());
    private SelenideElement publisherInput = $x(PUBLISHER_NAME.getSelector()).as(PUBLISHER_NAME.getAlias());
    private ElementsCollection publisherItems = $$x(PUBLISHER_ITEMS.getSelector()).as(PUBLISHER_ITEMS.getAlias());
    private ElementsCollection protectionTypeItems = $$x(PROTECTION_TYPE_DROPDOWN_ITEMS.getSelector()).as(PROTECTION_TYPE_DROPDOWN_ITEMS.getAlias());

    private Multipane geoMultipane = new Multipane(MultipaneNameImpl.GEO);
    private Multipane deviceMultipane = new Multipane(MultipaneNameImpl.DEVICE);
    private Multipane adSizeMultipane = new Multipane(MultipaneNameImpl.AD_SIZE);
    private Multipane adFormatMultipane = new Multipane(MultipaneNameImpl.AD_FORMAT);
    private Multipane inventoryMultipane = new Multipane(MultipaneNameImpl.INVENTORY);
    private Multipane demandSourcesMultipane = new Multipane(MultipaneNameImpl.DEMAND_SOURCES);
    private Multipane operatingSystemMultipane = new Multipane(MultipaneNameImpl.OPERATING_SYSTEM);

    private Multipane advertiserTypeMultipane = new ProtectionTypeMultipane(ProtectionTypeNameImpl.ADVERTISER);
    private Multipane adCategoriesTypeMultipane = new ProtectionTypeMultipane(ProtectionTypeNameImpl.AD_CATEGORIES);

    private FloorPriceField floorPriceField = new FloorPriceField();
    private ChangePublisherBanner changePublisherBanner = new ChangePublisherBanner();
}
