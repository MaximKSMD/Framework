package widgets.inventory.media.sidebar;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlatformType {

    IOS("IOS"),
    CTV("CTV"),
    ANDROID("Android"),
    PC_WEB("PC Web"),
    MOBILE_WEB("Mobile Web"),
    IOS_WEB_VIEW("IOS Web View"),
    ANDROID_WEB_VIEW("Android Web View");

    private String name;
}
