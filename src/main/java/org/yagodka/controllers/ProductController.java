package org.yagodka.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.yagodka.models.ProductDto;
import org.yagodka.models.ProductSummaryDto;
import org.yagodka.models.ProductUpdateDto;
import org.yagodka.responces.StandartResponse;
import org.yagodka.services.ProductService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/public/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable Long productId) {
        return productService.getProductById(productId);
    }

    @GetMapping
    public List<ProductSummaryDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public StandartResponse createProduct(@Valid @RequestBody ProductDto productDto) {
        return new StandartResponse("success", null, productService.createProduct(productDto));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "status", "error",
                        "message", "Missing required fields",
                        "errors", errors
                ));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<StandartResponse> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductUpdateDto updateDto) {
        productService.updateProduct(productId, updateDto);
        return ResponseEntity.ok(
                new StandartResponse("success", null, productId)
        );
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        HttpStatus status = productService.deleteProduct(productId);
        return ResponseEntity.status(status).build();
    }
}
