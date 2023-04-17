package widgets.common.multipane.item;

import widgets.common.multipane.MultipaneName;
import widgets.common.multipane.MultipaneNameImpl;
import widgets.common.multipane.item.common.CommonSelectTableItem;


public class SelectChildTableItem extends CommonSelectTableItem {

    private static final String SELECT_CHILD_TABLE_ITEM = "h3[text()='%s']/../..//table[@class='select-table fixed']/tbody/tr[contains(@class,'children')]";
    //descendant::h3[text()='%s']/../..//table[@class='select-table fixed']/tbody/tr[contains(@class,'children')][%s]//td[2]/div

    public SelectChildTableItem(int position, MultipaneName multipaneName) {
        super(position, SELECT_CHILD_TABLE_ITEM, multipaneName);
    }
}
