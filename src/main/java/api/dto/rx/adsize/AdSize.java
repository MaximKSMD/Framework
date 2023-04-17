package api.dto.rx.adsize;

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
public class AdSize {

    private int id;
    private String name;
    private boolean mobile;
    private boolean clickTrackable;
    private AdType adType;
    private String createdAt;
    private String updatedAt;

    public String toJson() {
        return ObjectMapperUtils.toJson(this);
    }
}
