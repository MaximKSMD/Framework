package widgets.common.categories;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.AccessLevel;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static widgets.common.categories.CategoriesListPanelElements.*;


/**
 * Keep Selectors of UI elements in {@link CategoriesListPanelElements}
 */
@Getter
public class CategoriesListPanel {

    @Getter(AccessLevel.NONE)
    private SelenideElement categoryRow = $x(CATEGORY_ROW.getSelector()).as(CATEGORY_ROW.getAlias());
    private ElementsCollection categoriesSelectedItems = $$x(CATEGORIES_SELECTED_ITEMS.getSelector())
            .as(CATEGORIES_SELECTED_ITEMS.getAlias());
    @Getter(AccessLevel.NONE)
    private SelenideElement categorySubList = $x(CATEGORY_SUBLIST.getSelector()).as(CATEGORY_SUBLIST.getAlias());
    @Getter(AccessLevel.NONE)
    private SelenideElement categoryCheckbox = $x(CATEGORY_CHECKBOX.getSelector()).as(CATEGORY_CHECKBOX.getAlias());
    @Getter(AccessLevel.NONE)
    private SelenideElement categoryGroupIcon = $x(CATEGORY_GROUP_ICON.getSelector()).as(CATEGORY_GROUP_ICON.getAlias());

    public SelenideElement getCategoryGroupIcon(CategoriesList category) {

        return $x(String.format(CATEGORY_GROUP_ICON.getSelector(), category.getName())).as(CATEGORY_GROUP_ICON.getAlias());
    }

    public SelenideElement getCategoryRow(CategoriesList category) {

        return $x(String.format(CATEGORY_ROW.getSelector(), category.getName())).as(CATEGORY_ROW.getAlias());
    }

    public SelenideElement getCategorySublist(CategoriesList category) {

        return $x(String.format(CATEGORY_SUBLIST.getSelector(), category.getName())).as(CATEGORY_SUBLIST.getAlias());
    }

    public SelenideElement getCategoryCheckbox(CategoriesList category) {

        return $x(String.format(CATEGORY_CHECKBOX.getSelector(), category.getName())).as(CATEGORY_CHECKBOX.getAlias());
    }
}