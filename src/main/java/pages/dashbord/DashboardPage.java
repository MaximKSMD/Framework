package pages.dashbord;

import lombok.Getter;
import pages.BasePage;
import widgets.dashboard.*;

@Getter
public class DashboardPage extends BasePage {

    private RatesByDay ratesByDay = new RatesByDay();
    private TopGeosGraph topGeosGraph = new TopGeosGraph();
    private ECPMTrendByDay ecpmTrendByDay = new ECPMTrendByDay();
    private RevenueByDayGraph revenueByDayGraph = new RevenueByDayGraph();
    private AdSpotOverviewGraph adSpotOverviewGraph = new AdSpotOverviewGraph();
    private TrafficOverviewGraph trafficOverviewGraph = new TrafficOverviewGraph();
}
