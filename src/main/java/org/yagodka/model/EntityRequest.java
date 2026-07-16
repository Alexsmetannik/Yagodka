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

    @NotBlank(message = "Type обязателен для заполнения")
    private String type;

    @NotBlank(message = "DisplayName обязателен для заполнения")
    private String displayName;

    @NotBlank(message = "Description обязателен для заполнения")
    private String description;

    @NotNull(message = "Estimation обязателен для заполнения")
    @Min(value = 0, message = "Значение Estimation должно быть от 0 до 10")
    @Max(value = 10, message = "Значение Estimation должно быть от 0 до 10")
    private Integer estimation;

    @NotBlank(message = "Image обязателен для заполнения")
    private String image;
}
