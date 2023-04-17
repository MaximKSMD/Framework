package widgets.yield.openPricing.sidebar;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UpdateExistingOpenPricingRulesSidebarElements {

    ERROR_ALERT_BY_FIELD_NAME("Error Alert under Field '%s'",
            "//label[text()='%s']/../../../div[2]//div[contains(@class,'v-messages__message')]"),
    PUBLISHER_NAME_INPUT( "'Publisher Name' Input", "//label[text()='Publisher Name']/../div[1]"),
    PUBLISHER_NAME_DROPDOWN("'Publisher Name' Input", "//label[text()='Publisher Name']/../div[2]"),
    DROPDOWN_ITEMS("Dropdown Items", "//div[contains(@class,'menuable__content__activ')]//div[@class='v-list-item__title']"),
    DOWNLOAD_CSV_TEMPLATE_ICON("'Download CSV Template' Icon", "//span[contains(text(),'Download CSV Template')]/i"),
    DOWNLOAD_EXISTING_OPEN_PRICING_ICON("'Download Existing Open Pricing Rules' Icon",
            "//span[contains(text(),'Download Existing Open Pricing Rules')]/i"),
    DOWNLOAD_CSV_TEMPLATE_BUTTON("'Download CSV Template' Button", "//span[contains(text(),'Download CSV Template')]/../../button"),
    DOWNLOAD_EXISTING_OPEN_PRICING_BUTTON("'Download Existing Open Pricing Rules' Button",
            "//span[contains(text(),'Download Existing Open Pricing Rules')]/../../button"),
    CSV_FILE_INPUT( "'CSV File' Input", "//*[@id='csv']/../div"),
    CSV_FILE( "'CSV File'", "//*[@id='csv']"),
    CSV_FILE_CLEAR( "'CSV File' Clear Icon", "//button[@aria-label='clear icon']"),
    UPLOAD_CSV_TEXT( "'Upload your CSV' Text", "//h2[text()='Upload your CSV']/../p"),
    UPDATE_PRICING_RULES_BUTTON("'Update Pricing Rules' Button", "//button/span[contains(text(),'Update Pricing Rules')]"),
    CLOSE_ICON("'Close Open Pricing' Icon", "//div[@class='v-toolbar__content']/button[contains(@class,'v-btn--round theme--dark')]");


    private String alias;
    private String selector;
}
