package api.preconditionbuilders;

import api.core.client.HttpClient;
import api.dto.GenericResponse;
import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.inventory.media.Media;
import api.dto.rx.inventory.media.MediaRequest;
import api.dto.rx.platformtype.PlatformType;
import api.services.MediaService;
import configurations.User;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static api.preconditionbuilders.PlatformTypesPrecondition.platformType;
import static java.lang.String.format;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Getter
@AllArgsConstructor
public class MediaPrecondition {

    private Media mediaResponse;
    private Integer responseCode;
    private MediaRequest mediaRequest;
    private GenericResponse<Media> mediaGetAllResponse;

    private MediaPrecondition(MediaPreconditionBuilder builder) {
        this.responseCode = builder.responseCode;
        this.mediaRequest = builder.mediaRequest;
        this.mediaResponse = builder.mediaResponse;
        this.mediaGetAllResponse = builder.mediaGetAllResponse;
    }

    public static MediaPreconditionBuilder media() {

        return new MediaPreconditionBuilder();
    }

    public static class MediaPreconditionBuilder {

        private Response response;
        private Media mediaResponse;
        private Integer responseCode;
        private MediaRequest mediaRequest;
        private GenericResponse<Media> mediaGetAllResponse;
        private final MediaService mediaService = new MediaService();

        public MediaPreconditionBuilder getMediaById(int id) {
            this.response = mediaService.getMediaById(id);
            this.responseCode = response.getStatusCode();
            this.mediaResponse = response.as(Media.class);

            return this;
        }

        public MediaPreconditionBuilder createNewMedia() {

            this.mediaRequest = createMediaRequest("MediaAuto");
            this.response = mediaService.createMedia(mediaRequest);
            this.mediaResponse = response.as(Media.class);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public MediaPreconditionBuilder createNewMedia(MediaRequest mediaRequest) {
            this.mediaRequest = mediaRequest;
            this.response = mediaService.createMedia(mediaRequest);
            this.mediaResponse = response.as(Media.class);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public MediaPreconditionBuilder createNewMedia(String name, Boolean isEnabled) {
            this.mediaRequest = createMediaRequest(name, isEnabled);
            this.response = mediaService.createMedia(mediaRequest);
            this.mediaResponse = response.as(Media.class);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public MediaPreconditionBuilder createNewMedia(String name, String url) {
            this.mediaRequest = createMediaRequest(name, url);
            this.response = mediaService.createMedia(mediaRequest);
            this.mediaResponse = response.as(Media.class);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public MediaPreconditionBuilder createNewMedia(String name, Integer id, Boolean isEnabled) {
            this.mediaRequest = createMediaRequest(name, id, isEnabled);
            this.response = mediaService.createMedia(mediaRequest);
            this.mediaResponse = response.as(Media.class);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public MediaPreconditionBuilder createNewMedia(String name) {
            this.mediaRequest = createMediaRequest(name);
            this.response = mediaService.createMedia(mediaRequest);
            this.mediaResponse = response.as(Media.class);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public MediaPreconditionBuilder createNewMedia(String name, String mediaType, String url, String bundle, Boolean isEnabled) {
            this.mediaRequest = createMediaRequest(name, mediaType, url, bundle, isEnabled);
            this.response = mediaService.createMedia(mediaRequest);
            this.mediaResponse = response.as(Media.class);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public MediaPreconditionBuilder getAllMediaList() {
            this.response = mediaService.getAll();
            this.mediaGetAllResponse = this.response.as(new TypeRef<GenericResponse<Media>>() {
            });
            this.responseCode = response.getStatusCode();

            return this;
        }

        public MediaPreconditionBuilder getMediaWithFilter(Map<String, Object> queryParams) {
            this.response = mediaService.getMediaWithFilter(queryParams);
            this.mediaGetAllResponse = this.response.as(new TypeRef<GenericResponse<Media>>() {
            });
            this.responseCode = response.getStatusCode();

            return this;
        }

        public MediaPreconditionBuilder deleteMedia(int id) {
            this.response = mediaService.deleteMedia(id);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public MediaPreconditionBuilder setCredentials(User user) {
            HttpClient.setCredentials(user);

            return this;
        }

        public MediaPrecondition.MediaPreconditionBuilder updateMedia(Media media) {

            var updateMediaRequest = Media.builder()
                    .name(media.getName())
                    .publisherId(media.getPublisherId())
                    .platformId(media.getPlatformId())
                    .url(media.getUrl())
                    .isEnabled(media.getIsEnabled())
                    .categoryIds(media.getCategoryIds())
                    .build();
            this.response = mediaService.updateMedia(updateMediaRequest);
            this.responseCode = response.getStatusCode();
            this.mediaResponse = response.as(Media.class);

            return this;
        }

        public MediaPrecondition.MediaPreconditionBuilder changeMediaStatus(int id, Boolean isEnabled) {
            this.response = mediaService.getMediaById(id);
            this.mediaResponse = this.response.as(Media.class);

            this.mediaResponse.setIsEnabled(isEnabled);

            this.response = mediaService.updateMedia(this.mediaResponse);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public MediaPrecondition build() {
            HttpClient.setCredentials(User.TEST_USER);

            return new MediaPrecondition(this);
        }

        private List<Media> getMediaResponseList() {

            return Arrays.asList(response.jsonPath().getObject("items", Media[].class));
        }

        private Publisher createPublisher() {

            return PublisherPrecondition.publisher()
                    .createNewPublisher()
                    .build()
                    .getPublisherResponse();
        }

        private MediaRequest createMediaRequest(String name) {

            return createMediaRequest(name, "http://autotestrak987.com");
        }

        private MediaRequest createMediaRequest(String name, String url) {

            Publisher publisher = createPublisher();

            return MediaRequest.builder()
                    .name(captionWithSuffix(name))
                    .publisherId(publisher.getId())
                    .platformId(2)
                    .url(url)
                    .isEnabled(true)
                    .categoryIds(List.of(1, 9))
                    .build();
        }

        private MediaRequest createMediaRequest(String name, String mediaType,
                                                String url, String bundle, Boolean isEnabled) {

            Publisher publisher = createPublisher();

            List<PlatformType> platformTypes = platformType()
                    .getAllPlatformsList().build().getPlatformTypesGetAllResponse().getItems();

            Integer platformId = platformTypes.stream()
                    .filter(x -> x.getName().equalsIgnoreCase(mediaType))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException
                            (String.format("Platform Id with name %s does not find", mediaType))).getId();

            return MediaRequest.builder()
                    .name(captionWithSuffix(name))
                    .publisherId(publisher.getId())
                    .platformId(platformId)
                    .appBundleId(bundle)
                    .url(url)
                    .isEnabled(isEnabled)
                    .categoryIds(List.of(1, 9))
                    .build();
        }

        private MediaRequest createMediaRequest(String name, Boolean isEnabled) {

            Publisher publisher = createPublisher();

            return createMediaRequest(name, publisher.getId(), isEnabled);
        }

        private MediaRequest createMediaRequest(String name, Boolean isEnabled, int publisherId) {

            return createMediaRequest(name, publisherId, isEnabled);
        }

        private MediaRequest createMediaRequest(String name, int id, Boolean isEnabled) {

            return MediaRequest.builder()
                    .name(captionWithSuffix(name))
                    .publisherId(id)
                    .platformId(2)
                    .url("http://autotestrak987.com")
                    .isEnabled(isEnabled)
                    .categoryIds(List.of(1, 9))
                    .build();
        }
    }
}
