package org.yagodka.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "entities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    @NotBlank(message = "type обязателен для заполнения")
    private ProductType type;

    @Column(name = "display_name", nullable = false)
    @NotBlank(message = "displayName обязателен для заполнения")
    private String displayName;

    @Column(name = "minus")
    private String minus;

    @Column(name = "plus")
    private String plus;

    @Column(name = "description")
    private String description;

    @Column(name = "estimation", nullable = false)
    @Min(value = 0, message = "Значение estimation должно быть от 0 до 10")
    @Max(value = 10, message = "Значение estimation должно быть от 0 до 10")
    @NotBlank(message = "estimation обязателен для заполнения")
    private Integer estimation;

    @Column(name = "image")
    private String image;

    @Column(name = "is_favourite")
    private Boolean isFavourite;

    @Column(name = "by_user", nullable = false)
    @NotBlank(message = "by_user обязателен для заполнения")
    private Long byUser;

    @Column(name = "add_date", nullable = false)
    @NotBlank(message = "add_date обязателен для заполнения")
    private String addDate;

    @Column(name = "update_date", nullable = false)
    @NotBlank(message = "update_date обязателен для заполнения")
    private String updateDate;
}
