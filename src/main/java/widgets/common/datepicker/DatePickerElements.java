
package widgets.common.datepicker;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DatePickerElements {

    YEAR_BUTTON_BY_VALUE("'%s' Year in Date Picker", "//ul/li[text()='%s']"),
    DAY_BUTTON_BY_VALUE("'%s' Day in Date Picker", "//table//button/div[text()='%s']"),
    DATE_RANGE_INPUT("'Date Range' Input Field", "//label[text()='Date Range']/../input"),
    MONTH_BUTTON_BY_VALUE("'%s' Month in Date Picker", "//table//button/div[text()='%s']"),
    NEXT_YEAR_BUTTON("'Next Year' button in Date Picker","//button[@aria-label='Next year']"),
    NEXT_MONTH_BUTTON("'Next Month' button in Date Picker","//button[@aria-label='Next month']"),
    SELECTED_DAY_BUTTON("Selected Day in Date Picker", "//table//button[contains(@class,'accent')]/div"),
    PREVIOUS_YEAR_BUTTON("'Previous year' button in Date Picker","//button[@aria-label='Previous year']"),
    PREVIOUS_MONTH_BUTTON("'Previous Month' button in Date Picker","//button[@aria-label='Previous month']"),
    MONTH_YEAR_HEADER_BUTTON("'Month-Year' Header Button in Date Picker", "//div[@class='v-date-picker-header__value']//button");

    private String alias;
    private String selector;
}
