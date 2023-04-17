package pages.inventory.adspots;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdSpotsPageElements {

    ADSPOT_ITEMS("Ad Spot Items", "//tbody/tr"),
    ADSPOTS_PAGE_TITLE("'Ad Spots' Page Title", "//h1"),
    EDIT_ADSPOT_BUTTON("'Edit Ad Spot' Button", "//button//span[text()='Edit Ad Spot']"),
    CREATE_ADSPOT_BUTTON("'Create Ad Spot' Button", "//button//span[text()='Create Ad Spot']"),
    ACTIVATE_AD_SPOT_BUTTON("'Activate Ad Spot' Button", "//button//span[contains(text(),'Activate Ad Spot')]"),
    DEACTIVATE_AD_SPOT_BUTTON("'Deactivate Ad Spot' Button", "//button//span[contains(text(),'Deactivate Ad Spot')]");

    private String alias;
    private String selector;
}
