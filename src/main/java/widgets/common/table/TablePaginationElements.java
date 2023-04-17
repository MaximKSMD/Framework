package widgets.common.table;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TablePaginationElements {

    NEXT( "Next", "//div[@class='v-data-footer__icons-after']/button"),
    PREVIOUS( "Previous", "//div[@class='v-data-footer__icons-before']/button"),
    PAGINATION_PANEL( "Pagination Panel", "//*[@class='v-data-footer']/div[@class='v-data-footer__pagination']"),
    NUMBERS_DROPDOWN( "Dropdown Numbers", "//div[@class='v-data-footer__select']//div[@class='v-input__control']"),
    ROW_NUMBERS_LIST( "Row Numbers List", "//div[contains(@class,'menuable__content__activ')]//div[@class='v-list-item__title']" ),
    PAGE_MENU( "Page Menu", "//*[@class='v-data-footer']/div[@class='v-data-footer__select']//div[@class='v-select__selections']/div");

    private String alias;
    private String selector;
}
