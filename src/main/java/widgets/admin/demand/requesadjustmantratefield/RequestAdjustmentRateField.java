package widgets.admin.demand.requesadjustmantratefield;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$x;
import static widgets.admin.demand.requesadjustmantratefield.RequestAdjustmentRateFieldElements.*;


/**
 * Keep Selectors of UI elements in {@link RequestAdjustmentRateFieldElements}
 */
@Getter
public class RequestAdjustmentRateField {

    private SelenideElement requestAdjustmentRateFieldInput = $x(REQUEST_ADJUSTMENT_RATE_INPUT.getSelector()).as(REQUEST_ADJUSTMENT_RATE_INPUT.getAlias());
    private SelenideElement requestAdjustmentRateFieldSuffix = $x(REQUEST_ADJUSTMENT_RATE_SUFFIX.getSelector()).as(REQUEST_ADJUSTMENT_RATE_SUFFIX.getAlias());
    private SelenideElement requestAdjustmentRateFieldUpButton = $x(REQUEST_ADJUSTMENT_RATE_UP_BUTTON.getSelector()).as(REQUEST_ADJUSTMENT_RATE_UP_BUTTON.getAlias());
    private SelenideElement requestAdjustmentRateFieldDownButton = $x(REQUEST_ADJUSTMENT_RATE_DOWN_BUTTON.getSelector()).as(REQUEST_ADJUSTMENT_RATE_DOWN_BUTTON.getAlias());

}
