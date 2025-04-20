package org.yagodka.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yagodka.models.ProductDto;
import org.yagodka.models.ProductSummaryDto;
import org.yagodka.services.ProductService;

import java.util.List;

@RestController
@RequestMapping("/public/product")
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

    @GetMapping("s")
    public List<ProductSummaryDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody ProductDto productDto) {
        HttpStatus status = productService.createProduct(productDto);
        return ResponseEntity.status(status).build();
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductDto productDto) {
        HttpStatus status = productService.updateProduct(productId, productDto);
        return ResponseEntity.status(status).build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        HttpStatus status = productService.deleteProduct(productId);
        return ResponseEntity.status(status).build();
    }
}
