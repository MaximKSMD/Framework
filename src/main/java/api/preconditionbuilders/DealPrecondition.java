package api.preconditionbuilders;

import configurations.User;
import api.dto.GenericResponse;
import api.services.DealService;
import api.core.client.HttpClient;
import api.dto.rx.sales.deals.Deal;
import io.restassured.response.Response;
import api.dto.rx.sales.deals.DealRequest;
import io.restassured.common.mapper.TypeRef;
import api.dto.rx.admin.publisher.Publisher;
import lombok.extern.slf4j.Slf4j;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.List;

import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Getter
@AllArgsConstructor
public class DealPrecondition {

    private Deal dealResponse;
    private Integer responseCode;
    private DealRequest dealRequest;
    private GenericResponse<Deal> dealGetAllResponse;

    private DealPrecondition(DealPreconditionBuilder builder) {
        this.dealRequest = builder.dealRequest;
        this.dealResponse = builder.dealResponse;
        this.responseCode = builder.responseCode;
        this.dealGetAllResponse = builder.dealGetAllResponse;
    }

    public static DealPreconditionBuilder deal() {

        return new DealPreconditionBuilder();
    }

    public static class DealPreconditionBuilder {

        private Response response;
        private Deal dealResponse;
        private Integer responseCode;
        private DealRequest dealRequest;
        private GenericResponse<Deal> dealGetAllResponse;
        private final DealService dealsService = new DealService();

        public DealPreconditionBuilder createNewDeal() {

            this.dealRequest = createDealRequest(captionWithSuffix("deal_auto"));
            this.response = dealsService.createDeal(dealRequest);
            this.dealResponse = response.as(Deal.class);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public DealPreconditionBuilder getDealWithFilter(Map<String, Object> queryParams) {

            this.response = dealsService.getDealWithFilter(queryParams);
            this.dealGetAllResponse = this.response.as(new TypeRef<>() {});
            this.responseCode = response.getStatusCode();

            return this;
        }

        public DealPreconditionBuilder getAllDealsList() {
            this.response = dealsService.getAll();
            this.dealGetAllResponse = this.response.as(new TypeRef<>() {});
            this.responseCode = response.getStatusCode();

            return this;
        }

        public DealPreconditionBuilder setCredentials(User user) {
            HttpClient.setCredentials(user);

            return this;
        }

        private Publisher createPublisher() {

            return PublisherPrecondition.publisher()
                    .createNewPublisher()
                    .build()
                    .getPublisherResponse();
        }

        private DealRequest createDealRequest(String name) {

            Publisher publisher = createPublisher();

            return DealRequest.builder()
                    .name(captionWithSuffix(name))
                    .publisherId(publisher.getId())
                    .privateAuctionId(4)
                    .dspIds(List.of(10))
                    .build();
        }

        public DealPrecondition build() {
            HttpClient.setCredentials(User.TEST_USER);

            return new DealPrecondition(this);
        }

    }
}