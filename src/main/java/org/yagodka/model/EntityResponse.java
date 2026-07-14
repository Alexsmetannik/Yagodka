package org.yagodka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityResponse {
    private Long id;
    private String type;
    private String displayName;
    private String description;
    private Integer estimation;
    private String image;
    private String addDate;
    private String updateDate;

    public static EntityResponse fromEntity(MyEntity myEntity) {
        return new EntityResponse(
                myEntity.getId(),
                myEntity.getType(),
                myEntity.getDisplayName(),
                myEntity.getDescription(),
                myEntity.getEstimation(),
                myEntity.getImage(),
                myEntity.getAddDate(),
                myEntity.getUpdateDate()
        );
    }
}
