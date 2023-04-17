package widgets.common.table;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.AccessLevel;
import lombok.Getter;
import widgets.common.table.filter.chip.ChipFilterOptionsItem;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static java.lang.String.format;
import static widgets.common.table.ColumnNames.DETAILS;
import static widgets.common.table.TableElements.*;

/**
 * Keep Selectors of UI elements in {@link TableElements}
 */
@Getter
public class TableData {

    private final SelenideElement clear = $x(CLEAR.getSelector()).as(CLEAR.getAlias());
    private final ElementsCollection rows = $$x(ROWS.getSelector()).as(ROWS.getAlias());
    private final SelenideElement tableBody = $x(BODY.getSelector()).as(BODY.getAlias());
    private final SelenideElement search = $x(SEARCH.getSelector()).as(SEARCH.getAlias());
    private final ElementsCollection columns = $$x(COLUMNS.getSelector()).as(COLUMNS.getAlias());
    @Getter(AccessLevel.NONE)
    private final ElementsCollection filterChips = $$x(FILTER_CHIPS.getSelector()).as(FILTER_CHIPS.getAlias());
    private final ElementsCollection cellByColumn = $$x(CELL_BY_COLUMN.getSelector()).as(CELL_BY_COLUMN.getAlias());
    private final SelenideElement clearAllButton = $x(CLEAR_ALL_BUTTON.getSelector()).as(CLEAR_ALL_BUTTON.getAlias());
    @Getter(AccessLevel.NONE)
    private final List<ChipFilterOptionsItem> filterChipsList = new ArrayList<>();

    public SelenideElement getCheckbox(int row) {

        return $x(String.format(CHECKBOX.getSelector(), row)).as(CHECKBOX.getAlias());
    }

    public SelenideElement getColumnHeader(String columnName) {

        return $x(String.format(COLUMN_HEADER.getSelector(), columnName)).as(COLUMN_HEADER.getAlias());
    }

    public ElementsCollection getCustomCells(ColumnNames columnName) {
        int columnId = getColumnId(columnName);

        return !columnName.equals(DETAILS) ?
                $$x(String.format(CELL_BY_COLUMN.getSelector(), columnId, columnId)) :
                $$x(format("%s%s", format(CELL_BY_COLUMN.getSelector(), columnId, columnId), BUTTON_SUFFIX.getSelector()));
    }

    public SelenideElement getCellByRowValue(ColumnNames columnName, ColumnNames columnNameByRow, String rowValue) {
        int columnId = getColumnId(columnName);
        int rowNumber = getRowNumber(columnNameByRow, rowValue);

        return $x(String.format(CELL_BY_COLUMN_ROW.getSelector(), rowNumber, columnId))
                .as(CELL_BY_COLUMN_ROW.getAlias());
    }

    public SelenideElement getCellByPositionInTable(ColumnNames column, int position) {

        return getCustomCells(column).get(position);
    }

    private Integer getColumnId(ColumnNames columnName) {

        return getColumns()
                .stream()
                .map(SelenideElement::getText)
                .collect(Collectors.toList())
                .indexOf(columnName.getName()) + 2;
    }

    private Integer getRowNumber(ColumnNames columnNameByRow, String rowValue) {

        return getCustomCells(columnNameByRow)
                .stream()
                .map(SelenideElement::getText)
                .collect(Collectors.toList())
                .indexOf(rowValue) + 1;
    }

    public int countFilterChipsItems() {
        int count = 0;

        if (filterChips.size() > 0) {
            count = (int) filterChips
                    .stream()
                    .map(se -> se.shouldBe(exist, visible))
                    .count();
        }

        return count;
    }

    private void countFilterChipsItemsOnPage() {
        countFilterChipsItems();

        var position = new AtomicInteger(1);
        if (filterChipsList.size() != 0) {
            filterChipsList.clear();
        }
        filterChipsList.addAll(filterChips.stream()
                .map(chip -> new ChipFilterOptionsItem(position.getAndIncrement()))
                .collect(Collectors.toList()));
    }

    public ChipFilterOptionsItem getChipItemByPositionInList(int position) {
        countFilterChipsItemsOnPage();

        return filterChipsList.get(position);
    }

    public ChipFilterOptionsItem getChipItemByName(String name) {
        countFilterChipsItemsOnPage();

         return filterChipsList.stream()
                    .filter(chip -> chip.getHeaderLabel().shouldBe(visible).getText().contains(name))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException(
                            format("The Filter Chip with name '%s' isn't presented in the list of chips", name)));
    }
}