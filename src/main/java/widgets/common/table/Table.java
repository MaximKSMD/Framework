package widgets.common.table;

import lombok.Getter;
import widgets.common.table.filter.ColumnFiltersBlock;
import widgets.common.table.filter.platformfilter.PlatformFilter;

@Getter
public class Table {
    TableData tableData = new TableData();
    ShowHideColumns showHideColumns = new ShowHideColumns();
    TablePagination tablePagination = new TablePagination();
    ColumnFiltersBlock columnFiltersBlock = new ColumnFiltersBlock();
}
