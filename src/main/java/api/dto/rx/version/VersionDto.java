package api.dto.rx.version;

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
public class VersionDto {

    private String date;
    private String author;
    private String version;
    private String appName;
    private String gitHash;

    public String toJson() {

        return ObjectMapperUtils.toJson(this);
    }

}
