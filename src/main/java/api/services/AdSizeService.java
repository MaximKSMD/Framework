package api.services;

import io.restassured.response.Response;

import static api.core.RakutenExchangeApi.GET_ALL_AD_SIZES;
import static api.core.client.HttpClient.get;
import static api.core.client.HttpClient.initURL;

public class AdSizeService extends BaseService {

    public Response getAll() {
        URL = initURL(GET_ALL_AD_SIZES);

        return get(URL);
    }
}
