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

    @NotBlank(message = "Type cannot be empty")
    private String type;

    @NotBlank(message = "Display name cannot be empty")
    private String displayName;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotNull(message = "Estimation cannot be null")
    @Min(value = 0, message = "Estimation must be between 0 and 10")
    @Max(value = 10, message = "Estimation must be between 0 and 10")
    private Integer estimation;

    @NotBlank(message = "Image cannot be empty")
    private String image;
}
