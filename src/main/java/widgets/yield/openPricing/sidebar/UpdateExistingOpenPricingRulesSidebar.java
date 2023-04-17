package widgets.yield.openPricing.sidebar;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import widgets.common.validationalert.ValidationBottomAlert;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static widgets.yield.openPricing.sidebar.UpdateExistingOpenPricingRulesSidebarElements.*;

/**
 * Keep Selectors of UI elements in {@link UpdateExistingOpenPricingRulesSidebarElements}
 */
@Getter
public class UpdateExistingOpenPricingRulesSidebar {

    private SelenideElement closeIcon = $x(CLOSE_ICON.getSelector()).as(CLOSE_ICON.getAlias());
    private SelenideElement updateExistingRulesButton = $x(UPDATE_PRICING_RULES_BUTTON.getSelector()).as(UPDATE_PRICING_RULES_BUTTON.getAlias());

    private ElementsCollection publisherNameDropdownItems = $$x(DROPDOWN_ITEMS.getSelector()).as(DROPDOWN_ITEMS.getAlias());
    private SelenideElement publisherNameDropdown = $x(PUBLISHER_NAME_DROPDOWN.getSelector()).as(PUBLISHER_NAME_DROPDOWN.getAlias());
    private SelenideElement publisherInput = $x(PUBLISHER_NAME_INPUT.getSelector()).as(PUBLISHER_NAME_INPUT.getAlias());

    private SelenideElement csvFileInput = $x(CSV_FILE_INPUT.getSelector()).as(CSV_FILE_INPUT.getAlias());
    private SelenideElement csvFile = $x(CSV_FILE.getSelector()).as(CSV_FILE.getAlias());
    private SelenideElement uploadCSVText = $x(UPLOAD_CSV_TEXT.getSelector()).as(UPLOAD_CSV_TEXT.getAlias());
    private SelenideElement csvFileClearIcon = $x(CSV_FILE_CLEAR.getSelector()).as(CSV_FILE_CLEAR.getAlias());
    private SelenideElement downloadCSVTemplateIcon = $x(DOWNLOAD_CSV_TEMPLATE_ICON.getSelector()).as(DOWNLOAD_CSV_TEMPLATE_ICON.getAlias());
    private SelenideElement downloadExistingOpenPricingRulesIcon = $x(DOWNLOAD_EXISTING_OPEN_PRICING_ICON.getSelector())
            .as(DOWNLOAD_EXISTING_OPEN_PRICING_ICON.getAlias());
    private SelenideElement downloadCSVTemplateButton = $x(DOWNLOAD_CSV_TEMPLATE_BUTTON.getSelector()).as(DOWNLOAD_CSV_TEMPLATE_BUTTON.getAlias());
    private SelenideElement downloadExistingOpenPricingRulesButton = $x(DOWNLOAD_EXISTING_OPEN_PRICING_BUTTON.getSelector())
            .as(DOWNLOAD_EXISTING_OPEN_PRICING_BUTTON.getAlias());
    private ValidationBottomAlert errorAlert = new ValidationBottomAlert();

    public SelenideElement getErrorAlertByFieldName(String fieldName) {

        return $x(String.format(ERROR_ALERT_BY_FIELD_NAME.getSelector(), fieldName))
                .as(String.format(ERROR_ALERT_BY_FIELD_NAME.getAlias(), fieldName));
    }

}
