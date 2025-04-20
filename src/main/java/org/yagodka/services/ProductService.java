package org.yagodka.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.yagodka.entity.Product;
import org.yagodka.models.ProductDto;
import org.yagodka.models.ProductSummaryDto;
import org.yagodka.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

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
        );
    }

    private ProductSummaryDto convertToSummaryDto(Product product) {
        return new ProductSummaryDto(
        );
    }
}
