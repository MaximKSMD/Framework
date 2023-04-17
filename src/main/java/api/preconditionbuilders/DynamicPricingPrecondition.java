package api.preconditionbuilders;

import api.core.client.HttpClient;
import api.dto.GenericResponse;
import api.dto.rx.inventory.adspot.AdSpot;
import api.dto.rx.inventory.adspot.AdSpotRequest;
import api.dto.rx.inventory.adspot.Banner;
import api.dto.rx.inventory.media.Media;
import api.dto.rx.yield.dynamicpricing.DynamicPricing;
import api.dto.rx.yield.dynamicpricing.DynamicPricingRequest;
import api.dto.rx.yield.openpricing.*;
import api.services.DynamicPricingService;
import api.services.OpenPricingService;
import configurations.User;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Getter
@AllArgsConstructor
public class DynamicPricingPrecondition {

    private Integer responseCode;
    private DynamicPricing dynamicPricingResponse;
    private DynamicPricingRequest dynamicPricingRequest;
    private GenericResponse<DynamicPricing> dynamicPricingGetAllResponse;

    private DynamicPricingPrecondition(DynamicPricingPreconditionBuilder builder) {
        this.responseCode = builder.responseCode;
        this.dynamicPricingRequest = builder.dynamicPricingRequest;
        this.dynamicPricingResponse = builder.dynamicPricingResponse;
        this.dynamicPricingGetAllResponse = builder.dynamicPricingGetAllResponse;
    }

    public static DynamicPricingPreconditionBuilder dynamicPricing() {

        return new DynamicPricingPreconditionBuilder();
    }

    public static class DynamicPricingPreconditionBuilder {

        private Response response;
        private Integer responseCode;
        private DynamicPricing dynamicPricingResponse;
        private DynamicPricingRequest dynamicPricingRequest;
        private GenericResponse<DynamicPricing> dynamicPricingGetAllResponse;
        private DynamicPricingService dynamicPricingService = new DynamicPricingService();

        public DynamicPricingPrecondition.DynamicPricingPreconditionBuilder createNewDynamicPricing() {

            Media media = MediaPrecondition.media().createNewMedia().build().getMediaResponse();

            AdSpot adSpot = AdSpotPrecondition.adSpot().
                    createNewAdSpot(AdSpotRequest.builder()
                            .name(captionWithSuffix("AdSpot"))
                            .enabled(true)
                            .publisherId(media.getPublisherId())
                            .publisherName(media.getPublisherName())
                            .floorPrice(9.99)
                            .positionId(1)
                            .mediaId(media.getId())
                            .coppa(true)
                            .categoryIds(List.of(1, 2))
                            .sizeIds(List.of(10))
                            .banner(Banner.builder()
                                    .enabled(true)
                                    .floorPrice(8.88)
                                    .sizeIds(List.of(3))
                                    .build())
                            .build())
                    .build()
                    .getAdSpotResponse();

            this.dynamicPricingRequest = DynamicPricingRequest.builder()
                    .name(captionWithSuffix("OpenPricing"))
                    .active(true)
                    .floorPrice(5.66)
                    .notes("autotest")
                    .priority(1)
                    .publisherName(media.getPublisherName())
                    .publisherId(media.getPublisherId())
                    .rule(Rule.builder()
                            .adspot(AdSpotRule.builder()
                                    .includedAdspots(List.of(adSpot.getId()))
                                    .excludedAdspots(List.of())
                                    .build())
                            .adFormat(AdFormat.builder()
                                    .adFormats(List.of(2, 3))
                                    .exclude(false)
                                    .build())
                            .adSize(AdSize.builder()
                                    .adSizes(List.of(10))
                                    .exclude(false).build())
                            .media(MediaRule.builder()
                                    .media(List.of(media.getId()))
                                    .exclude(false).build())
                            .deviceType(DeviceType.builder()
                                    .deviceTypes(List.of(4))
                                    .exclude(false)
                                    .build())
                            .geo(Geo.builder()
                                    .geos(List.of(221))
                                    .exclude(false)
                                    .build())
                            .deviceOS(DeviceOS.builder()
                                    .deviceOSs(List.of(4))
                                    .exclude(false)
                                    .build())
                            .dsp(Dsp.builder()
                                    .dsps(null)
                                    .exclude(false)
                                    .build())
                            .build())
                    .build();

            this.response = dynamicPricingService.createDynamicPricing(dynamicPricingRequest);
            this.dynamicPricingResponse = response.as(DynamicPricing.class);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public DynamicPricingPreconditionBuilder getDynamicPricingList() {
            this.response = dynamicPricingService.getAll();

            this.dynamicPricingGetAllResponse = this.response.as(new TypeRef<GenericResponse<DynamicPricing>>() {});
            this.responseCode = response.getStatusCode();

            return this;
        }

        private List<DynamicPricing> getDynamicPricingResponseList() {

            return Arrays.asList(response.jsonPath().getObject("items", DynamicPricing[].class));
        }

        public DynamicPricingPreconditionBuilder deleteDynamicPricing(int id) {
            this.response = dynamicPricingService.deleteDynamicPricing(id);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public DynamicPricingPreconditionBuilder setCredentials(User user) {
            HttpClient.setCredentials(user);

            return this;
        }

        public DynamicPricingPrecondition build() {

            return new DynamicPricingPrecondition(this);
        }

        public DynamicPricingPreconditionBuilder getDynamicPricingWithFilter(Map<String, Object> queryParams) {
            this.response = dynamicPricingService.getDynamicPricingWithFilter(queryParams);

            this.dynamicPricingGetAllResponse = this.response.as(new TypeRef<GenericResponse<DynamicPricing>>() {});
            this.responseCode = response.getStatusCode();

            return this;
        }
    }
}
