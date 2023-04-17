package pages.admin.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UsersPageElements {

    USERS_PAGE_TITLE("'Users' Page Title", "//h1"),
    USER_ITEMS("User Items", "//tbody/tr"),
    EDIT_USER_BUTTON("'Edit User' Button", "//button//span[text()='Edit Use']"),
    CREATE_USER_BUTTON("'Create User' Button", "//button//span[contains(text(),'Create User')]"),
    ACTIVATE_USER_BUTTON("'Activate User' Button", "//button//span[contains(text(),'Activate USer')]"),
    DEACTIVATE_USER_BUTTON("'Deactivate User' Button", "//button//span[contains(text(),'Deactivate USer')]");

    private String alias;
    private String selector;
}