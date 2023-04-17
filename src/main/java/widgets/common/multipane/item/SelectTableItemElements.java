package widgets.common.multipane.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SelectTableItemElements {

    EXPAND_INNER_ITEM_ICON("'Expand Inner Item' Icon of row %s in table list in Multipane", "//td/i"),
    NESTED_ITEM_NAME_LABEL("'Nested Item Name' Label of row %s in table list in Multipane", "//td[2]/div[contains(@title,'>')]");

    private String alias;
    private String selector;
}
