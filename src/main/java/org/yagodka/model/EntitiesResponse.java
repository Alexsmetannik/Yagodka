package org.yagodka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntitiesResponse {
    private Long id;
    private ProductType type;
    private String displayName;
    private String description;
    private Integer estimation;
    private String image;

    public static EntitiesResponse fromEntity(MyEntity myEntity) {
        return new EntitiesResponse(
                myEntity.getId(),
                myEntity.getType(),
                myEntity.getDisplayName(),
                myEntity.getDescription(),
                myEntity.getEstimation(),
                myEntity.getImage()
        );
    }
}
