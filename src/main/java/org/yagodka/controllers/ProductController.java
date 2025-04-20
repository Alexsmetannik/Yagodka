package org.yagodka.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yagodka.models.ApiResponse;
import org.yagodka.models.ProductDto;
import org.yagodka.models.ProductSummaryDto;
import org.yagodka.services.ProductService;

import java.util.List;

@RestController
@RequestMapping("/public/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

/*    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }*/

    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable Long productId) {
        return productService.getProductById(productId);
    }

    @GetMapping
    public List<ProductSummaryDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createProduct(@RequestBody ProductDto productDto) {
        Long productId = productService.createProduct(productDto);
        return ResponseEntity.ok(
                new ApiResponse("success", null, productId)
        );
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductDto productDto) {
        productService.updateProduct(productId, productDto);
        return ResponseEntity.ok(
                new ApiResponse("success", null, productId)
        );
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        HttpStatus status = productService.deleteProduct(productId);
        return ResponseEntity.status(status).build();
    }
}
