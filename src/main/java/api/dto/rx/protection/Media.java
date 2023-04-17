package api.dto.rx.protection;

import zutils.ObjectMapperUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Media {

    private List<Integer> media;
    private Boolean exclude;

    public String toJson() {
        return ObjectMapperUtils.toJson(this);
    }
}
