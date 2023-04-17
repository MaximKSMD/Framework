package api.services;

import api.dto.rx.admin.user.UserRequest;
import api.dto.rx.admin.user.UserDto;
import io.restassured.response.Response;

import java.util.Map;

import static api.core.RakutenExchangeApi.*;
import static api.core.client.HttpClient.*;

public class UserService extends BaseService {

    public Response createUser(UserRequest body) {
        //UserResponse
        URL = initURL(CREATE_USER);

        return post(URL, body.toJson());
    }

    public Response updateUser(UserDto body) {
        //UserResponse
        URL = initURL(UPDATE_USER.setParameters(body.getId()));

        return put(URL, body.toJson());
    }

    public Response getUserById(int id) {
        //UserResponse
        URL = initURL(GET_USER.setParameters(id));

        return get(URL);
    }

    public Response deleteUser(int id) {
        //UserResponse
        URL = initURL(DELETE_USER.setParameters(id));

        return delete(URL);
    }

    public Response getAll() {
        //List<UserResponse>
        URL = initURL(GET_ALL_USERS);

        return get(URL);
    }

    public Response getUsersWithFilter(Map<String, Object> queryParams) {
        URL = initURL(GET_ALL_USERS);

        return get(URL, queryParams);
    }
}
