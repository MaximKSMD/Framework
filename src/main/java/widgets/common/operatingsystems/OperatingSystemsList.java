package widgets.common.operatingsystems;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperatingSystemsList {

    IOS("iOS"),
    MACOSX("MacOSX"),
    ANDROID("Android"),
    WINDOWS("Windows");

    private String name;
}