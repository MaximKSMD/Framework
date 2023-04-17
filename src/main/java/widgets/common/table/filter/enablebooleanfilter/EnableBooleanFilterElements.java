
package widgets.common.table.filter.enablebooleanfilter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnableBooleanFilterElements {
    ENABLED_RADIO_BUTTON("'Active' button in in 'Enabled/Disabled' Filter","//div[@role='menu']//label[text()='Enabled']/..//input"),
    DISABLED_RADIO_BUTTON("'Active' button in in 'Enabled/Disabled' Filter","//div[@role='menu']//label[text()='Disabled']/..//input");

    private String alias;
    private String selector;
}
