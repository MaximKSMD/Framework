package api.dto.rx.admin.publisher;

import zutils.ObjectMapperUtils;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PublisherRequest {

    private String name;
    private String mail;
    private String domain;
    private String currency;
    private Boolean isEnabled;
    private String salesAccountName;
    private List<Integer> dspIds;
    private List<Integer> categoryIds;

    public String toJson() {

        return ObjectMapperUtils.toJson(this);
    }
}
