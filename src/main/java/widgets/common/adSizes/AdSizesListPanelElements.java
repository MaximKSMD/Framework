package widgets.common.adSizes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdSizesListPanelElements {

    AD_SIZES_SELECTED_ITEMS("Ad Sizes Selected Items",
            "//div[contains(@class,'menuable__content__active')]"),
    AD_SIZE_CHECKBOX("Category Checkbox", "//div[contains(@class,'menuable__content__active')]" +
            "//div[@class='v-list-item__title' and text()='%s']/../../div[@class='v-list-item__action']" +
            "/div[@class='v-simple-checkbox']/div");


    private String alias;
    private String selector;
}
