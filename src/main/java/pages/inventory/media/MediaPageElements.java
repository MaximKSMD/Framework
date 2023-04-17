package pages.inventory.media;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MediaPageElements {

    MEDIA_PAGE_TITLE( "'Media' Page Title", "//h1"),
    EDIT_MEDIA_BUTTON("'Edit Media' Button", "//button//span[text()='Edit Media']"),
    CREATE_MEDIA_BUTTON("'Create Media' Button", "//button//span[text()='Create Media']"),
    ACTIVATE_MEDIA_BUTTON("'Activate Media' Button", "//button//span[text()='Activate Media']"),
    DEACTIVATE_MEDIA_BUTTON("'Deactivate Media' Button", "//button//span[text()='Deactivate Media']");

    private String alias;
    private String selector;
}
