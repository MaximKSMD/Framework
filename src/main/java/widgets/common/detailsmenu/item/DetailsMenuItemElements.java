package widgets.common.detailsmenu.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DetailsMenuItemElements {

    NAME( "'Name' Label", "//span"),
    EXCLUDED_ICON( "'Excluded' Icon", "//i[contains(@style,'red')]"),
    INCLUDED_ICON( "'Included' Icon", "//i[contains(@style,'green')]");

    private String alias;
    private String selector;
}
