package api.services;

import api.dto.rx.privateauction.PrivateAuction;
import api.dto.rx.privateauction.PrivateAuctionRequest;
import io.restassured.response.Response;

import java.util.Map;

import static api.core.RakutenExchangeApi.*;
import static api.core.client.HttpClient.*;

public class PrivateAuctionService extends BaseService {

    public Response getAll() {
        URL = initURL(GET_ALL_PRIVATE_AUCTIONS);

        return get(URL);
    }

    public Response getPrivateAuctionsWithFilter(Map<String, Object> queryParams) {
        URL = initURL(GET_ALL_PRIVATE_AUCTIONS);

        return get(URL, queryParams);
    }

    public Response createPrivateAuction(PrivateAuctionRequest body){
        URL = initURL(CREATE_PRIVATE_AUCTION);

        return post(URL, body);
    }

    public Response updatePrivateAuction(PrivateAuction privateAuction){
        URL = initURL(UPDATE_PRIVATE_AUCTION);

        return put(URL, privateAuction.toJson());
    }

    public Response getPrivateAuctionByID(Map<String, Object> queryParams){
        URL = initURL(UPDATE_PRIVATE_AUCTION);

        return get(URL, queryParams);
    }
}
