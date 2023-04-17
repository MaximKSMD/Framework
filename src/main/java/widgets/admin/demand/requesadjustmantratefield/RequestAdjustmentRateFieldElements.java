
package widgets.admin.demand.requesadjustmantratefield;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequestAdjustmentRateFieldElements {

    //ToDo Add Selectors
    REQUEST_ADJUSTMENT_RATE_UP_BUTTON("'Request Adjustment Rate Up' button","//todo add Selector"),
    REQUEST_ADJUSTMENT_RATE_DOWN_BUTTON("'Request Adjustment Rate Down' button","//todo add Selector"),
    REQUEST_ADJUSTMENT_RATE_INPUT("'Request Adjustment Rate' Input Field", "//label[text()='Request Adjustment Rate']/../../div/input"),
    REQUEST_ADJUSTMENT_RATE_SUFFIX("''Request Adjustment Rate' Field Suffix", "//label[text()='Request Adjustment Rate']/../../div/div");

    private String alias;
    private String selector;
}
