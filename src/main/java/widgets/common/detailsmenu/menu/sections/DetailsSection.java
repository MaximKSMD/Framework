package widgets.common.detailsmenu.menu.sections;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import lombok.Getter;
import org.openqa.selenium.WebElement;
import widgets.common.detailsmenu.DetailsSectionName;
import widgets.common.detailsmenu.item.DetailsMenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;
import static java.lang.String.format;
import static widgets.common.detailsmenu.menu.sections.DetailsSectionElements.DETAILS_MENU_ITEMS;

public class DetailsSection {

    @Getter
    private DetailsSectionName detailsSection;
    private ElementsCollection menuMenuSectionItems;

    private List<DetailsMenuItem> menuSectionList;

    public DetailsSection(DetailsSectionName detailsSection){
        this.detailsSection = detailsSection;
        this.menuMenuSectionItems = $$x(format(DETAILS_MENU_ITEMS.getSelector(), this.detailsSection.getName()))
                .as(format(DETAILS_MENU_ITEMS.getAlias(), this.detailsSection.getName()));
        this.menuSectionList = new ArrayList<>();
    }


    public int countSectionItems() {

        return (int) menuMenuSectionItems.shouldBe(
                        CollectionCondition.allMatch("Wait to collection element to be visible", WebElement::isDisplayed))
                .stream()
                .map(se -> se.shouldBe(exist,visible))
                .count();
    }

    private void countMenuSectionItemsOnPage() {
        countSectionItems();
        var position = new AtomicInteger(1);
        if (menuSectionList.size() != 0) {
            menuSectionList.clear();
        }
        menuSectionList.addAll(menuMenuSectionItems.stream()
                .map(publisher -> new DetailsMenuItem(position.getAndIncrement(), detailsSection))
                .collect(Collectors.toList()));
    }

    public DetailsMenuItem getMenuItemByPositionInList(int position) {
        countMenuSectionItemsOnPage();

        return menuSectionList.get(position);
    }

    public DetailsMenuItem getMenuItemByName(String name) {
        countMenuSectionItemsOnPage();

        return menuSectionList.stream()
                .filter(pub -> pub.getName().shouldBe(visible).getText().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        format("The item with name '%s' isn't presented in the '%s' Menu Section ", name, detailsSection.getName())));
    }
}
