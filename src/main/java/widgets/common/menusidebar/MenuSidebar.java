package widgets.common.menusidebar;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$x;
import static widgets.common.menusidebar.MenuSidebarElements.*;


/**
 * Keep Selectors of UI elements in {@link MenuSidebarElements}
 */
@Getter
public class MenuSidebar {

    private SelenideElement profileAvatar = $x(PROFILE_AVATAR.getSelector()).as(PROFILE_AVATAR.getAlias());

    public SelenideElement getSidebarOptionByValue(MenuSidebarOptions option) {

        return $x(String.format(SIDEBAR_OPTION_BY_VALUE.getSelector(), option.getText()))
                .as(String.format(SIDEBAR_OPTION_BY_VALUE.getAlias(), option.getText()));
    }


}
