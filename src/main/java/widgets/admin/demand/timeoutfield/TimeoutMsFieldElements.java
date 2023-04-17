
package widgets.admin.demand.timeoutfield;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeoutMsFieldElements {

    //ToDo Add Selectors
    TIMEOUT_MS_UP_BUTTON("'Timeout(ms)' button","//todo add Selector"),
    TIMEOUT_MS_DOWN_BUTTON("'Timeout(ms)' button","//todo add Selector"),
    TIMEOUT_MS_INPUT("'Timeout(ms)' Input Field", "//label[text()='Timeout(ms)']/../../div/input"),
    TIMEOUT_MS_SUFFIX("'Timeout(ms)' Field Prefix", "//label[text()='Timeout(ms)']/../../div/div");

    private String alias;
    private String selector;
}
