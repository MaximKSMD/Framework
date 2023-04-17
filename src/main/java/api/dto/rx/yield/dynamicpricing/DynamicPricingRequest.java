package api.dto.rx.yield.dynamicpricing;

import api.dto.rx.yield.openpricing.Rule;
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
public class DynamicPricingRequest {

    private Rule rule;
    private Integer id;
    private String name;
    private String notes;
    private Boolean active;
    private Integer priority;
    private Double floorPrice;
    private Integer publisherId;
    private String publisherName;

    public String toJson() {
        return ObjectMapperUtils.toJson(this);
    }
}
