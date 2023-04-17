package widgets.common.table.filter.chip;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChipFilterOptionsItemElements {

    CLOSE_ICON("Close Icon","span/button"),
    HEADER_LABEL("Header Label","span"),
    LIST_ITEMS("Chip's Items","descendant::span[@class='chip-list-container']" +
            "/span[contains(@class,'truncate')]/span[@class='v-chip__content']");

    private String alias;
    private String selector;
}
