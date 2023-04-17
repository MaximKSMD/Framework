package api.services;

import api.dto.rx.yield.dynamicpricing.DynamicPricingRequest;
import io.restassured.response.Response;

import java.util.Map;

import static api.core.RakutenExchangeApi.*;
import static api.core.client.HttpClient.*;

public class DynamicPricingService extends BaseService {

    public Response createDynamicPricing(DynamicPricingRequest body) {
        URL = initURL(CREATE_DYNAMIC_PRICING);

        return post(URL, body.toJson());
    }

    public Response getDynamicPricingWithFilter(Map<String, Object> queryParams) {
        URL = initURL(GET_ALL_DYNAMIC_PRICING);

        return get(URL, queryParams);
    }

    public Response getAll() {
        URL = initURL(GET_ALL_DYNAMIC_PRICING);

        return get(URL);
    }

    public Response deleteDynamicPricing(Integer id) {
        URL = initURL(DELETE_DYNAMIC_PRICING.setParameters(id));

        return delete(URL);
    }
}
