package widgets.inventory.adSpots.sidebar.adtagcard.video.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequiredParameter {

    WIDTH("Width"),
    HEIGHT("Height"),
    CACHE_BUSTER("Cache buster"),
    DEVICE_IDENTIFIER("Device Identifier");

    private String name;
}
