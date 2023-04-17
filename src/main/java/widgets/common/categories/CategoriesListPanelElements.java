package widgets.common.categories;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoriesListPanelElements {

    CATEGORIES_SELECTED_ITEMS("Categories Selected Items",
            "//label[text()='Categories']/..//span[@class='v-chip__content']"),
    CATEGORY_ROW("Category Row", "//div[@class='v-list-item__content' and text()='%s']"),
    CATEGORY_SUBLIST("Category Sub-list", "//div[@class='v-list-item__content' and text()='%s']/.."),
    CATEGORY_CHECKBOX("Category Checkbox", "//div[@class='v-list-item__content' and text()='%s']/.." +
            "/div[@class='v-list-item__action']//input[@role='checkbox']/.."),
    CATEGORY_GROUP_ICON("Category Group Icon", "//div[@class='v-list-item__content' and text()='%s']/.." +
            "/div[contains(@class,'v-list-group')]");


    private String alias;
    private String selector;
}
