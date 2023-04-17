package widgets.admin.demand.multipane;

import lombok.AllArgsConstructor;
import lombok.Getter;
import widgets.common.multipane.MultipaneName;

@Getter
@AllArgsConstructor
public enum DemandSourceTypeNameImpl implements MultipaneName {

    ALLOWED_COUNTRIES("Allowed Countries");

    private final String name;
}
