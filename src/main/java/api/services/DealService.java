package api.services;

import api.dto.rx.sales.deals.Deal;
import io.restassured.response.Response;
import api.dto.rx.sales.deals.DealRequest;

import java.util.Map;

import static api.core.RakutenExchangeApi.*;
import static api.core.client.HttpClient.*;

public class DealService extends BaseService {

    public Response getDealById(int id) {
        URL = initURL(CREATE_DEAL.setParameters(id));

        return get(URL);
    }

    public Response createDeal(DealRequest body) {
        URL = initURL(CREATE_DEAL);

        return post(URL, body.toJson());
    }

    public Response getAll() {
        URL = initURL(CREATE_DEAL);

        return get(URL);
    }

    public Response updateDeal(Deal deal) {
        URL = initURL(UPDATE_DEAL.setParameters(deal.getId()));

        return put(URL, deal.toJson());
    }

    public Response getDealWithFilter(Map<String, Object> queryParams) {
        URL = initURL(GET_ALL_DEALS);

        return get(URL,queryParams);
    }

}
