package org.yagodka.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityRequest {

    @NotBlank(message = "type обязателен для заполнения")
    private ProductType type;

    @NotBlank(message = "displayName обязателен для заполнения")
    private String displayName;

    private String minus;

    private String plus;

    private String description;

    @NotNull(message = "estimation обязателен для заполнения")
    @Min(value = 0, message = "Значение estimation должно быть от 0 до 10")
    @Max(value = 10, message = "Значение estimation должно быть от 0 до 10")
    private Integer estimation;

    private String image;

    private Boolean isFavourite;

    @NotBlank(message = "byUser обязателен для заполнения")
    private Long byUser;
}
