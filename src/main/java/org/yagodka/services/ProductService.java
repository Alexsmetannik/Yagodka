package org.yagodka.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.yagodka.entity.Product;
import org.yagodka.models.ProductDto;
import org.yagodka.models.ProductSummaryDto;
import org.yagodka.models.ProductUpdateDto;
import org.yagodka.repository.ProductRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor
//@Slf4j
@Transactional
public class ProductService {
    private static final Logger log = LogManager.getLogger(ProductService.class);
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public ProductDto getProductById(Long id) {
        log.info("Fetching product with id: {}", id);
        return productRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> {
                    log.error("Product not found with id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
                });
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

    public Long createProduct(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setOverallScore(productDto.getOverallScore());
        product.setAuthor(productDto.getAuthor());
        product.setPhotos(productDto.getPhotos());

        return productRepository.save(product).getId();
    }

    public void updateProduct(Long id, ProductUpdateDto updateDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        log.info("Updating product {} with data: {}", id, updateDto);

        if (updateDto.getName() != null) {
            product.setName(updateDto.getName());
        }
        if (updateDto.getDescription() != null) {
            product.setDescription(updateDto.getDescription());
        }
        if (updateDto.getOverallScore() != null) {
            product.setOverallScore(updateDto.getOverallScore());
        }
        if (updateDto.getPhotos() != null) {
            product.setPhotos(updateDto.getPhotos());
        }

        productRepository.save(product);
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
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setOverallScore(product.getOverallScore());
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
        dto.setOverallScore(product.getOverallScore());
        dto.setPhotos(product.getPhotos());

        log.trace("Converted product {} to DTO", product.getId());
        return dto;
    }
}
