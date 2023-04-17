package widgets.common.multipane;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MultipaneNameImpl implements MultipaneName {

    GEO("Geo"),
    DEVICE("Device"),
    AD_SIZE("Ad Size"),
    AD_FORMAT("Ad Format"),
    INVENTORY("Inventory"),
    DEMAND_SOURCES("Demand Sources"),
    OPERATING_SYSTEM("Operating System");

    private final String name;
}
