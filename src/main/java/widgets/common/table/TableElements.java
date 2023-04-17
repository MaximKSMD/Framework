package widgets.common.table;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TableElements {

    ROWS( "Table Rows", "//tbody/tr"),
    BODY( "Table Body", "//body//table[1]"),
    BUTTON_SUFFIX("Button Suffix","/button"),
    COLUMNS( "Table Columns", "//thead/tr/th/span"),
    CELL_BY_COLUMN( "Cell By Column", "//tbody/tr/td[%s]"),
    CELL_BY_COLUMN_ROW( "Cell By Column", "//tbody/tr[%s]/td[%s]"),
    CHECKBOX( "Row Checkbox", "//table//tr[%s]/td[1]/div/div[1]/i"),
    CLEAR( "Clear Icon", "//*[@class='v-input__icon v-input__icon--clear']/button"),
    CLEAR_ALL_BUTTON("'Clear All' Button", "//div[contains(text(),'Clear All')]/../.."),
    FILTER_CHIPS("Filter chips toolbar","//div[contains(@class,'chip-container')]/span"),
    SEARCH( "Search Textbox", "//*[@class='v-text-field__slot']/label[text()='Search by name']/../input"),

    COLUMN_HEADER( "Column Header", "//div[@class='v-data-table__wrapper']//thead//th/span[text()='%s']/parent::th");

    private String alias;
    private String selector;
}
