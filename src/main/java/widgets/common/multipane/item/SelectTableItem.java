package widgets.common.multipane.item;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import widgets.common.multipane.MultipaneName;
import widgets.common.multipane.item.common.CommonSelectTableItem;

import static com.codeborne.selenide.Selenide.$x;
import static java.lang.String.format;
import static widgets.common.multipane.item.SelectTableItemElements.*;

/**
 * Keep Selectors of UI elements in {@link SelectTableItemElements}
 * 'activeIcon' , 'inactiveIcon', 'associatedWithPublisherIcon' fields are
 * available only for DEMAND_SOURCES Multipane
 */
@Getter
public class SelectTableItem extends CommonSelectTableItem {

    private SelenideElement nestedItemNameLabel;
    private SelenideElement expandInnerItemButton;

    private static final String SELECT_TABLE_ITEM = "h3[text()='%s']/../..//table[contains(@class,'select-table')]/tbody";
    //descendant::h3[text()='%s']/../..//table[contains(@class,'select-table')]/tbody

    public SelectTableItem(int position, MultipaneName multipaneNameImpl) {
        super(position, SELECT_TABLE_ITEM, multipaneNameImpl);

        this.nestedItemNameLabel = $x(NESTED_ITEM_NAME_LABEL.getSelector()).as(format("%s%s", NESTED_ITEM_NAME_LABEL.getAlias(), position));
        this.expandInnerItemButton = $x(EXPAND_INNER_ITEM_ICON.getSelector()).as(format("%s%s", EXPAND_INNER_ITEM_ICON.getAlias(), position));
    }
}
