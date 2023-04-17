package widgets.common.devices;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeviceList {

    PHONE("Phone"),
    TABLET("Tablet"),
    SET_TOP_BOX("Set Top Box"),
    CONNECTED_TV("Connected TV"),
    MOBILE_TABLET("Mobile/Tablet"),
    CONNECTED_DEVICE("Connected Device"),
    PERSONAL_COMPUTER("Personal Computer");

    private String device;
}