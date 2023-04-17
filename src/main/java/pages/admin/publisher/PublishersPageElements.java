package pages.admin.publisher;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PublishersPageElements {


    PUBLISHER_ITEMS( "Publisher Items", "//tbody/tr"),
    PUBLISHERS_PAGE_TITLE( "'Publishers' Page Title", "//h1"),
    CREATE_PUBLISHER_BUTTON("'Create Publisher' Button", "//button//span[text()='Create Publisher']"),
    EDIT_PUBLISHER_BUTTON("'Edit Publisher' Button", "//button//span[text()='Edit Publisher']");

    private String alias;
    private String selector;
}
