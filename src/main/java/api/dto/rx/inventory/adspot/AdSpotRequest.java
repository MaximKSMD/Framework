package api.dto.rx.inventory.adspot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import zutils.ObjectMapperUtils;

import api.dto.rx.inventory.adspot.Banner;
import api.dto.rx.inventory.adspot.Video;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdSpotRequest {

    private Integer ttl;
    private Boolean coppa;
    private String  name;

    private Video   video;
    private Banner  banner;
    private Integer mediaId;
    private Boolean enabled;
    private Boolean testMode;
    private String  currency;
    private String  mediaName;
    private Double  floorPrice;
    private Integer positionId;
    private Integer publisherId;
    private Boolean customSizing;
    private String  publisherName;
    private Integer prebidCacheEnabled;
    private Boolean floorPriceAutomated;
    @JsonIgnoreProperties("native")
    private Native anative;
    private List<Integer> sizeIds;
    private List<Integer> categoryIds;

    public String toJson() {

        return ObjectMapperUtils.toJson(this);
    }
}