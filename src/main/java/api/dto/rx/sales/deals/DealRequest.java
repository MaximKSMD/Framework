package api.dto.rx.sales.deals;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class DealRequest {

    private String url;
    private String name;
    private Integer publisherId;
    private Integer privateAuctionId;
    private List<Integer> categoryIds;
    private List<Integer> dspIds;

    public String toJson() {

        return ObjectMapperUtils.toJson(this);
    }
}