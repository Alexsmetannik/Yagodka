package org.yagodka.product.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.yagodka.product.dto.ProductDto;
import org.yagodka.product.dto.ProductSummaryDto;
import org.yagodka.product.dto.ProductUpdateDto;
import org.yagodka.product.responces.StandardResponse;
import org.yagodka.product.services.ProductService;

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
    public List<ProductSummaryDto> getAllProducts(
        @RequestParam(required = false) Integer count,
        @RequestParam(required = false) String filter,
        @RequestParam(required = false) String sort) {
            return productService.getAllProducts(count, filter, sort);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public StandardResponse createProduct(@Valid @RequestBody ProductDto productDto) {
        return new StandardResponse("success", null, productService.createProduct(productDto));
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
    public ResponseEntity<StandardResponse> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductUpdateDto updateDto) {
        productService.updateProduct(productId, updateDto);
        return ResponseEntity.ok(
                new StandardResponse("success", null, productId)
        );
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        HttpStatus status = productService.deleteProduct(productId);
        return ResponseEntity.status(status).build();
    }
}
