package widgets.common.table;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static widgets.common.table.ShowHideColumnsElements.*;
import static widgets.common.table.ShowHideColumnsElements.TABLE_HEADER_CHECKBOX;

/**
 * Keep Selectors of UI elements in {@link ShowHideColumnsElements}
 */
@Getter
public class ShowHideColumns {

    private SelenideElement optionsList = $x(OPTIONS_LIST.getSelector()).as(OPTIONS_LIST.getAlias());
    private SelenideElement menuOptions = $x(TABLE_OPTIONS_MENU.getSelector()).as(TABLE_OPTIONS_MENU.getAlias());
    private ElementsCollection optionItems = $$x(TABLE_OPTIONS_ELEMENTS.getSelector()).as(TABLE_OPTIONS_ELEMENTS.getAlias());
    private SelenideElement showHideColumnsBtn = $x(SHOW_HIDE_COLUMNS_COMPONENTS_BUTTON.getSelector()).as(SHOW_HIDE_COLUMNS_COMPONENTS_BUTTON.getAlias());

    public SelenideElement getMenuItemCheckbox(ColumnNames column) {

        return $x(String.format(MENU_ITEM_CHECKBOX.getSelector(), column.getName())).as(MENU_ITEM_CHECKBOX.getAlias());
    }

    public SelenideElement getTableHeaderCheckbox(ColumnNames column) {

        return $x(String.format(TABLE_HEADER_CHECKBOX.getSelector(), column.getName())).as(TABLE_HEADER_CHECKBOX.getAlias());
    }

    public SelenideElement getStatusItemRadio(Statuses status) {

        return $x(String.format(ITEM_STATUS_RADIO.getSelector(), status.getStatus())).as(ITEM_STATUS_RADIO.getAlias());
    }

}