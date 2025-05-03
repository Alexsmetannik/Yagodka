package org.yagodka.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    @JsonProperty("id")
    private Long id;

    @Size(max = 1000, message = "dignities must be less than 1000 characters")
    @JsonProperty("dignities")
    private String dignities;

    @Size(max = 1000, message = "disadvantages must be less than 1000 characters")
    @JsonProperty("disadvantages")
    private String disadvantages;

    @NotBlank(message = "{comment.result.required}")
    @Size(max = 1000, message = "result must be less than 1000 characters")
    @JsonProperty("result")
    private String result;

    @NotNull(message = "{comment.myScore.required}")
    @DecimalMin(value = "0.0", message = "myScore must be at least 0.0")
    @DecimalMax(value = "5.0", message = "myScore must be at most 5.0")
    @JsonProperty("my score")
    private Double myScore;

    @NotBlank(message = "{comment.author.required}")
    @Size(max = 50, message = "author must be less than 50 characters")
    @JsonProperty("author")
    private String author;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDignities() {
        return dignities;
    }

    public void setDignities(String dignities) {
        this.dignities = dignities;
    }

    public String getDisadvantages() {
        return disadvantages;
    }

    public void setDisadvantages(String disadvantages) {
        this.disadvantages = disadvantages;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Double getMyScore() {
        return myScore;
    }

    public void setMyScore(Double myScore) {
        this.myScore = myScore;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
