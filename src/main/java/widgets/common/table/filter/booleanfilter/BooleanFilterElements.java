
package widgets.common.table.filter.booleanfilter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BooleanFilterElements {

    NO_RADIO_BUTTON("'No' button in 'Active/Inactive' Filter","//div[@role='menu']//label[text()='No']/..//input"),
    YES_RADIO_BUTTON("'Yes' button in 'Active/Inactive' Filter","//div[@role='menu']//label[text()='Yes']/..//input");

    private String alias;
    private String selector;
}
