package widgets.common.adformats;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdFormatsList {

    BANNER("Banner"),
    NATIVE("Native"),
    VIDEO("Video");

    private String name;
}