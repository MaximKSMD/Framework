package widgets.common.table.filter.chip;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.NoSuchElementException;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static java.lang.String.format;
import static widgets.common.table.filter.chip.ChipFilterOptionsItemElements.*;

/**
 * Keep Selectors of UI elements in {@link ChipFilterOptionsItemElements}
 */
@Getter
public class ChipFilterOptionsItem {

    private int position;

    private SelenideElement closeIcon;
    private SelenideElement headerLabel;
    @Getter(AccessLevel.NONE)
    private ElementsCollection chipItems;

    //TODO: need to remove after dev merge
   private static final String CHIP_ITEM = "//div[contains(@class,'chip-container')]/span";
  //  private static final String CHIP_ITEM = "//span[contains(@class,'container-chip')]";
    public ChipFilterOptionsItem(int position) {
        this.position = position;
        this.closeIcon = $x(buildXpath(CLOSE_ICON.getSelector())).as(CLOSE_ICON.getAlias());
        this.chipItems = $$x(buildXpath(LIST_ITEMS.getSelector())).as(LIST_ITEMS.getAlias());
        this.headerLabel = $x(buildXpath(HEADER_LABEL.getSelector())).as(HEADER_LABEL.getAlias());
    }

    public int countFilterOptionsChipItems() {
        int count = 0;

        if (chipItems.size() > 0) {
            count = (int) chipItems
                    .stream()
                    .map(se -> se.shouldBe(exist, visible))
                    .count();
        }

        return count;
    }

    public SelenideElement getChipFilterOptionItemByName(String name) {
        countFilterOptionsChipItems();

        return chipItems.stream()
                .filter(chip -> chip.getText().contains(name))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        format("The Filter Option with content '%s' isn't presented in the chip", name)));
    }

    public SelenideElement getChipFilterOptionItemByPosition(int position) {
        countFilterOptionsChipItems();

        return chipItems.get(position);
    }

    public String buildXpath(String elementXpath) {

        return (format("%s[%s]/%s", CHIP_ITEM, position, elementXpath));
    }
}
