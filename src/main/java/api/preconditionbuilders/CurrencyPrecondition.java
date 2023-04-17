package api.preconditionbuilders;

import api.core.client.HttpClient;
import api.dto.GenericResponse;
import api.dto.rx.currency.Currency;
import api.services.CurrencyService;
import configurations.User;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Getter
@AllArgsConstructor
public class CurrencyPrecondition {

    private Integer responseCode;
    private GenericResponse<Currency> currencyGetAllResponse;

    private CurrencyPrecondition(CurrencyPreconditionBuilder builder) {
        this.responseCode = builder.responseCode;
        this.currencyGetAllResponse = builder.currencyGetAllResponse;
    }

    public static CurrencyPreconditionBuilder currency() {

        return new CurrencyPreconditionBuilder();
    }

    public static class CurrencyPreconditionBuilder {

        private Response response;
        private Integer responseCode;
        private GenericResponse<Currency> currencyGetAllResponse;

        private final CurrencyService CurrencyService = new CurrencyService();

        public CurrencyPreconditionBuilder getAllCurrency() {
            this.response = CurrencyService.getAll();
            this.currencyGetAllResponse = this.response.as(new TypeRef<GenericResponse<Currency>>() {});
            this.responseCode = response.getStatusCode();

            return this;
        }

        public CurrencyPreconditionBuilder getAllCurrencyWithFilter(Map<String, Object> queryParams) {
            this.response = CurrencyService.getCurrencyWithFilter(queryParams);
            this.currencyGetAllResponse = this.response.as(new TypeRef<GenericResponse<Currency>>() {});
            this.responseCode = response.getStatusCode();

            return this;
        }

        public CurrencyPreconditionBuilder setCredentials(User user) {
            HttpClient.setCredentials(user);

            return this;
        }

        public CurrencyPrecondition build() {

            return new CurrencyPrecondition(this);
        }
    }
}
