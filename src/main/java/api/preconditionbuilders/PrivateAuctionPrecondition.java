package api.preconditionbuilders;

import api.core.client.HttpClient;
import api.dto.GenericResponse;
import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.common.Currency;
import api.dto.rx.privateauction.PrivateAuction;
import api.dto.rx.privateauction.PrivateAuctionRequest;
import api.services.PrivateAuctionService;
import configurations.User;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Getter
@AllArgsConstructor
public class PrivateAuctionPrecondition {

    private Integer responseCode;
    private PrivateAuction privateAuctionResponse;
    private PrivateAuctionRequest privateAuctionRequest;
    private GenericResponse<PrivateAuction> privateAuctionsGetAllResponse;

    private PrivateAuctionPrecondition(PrivateAuctionPreconditionBuilder builder) {
        this.responseCode = builder.responseCode;
        this.privateAuctionRequest = builder.privateAuctionRequest;
        this.privateAuctionResponse = builder.privateAuctionResponse;
        this.privateAuctionsGetAllResponse = builder.privateAuctionsGetAllResponse;
    }

    public static PrivateAuctionPreconditionBuilder privateAuction() {

        return new PrivateAuctionPreconditionBuilder();
    }

    public static class PrivateAuctionPreconditionBuilder {

        private Response response;
        private Integer responseCode;
        private PrivateAuction privateAuctionResponse;
        private PrivateAuctionRequest privateAuctionRequest;
        private GenericResponse<PrivateAuction> privateAuctionsGetAllResponse;

        private final PrivateAuctionService privateAuctionService = new PrivateAuctionService();

        public PrivateAuctionPreconditionBuilder getAllPrivateAuctions() {

            this.response = privateAuctionService.getAll();
            this.privateAuctionsGetAllResponse = this.response.as(new TypeRef<GenericResponse<PrivateAuction>>() {});
            this.responseCode = response.getStatusCode();

            return this;
        }

        public PrivateAuctionPreconditionBuilder getAllGeosWithFilter(Map<String, Object> queryParams) {

            this.response = privateAuctionService.getPrivateAuctionsWithFilter(queryParams);
            this.privateAuctionsGetAllResponse = this.response.as(new TypeRef<GenericResponse<PrivateAuction>>() {});
            this.responseCode = response.getStatusCode();

            return this;
        }

        public PrivateAuctionPreconditionBuilder createPrivateAuction() {

            this.privateAuctionRequest = createPrivateAuctionRequest(captionWithSuffix("AuctionAuto"));
            this.response = privateAuctionService.createPrivateAuction(privateAuctionRequest);
            this.privateAuctionResponse = response.as(PrivateAuction.class);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public PrivateAuctionPreconditionBuilder createPrivateAuction(PrivateAuctionRequest request) {

            this.privateAuctionRequest = request;
            this.response = privateAuctionService.createPrivateAuction(privateAuctionRequest);
            this.privateAuctionResponse = response.as(PrivateAuction.class);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public PrivateAuctionRequest createPrivateAuctionRequest(String name){
            Publisher publisher = createPublisher();
            ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("UTC"));
            
            return PrivateAuctionRequest.builder()
                    .name(name)
                    .publisherId(publisher.getId())
                    .enabled(true)
                    .noEndDate(false)
                    .startDate(format("%s-%s-%sT00:00:00Z", currentDate.plusMonths(1).getYear(),
                            currentDate.plusMonths(1).getMonth().getValue() < 10 ? format("0%s", currentDate.plusMonths(1).getMonth().getValue())
                                    : currentDate.plusMonths(1).getMonth().getValue(), "02"))
                    .endDate(format("%s-%s-%sT00:00:00Z", currentDate.plusMonths(1).getYear(),
                            currentDate.plusMonths(1).getMonth().getValue() < 10 ? format("0%s", currentDate.plusMonths(1).getMonth().getValue())
                                    : currentDate.plusMonths(1).getMonth().getValue(), "20"))
                    .relatedPackages(List.of())
                    .optimized(true)
                    .build();
         }

        private Publisher createPublisher() {

            return PublisherPrecondition.publisher()
                    .createNewPublisher(captionWithSuffix("autoAuctionPub"),
                            true,
                            Currency.JPY,
                            List.of(1),
                            List.of(24,30))
                    .build()
                    .getPublisherResponse();
        }

        public PrivateAuctionPreconditionBuilder setCredentials(User user) {
            HttpClient.setCredentials(user);

            return this;
        }

        public PrivateAuctionPrecondition build() {

            return new PrivateAuctionPrecondition(this);
        }
    }
}
