package widgets.sales.privateauctions.sidebar;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import widgets.common.datepicker.DatePicker;
import widgets.common.multipane.Multipane;
import widgets.common.multipane.MultipaneNameImpl;
import widgets.common.validationalert.ValidationBottomAlert;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static widgets.sales.privateauctions.sidebar.PrivateAuctionSidebarElements.*;

@Getter
public abstract class PrivateAuctionSidebar {

    private SelenideElement title = $x(TITLE.getSelector()).as(TITLE.getAlias());
    private SelenideElement nameInput = $x(NAME.getSelector()).as(NAME.getAlias());
    private SelenideElement closeIcon = $x(CLOSE_ICON.getSelector()).as(CLOSE_ICON.getAlias());
    private SelenideElement activeToggle = $x(ACTIVE_TOGGLE.getSelector()).as(ACTIVE_TOGGLE.getAlias());
    private SelenideElement dateRangeInput = $x(DATE_RANGE_INPUT.getSelector()).as(DATE_RANGE_INPUT.getAlias());
    private SelenideElement startDateInput = $x(START_DATE_INPUT.getSelector()).as(START_DATE_INPUT.getAlias());
    private SelenideElement alwaysOnToggle = $x(ALWAYS_ON_TOGGLE.getSelector()).as(ALWAYS_ON_TOGGLE.getAlias());
    private ElementsCollection publisherItems = $$x(PUBLISHER_ITEMS.getSelector()).as(PUBLISHER_ITEMS.getAlias());
    private SelenideElement optimizeCheckbox = $x(OPTIMIZE_CHECKBOX.getSelector()).as(OPTIMIZE_CHECKBOX.getAlias());
    private SelenideElement publisherNameInput = $x(PUBLISHER_NAME_INPUT.getSelector()).as(PUBLISHER_NAME_INPUT.getAlias());
    private SelenideElement saveAndCloseButton = $x(SAVE_AND_CLOSE_BUTTON.getSelector()).as(SAVE_AND_CLOSE_BUTTON.getAlias());
    private SelenideElement publisherNameDropdown = $x(PUBLISHER_NAME_DROPDOWN.getSelector()).as(PUBLISHER_NAME_DROPDOWN.getAlias());
    private SelenideElement saveAndCreateDealButton = $x(SAVE_AND_CREATE_DEAL_BUTTON.getSelector()).as(SAVE_AND_CREATE_DEAL_BUTTON.getAlias());

    private Multipane geoMultipane = new Multipane(MultipaneNameImpl.GEO);

    private Multipane deviceMultipane = new Multipane(MultipaneNameImpl.DEVICE);
    private Multipane adSizeMultipane = new Multipane(MultipaneNameImpl.AD_SIZE);
    private Multipane adFormatMultipane = new Multipane(MultipaneNameImpl.AD_FORMAT);
    private Multipane inventoryMultipane = new Multipane(MultipaneNameImpl.INVENTORY);
    private Multipane operatingSystemMultipane = new Multipane(MultipaneNameImpl.OPERATING_SYSTEM);

    private DatePicker dateRangeField = new DatePicker();
    private ValidationBottomAlert errorAlert = new ValidationBottomAlert();

    public SelenideElement getErrorAlertByFieldName(String fieldName) {

        return $x(String.format(ERROR_BY_FILED_NAME.getSelector(), fieldName))
                .as(String.format(ERROR_BY_FILED_NAME.getAlias(), fieldName));
    }
}
