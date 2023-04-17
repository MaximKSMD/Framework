package api.services;

import api.dto.rx.yield.openpricing.OpenPricing;
import api.dto.rx.yield.openpricing.OpenPricingRequest;
import io.restassured.response.Response;

import java.util.Map;

import static api.core.RakutenExchangeApi.*;
import static api.core.client.HttpClient.*;

public class OpenPricingService extends BaseService {

    public Response createOpenPricing(OpenPricingRequest body) {
        URL = initURL(CREATE_OPEN_PRICING);

        return post(URL, body.toJson());
    }

    public Response updateOpenPricing(OpenPricing rule) {
        URL = initURL(UPDATE_OPEN_PRICING.setParameters(rule.getId()));

        return put(URL, rule.toString());
    }

    public Response getOpenPricingWithFilter(Map<String, Object> queryParams) {
        URL = initURL(GET_ALL_OPEN_PRICING);

        return get(URL, queryParams);
    }

    public Response getAll() {
        URL = initURL(GET_ALL_OPEN_PRICING);

        return get(URL);
    }

    public Response export(Map<String, Object> queryParams) {
        URL = initURL(EXPORT_OPEN_PRICING_FLOOR_PRICE);

        return get(URL, queryParams);
    }

    public Response deleteOpenPricing(Integer id) {
        URL = initURL(DELETE_OPEN_PRICING.setParameters(id));

        return delete(URL);
    }
}
