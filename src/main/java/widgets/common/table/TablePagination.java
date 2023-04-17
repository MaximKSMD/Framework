package widgets.common.table;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static widgets.common.table.TablePaginationElements.*;

/**
 * Keep Selectors of UI elements in {@link TablePaginationElements}
 */
@Getter
public class TablePagination {

    private final SelenideElement next = $x(NEXT.getSelector()).as(NEXT.getAlias());
    private final SelenideElement previous = $x(PREVIOUS.getSelector()).as(PREVIOUS.getAlias());
    private final SelenideElement pageMenu = $x(PAGE_MENU.getSelector()).as(PAGE_MENU.getAlias());
    private final SelenideElement paginationPanel = $x(PAGINATION_PANEL.getSelector()).as(PAGINATION_PANEL.getAlias());
    private final ElementsCollection rowNumbersList = $$x(ROW_NUMBERS_LIST.getSelector()).as(ROW_NUMBERS_LIST.getAlias());

}