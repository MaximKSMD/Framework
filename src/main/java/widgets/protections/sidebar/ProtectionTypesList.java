package widgets.protections.sidebar;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProtectionTypesList {

    ADVERTISER("Advertiser"),
    AD_CATEGORIES("Ad Categories"),
    SUPPLY_BLOCKS("Supply Blocks");

    private String type;
}