package api.dto.rx.protection;

import zutils.ObjectMapperUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProtectionRequest {
    private String name;
    private Integer publisherId;
    private Boolean active;
    private Integer typeId;
    private Rule rule;
    private Boolean adminOnly;
    private String publisherName;
    private String createdBy;
    private String updatedBy;

    public String toJson() {
        return ObjectMapperUtils.toJson(this);
    }
}
