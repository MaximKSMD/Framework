package api.dto.rx.inventory.adspot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import zutils.ObjectMapperUtils;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdSpot {

    private Integer id;
    private Integer ttl;
    private String  name;
    private Boolean coppa;
    private Integer mediaId;
    private Boolean enabled;
    @JsonProperty(value="native")
    private Native  anative;
    private Boolean testMode;
    private String  currency;
    private String  mediaName;
    private String  createdAt;
    private String  updatedAt;
    private Double  floorPrice;
    private Integer positionId;
    private Integer publisherId;
    private Boolean customSizing;
    private String  publisherName;

    private List<Integer> sizeIds;
    private List<Integer> categoryIds;

    private Boolean prebidCacheEnabled;
    private Boolean floorPriceAutomated;

    public String toJson() {

        return ObjectMapperUtils.toJson(this);
    }
}