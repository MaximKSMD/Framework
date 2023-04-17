package widgets.admin.demand.multipane;

import lombok.Getter;
import widgets.common.multipane.Multipane;

@Getter
public class DemandSourceTypeMultipane extends Multipane {

    public DemandSourceTypeMultipane(DemandSourceTypeNameImpl demandSourceTypeName){
        super(demandSourceTypeName);
    }
}