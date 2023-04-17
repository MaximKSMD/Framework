package api.core.client;

import api.core.AuthApi;
import api.core.RakutenExchangeApi;
import api.dto.auth.AuthRequest;
import api.dto.auth.AuthResponse;
import configurations.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static configurations.ConfigurationLoader.getConfig;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.apache.http.HttpStatus.*;

@Slf4j
public class HttpClient {

    private static String URL;
    private static String mail;
    private static String password;
    private static String token;

    public static Response get(String url) {
        setToken();

        return executeGet(token, url);
    }

    public static Response get(String url, Map<String, Object> parameters) {
        url = setRequestParamsIfPresent(parameters, url);
        setToken();

        return executeGet(token, url);
    }

    public static Response post(String url, Object body, Map<String, Object> parameters) {
        url = setRequestParamsIfPresent(parameters, url);
        setToken();

        return executePost(token, url, body);
    }

    public static Response post(String url, Object body) {
        setToken();

        return executePost(token, url, body);
    }

    public static Response put(String url, Object body) {
        setToken();

        return executePut(token, url, body);
    }

    public static Response postToFileUpload(String url, String filePath, Map<String, Object> parameters) {
        url = setRequestParamsIfPresent(parameters, url);
        setToken();

        return executePostToUploadFile(token, url, filePath);
    }

    public static Response put(String url, Object body, Map<String, Object> parameters) {
        url = setRequestParamsIfPresent(parameters, url);
        setToken();

        return executePut(token, url, body);
    }

    public static Response delete(String url) {
        setToken();

        return executeDelete(token, url);
    }

    public static Response delete(String url, Map<String, Object> parameters) {
        url = setRequestParamsIfPresent(parameters, url);
        setToken();

        return executeDelete(token, url);
    }

    private static String setRequestParamsIfPresent(Map<String, Object> parameters, String url) {
        if (!parameters.isEmpty()) {
            String params = "";
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {

                params = format("%s&%s=%s", params, entry.getKey(), entry.getValue().toString());
            }
            url = format("%s?%s", url, params).replace("?&", "?");
            log.info(url);
        }

        return url;
    }

    public static void setCredentials(User user) {
        mail = user.getMail();
        password = user.getPassword();
        //setToken();
    }

    public static String getToken(){

        return token;
    }

    private static void setToken() {
        URL = initURL(AuthApi.GET_TOKEN);

        if (mail == null || password == null) {
            log.warn("Login or password for request is 'null'. User 'developer' will be used for building request credentials'");
            mail = User.TEST_USER.getMail();
            password = User.TEST_USER.getPassword();
        } else {
            log.info(String.format("User '%s' with password '%s' will be used for building request credentials'", mail, password));
        }
        log.info("Post to get token POST {}", URL);

        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .body(buildUserPayload(mail, password))
                        .when()
                        .post(URL);

        logResponse(response);

        if (response.getStatusCode() != SC_OK | response.getStatusCode() != SC_CREATED ||
                response.getStatusCode() != SC_ACCEPTED || response.getStatusCode() != SC_NO_CONTENT) {
            AuthResponse authResponse = response
                    .then()
                    .contentType(ContentType.JSON)
                    .extract()
                    .as(AuthResponse.class);


            log.info("Token {}", authResponse.getToken());
            token = authResponse.getToken();

            log.info("Post to get token POST {}", URL);
        } else {
            throw new RuntimeException("Failed to get token");
        }
    }

    private static RequestSpecification authorizedClient(String token, ContentType contentType) {

        return RestAssured.given()
                .contentType(contentType)
                .header("Authorization", "Bearer " + token);
    }

    private static AuthRequest buildUserPayload(String username, String password) {

        return AuthRequest.builder()
                .mail(username)
                .password(password)
                .build();
    }

    @SneakyThrows
    public static String initURL(Enum actionsEnum) {

        if (actionsEnum instanceof RakutenExchangeApi) {
        RakutenExchangeApi request = (RakutenExchangeApi) actionsEnum;

            return format("%s%s", getConfig().getBaseApiUrl(),
                    request.getEndpoint());
        } else if (actionsEnum instanceof AuthApi) {
            AuthApi request = (AuthApi) actionsEnum;

            return format("%s%s", getConfig().getBaseApiUrl(),
                    request.getEndpoint());
        } else {

            throw new IOException(format("init Url in %s must get it's own enum endpoint", actionsEnum.getClass().getName()));
        }
    }

    public static String initURL(String endpoint) {

        return format("%s%s", getConfig().getBaseUrl().replace("publisher-portal", "s-api"), endpoint);
    }

    private static Response executeGet(String token, String url) {
        log.info("GET {}", url);
        step(format("GET %s", url));

        Response response = authorizedClient(token, ContentType.JSON)
                .when()
                .get(url);

        logResponse(response);

        return response;
    }

    private static Response executePost(String token, String url, Object body) {
        log.info("POST {}", url);
        log.info("Request body {}", body.toString());
        step(format("POST %s \nRequest body %s", url, body.toString()));

        Response response = authorizedClient(token, ContentType.JSON)
                .body(body)
                .when()
                .post(url);

        logResponse(response);

        return response;
    }

    private static Response executePostToUploadFile(String token, String url, String filePath) {
        log.info("POST to file upload {}", url);
        log.info("File Ptah {}", filePath);
        step(format("POST %s \nFile Ptah %s", url, filePath));


        Response response = authorizedClient(token, ContentType.JSON)
                .contentType("multipart/form-data")
                .multiPart("file", new File(filePath), "application/x-zip-compressed")
                .when()
                .post(url);

        logResponse(response);
        log.info("File {} has been successfully uploaded ", filePath);

        return response;
    }

    private static Response executePut(String token, String url, Object body) {
        log.info("PUT {}", url);
        log.info("Request body {}", body.toString());
        step(format("PUT %s \nRequest body %s", url, body.toString()));

        Response response = authorizedClient(token, ContentType.JSON)
                .body(body)
                .when()
                .put(url);

        logResponse(response);

        return response;
    }

    private static Response executeDelete(String token, String url) {
        log.info("Delete {}", url);
        step(format("DELETE %s", url));

        Response response = authorizedClient(token, ContentType.JSON)
                .when()
                .delete(url);

        logResponse(response);

        return response;
    }

    private static void logResponse(Response response) {
        switch (response.getStatusCode()) {
            case SC_OK:
            case SC_CREATED:
            case SC_ACCEPTED:
            case SC_NO_CONTENT:
                log.info("Response Code: {}", response.getStatusCode());
                //log.info("Response Body: {}", response.getBody().prettyPrint());
                step(format(
                        "Response Code: %s ",
                        response.getStatusCode()));
                step(format(
                        "\nResponse Body: '%s'",
                        response.getBody().prettyPrint()));
                break;
            default:
                log.error("Response Code: {}", response.getStatusCode());
                //log.error("Response Body: {}", response.getBody().prettyPrint());
                step(format(
                        "Response Code: %s \nResponse Body: '%s'",
                        response.getStatusCode(), response.getBody().prettyPrint()));
                break;
        }
    }
}
