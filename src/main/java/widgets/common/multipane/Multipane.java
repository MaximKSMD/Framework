package widgets.common.multipane;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.AccessLevel;
import lombok.Getter;
import org.openqa.selenium.WebElement;
import widgets.common.multipane.item.SelectChildTableItem;
import widgets.common.multipane.item.SelectTableItem;
import widgets.common.multipane.item.included.IncludedTableItem;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static widgets.common.multipane.MultipaneElements.*;

/**
 * Keep Selectors of UI elements in {@link MultipaneElements}
 */
@Getter
public class Multipane {

    protected MultipaneName multipaneName;

    private SelenideElement searchInput;
    private SelenideElement excludedIcon;
    private SelenideElement includedIcon;
    private SelenideElement showInactive;
    private SelenideElement excludedBanner;
    private SelenideElement includedBanner;
    private SelenideElement panelNameLabel;
    private SelenideElement clearAllButton;
    private SelenideElement clearSearchIcon;
    private SelenideElement includeAllButton;
    private SelenideElement selectionInfoLabel;
    private SelenideElement selectionInfoExcludedLabel;
    private SelenideElement itemsQuantityString;

    private static final String MULTIPANE_ROOT = "//h3[text()='%s']/../..";
    //h3[text()='%s']/../..

    @Getter(AccessLevel.NONE)
    private ElementsCollection selectTableItems;
    @Getter(AccessLevel.NONE)
    private ElementsCollection selectChildTableItems;
    @Getter(AccessLevel.NONE)
    private ElementsCollection includedExcludedTableItems;
    @Getter(AccessLevel.NONE)
    private List<SelectTableItem> selectTableItemsList = new ArrayList<>();
    @Getter(AccessLevel.NONE)
    private List<IncludedTableItem> includedExcludedItemsList = new ArrayList<>();
    @Getter(AccessLevel.NONE)
    private List<SelectChildTableItem> selectTableChildItemsList = new ArrayList<>();

    public Multipane(MultipaneName multipaneName) {
        this.multipaneName = multipaneName;

        this.searchInput = $x(buildXpath(SEARCH_INPUT.getSelector())).as(SEARCH_INPUT.getAlias());
        this.excludedIcon = $x(buildXpath(EXCLUDED_ICON.getSelector())).as(EXCLUDED_ICON.getAlias());
        this.includedIcon = $x(buildXpath(INCLUDED_ICON.getSelector())).as(INCLUDED_ICON.getAlias());
        this.excludedBanner = $x(buildXpath(EXCLUDED_BANNER.getSelector())).as(EXCLUDED_BANNER.getAlias());
        this.includedBanner = $x(buildXpath(INCLUDED_BANNER.getSelector())).as(INCLUDED_BANNER.getAlias());
        this.panelNameLabel = $x(buildXpath(PANEL_NAME_LABEL.getSelector())).as(PANEL_NAME_LABEL.getAlias());
        this.clearAllButton = $x(buildXpath(CLEAR_ALL_BUTTON.getSelector())).as(CLEAR_ALL_BUTTON.getAlias());
        this.clearSearchIcon = $x(buildXpath(CLEAR_SEARCH_ICON.getSelector())).as(CLEAR_SEARCH_ICON.getAlias());
        this.includeAllButton = $x(buildXpath(INCLUDE_ALL_BUTTON.getSelector())).as(INCLUDE_ALL_BUTTON.getAlias());
        this.showInactive = $x(buildXpath(SHOW_INACTIVE_TOGGLE.getSelector())).as(SHOW_INACTIVE_TOGGLE.getAlias());
        this.selectionInfoLabel = $x(buildXpath(SELECTION_INFO_LABEL.getSelector())).as(SELECTION_INFO_LABEL.getAlias());
        this.itemsQuantityString = $x(buildXpath(ITEMS_QUANTITY_STRING.getSelector())).as(ITEMS_QUANTITY_STRING.getAlias());
        this.selectionInfoExcludedLabel = $x(buildXpath(SELECTION_INFO_EXCLUDED_LABEL.getSelector())).as(SELECTION_INFO_EXCLUDED_LABEL.getAlias());

        this.selectTableItems = $$x(buildXpath(SELECT_TABLE_ITEMS.getSelector()));
        this.selectChildTableItems = $$x(buildXpath(SELECT_CHILD_TABLE_ITEMS.getSelector()));
        this.includedExcludedTableItems = $$x(buildXpath(INCLUDED_EXCLUDED_TABLE_ITEMS.getSelector()));
    }

    /**
     * TableItems Section
     */

    public int countSelectTableItems() {
        panelNameLabel.shouldHave(attribute("aria-expanded", "true"))
                .scrollIntoView(true);

        int count = 0;

        if (selectTableItems.size()>0)
            count = (int)selectTableItems.shouldBe(CollectionCondition.allMatch("Wait to collection element to be visible",
                        WebElement::isDisplayed))
                .stream()
                .map(se -> se.shouldBe(exist).scrollTo().shouldBe(visible))
                .count();

        return count;
    }

    private void addSelectTableItemsToList() {
        countSelectTableItems();
        var position = new AtomicInteger(1);
        if (selectTableItemsList.size() != 0) {
            selectTableItemsList.clear();
        }
        selectTableItemsList.addAll(selectTableItems.stream()
                .map(buyersCard -> new SelectTableItem(position.getAndIncrement(), multipaneName))
                .collect(Collectors.toList()));
    }

    public SelectTableItem getSelectTableItemByPositionInList(int position) {
        addSelectTableItemsToList();

        return selectTableItemsList.get(position);
    }

    public SelectTableItem getSelectTableItemByName(String selectItemName) {
        addSelectTableItemsToList();

        return selectTableItemsList.stream()
                .filter(pub -> pub.getName().shouldBe(visible).getText()
                        .equalsIgnoreCase(selectItemName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        format("The SelectTableItem with name '%s' isn't presented in Select Table of Multipane",
                                selectItemName)));
    }

    /**
     * End of TableItems Section
     * <p>
     * TableChildItems Section
     */

    public int countSelectChildTableItems() {
        panelNameLabel.shouldHave(attribute("aria-expanded", "true"))
                .scrollIntoView(true);

        int count = 0;

        if (selectChildTableItems.size() > 0)
            count = (int) selectChildTableItems.shouldBe(CollectionCondition.allMatch("Wait to collection element to be visible",
                            WebElement::isDisplayed))
                    .stream()
                    .map(se -> se.shouldBe(exist).scrollTo().shouldBe(visible))

                    .count();

        return count;

    }

    private void addSelectChildTableItemsToList() {
        countSelectChildTableItems();
        var position = new AtomicInteger(1);
        if (selectTableChildItemsList.size() != 0) {
            selectTableChildItemsList.clear();
        }
        selectTableChildItemsList.addAll(selectChildTableItems.stream()
                .map(buyersCard -> new SelectChildTableItem(position.getAndIncrement(), multipaneName))
                .collect(Collectors.toList()));
    }

    public SelectChildTableItem getSelectChildTableItemByPositionInList(int position) {
        addSelectChildTableItemsToList();

        return selectTableChildItemsList.get(position);
    }

    public SelectChildTableItem getSelectChildTableItemByName(String selectItemName) {
        addSelectChildTableItemsToList();

        return selectTableChildItemsList.stream()
                .filter(pub -> requireNonNull(pub.getName().shouldBe(visible).getText())
                        .equalsIgnoreCase(selectItemName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        format("The SelectChildTableItem with name '%s' isn't presented in Select Table of Multipane",
                                selectItemName)));
    }

    /**
     * End of TableChildItems Section
     * <p>
     * <p>
     * IncludedExcludedTableItems Section
     */

    public int countIncludedExcludedTableItems() {
        panelNameLabel.shouldHave(attribute("aria-expanded", "true"))
                .scrollIntoView(true);

        int count = 0;

        if (includedExcludedTableItems.size() > 0)
            count = (int) includedExcludedTableItems.shouldBe(CollectionCondition.allMatch("Wait to collection element to be visible",
                            WebElement::isDisplayed))
                    .stream()
                    .map(se -> se.shouldBe(exist).scrollTo().shouldBe(visible))
                    .count();

        return count;
    }

    private void addIncludedExcludedTableItemsToList() {
        countIncludedExcludedTableItems();
        var position = new AtomicInteger(1);
        if (includedExcludedItemsList.size() != 0) {
            includedExcludedItemsList.clear();
        }
        includedExcludedItemsList.addAll(includedExcludedTableItems.stream()
                .map(buyersCard -> new IncludedTableItem(position.getAndIncrement(), multipaneName))
                .collect(Collectors.toList()));
    }

    public IncludedTableItem getIncludedExcludedTableItemByPositionInList(int position) {
        addIncludedExcludedTableItemsToList();

        return includedExcludedItemsList.get(position);
    }

    public IncludedTableItem getIncludedExcludedTableItemByName(String selectItemName) {
        addIncludedExcludedTableItemsToList();

        return includedExcludedItemsList.stream()
                .filter(pub -> requireNonNull(pub.getName().shouldBe(visible).getText())
                        .equalsIgnoreCase(selectItemName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        format("The IncludedExcludedTableItem with name '%s' isn't presented in Select Table of Multipane",
                                selectItemName)));
    }

    /**
     * End of IncludedExcludedTableItems Section
     */

    protected String buildXpath(String elementXpath) {

        return (format("%s%s", format(MULTIPANE_ROOT, multipaneName.getName()), elementXpath));
    }
}