package widgets.common.categories;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoriesList {

    HUMOR("Humor", "Arts & Entertainment"),
    MUSIC("Music", "Arts & Entertainment"),
    ART_ENTERTAIMENTS("Arts & Entertainment", ""),
    TELEVISION("Television", "Arts & Entertainment"),
    BOOKS_LITERATURE("Books & Literature", "Arts & Entertainment"),
    CELEBRITY_FAN_GOSSIP("Celebrity Fan/Gossip", "Arts & Entertainment"),

    AUTOMOTIVE("Automotive", ""),
    AUTO_PARTS("Auto Parts", "Automotive"),
    AUTO_REPAIR("Auto Repair", "Automotive"),

    EDUCATION("Education", ""),
    ART_HISTORY("Art History", "Education"),
    COLLEGE_LIFE("College Life", "Education");

    private String name;
    private String parent;

}