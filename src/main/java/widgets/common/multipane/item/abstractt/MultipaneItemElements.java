package widgets.common.multipane.item.abstractt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MultipaneItemElements {

    NAME("'Name' Label of row %s in table list in Multipane", "//td/div"),
    DEMAND_SOURCES_NAME("'Name' Label of row %s in table list in Multipane", "//td//span[2]"),
    INVENTORY_NAME("'Name' Label of row %s in table list in Multipane", "//td/div[not(contains(@title,'>'))]"),
    INVENTORY_TYPE("'Type' Label of row %s in table list in Multipane", "//td[not(contains(@class,'first')) and not(contains(@style,'70'))]/div"),
    ASSOCIATED_WITH_PUBLISHER_ICON("'Associated with publisher' icon of row %s in table list in Multipane", "/tr/td//i[@title='Associated with publisher']");

    private String alias;
    private String selector;
}
