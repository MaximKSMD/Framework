package widgets.common.table.filter.singlepanefilter.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SinglepaneItemElements {

    ID("'ID' Label of row %s in table list in Singlepane", "//td[%s]/div"),
    NAME("'Name' Label of row %s in table list in Singlepane", "//td[%s]/div"),
    INCLUDED_ICON("'Include' Label of row %s in table list in Singlepane",
            "//tr[@class='select-row']/td[%s][contains(@class,'included')]"),
    INCLUDE_BUTTON("'Include' Button of row %s in table list in Singlepane",
            "//tr[@class='select-row']/td[%s]/div[contains(@class,'include')]/button[2]");

    private String alias;
    private String selector;
}
