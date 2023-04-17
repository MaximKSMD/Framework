package pages.protections;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProtectionsPageElements {

    PROTECTION_ITEMS("Protection Items", "//tbody/tr"),
    PROTECTIONS_PAGE_TITLE("'Protections' Page Title", "//h1"),
    ACTIVATE_PROTECTION_BUTTON("'Activate Protection' Button", "//button//span[text()='Activate']"),
    EDIT_PROTECTION_BUTTON("'Edit Protection' Button", "//button//span[text()='Edit Protections']"),
    CREATE_PROTECTION_BUTTON("'Create Protection' Button", "//button//span[text()='Create Protections']"),
    DEACTIVATE_PROTECTION_BUTTON("'Deactivate Protection' Button", "//button//span[text()='Deactivate']");

    private String alias;
    private String selector;
}
