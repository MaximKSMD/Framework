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
public class DynamicPricing {

    private Integer id;
    private Rule rule;
    private String notes;
    private Boolean active;
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;
    private Integer publisherId;
    private String  publisherName;

    public String toJson() {

        return ObjectMapperUtils.toJson(this);
    }
}
