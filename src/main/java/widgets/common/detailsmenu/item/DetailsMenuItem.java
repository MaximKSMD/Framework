package widgets.common.detailsmenu.item;

import com.codeborne.selenide.SelenideElement;
import lombok.AccessLevel;
import lombok.Getter;
import widgets.common.detailsmenu.DetailsSectionName;

import static com.codeborne.selenide.Selenide.$x;
import static java.lang.String.format;
import static widgets.common.detailsmenu.item.DetailsMenuItemElements.*;

/**
 * Keep Selectors of UI elements in {@link DetailsMenuItemElements}
 */
@Getter
public class DetailsMenuItem {

    @Getter(AccessLevel.NONE)
    private int position;
    @Getter(AccessLevel.NONE)
    private String detailsSectionName;

    private SelenideElement name;
    private SelenideElement includedIcon;
    private SelenideElement excludedIcon;

    private static final String DETAILS_MENU_ITEM =
            "div[contains(@class,'active') and @role]//div[contains(text(),'%s')]/..//div/div";
    //descendant::div[contains(@class,'active') and @role]//div[contains(text(),'Inventory')]/..//div/div

    public DetailsMenuItem(int position, DetailsSectionName detailsSectionName) {
        this.position = position;
        this.detailsSectionName = detailsSectionName.getName();

        this.name = $x(buildXpath(NAME.getSelector())).as(NAME.getAlias());
        this.includedIcon = $x(buildXpath(INCLUDED_ICON.getSelector())).as(INCLUDED_ICON.getAlias());
        this.excludedIcon = $x(buildXpath(EXCLUDED_ICON.getSelector())).as(EXCLUDED_ICON.getAlias());
    }

    public String buildXpath(String elementXpath) {

        return format(format("//descendant::%s[%s]%s", DETAILS_MENU_ITEM, position, elementXpath), detailsSectionName);
    }
}
