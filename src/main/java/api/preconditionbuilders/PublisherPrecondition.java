package api.preconditionbuilders;

import api.core.client.HttpClient;
import api.dto.GenericResponse;
import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.admin.publisher.PublisherRequest;
import api.dto.rx.common.Currency;
import api.services.PublisherService;
import configurations.User;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static zutils.FakerUtils.*;

@Slf4j
@Getter
@AllArgsConstructor
public class PublisherPrecondition {

    private Integer responseCode;
    private Publisher publisherResponse;
    private PublisherRequest publisherRequest;
    private GenericResponse<Publisher> publisherGetAllResponse;

    private PublisherPrecondition(PublisherPreconditionBuilder builder) {
        this.responseCode = builder.responseCode;
        this.publisherRequest = builder.publisherRequest;
        this.publisherResponse = builder.publisherResponse;
        this.publisherGetAllResponse = builder.publisherGetAllResponse;
    }

    public static PublisherPreconditionBuilder publisher() {

        return new PublisherPreconditionBuilder();
    }

    public static class PublisherPreconditionBuilder {

        private Response response;
        private Integer responseCode;
        private Publisher publisherResponse;
        private PublisherRequest publisherRequest;
        private GenericResponse<Publisher> publisherGetAllResponse;
        private PublisherService publisherService = new PublisherService();

        public PublisherPreconditionBuilder createNewPublisher() {
            performPublisherCreation(captionWithSuffix("000Pub_Auto"), true);
            return this;
        }

        public PublisherPreconditionBuilder createNewPublisher(String name) {
            performPublisherCreation(name, true);

            return this;
        }

        public PublisherPreconditionBuilder createNewPublisher(String name, Boolean isEnabled) {
            performPublisherCreation(name, isEnabled);

            return this;
        }

        private void performPublisherCreation(String name, Boolean isEnabled) {
            this.publisherRequest = getPublisherRequest(name, isEnabled);

            this.response = publisherService.createPublisher(publisherRequest);
            this.publisherResponse = response.as(Publisher.class);
            this.responseCode = response.getStatusCode();
        }

        public PublisherPreconditionBuilder createNewPublisher(PublisherRequest publisherRequest) {
            this.publisherRequest = publisherRequest;
            this.response = publisherService.createPublisher(publisherRequest);
            this.publisherResponse = response.as(Publisher.class);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public PublisherPreconditionBuilder createNewPublisher(String name, Boolean isEnabled, Currency currency,
                                                               List<Integer> categoryIds, List<Integer>dspIds){
            this.publisherRequest = getPublisherRequest(name, isEnabled, currency, categoryIds, dspIds);
            this.response = publisherService.createPublisher(publisherRequest);
            this.publisherResponse = response.as(Publisher.class);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public PublisherPreconditionBuilder updatePublisher(Publisher publisher) {

            var updatePublisherRequest = PublisherRequest.builder()
                    .name(publisher.getName())
                    .salesAccountName(publisher.getSalesAccountName())
                    .mail(publisher.getMail())
                    .isEnabled(publisher.getIsEnabled())
                    .domain(publisher.getDomain())
                    .currency(publisher.getCurrency())
                    .categoryIds(publisher.getCategoryIds())
                    .dspIds(publisher.getDspIds())
                    .build();
            this.response = publisherService.updatePublisher(updatePublisherRequest, publisher.getId());
            this.responseCode = response.getStatusCode();

            return this;
        }

        public PublisherPreconditionBuilder getPublishersList() {
            this.response = publisherService.getAll();

            this.publisherGetAllResponse = this.response.as(new TypeRef<GenericResponse<Publisher>> () {});
            this.responseCode = response.getStatusCode();

            return this;
        }

        private PublisherRequest getPublisherRequest(String name, Boolean isEnabled) {

            return getPublisherRequest(name, isEnabled, Currency.JPY, List.of(1, 9), List.of(7));
        }

       // TODO need to add verification categoryIds and DSPIds. They should exist
        private PublisherRequest getPublisherRequest(String name, Boolean isEnabled, Currency currency,
                                                     List<Integer> categoryIds, List<Integer>dspIds) {

            return PublisherRequest.builder()
                    .name(name)
                    .salesAccountName("person_auto")
                    .mail(randomMail())
                    .isEnabled(isEnabled)
                    .domain(randomUrl())
                    .currency(currency.getAlias())
                    .categoryIds(categoryIds)
                    .dspIds(dspIds)
                    .build();
        }

        public PublisherPreconditionBuilder changePublisherStatus(int id, Boolean isEnabled) {
            this.response = publisherService.getPublisher(id);
            this.publisherResponse = this.response.as(Publisher.class);
            this.publisherRequest = this.response.as(PublisherRequest.class);

            this.publisherRequest.setIsEnabled(isEnabled);

            this.response = publisherService.updatePublisher(this.publisherRequest, id);
            this.responseCode = response.getStatusCode();

            return this;
        }

        private List<Publisher> getPublisherResponseList() {

            return Arrays.asList(response.jsonPath().getObject("items", Publisher[].class));
        }

        public PublisherPreconditionBuilder deletePublisher(int id) {
            this.response = publisherService.deletePublisher(id);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public PublisherPreconditionBuilder setCredentials(User user) {
            HttpClient.setCredentials(user);

            return this;
        }

        public PublisherPrecondition build() {
            HttpClient.setCredentials(User.TEST_USER);

            return new PublisherPrecondition(this);
        }

        public PublisherPreconditionBuilder getPublishersListWithFilter(Map<String, Object> queryParams) {
            this.response = publisherService.getPublisherWithFilter(queryParams);

            this.publisherGetAllResponse = this.response.as(new TypeRef<GenericResponse<Publisher>> () {});
            this.responseCode = response.getStatusCode();

            return this;
        }
    }
}
