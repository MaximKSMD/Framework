package api.dto.rx.sales.deals;

import zutils.ObjectMapperUtils;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Deal {
    private Integer id;
    private String name;
    private String token;
    private Integer dspId;
    private String dspName;
    private String endDate;
    private Boolean enabled;
    private String currency;
    private String startDate;
    private String createdBy;
    private String createdAt;
    private String updatedAt;
    private String updatedBy;
    private Double floorPrice;
    private String noEndDate;
    private List<Buyers> buyers;
    private Integer publisherId;
    private String publisherName;
    private String privateAuctionName;
    private Integer privateAuctionId;

    public String toJson() {
        return ObjectMapperUtils.toJson(this);
    }
}
