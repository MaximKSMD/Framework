package widgets.common.table;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Statuses {

    BOTH("Both"),
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    ONBOARDING("Onboarding");

    private String status;

}