package api.services;

import io.restassured.response.Response;

import java.util.Map;

import static api.core.client.HttpClient.get;
import static api.core.client.HttpClient.initURL;
import static api.core.RakutenExchangeApi.GET_ALL_CURRENCIES;

public class CurrencyService extends BaseService {

    public Response getAll() {
        URL = initURL(GET_ALL_CURRENCIES);


        return get(URL);
    }

    public Response getCurrencyWithFilter(Map<String, Object> queryParams) {
        URL = initURL(GET_ALL_CURRENCIES);

        return get(URL, queryParams);
    }
}
