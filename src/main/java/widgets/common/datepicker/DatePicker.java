package widgets.common.datepicker;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$x;
import static widgets.common.datepicker.DatePickerElements.*;

/**
 * Keep Selectors of UI elements in {@link DatePickerElements}
 */
@Getter
public class DatePicker {

    private SelenideElement dateRangeInput = $x(DATE_RANGE_INPUT.getSelector()).as(DATE_RANGE_INPUT.getAlias());
    private SelenideElement nextYearButton = $x(NEXT_YEAR_BUTTON.getSelector()).as(NEXT_YEAR_BUTTON.getAlias());
    private SelenideElement nextMonthButton = $x(NEXT_MONTH_BUTTON.getSelector()).as(NEXT_MONTH_BUTTON.getAlias());
    private SelenideElement selectedDayButton = $x(SELECTED_DAY_BUTTON.getSelector()).as(SELECTED_DAY_BUTTON.getAlias());
    private SelenideElement previousYearButton = $x(PREVIOUS_YEAR_BUTTON.getSelector()).as(PREVIOUS_YEAR_BUTTON.getAlias());
    private SelenideElement previousMonthButton = $x(PREVIOUS_MONTH_BUTTON.getSelector()).as(PREVIOUS_MONTH_BUTTON.getAlias());
    private SelenideElement monthOrYearHeaderButton = $x(MONTH_YEAR_HEADER_BUTTON.getSelector()).as(MONTH_YEAR_HEADER_BUTTON.getAlias());

    public SelenideElement getYearButtonByValue(String year) {

        return $x(String.format(YEAR_BUTTON_BY_VALUE.getSelector(), year)).as(String.format(YEAR_BUTTON_BY_VALUE.getAlias(), year));

    }

    public SelenideElement getDayButtonByValue(String day) {

        return $x(String.format(DAY_BUTTON_BY_VALUE.getSelector(), day)).as(String.format(DAY_BUTTON_BY_VALUE.getAlias(), day));

    }

    public SelenideElement getMonthButtonByValue(String month) {

        return $x(String.format(MONTH_BUTTON_BY_VALUE.getSelector(), month)).as(String.format(MONTH_BUTTON_BY_VALUE.getAlias(), month));

    }
}
