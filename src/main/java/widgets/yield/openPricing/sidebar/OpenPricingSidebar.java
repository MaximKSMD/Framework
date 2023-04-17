package widgets.yield.openPricing.sidebar;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import widgets.common.multipane.Multipane;
import widgets.common.multipane.MultipaneNameImpl;
import widgets.common.warningbanner.ChangePublisherBanner;
import widgets.common.floorprice.FloorPriceField;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static java.lang.String.*;
import static widgets.sales.privateauctions.sidebar.PrivateAuctionSidebarElements.TITLE;
import static widgets.yield.openPricing.sidebar.OpenPricingSidebarElements.*;

/**
 * Keep Selectors of UI elements in {@link OpenPricingSidebarElements}
 */
@Getter
public abstract class OpenPricingSidebar {

    private SelenideElement title = $x(TITLE.getSelector()).as(TITLE.getAlias());
    private SelenideElement closeIcon = $x(CLOSE_ICON.getSelector()).as(CLOSE_ICON.getAlias());
    private SelenideElement saveButton = $x(SAVE_BUTTON.getSelector()).as(SAVE_BUTTON.getAlias());
    private SelenideElement publisherInput = $x(PUBLISHER_NAME_INPUT.getSelector()).as(PUBLISHER_NAME_INPUT.getAlias());
    private SelenideElement nameInput = $x(OPEN_PRICING_FIELD_NAME.getSelector()).as(OPEN_PRICING_FIELD_NAME.getAlias());
    private ElementsCollection publisherNameDropdownItems = $$x(DROPDOWN_ITEMS.getSelector()).as(DROPDOWN_ITEMS.getAlias());
    private SelenideElement publisherNameDropdown = $x(PUBLISHER_NAME_DROPDOWN.getSelector()).as(PUBLISHER_NAME_DROPDOWN.getAlias());
    private FloorPriceField floorPriceField = new FloorPriceField();

    private Multipane geoMultipane = new Multipane(MultipaneNameImpl.GEO);
    private Multipane deviceMultipane = new Multipane(MultipaneNameImpl.DEVICE);
    private Multipane adSizeMultipane = new Multipane(MultipaneNameImpl.AD_SIZE);
    private Multipane adFormatMultipane = new Multipane(MultipaneNameImpl.AD_FORMAT);
    private Multipane inventoryMultipane = new Multipane(MultipaneNameImpl.INVENTORY);
    private Multipane demandSourcesMultipane = new Multipane(MultipaneNameImpl.DEMAND_SOURCES);
    private Multipane operatingSystemMultipane = new Multipane(MultipaneNameImpl.OPERATING_SYSTEM);

    private ChangePublisherBanner changePublisherBanner = new ChangePublisherBanner();

    public SelenideElement getErrorLabelByFieldName(String fieldName) {

        return $x(format(OPEN_PRICING_FIELD_NAME.getSelector(), fieldName))
                .as(format(OPEN_PRICING_FIELD_NAME.getAlias(), fieldName));
    }

}
