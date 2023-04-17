package widgets.common.detailsmenu.menu.sections;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DetailsSectionElements {

    DETAILS_MENU_ITEMS( "Details Menu '%s' Items",
            "//div[contains(@class,'active') and @role]//div[contains(text(),'%s')]/..//span");

    private String alias;
    private String selector;
}
