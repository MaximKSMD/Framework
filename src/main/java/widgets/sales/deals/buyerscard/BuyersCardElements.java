
package widgets.sales.deals.buyerscard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BuyersCardElements {

    ACTIVE_CHECK_BOX("'Active' CheckBox", "//input[@type='checkbox']"),
    DELETE_CARD_ICON("'Delete Buyer Card' Icon", "//i[contains(@class,'trash')]/../.."),
    EXPAND_COLLAPSE_BUTTON("'Expand/Collapse Buyer Card Icon'", "//i[contains(@class,'chevron')]"),
    ADVERTISER_ID_INPUT("'Advertiser ID' Input", "//label[text()='Advertiser ID']/../input[@type='text']"),
    DSP_SEAT_ID_COMBO( "'DSP Seat ID' ComboBox", "//label[text()='DSP Seat ID']/../input[@type='text']"),
    DSP_SEAT_NAME_INPUT("'DSP Seat Name' Input", "//label[text()='DSP Seat Name']/../input[@type='text']"),
    ADVERTISER_NAME_INPUT("'Advertiser Name' Input", "//label[text()='Advertiser Name']/../input[@type='text']"),
    AUTOCOMPLETE_ITEMS("Autocomplete Item", "//div[contains(@class,'content__active')]//span[@class='elipsisText']"),
    DSP_SEAT_PASSTHROUGH_STRING_INPUT( "'DSP Seat Passthrough String' Input", "//label[text()='DSP Seat Passthrough String']/../input[@type='text']"),
    DSP_DOMAIN_ADVERTISER_PASSTHROUGHT_STRING_INPUT("'DSP Domain Advertiser Passthrough String' Input", "//label[text()='DSP Domain Advertiser Passthrough String']/../input[@type='text']");

    private String alias;
    private String selector;
}
