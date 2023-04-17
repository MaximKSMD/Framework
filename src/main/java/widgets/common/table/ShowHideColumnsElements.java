package widgets.common.table;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ShowHideColumnsElements {

    MENU_ITEM_CHECKBOX( "Menu Item Checkbox", "//label[text()='%s']/..//i"),
    CHECKBOX_PARENT("Checkbox Parent Element", "//label[text()='%s']/../div"),
    TABLE_HEADER_CHECKBOX( "Table Item Checkbox", "//span[text()='%s']/../..//i"),
    TABLE_OPTIONS_ELEMENTS( "'Table Options' Elements", "//div[@role='menuitem']"),
    TABLE_OPTIONS_MENU( "Table Options Menu", "//*[@class='v-list v-sheet theme--light']"),
    OPTIONS_LIST( "'Table Options' List", "//*[@class='v-menu__content theme--light menuable__content__active']"),
    SHOW_HIDE_COLUMNS_COMPONENTS_BUTTON( "'Show/Hide Columns' button", "//button/span[contains(text(),'Show/Hide Columns')]/.."),
    ITEM_STATUS_RADIO( "Menu item 'Status' RadioButton", "//div[@role='radiogroup']//label[text()='%s']/..//input"),
    MENU_ITEM( "Menu item", "//*[@class='v-list v-sheet theme--light']//*[@class='v-input__slot']/label[text()='%s']/../div");

    private String alias;
    private String selector;

}
