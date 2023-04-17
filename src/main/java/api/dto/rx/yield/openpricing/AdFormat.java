package api.dto.rx.yield.openpricing;

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
public class AdFormat {

    private Boolean exclude;
    private List<Integer> adFormats;

    public String toJson() {
        return ObjectMapperUtils.toJson(this);
    }
}
