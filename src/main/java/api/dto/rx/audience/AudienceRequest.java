package api.dto.rx.audience;

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
public class AudienceRequest {

    private String name;
    private String segmentId;
    private String description;
    private String publisherCode;

    public String toJson() {

        return ObjectMapperUtils.toJson(this);
    }
}
