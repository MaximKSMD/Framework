package widgets.common.table.filter.abstractt;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$x;


/**
 * Keep Selectors of UI elements in {@link BaseFilterElements}
 */
@Getter
public abstract class BaseFilter {

    private SelenideElement backButton = $x(BaseFilterElements.BACK_BUTTON.getSelector()).as(BaseFilterElements.BACK_BUTTON.getAlias());
    private SelenideElement cancelButton = $x(BaseFilterElements.CANCEL_BUTTON.getSelector()).as(BaseFilterElements.CANCEL_BUTTON.getAlias());
    private SelenideElement submitButton = $x(BaseFilterElements.SUBMIT_BUTTON.getSelector()).as(BaseFilterElements.SUBMIT_BUTTON.getAlias());
    private SelenideElement filterHeaderLabel = $x(BaseFilterElements.FILTER_HEADER.getSelector()).as(BaseFilterElements.FILTER_HEADER.getAlias());

}
