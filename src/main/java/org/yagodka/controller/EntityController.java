package org.yagodka.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yagodka.model.ApiResponse;
import org.yagodka.model.EntitiesResponse;
import org.yagodka.model.EntityRequest;
import org.yagodka.model.EntityResponse;
import org.yagodka.service.EntityService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/v1/public/entity")
public class EntityController {

    @Autowired
    private EntityService entityService;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<EntitiesResponse>>> getEntities(
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String order) {

        try {
            if (order != null && !order.isEmpty() &&
                    !"ASC".equalsIgnoreCase(order) && !"DESC".equalsIgnoreCase(order)) {
                ApiResponse<List<EntitiesResponse>> response = ApiResponse.error(List.of("request is bad"));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if (order == null || order.isEmpty()) {
                order = "ASC";
            }

            List<EntitiesResponse> entities = entityService.getEntities(filter, order);

            ApiResponse<List<EntitiesResponse>> response = ApiResponse.success(entities);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ApiResponse<List<EntitiesResponse>> response = ApiResponse.error(List.of("entity не найден"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<List<EntityResponse>>> createEntity(
            @Valid @RequestBody EntityRequest request) {

        try {
            EntityResponse createdEntity = entityService.createEntity(request);
            ApiResponse<List<EntityResponse>> response = ApiResponse.success(Collections.singletonList(createdEntity));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            ApiResponse<List<EntityResponse>> response = ApiResponse.error(List.of("entity не создан"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<List<EntityResponse>>> updateEntity(
            @PathVariable Long id,
            @RequestBody EntityRequest request) {

        try {
            if (request.getEstimation() != null && (request.getEstimation() < 0 || request.getEstimation() > 10)) {
                ApiResponse<List<EntityResponse>> response = ApiResponse.error(List.of("request is bad"));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            EntityResponse updatedEntity = entityService.updateEntity(id, request);
            ApiResponse<List<EntityResponse>> response = ApiResponse.success(Collections.singletonList(updatedEntity));
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            if (e.getMessage().equals("Entity not found")) {
                ApiResponse<List<EntityResponse>> response = ApiResponse.error(List.of("entity не найден"));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            ApiResponse<List<EntityResponse>> response = ApiResponse.error(List.of("request is bad"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ApiResponse<List<EntityResponse>> response = ApiResponse.error(List.of("request is bad"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEntity(@PathVariable Long id) {

        try {
            entityService.deleteEntity(id);
            ApiResponse<Void> response = new ApiResponse<>("success", null, null);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            if (e.getMessage().equals("Entity not found")) {
                ApiResponse<Void> response = ApiResponse.error(List.of("entity не найден"));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            ApiResponse<Void> response = ApiResponse.error(List.of("request is bad"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ApiResponse<Void> response = ApiResponse.error(List.of("request is bad"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EntityResponse>> getEntityById(@PathVariable Long id) {

        try {
            EntityResponse entity = entityService.getEntityById(id);
            ApiResponse<EntityResponse> response = ApiResponse.success(entity);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Entity not found")) {
                ApiResponse<EntityResponse> response = ApiResponse.error(List.of("entity не найден"));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            ApiResponse<EntityResponse> response = ApiResponse.error(List.of("request is bad"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ApiResponse<EntityResponse> response = ApiResponse.error(List.of("request is bad"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
