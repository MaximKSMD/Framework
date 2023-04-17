package widgets.common.table.filter.calendarFilter;

import lombok.Getter;
import widgets.common.datepicker.DatePicker;
import widgets.common.table.filter.abstractt.BaseFilter;

@Getter
public class CalendarFilter extends BaseFilter {

    private DatePicker calendar = new DatePicker();
}
