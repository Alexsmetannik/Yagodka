package org.yagodka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String status;
    private List<String> errors;
    private T entities;

    public static <T> ApiResponse<T> success(T entities) {
        return new ApiResponse<>("success", null, entities);
    }

    public static <T> ApiResponse<T> error(List<String> errors) {
        return new ApiResponse<>("error", errors, null);
    }
}
