package widgets.common.table.filter.booleanfilter;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import widgets.common.table.filter.abstractt.BaseFilter;

import static com.codeborne.selenide.Selenide.$x;


/**
 * Keep Selectors of UI elements in {@link BooleanFilterElements}
 */
@Getter
public class BooleanFilter extends BaseFilter {

    private SelenideElement noRadioButton = $x(BooleanFilterElements.NO_RADIO_BUTTON.getSelector()).as(BooleanFilterElements.NO_RADIO_BUTTON.getAlias());
    private SelenideElement yesRadioButton = $x(BooleanFilterElements.YES_RADIO_BUTTON.getSelector()).as(BooleanFilterElements.YES_RADIO_BUTTON.getAlias());

}
