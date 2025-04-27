package org.yagodka.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateDto {

    @Size(max = 50, message = "type must be less than 50 characters")
    @JsonProperty("type")
    private String typeProduct;

    @Size(max = 30, message = "name must be less than 30 characters")
    @JsonProperty("name")
    private String name;

    @Size(max = 1000, message = "description must be less than 1000 characters")
    @JsonProperty("description")
    private String description;

    @DecimalMin(value = "0.0", message = "overall score must be at least 0.0")
    @DecimalMax(value = "5.0", message = "overall score must be at most 5.0")
    @JsonProperty("overall score")
    private Double overallScore;

    @Size(max = 1000, message = "photo must be less than 1000 characters")
    @JsonProperty("photo")
    private String photo;

    public String getTypeProduct() {
        return typeProduct;
    }

    public void setTypeProduct(String typeProduct) {
        this.typeProduct = typeProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(Double overallScore) {
        this.overallScore = overallScore;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
