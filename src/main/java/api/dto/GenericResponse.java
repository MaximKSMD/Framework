package api.dto;

import api.dto.rx.inventory.media.Media;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponse<T> {
     private Integer total;
     private List<T> items;
}
