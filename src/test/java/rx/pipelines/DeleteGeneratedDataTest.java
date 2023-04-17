package rx.pipelines;

import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.admin.user.UserDto;
import api.dto.rx.audience.Audience;
import api.dto.rx.demandsource.DemandSource;
import api.dto.rx.inventory.adspot.AdSpot;
import api.dto.rx.inventory.media.Media;
import api.dto.rx.privateauction.PrivateAuction;
import api.dto.rx.protection.Protection;
import api.dto.rx.sales.deals.Deal;
import api.dto.rx.yield.dynamicpricing.DynamicPricing;
import api.dto.rx.yield.openpricing.OpenPricing;
import api.preconditionbuilders.*;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;
import rx.BaseTest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static configurations.User.USER_FOR_DELETION;

@Slf4j
public class DeleteGeneratedDataTest extends BaseTest {

    private static final String PREFIX_MEDIA = "auto";
    private static final String PREFIX_USERS_2 = "auto";
    private static final String PREFIX_AD_SPOTS = "auto";
    private static final String PREFIX_AUDIENCE = "auto";
    private static final String PREFIX_PUBLISHERS = "auto";
    private static final String PREFIX_OPEN_PRICING = "auto";
    private static final String PREFIX_PROTECTIONS_1 = "api";
    private static final String PREFIX_DEMAND_SOURCE = "auto";
    private static final String PREFIX_PROTECTIONS_2 = "auto";
    private static final String PREFIX_USERS_1 = "Test Account";
    private static final String PREFIX_DYNAMIC_PRICING = "auto";
    private static final String PREFIX_PRIVATE_AUCTIONS = "auto";


    @Test(priority = 4)
    public void deleteProtectionsByPrefix() {
        deleteProtections(PREFIX_PROTECTIONS_1);
        deleteProtections(PREFIX_PROTECTIONS_2);
    }

    @Step("Delete Protections")
    private void deleteProtections(String prefix) {
        final var deleted = new AtomicInteger(0);
        getAllProtectionIdsByParams(prefix).forEach(
                protectionId -> {
                    if (ProtectionsPrecondition.protection()
                            .setCredentials(USER_FOR_DELETION)
                            .deleteProtection(protectionId)
                            .build()
                            .getResponseCode() == HttpStatus.SC_NO_CONTENT) {
                        deleted.getAndIncrement();
                    }
                });
        log.info(String.format("Deleted %s protections items", deleted));
    }

    @Test(priority = 3)
    public void deleteAdSpots() {
        final var deleted = new AtomicInteger(0);
        getAllAdSpotIdsByParams().forEach(
                adSpotId -> {
                    if (AdSpotPrecondition.adSpot()
                            .setCredentials(USER_FOR_DELETION)
                            .deleteAdSpot(adSpotId)
                            .build()
                            .getResponseCode() == HttpStatus.SC_NO_CONTENT) {
                        deleted.getAndIncrement();
                    }
                });
        log.info(String.format("Deleted ad spots items %s", deleted));
    }

    @Test(priority = 5)
    public void deleteMedia() {
        var deleted = new AtomicInteger(0);
        getAllMediaIdsByParams().forEach(
                mediaId -> {
                    if (MediaPrecondition.media().
                            setCredentials(USER_FOR_DELETION).
                            deleteMedia(mediaId)
                            .build()
                            .getResponseCode() == HttpStatus.SC_NO_CONTENT) {
                        deleted.getAndIncrement();
                    }
                });
        log.info(String.format("Deleted media items %s", deleted));
    }

    @Test(priority = 1)
    public void deleteOpenPricing() {
        var deleted = new AtomicInteger(0);
        getAllPricingIdsByParams().forEach(pricingId -> {
            if (OpenPricingPrecondition.openPricing()
                    .setCredentials(USER_FOR_DELETION)
                    .deleteOpenPricing(pricingId)
                    .build()
                    .getResponseCode() == HttpStatus.SC_NO_CONTENT) {
                deleted.getAndIncrement();
            }
        });
        log.info(String.format("Deleted open pricing items %s", deleted));
    }

    @Test(priority = 2)
    public void deleteDynamicPricing() {
        var deleted = new AtomicInteger(0);
        getAllDynamicPricingIdsByParams().forEach(dynamicPricingId -> {
            if (DynamicPricingPrecondition.dynamicPricing()
                    .setCredentials(USER_FOR_DELETION)
                    .deleteDynamicPricing(dynamicPricingId)
                    .build()
                    .getResponseCode() == HttpStatus.SC_NO_CONTENT) {
                deleted.getAndIncrement();
            }
        });
        log.info(String.format("Deleted dynamic pricing items %s", deleted));
    }

    @Test(priority = 6)
    public void deleteUsers() {
        var deleted = new AtomicInteger(0);
        getAllUserIds().forEach(userId -> {
            if (UsersPrecondition.user()
                    .setCredentials(USER_FOR_DELETION)
                    .deleteUser(userId)
                    .build()
                    .getResponseCode() == HttpStatus.SC_NO_CONTENT) {
                deleted.getAndIncrement();
            }
        });
        log.info(String.format("Deleted users items %s", deleted));
    }


    @Test(priority = 7)
    public void deletePublishers() {
        var deleted = new AtomicInteger(0);
        var relatedToUsersPublisherIds = getRelatedToUserPublisherIds();
        var relatedToDealsPublisherIds = getRelatedToDealsPublisherIds();
        var relatedToMediaPublisherIds = getRelatedToMediaPublisherIds();
        var relatedToAdSpotsPublisherIds = getRelatedToAdSpotsPublisherIds();
        var relatedToProtectionsPublisherIds = getRelatedToProtectionPublisherIds();
        var relatedToOpenPricingPublisherIds = getRelatedToOpenPricingPublisherIds();
        var relatedToPrivateAuctionsPublisherIds = getRelatedToPrivateAuctionsPublisherIds();

        getAllPublisherIdsByParams().stream()
                .filter(pubId -> !relatedToPrivateAuctionsPublisherIds.contains(pubId) &&
                        !relatedToUsersPublisherIds.contains(pubId) &&
                        !relatedToMediaPublisherIds.contains(pubId) &&
                        !relatedToDealsPublisherIds.contains(pubId) &&
                        !relatedToAdSpotsPublisherIds.contains(pubId) &&
                        !relatedToProtectionsPublisherIds.contains(pubId) &&
                        !relatedToOpenPricingPublisherIds.contains(pubId))
                .collect(Collectors.toList())
                .forEach(publisherId -> {
                            if (PublisherPrecondition.publisher()
                                    .setCredentials(USER_FOR_DELETION)
                                    .deletePublisher(publisherId)
                                    .build()
                                    .getResponseCode() == HttpStatus.SC_NO_CONTENT) {
                                deleted.getAndIncrement();
                            }
                        });
        log.info(String.format("Deleted publishers items %s", deleted));
    }

    @Test(priority = 8)
    public void updatePublishers() {
        var updated = new AtomicInteger(0);
        getAllPublishersByParams().stream()
                .filter(publisher-> publisher.getIsEnabled().equals(true))
                .forEach(publisher -> {
                    publisher.setIsEnabled(false);
                    if (PublisherPrecondition.publisher()
                            .updatePublisher(publisher)
                            .build()
                            .getResponseCode() == HttpStatus.SC_NO_CONTENT) {
                        updated.getAndIncrement();
                    }
                });

        log.info(String.format("Updated publishers items %s ", updated));
    }

    @Test(priority = 9)
    public void deleteDemandSource() {
        var deleted = new AtomicInteger(0);
        getAllDemandSourceIdsByParams().forEach(uspId -> {
            if (DemandSourcePrecondition.demandSource()
                    .setCredentials(USER_FOR_DELETION)
                    .deleteDemandSource(uspId)
                    .build()
                    .getResponseCode() == HttpStatus.SC_NO_CONTENT) {
                deleted.getAndIncrement();
            }
        });

        log.info(String.format("Deleted Demand Source items %s ", deleted));
    }

    @Test(priority = 9)
    public void deleteAudiences() {
        var deleted = new AtomicInteger(0);
        getAllAudienceIds().forEach(audId -> {
            if (AudiencePrecondition.audience()
                    .setCredentials(USER_FOR_DELETION)
                    .deleteAudience(audId)
                    .build()
                    .getResponseCode() == HttpStatus.SC_NO_CONTENT) {
                deleted.getAndIncrement();
            }
        });

        log.info(String.format("Deleted Audience items %s ", deleted));
    }

    private List<Integer> getAllAudienceIds() {

        return AudiencePrecondition.audience()
                .getAll()
                .build()
                .getAudienceGetAllResponse()
                .getItems().stream()
                .map(Audience::getId)
                .collect(Collectors.toList());
    }

    private List<Integer> getAllMediaIdsByParams() {

        return MediaPrecondition.media()
                .getMediaWithFilter(Map.of("search", PREFIX_MEDIA, "sort", "id-desc"))
                .build()
                .getMediaGetAllResponse()
                .getItems().stream()
                .map(Media::getId)
                .collect(Collectors.toList());
    }

    private List<Integer> getAllAdSpotIdsByParams() {

        return AdSpotPrecondition.adSpot()
                .getAdSpotsWithFilter(Map.of("search", PREFIX_AD_SPOTS, "sort", "id-desc"))
                .build()
                .getAdSpotsGetAllResponse()
                .getItems().stream()
                .map(AdSpot::getId)
                .collect(Collectors.toList());
    }

    private List<Integer> getAllPricingIdsByParams() {

        return OpenPricingPrecondition.openPricing()
                .getOpenPricingWithFilter(Map.of("search", PREFIX_OPEN_PRICING, "sort", "id-desc"))
                .build()
                .getOpenPricingGetAllResponse()
                .getItems().stream()
                .map(OpenPricing::getId)
                .collect(Collectors.toList());
    }

    private List<Integer> getAllDynamicPricingIdsByParams() {

        return DynamicPricingPrecondition.dynamicPricing()
                .getDynamicPricingWithFilter(Map.of("search", PREFIX_DYNAMIC_PRICING, "sort", "id-desc"))
                .build()
                .getDynamicPricingGetAllResponse()
                .getItems().stream()
                .map(DynamicPricing::getId)
                .collect(Collectors.toList());
    }

    private List<Integer> getAllDemandSourceIdsByParams() {

        return DemandSourcePrecondition.demandSource()
                .getDSPsWithFilter(Map.of("name", PREFIX_DEMAND_SOURCE, "sort", "created_at-desc"))
                .build()
                .getDemandSourceGetAllResponse()
                .getItems().stream()
                .map(DemandSource::getId)
                .collect(Collectors.toList());
    }

    private List<Integer> getAllProtectionIdsByParams(String prefix) {

        return ProtectionsPrecondition.protection()
                .getProtectionsWithFilter(Map.of("search", prefix, "sort", "id-desc"))
                .build()
                .getProtectionsGetAllResponse()
                .getItems().stream()
                .map(Protection::getId)
                .collect(Collectors.toList());
    }

    private List<Integer> getRelatedToDealsPublisherIds() {

        return DealPrecondition.deal()
                .getAllDealsList()
                .build()
                .getDealGetAllResponse()
                .getItems().stream()
                .filter(deal -> deal.getName().contains(PREFIX_PRIVATE_AUCTIONS))
                .map(Deal::getPublisherId)
                .collect(Collectors.toList());
    }

    private List<Integer> getRelatedToPrivateAuctionsPublisherIds() {

        return PrivateAuctionPrecondition.privateAuction()
                .getAllPrivateAuctions()
                .build()
                .getPrivateAuctionsGetAllResponse()
                .getItems().stream()
                .filter(privateAuction -> privateAuction.getName().contains(PREFIX_PRIVATE_AUCTIONS))
                .map(PrivateAuction::getPublisherId)
                .collect(Collectors.toList());
    }

    private List<Integer> getRelatedToAdSpotsPublisherIds() {

        return AdSpotPrecondition.adSpot()
                .getAllAdSpotsList()
                .build()
                .getAdSpotsGetAllResponse()
                .getItems().stream()
                .filter(adSpot -> adSpot.getName().contains(PREFIX_AD_SPOTS))
                .map(AdSpot::getPublisherId)
                .collect(Collectors.toList());
    }

    private List<Integer> getRelatedToMediaPublisherIds() {

        return MediaPrecondition.media()
                .getAllMediaList()
                .build()
                .getMediaGetAllResponse()
                .getItems().stream()
                .filter(media -> media.getName().contains(PREFIX_MEDIA))
                .map(Media::getPublisherId)
                .collect(Collectors.toList());
    }

    private List<Integer> getRelatedToProtectionPublisherIds() {

        return ProtectionsPrecondition.protection()
                .getAllProtectionsList()
                .build()
                .getProtectionsGetAllResponse()
                .getItems().stream()
                .filter(protection -> protection.getName().contains(PREFIX_PROTECTIONS_1) ||
                                protection.getName().contains(PREFIX_PROTECTIONS_2))
                .map(Protection::getPublisherId)
                .collect(Collectors.toList());
    }

    private List<Integer> getRelatedToUserPublisherIds() {

        return UsersPrecondition.user()
                .getAllUsers()
                .build()
                .getUserGetAllResponse()
                .getItems().stream()
                .filter(user -> user.getName().contains(PREFIX_USERS_1) ||
                        user.getName().toLowerCase().contains(PREFIX_USERS_2))
                .map(UserDto::getPublisherId)
                .collect(Collectors.toList());
    }

    private List<Integer> getRelatedToOpenPricingPublisherIds() {

        return OpenPricingPrecondition.openPricing()
                .getOpenPricingList()
                .build()
                .getOpenPricingGetAllResponse()
                .getItems().stream()
                .filter(op -> op.getName().contains(PREFIX_PUBLISHERS))
                .map(OpenPricing::getPublisherId)
                .collect(Collectors.toList());
    }

    private List<Integer> getAllPublisherIdsByParams() {

        return getAllPublishersByParams().stream()
                .map(Publisher::getId)
                .collect(Collectors.toList());
    }

    private List<Publisher> getAllPublishersByParams() {

        return PublisherPrecondition.publisher()
                .getPublishersListWithFilter(Map.of("search", PREFIX_PUBLISHERS, "sort", "id-desc"))
                .build()
                .getPublisherGetAllResponse()
                .getItems();
    }

    private List<Integer> getAllUserIds() {

        return UsersPrecondition.user()
                .getAllUsers()
                .build()
                .getUserGetAllResponse()
                .getItems().stream()
                .filter(user -> user.getName().contains(PREFIX_USERS_1) ||
                        user.getName().toLowerCase().contains(PREFIX_USERS_2))
                .map(UserDto::getId)
                .collect(Collectors.toList());
    }
}
