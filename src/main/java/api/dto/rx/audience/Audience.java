package api.dto.rx.audience;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zutils.ObjectMapperUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Audience extends AudienceRequest {

    private Integer id;

    public String toJson() {

        return ObjectMapperUtils.toJson(this);
    }
}
