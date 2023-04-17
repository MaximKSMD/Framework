package pages.admin.publisher;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.AccessLevel;
import lombok.Getter;
import org.openqa.selenium.WebElement;
import pages.BasePage;
import widgets.admin.publisher.tableitem.PublisherTableItem;
import widgets.common.table.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static java.lang.String.format;

/**
 * Keep Selectors of UI elements in {@link PublishersPageElements}
 */
@Getter
public class PublishersPage extends BasePage {

    @Getter(AccessLevel.NONE)
    private ElementsCollection publisherItems = $$x(PublishersPageElements.PUBLISHER_ITEMS.getSelector()).as(PublishersPageElements.PUBLISHER_ITEMS.getAlias());
    private SelenideElement pageTitle = $x(PublishersPageElements.PUBLISHERS_PAGE_TITLE.getSelector()).as(PublishersPageElements.PUBLISHERS_PAGE_TITLE.getAlias());
    private SelenideElement createPublisherButton = $x(PublishersPageElements.CREATE_PUBLISHER_BUTTON.getSelector()).as(PublishersPageElements.CREATE_PUBLISHER_BUTTON.getAlias());
    @Getter(AccessLevel.NONE)
    private List<PublisherTableItem> publisherList = new ArrayList<>();
    private Table table = new Table();

    public int countPublisherItems() {

        return (int) publisherItems.shouldBe(
                CollectionCondition.allMatch("Wait to collection element to be visible", WebElement::isDisplayed),
                        CollectionCondition.size(20))
                .stream()
                .map(se -> se.shouldBe(exist,visible))
                .count();
    }

    private void countPublisherItemsOnPage() {
        countPublisherItems();
        var position = new AtomicInteger(1);
        if (publisherList.size() != 0) {
            publisherList.clear();
        }
        publisherList.addAll(publisherItems.stream()
                .map(publisher -> new PublisherTableItem(position.getAndIncrement()))
                .collect(Collectors.toList()));
    }

    public PublisherTableItem getPublisherItemByPositionInList(int position) {
        countPublisherItemsOnPage();

        return publisherList.get(position);
    }

    public PublisherTableItem getPublisherItemByName(String name) {
        countPublisherItemsOnPage();

        return publisherList.stream()
                .filter(pub -> pub.getPublisherName().shouldBe(visible).getText().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        format("The Publisher with name '%s' isn't presented in the list of Publisher", name)));
    }
}
