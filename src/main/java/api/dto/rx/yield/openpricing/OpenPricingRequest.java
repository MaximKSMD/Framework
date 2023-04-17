package api.dto.rx.yield.openpricing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import zutils.ObjectMapperUtils;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenPricingRequest {

    private Rule rule;
    private String name;
    private String notes;
    private Boolean active;
    private Boolean priority;
    private Double floorPrice;
    private Integer publisherId;

    public String toJson() {
        return ObjectMapperUtils.toJson(this);
    }
}
