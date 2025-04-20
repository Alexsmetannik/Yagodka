package org.yagodka.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateDto {
    private String name;
    private String description;
    private Double score;
    private String author;
}
