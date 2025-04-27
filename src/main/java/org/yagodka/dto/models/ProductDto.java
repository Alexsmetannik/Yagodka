package org.yagodka.dto.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    @NotBlank(message = "{product.type.required}")
    @JsonProperty("type")
    private String typeProduct;

    @NotBlank(message = "{product.name.required}")
    @Size(max = 30, message = "name must be less than 30 characters")
    @JsonProperty("name")
    private String name;

    @NotBlank(message = "{product.description.required}")
    @Size(max = 1000, message = "description must be less than 1000 characters")
    @JsonProperty("description")
    private String description;

    @NotNull(message = "{product.overallScore.required}")
    @DecimalMin(value = "0.0", message = "overall score must be at least 0.0")
    @DecimalMax(value = "5.0", message = "overall score must be at most 5.0")
    @JsonProperty("overall score")
    private Double overallScore;

    @NotBlank(message = "{product.author.required}")
    @Size(max = 50, message = "author must be less than 50 characters")
    @JsonProperty("author")
    private String author;

    @Size(max = 1000, message = "photos must be less than 1000 characters")
    @JsonProperty("photos")
    private String photos;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Double getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(Double overallScore) {
        this.overallScore = overallScore;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }
}