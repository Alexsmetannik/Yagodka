package org.yagodka.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.yagodka.entity.Product;
import org.yagodka.models.ProductDto;
import org.yagodka.models.ProductSummaryDto;
import org.yagodka.repository.ProductRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private static final Logger log = LogManager.getLogger(ProductService.class);
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDto getProductById(Long id) {
        log.info("Fetching product with id: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product not found with id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
                });

        log.info("Found product: {}", product);
        return convertToDto(product);
    }

    public List<ProductSummaryDto> getAllProducts() {
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {
            log.warn("No products found in database");
            return Collections.emptyList();
        }

        log.debug("Found {} products", products.size());
        return products.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    public HttpStatus createProduct(ProductDto productDto) {
        log.info("Creating product with data: {}", productDto); // Добавьте это

        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setScore(productDto.getScore());
        product.setAuthor(productDto.getAuthor());

        Product saved = productRepository.save(product);
        log.info("Saved product: {}", saved); // И это

        return HttpStatus.CREATED;
    }

    public HttpStatus updateProduct(Long id, ProductDto productDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setScore(productDto.getScore());
        product.setAuthor(productDto.getAuthor());

        productRepository.save(product);
        return HttpStatus.OK;
    }

    public HttpStatus deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        productRepository.deleteById(id);
        return HttpStatus.NO_CONTENT;
    }

    private ProductDto convertToDto(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setScore(product.getScore());
        dto.setAuthor(product.getAuthor());

        log.debug("Converted Product to DTO: {}", dto); // Добавьте логирование
        return dto;
    }

    private ProductSummaryDto convertToSummaryDto(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        ProductSummaryDto dto = new ProductSummaryDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setScore(product.getScore());

        log.trace("Converted product {} to DTO", product.getId());
        return dto;
    }
}
