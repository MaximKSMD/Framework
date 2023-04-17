package widgets.common.multipane.item.included;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import widgets.common.multipane.MultipaneName;
import widgets.common.multipane.item.abstractt.MultipaneItem;

import static com.codeborne.selenide.Selenide.$x;
import static java.lang.String.format;
import static widgets.common.multipane.item.included.IncludedTableItemElements.PARENT_LABEL;
import static widgets.common.multipane.item.included.IncludedTableItemElements.REMOVE_BUTTON;

/**
 * Keep Selectors of UI elements in {@link IncludedTableItemElements}
 */
@Getter
public class IncludedTableItem extends MultipaneItem {

    private SelenideElement parentLabel;
    private SelenideElement removeButton;

    private static final String INCLUDE_EXCLUDE_TABLE_ITEM = "h3[text()='%s']/../..//table[contains(@class,'included-table')]//tr";

    public IncludedTableItem(int position, MultipaneName multipaneName) {
        super(position, INCLUDE_EXCLUDE_TABLE_ITEM, multipaneName);

        this.parentLabel = $x(buildXpath(PARENT_LABEL.getSelector())).as(format("%s%s", PARENT_LABEL.getAlias(), position));
        this.removeButton = $x(buildXpath(REMOVE_BUTTON.getSelector())).as(format("%s%s", REMOVE_BUTTON.getAlias(), position));
    }
}
