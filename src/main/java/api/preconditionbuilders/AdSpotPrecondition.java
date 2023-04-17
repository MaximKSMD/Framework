package api.preconditionbuilders;

import api.core.client.HttpClient;
import api.dto.GenericResponse;
import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.common.Currency;
import api.dto.rx.inventory.adspot.AdSpot;
import api.dto.rx.inventory.adspot.AdSpotRequest;
import api.dto.rx.inventory.adspot.Video;
import api.dto.rx.inventory.media.Media;
import api.services.AdSpotService;
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
public class AdSpotPrecondition {

    private Integer responseCode;
    private AdSpot adSpotResponse;
    private AdSpotRequest adSpotRequest;
    private GenericResponse<AdSpot> adSpotsGetAllResponse;

    private AdSpotPrecondition(AdSpotPreconditionBuilder builder) {
        this.responseCode = builder.responseCode;
        this.adSpotRequest = builder.adSpotRequest;
        this.adSpotResponse = builder.adSpotResponse;
        this.adSpotsGetAllResponse = builder.adSpotsGetAllResponse;
    }

    public static AdSpotPreconditionBuilder adSpot() {

        return new AdSpotPreconditionBuilder();
    }
    public static class AdSpotPreconditionBuilder {

        private Response response;
        private Integer responseCode;
        private AdSpot adSpotResponse;
        private AdSpotRequest adSpotRequest;
        private final AdSpotService adSpotService = new AdSpotService();
        private GenericResponse<AdSpot> adSpotsGetAllResponse;

        public AdSpotPreconditionBuilder createNewAdSpot() {
            this.adSpotRequest = createAdSpotRequest(captionWithSuffix("ad_spot_auto"));
            this.response = adSpotService.createAdSpot(adSpotRequest);
            this.adSpotResponse = response.as(AdSpot.class);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public AdSpotPreconditionBuilder createNewAdSpot(AdSpotRequest adSpotRequest) {
            this.adSpotRequest = adSpotRequest;
            this.response = adSpotService.createAdSpot(adSpotRequest);
            this.adSpotResponse = response.as(AdSpot.class);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public AdSpotPreconditionBuilder createNewAdSpot(String name) {
            this.adSpotRequest = createAdSpotRequest(name);
            this.response = adSpotService.createAdSpot(adSpotRequest);
            this.adSpotResponse = response.as(AdSpot.class);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public AdSpotPreconditionBuilder createNewAdSpot(String name, String publisherName, Boolean isEnabled) {
            this.adSpotRequest = createAdSpotRequest(name, publisherName, isEnabled);
            this.response = adSpotService.createAdSpot(adSpotRequest);
            this.adSpotResponse = response.as(AdSpot.class);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public AdSpotPreconditionBuilder createNewAdSpot(String name, Boolean isEnabled) {
            this.adSpotRequest = createAdSpotRequest(name, isEnabled);
            this.response = adSpotService.createAdSpot(adSpotRequest);
            this.adSpotResponse = response.as(AdSpot.class);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public AdSpotPreconditionBuilder createNewAdSpot(String name, Integer publisherId, Integer mediaId, Boolean isEnabled) {

            this.adSpotRequest = createAdSpotRequest(name, publisherId, mediaId, isEnabled);
            this.response = adSpotService.createAdSpot(adSpotRequest);
            this.adSpotResponse = response.as(AdSpot.class);
            this.responseCode = response.getStatusCode();

            return this;
        }

        private AdSpotRequest createAdSpotRequest(String name, Integer publisherId, Integer mediaId, Boolean isEnabled) {

            return AdSpotRequest.builder()
                    .name(name)
                    .enabled(isEnabled)
                    .publisherId(publisherId)
                    .currency(Currency.JPY.name())
                    .floorPrice(11.00)
                    .positionId(1)
                    .mediaId(mediaId)
                    .coppa(true)
                    .sizeIds(List.of(10))
                    .floorPriceAutomated(true)
                    .testMode(false)
                    .categoryIds(List.of(1, 9))
                    .video(createVideo())
                    .build();
        }
        private AdSpotRequest createAdSpotRequest(String name, String publisherName, Boolean isEnabled) {
            Publisher publisher = createPublisher(publisherName);
            Media media = createMedia("autoMedia",publisher.getId());

            return createAdSpotRequest(name, publisher.getId(), media.getId(), isEnabled);
        }
        private AdSpotRequest createAdSpotRequest(String name, Boolean isEnabled) {
            Publisher publisher = createPublisher("autoPub");
            Media media = createMedia("autoMedia",publisher.getId());

            return AdSpotRequest.builder()
                    .name(name)
                    .enabled(isEnabled)
                    .publisherId(media.getPublisherId())
                    .currency(Currency.JPY.name())
                    .floorPrice(11.00)
                    .positionId(1)
                    .mediaId(media.getId())
                    .coppa(true)
                    .sizeIds(List.of(10))
                    .floorPriceAutomated(true)
                    .testMode(false)
                    .categoryIds(List.of(1, 9))
                    .video(createVideo())
                    .build();
        }
        private Media createMedia(){

            return MediaPrecondition.media()
                    .createNewMedia()
                    .build()
                    .getMediaResponse();
        }
        private Media createMedia(String name, Integer id){

            return MediaPrecondition.media()
                    .createNewMedia(name,id,true)
                    .build()
                    .getMediaResponse();
        }
        private Publisher createPublisher(){

            return PublisherPrecondition.publisher()
                    .createNewPublisher()
                    .build()
                    .getPublisherResponse();
        }
        private Publisher createPublisher(String name){

            return PublisherPrecondition.publisher()
                    .createNewPublisher(name)
                    .build()
                    .getPublisherResponse();
        }
        private Video createVideo(){

            return  Video.builder()
                    .floorPrice(23.00)
                    .maxDuration(10)
                    .enabled(true)
                    .placementType(1)
                    .sizeIds(List.of(10))
                    .playbackMethodIds(List.of(1))
                    .build();
        }

        private  AdSpotRequest createAdSpotRequest(String name){
            Media media = createMedia();

            return AdSpotRequest.builder()
                    .name(name)
                    .enabled(true)
                    .publisherId(media.getPublisherId())
                    .currency(Currency.JPY.name())
                    .floorPrice(11.00)
                    .positionId(1)
                    .mediaId(media.getId())
                    .positionId(1)
                    .coppa(true)
                    .sizeIds(List.of(10))
                    .floorPriceAutomated(true)
                    .testMode(false)
                    .categoryIds(List.of(1, 9))
                    .video(createVideo())
                    .build();
        }

        public AdSpotPreconditionBuilder getAllAdSpotsList() {
            this.response = adSpotService.getAll();

            this.adSpotsGetAllResponse = this.response.as(new TypeRef<GenericResponse<AdSpot>>() {});
            this.responseCode = response.getStatusCode();

            return this;
        }

        private List<AdSpot> getAdSpotsResponseList() {

            return Arrays.asList(response.jsonPath().getObject("items", AdSpot[].class));
        }

        public AdSpotPreconditionBuilder getAdSpotsWithFilter(Map<String, Object> queryParams) {
            this.response = adSpotService.getAdSpotsWithFilter(queryParams);

            this.adSpotsGetAllResponse = this.response.as(new TypeRef<GenericResponse<AdSpot>>() {});
            this.responseCode = response.getStatusCode();

            return this;
        }

        public AdSpotPreconditionBuilder deleteAdSpot(int id) {
            this.response = adSpotService.deleteAdSpot(id);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public AdSpotPreconditionBuilder setCredentials(User user){
            HttpClient.setCredentials(user);

            return this;
        }
        public AdSpotPrecondition build() {

            return new AdSpotPrecondition(this);
        }
    }
}
