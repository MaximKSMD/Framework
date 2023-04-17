
package widgets.common.menusidebar;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MenuSidebarElements {

    PROFILE_AVATAR("'Profile Avatar' icon in Sidebar", "//div[contains(@class,'avatar')]"),
    SIDEBAR_OPTION_BY_VALUE("'%s' Month in Date Picker", "//div[contains(@class,'navigation')]//div[text()='%s']");

    private String alias;
    private String selector;
}
