package org.yagodka.services;

import org.yagodka.models.ProductCreateDto;
import org.yagodka.models.ProductDto;
import org.yagodka.models.ProductSummaryDto;
import org.yagodka.entity.Product;
import org.yagodka.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        return convertToDto(product);
    }

    public List<ProductSummaryDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    public HttpStatus createProduct(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setScore(productDto.getScore());
        product.setAuthor(productDto.getAuthor());

        productRepository.save(product);
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
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getScore(),
                product.getAuthor()
        );
    }

    private ProductSummaryDto convertToSummaryDto(Product product) {
        return new ProductSummaryDto(
                product.getId(),
                product.getName(),
                product.getScore()
        );
    }
}
