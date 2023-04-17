package widgets.common.table.filter;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import widgets.common.table.ColumnNames;
import widgets.common.table.filter.activebooleanfilter.ActiveBooleanFilter;
import widgets.common.table.filter.booleanfilter.BooleanFilter;
import widgets.common.table.filter.calendarFilter.CalendarFilter;
import widgets.common.table.filter.enablebooleanfilter.EnableBooleanFilter;
import widgets.common.table.filter.platformfilter.PlatformFilter;
import widgets.common.table.filter.rolefilter.RoleFilter;
import widgets.common.table.filter.singlepanefilter.SinglepaneFilter;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static widgets.common.table.filter.FilterOptionsElements.*;

/**
 * Keep Selectors of UI elements in {@link FilterOptionsElements}
 */
@Getter
public class ColumnFiltersBlock {

    private SelenideElement filterOptionsMenu = $x(FILTER_OPTIONS_MENU.getSelector()).as(FILTER_OPTIONS_MENU.getAlias());
    private ElementsCollection filterOptionItems = $$x(FILTER_OPTIONS_ITEMS.getSelector()).as(FILTER_OPTIONS_ITEMS.getAlias());
    private SelenideElement columnsFilterButton = $x(COLUMNS_FILTER_BUTTON.getSelector()).as(COLUMNS_FILTER_BUTTON.getAlias());
    private SinglepaneFilter singlepaneFilter = new SinglepaneFilter();

    RoleFilter roleFilter = new RoleFilter();
    BooleanFilter booleanFilter = new BooleanFilter();
    CalendarFilter calendarFilter = new CalendarFilter();
    PlatformFilter platformFilter = new PlatformFilter();
    ActiveBooleanFilter activeBooleanFilter = new ActiveBooleanFilter();
    EnableBooleanFilter enableBooleanFilter = new EnableBooleanFilter();

    public SelenideElement getFilterOptionByName(ColumnNames column) {

        return $x(String.format(FILTER_OPTION_BY_NAME.getSelector(), column.getName()))
                .as(String.format(FILTER_OPTION_BY_NAME.getAlias(), column.getName()));
    }
}