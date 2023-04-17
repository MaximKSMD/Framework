package rx.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MultipaneConstants {

    ONE_GEO_IS_INCLUDED("%s geo is included"),
    ONE_MEDIA_IS_INCLUDED("%s media is included"),
    ONE_DEVICE_IS_INCLUDED("%s device is included"),
    ONE_DEVICE_IS_EXCLUDED("%s device is excluded"),
    ONE_AD_SPOT_IS_INCLUDED("%s ad spot is included"),
    ONE_AD_SIZE_IS_INCLUDED("%s ad size is included"),
    ONE_AD_SIZE_IS_EXCLUDED("%s ad size is excluded"),
    ONE_AD_FORMAT_IS_INCLUDED("%s ad format is included"),
    ONE_AD_FORMAT_IS_EXCLUDED("%s ad format is excluded"),
    ONE_MEDIA_IS_EXCLUDED("%s media is excluded"),
    ONE_DEMAND_SOURCE_IS_INCLUDED("%s demand source is included"),
    ONE_DEMAND_SOURCE_IS_EXCLUDED("%s demand source is excluded"),
    ONE_OPERATING_SYSTEM_IS_INCLUDED("%s operating system is included"),

    GEOS_ARE_INCLUDED("%s geos are included"),
    ONE_GEO_EXCLUDED("%s geo is excluded"),
    GEOS_ARE_EXCLUDED("%s geos are excluded"),
    MEDIA_ARE_INCLUDED("%s media are included"),
    DEVICES_ARE_INCLUDED("%s devices are included"),
    DEVICES_ARE_EXCLUDED("%s devices are excluded"),
    AD_SPOTS_ARE_INCLUDED("%s ad spots are included"),
    AD_SIZES_ARE_INCLUDED("%s ad sizes are included"),
    AD_SIZES_ARE_EXCLUDED("%s ad sizes are excluded"),
    AD_FORMATS_ARE_INCLUDED("%s ad formats are included"),
    AD_FORMATS_ARE_EXCLUDED("%s ad formats are excluded"),
    MEDIA_ARE_EXCLUDED("%s media are excluded"),
    DEMAND_SOURCES_ARE_INCLUDED("%s demand sources are included"),
    DEMAND_SOURCES_ARE_EXCLUDED("%s demand sources are excluded"),
    MEDIA_AD_SPOTS_ARE_INCLUDED("%s media, %s ad spots are included"),
    MEDIA_ONE_AD_SPOT_ARE_INCLUDED("%s media, %s ad spot are included"),
    OPERATING_SYSTEMS_ARE_INCLUDED("%s operating systems are included"),
    MEDIA_IS_INCLUDED_AD_SPOT_IS_EXCLUDED("%s media is included and %s ad spot is excluded"),
    MEDIA_ARE_INCLUDED_AD_SPOT_IS_EXCLUDED("%s media are included and %s ad spot is excluded"),
    MEDIA_ARE_INCLUDED_AD_SPOTS_ARE_EXCLUDED("%s media are included and %s ad spots are excluded"),

    ALL_GEOS_ARE_INCLUDED("All geos are included"),
    ALL_DEVICES_ARE_INCLUDED("All devices are included"),
    ALL_AD_SIZES_ARE_INCLUDED("All ad sizes are included"),
    ALL_GEOS_ARE_INCLUDED_DETAILS("All Geos are included"),
    ALL_INVENTORY_IS_INCLUDED("All inventory is included"),
    ALL_INVENTORY_ARE_INCLUDED("All inventory are included"),
    ALL_AD_FORMATS_ARE_INCLUDED("All ad formats are included"),
    ALL_AD_FORMATS_ARE_EXCLUDED("All ad formats are excluded"),
    ALL_AD_SIZES_ARE_INCLUDED_DETAILS("All ad sizes are included"),
    ALL_INVENTORY_ARE_INCLUDED_DETAILS("All Inventory is included"),
    ALL_DEMAND_SOURCES_ARE_INCLUDED("All demand sources are included"),
    ALL_OPERATING_SYSTEMS_ARE_INCLUDED("All operating systems are included"),
    ALL_OPERATING_SYSTEMS_ARE_INCLUDED_DETAILS("All Operating Systems are included");

    private final String value;

    public String setQuantity(Object... parameters) {

        return parameters.length > 0 ? String.format(value, parameters) : value;
    }
}
