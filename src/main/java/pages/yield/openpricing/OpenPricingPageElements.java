package pages.yield.openpricing;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OpenPricingPageElements {

    OPEN_PRICE_PAGE_TITLE("'Open Pricing Rules' Page Title", "//h1"),
    OPEN_PRICING_ITEMS("Open Pricing Items", "//tbody/tr"),
    CREATE_OPEN_PRICING_BUTTON("'Create Open Pricing Rule' Button", "//button//span[text()='CREATE OPEN PRICING RULE']"),
    UPLOAD_OPEN_PRICING_BUTTON("'Bulk Upload Open Pricing Rules' Button", "//button//span[text()='BULK UPLOAD OPEN PRICING RULES']"),
    UPDATE_EXISTING_OPEN_PRICING_ITEM("'Update Existing Open Pricing Rules' Button", "//div[@role='menu']//div[text()='Update Existing Open Pricing Rules']"),
    EDIT_OPEN_PRICING_BUTTON("'Edit Open Pricing Rule' Button", "//button//span[text()='Edit Open Pricing Rule']"),
    ACTIVATE_OPEN_PRICING_BUTTON("'Activate OpenPricing' Button", "//button//span[text()='Activate']"),
    DEACTIVATE_OPEN_PRICING_BUTTON("'Deactivate OpenPricing' Button", "//button//span[text()='Deactivate']");

    private String alias;
    private String selector;
}
