package api.dto.rx.admin.publisher;

import zutils.ObjectMapperUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Publisher extends PublisherRequest{

    private Integer id;
    private String createdAt;
    private String updatedAt;
    private String createdBy;
    private String updatedBy;

    public String toJson() {

        return ObjectMapperUtils.toJson(this);
    }
}
