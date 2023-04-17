package widgets.inventory.adSpots.sidebar.adtagcard.video.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OptionalParameter {

    GENDER("Gender"),
    IFA_TYPE("IFA Type"),
    LANGUAGE("Language"),
    CONTENT_GENRE("Content Genre"),
    CONTENT_TITLE("Content Title"),
    CONTENT_SERIES("Content Series"),
    CONTENT_RATING("Content Rating"),
    CONTENT_LANGUAGE("Content Language");

    private String name;
}
