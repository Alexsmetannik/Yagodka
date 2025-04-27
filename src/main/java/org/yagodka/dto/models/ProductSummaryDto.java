package org.yagodka.dto.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSummaryDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("type")
    private String typeProduct;

    @JsonProperty("name")
    private String name;

    @JsonProperty("overall score")
    private Double overallScore;

    @JsonProperty("myScore")
    private Double myScore;

    @JsonProperty("photo")
    private String photo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Double getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(Double overallScore) {
        this.overallScore = overallScore;
    }

    public Double getMyScore() {
        return myScore;
    }

    public void setMyScore(Double myScore) {
        this.myScore = myScore;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
