package api.dto.rx.inventory.media;

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
public class Media {

    private Integer id;
    private String  url;
    private String  name;
    private String  iconPath;
    private Boolean isEnabled;
    private String  createdAt;
    private String  updatedAt;
    private Integer platformId;
    private String  identifier;
    private String  appBundleId;
    private Boolean appReleased;
    private Integer publisherId;
    private String publisherName;
    private List<Integer> categoryIds;


    public String toJson() {

        return ObjectMapperUtils.toJson(this);
    }
}