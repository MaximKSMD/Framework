package api.services;

import api.dto.rx.admin.publisher.PublisherRequest;
import io.restassured.response.Response;

import java.util.Map;

import static api.core.RakutenExchangeApi.*;
import static api.core.client.HttpClient.*;

public class PublisherService extends BaseService {

    public Response createPublisher(PublisherRequest body) {
        URL = initURL(CREATE_PUBLISHER);

        return post(URL, body.toJson());
    }

    public Response updatePublisher(PublisherRequest publisher, int publisherId) {
        URL = initURL(UPDATE_PUBLISHER.setParameters(publisherId));

        return put(URL, publisher.toJson());
    }

    public Response getAll() {
        URL = initURL(GET_PUBLISHERS);

        return get(URL);
    }

    public Response deletePublisher(int id) {
        URL = initURL(DELETE_PUBLISHER.setParameters(id));

        return delete(URL);
    }

    public Response getPublisher(int id){
        URL = initURL(GET_PUBLISHER.setParameters(id));

        return get(URL);

    }

    public Response getPublisherWithFilter(Map<String, Object> queryParams) {
        URL = initURL(GET_PUBLISHERS);

        return get(URL, queryParams);
    }
}
