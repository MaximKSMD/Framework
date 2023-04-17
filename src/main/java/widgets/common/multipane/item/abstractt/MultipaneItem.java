package widgets.common.multipane.item.abstractt;

import com.codeborne.selenide.SelenideElement;
import lombok.AccessLevel;
import lombok.Getter;
import widgets.common.multipane.MultipaneName;
import widgets.common.multipane.MultipaneNameImpl;

import static com.codeborne.selenide.Selenide.$x;
import static java.lang.String.format;
import static widgets.common.multipane.item.abstractt.MultipaneItemElements.*;

/**
 * Keep Selectors of UI elements in {@link MultipaneItemElements}
 * 'type' field is presented only for INVENTORY multipane
 * 'name' field is presented all multipane components,
 *  but has unique xpath selector for DEMAND_SOURCES multipane
 */
@Getter
public abstract class MultipaneItem {

    protected SelenideElement name;
    protected SelenideElement type;
    private SelenideElement associatedWithPublisherIcon;

    @Getter(AccessLevel.NONE)
    private int position;
    @Getter(AccessLevel.NONE)
    private String multipaneName;
    @Getter(AccessLevel.NONE)
    protected String multipaneItem;

    public MultipaneItem(int position, String  multipaneItem, MultipaneName multipaneName) {
        this.position = position;
        this.multipaneItem = multipaneItem;
        this.multipaneName = multipaneName.getName();
        this.associatedWithPublisherIcon = $x(buildXpath(ASSOCIATED_WITH_PUBLISHER_ICON.getSelector()))
                .as(format("%s%s", ASSOCIATED_WITH_PUBLISHER_ICON.getAlias(), position));

        switch ((MultipaneNameImpl) multipaneName) {
            case INVENTORY:
                this.name = $x(buildXpath(INVENTORY_NAME.getSelector())).as(format("%s%s", INVENTORY_NAME.getAlias(), position));
                this.type = $x(buildXpath(INVENTORY_TYPE.getSelector())).as(format("%s%s", INVENTORY_TYPE.getAlias(), position));
                break;
            case DEMAND_SOURCES:
                this.name = $x(buildXpath(DEMAND_SOURCES_NAME.getSelector())).as(format("%s%s", DEMAND_SOURCES_NAME.getAlias(), position));
                break;

            default:
                this.name = $x(buildXpath(NAME.getSelector())).as(format("%s%s", NAME.getAlias(), position));
        }
    }

    protected String buildXpath(String elementXpath) {

        return format("%s[%s]%s",
                format("//descendant::%s",
                        format(multipaneItem, multipaneName)),
                position,
                elementXpath);
    }
}
