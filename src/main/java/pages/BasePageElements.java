package pages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BasePageElements {

    SNACKBAR( "'SnackBar' element", ""),
    LOGO( "'Logo' Icon", "//div[@class='logo']/../.."),
    SIDEBAR("'Sidebar'", "//div[@class='overlay']"),
    TABLE_PROGRESSBAR("'Table ProgressBar'", "//div[@role='progressbar']"),
    NUXT_PROGRESSBAR("'Page Load Progressbar'", "//div[@class='nuxt-progress']"),
    CIRCLE_ICON("'Version Circle Icon'", "//i[@title='Active']"),
    VERSION_TEXT("'Version div'", "//div[@class='version']"),
    BUTTON_BY_CAPTION("Button with Caption", "//span[text()='%s']/..");

    private String alias;
    private String selector;
}
