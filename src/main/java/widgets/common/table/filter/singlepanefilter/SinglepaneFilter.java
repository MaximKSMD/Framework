package widgets.common.table.filter.singlepanefilter;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.AccessLevel;
import lombok.Getter;
import org.openqa.selenium.WebElement;
import widgets.common.table.ColumnNames;
import widgets.common.table.filter.abstractt.BaseFilter;
import widgets.common.table.filter.singlepanefilter.item.SinglepaneItem;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static java.lang.String.format;
import static widgets.admin.users.sidebar.AbstractUserSidebarElements.ERROR_ALERT_BY_FIELD_NAME;
import static widgets.common.table.filter.singlepanefilter.SinglepaneFilterElements.*;


/**
 * Keep Selectors of UI elements in {@link SinglepaneFilterElements}
 */

@Getter
public class SinglepaneFilter extends BaseFilter {

    protected String singlepaneName;

    private SelenideElement searchIcon = $x(SEARCH_ICON.getSelector()).as(SEARCH_ICON.getAlias());
    private SelenideElement searchInput = $x(SEARCH_INPUT.getSelector()).as(SEARCH_INPUT.getAlias());
    private SelenideElement clearAllButton = $x(CLEAR_ALL_BUTTON.getSelector()).as(CLEAR_ALL_BUTTON.getAlias());
    private SelenideElement includeAllButton = $x(INCLUDE_ALL_BUTTON.getSelector()).as(INCLUDE_ALL_BUTTON.getAlias());
    private SelenideElement itemsTotalQuantityLabel = $x(TOTAL_ITEMS_QUANTITY_LABEL.getSelector()).as(TOTAL_ITEMS_QUANTITY_LABEL.getAlias());
    private SelenideElement itemsIncludedQuantityLabel = $x(INCLUDED_ITEMS_QUANTITY_LABEL.getSelector()).as(INCLUDED_ITEMS_QUANTITY_LABEL.getAlias());

    @Getter(AccessLevel.NONE)
    private ElementsCollection filterItems = $$x(FILTER_ITEMS.getSelector()).as(FILTER_ITEMS.getAlias());

    @Getter(AccessLevel.NONE)
    private List<SinglepaneItem> filterItemsList = new ArrayList<>();

    public SinglepaneFilter() {
        this.singlepaneName = ColumnNames.NAME.getName();
    }

    public SinglepaneItem getFilterItemByPositionInList(int position) {
        addIncludeFilterItemsToList();

        return filterItemsList.get(position);
    }

    public int countIncludedItems() {
        int count = 0;

        count = (int) filterItems.shouldBe(CollectionCondition.allMatch("Wait to collection element to be visible",
                        WebElement::isDisplayed))
                .stream()
                .map(se -> se.shouldBe(exist).scrollTo().shouldBe(visible))
                .count();

        return count;
    }

    private void addIncludeFilterItemsToList() {
        countIncludedItems();
        var position = new AtomicInteger(1);
        if (filterItemsList.size() != 0) {
            filterItemsList.clear();
        }
        filterItemsList.addAll(filterItems.stream()
                .map(e -> new SinglepaneItem(position.getAndIncrement()))
                .collect(Collectors.toList()));
    }

    public SinglepaneItem getFilterItemByName(String filterItemName) {
        addIncludeFilterItemsToList();

        return filterItemsList.stream()
                .filter(pub -> pub.getName().shouldBe(visible).getText()
                        .equalsIgnoreCase(filterItemName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        format("The FilterItem with name '%s' isn't presented in Filter Table of Singlepane",
                                filterItemName)));
    }
}
