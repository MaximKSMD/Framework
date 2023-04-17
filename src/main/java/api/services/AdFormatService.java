package api.services;

import io.restassured.response.Response;

import static api.core.RakutenExchangeApi.GET_ALL_AD_FORMATS;
import static api.core.client.HttpClient.get;
import static api.core.client.HttpClient.initURL;

public class AdFormatService extends BaseService {

    public Response getAll() {
        URL = initURL(GET_ALL_AD_FORMATS);

        return get(URL);
    }
}
