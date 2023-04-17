package pages.admin.users;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import pages.BasePage;
import widgets.common.table.Table;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static pages.admin.users.UsersPageElements.*;
import static pages.admin.users.UsersPageElements.DEACTIVATE_USER_BUTTON;

/**
 * Keep Selectors of UI elements in {@link UsersPageElements}
 */
@Getter
public class UsersPage extends BasePage {

    private SelenideElement pageTitle = $x(USERS_PAGE_TITLE.getSelector()).as(USERS_PAGE_TITLE.getAlias());
    private ElementsCollection userItems = $$x(USER_ITEMS.getSelector()).as(USER_ITEMS.getAlias());
    private SelenideElement createUserButton = $x(CREATE_USER_BUTTON.getSelector()).as(CREATE_USER_BUTTON.getAlias());
    private SelenideElement activateUserButton = $x(ACTIVATE_USER_BUTTON.getSelector()).as(ACTIVATE_USER_BUTTON.getAlias());
    private SelenideElement deactivateUserButton = $x(DEACTIVATE_USER_BUTTON.getSelector()).as(DEACTIVATE_USER_BUTTON.getAlias());

    private Table usersTable = new Table();
}
