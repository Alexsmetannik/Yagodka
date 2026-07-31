package org.yagodka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityResponse {
    private Long id;
    private ProductType type;
    private String displayName;
    private String minus;
    private String plus;
    private String description;
    private Integer estimation;
    private String image;
    private Boolean isFavourite;
    private Long byUser;
    private String addDate;
    private String updateDate;

    public static EntityResponse fromEntity(MyEntity myEntity) {
        return new EntityResponse(
                myEntity.getId(),
                myEntity.getType(),
                myEntity.getDisplayName(),
                myEntity.getMinus(),
                myEntity.getPlus(),
                myEntity.getDescription(),
                myEntity.getEstimation(),
                myEntity.getImage(),
                myEntity.getIsFavourite(),
                myEntity.getByUser(),
                myEntity.getAddDate(),
                myEntity.getUpdateDate()
        );
    }
}
