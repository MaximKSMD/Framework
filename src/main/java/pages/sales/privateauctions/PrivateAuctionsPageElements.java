package pages.sales.privateauctions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PrivateAuctionsPageElements {

    PRIVATE_AUCTIONS_ITEMS( "'Private Auctions' Items", "//tbody/tr"),
    PRIVATE_AUCTIONS_PAGE_TITLE( "'Private Auctions' Page Title", "//h1"),
    CREATE_PRIVATE_AUCTIONS_BUTTON("'Create Private Auctions' Button", "//button//span[text()='Create Private Auction']"),
    EDIT_PRIVATE_AUCTIONS_BUTTON("'Edit Private Auctions' Button", "//button//span[text()='Edit Private Auction']");

    private String alias;
    private String selector;
}
