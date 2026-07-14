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

    @Column(nullable = false)
    @NotBlank(message = "Type cannot be empty")
    private String type;

    @Column(name = "display_name", nullable = false)
    @NotBlank(message = "Display name cannot be empty")
    private String displayName;

    @Column(nullable = false)
    @NotBlank(message = "Description cannot be empty")
    private String description;

    @Column(nullable = false)
    @Min(value = 0, message = "Estimation must be between 0 and 10")
    @Max(value = 10, message = "Estimation must be between 0 and 10")
    private Integer estimation;

    @Column(nullable = false)
    @NotBlank(message = "Image cannot be empty")
    private String image;

    @Column(name = "add_date", nullable = false)
    private String addDate;

    @Column(name = "update_date", nullable = false)
    private String updateDate;
}
