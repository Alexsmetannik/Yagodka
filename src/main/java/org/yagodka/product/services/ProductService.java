package org.yagodka.product.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.yagodka.comment.dto.CommentDto;
import org.yagodka.comment.entity.Comment;
import org.yagodka.comment.repository.CommentRepository;
import org.yagodka.product.entity.Product;
import org.yagodka.product.dto.ProductDto;
import org.yagodka.product.dto.ProductSummaryDto;
import org.yagodka.product.dto.ProductUpdateDto;
import org.yagodka.product.entity.TypeProduct;
import org.yagodka.product.repository.ProductRepository;
import org.yagodka.product.repository.TypeProductRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {
    private static final Logger log = LogManager.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final TypeProductRepository typeProductRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, TypeProductRepository typeProductRepository, CommentRepository commentRepository) {
        this.productRepository = productRepository;
        this.typeProductRepository = typeProductRepository;
        this.commentRepository = commentRepository;
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

    @Transactional(readOnly = true)
    public List<ProductSummaryDto> getAllProducts(Integer count, String filter, String sort) {
        List<Product> products = productRepository.findAll();

        // Фильтрация (если указан параметр filter)
        if (filter != null) {
            switch (filter.toLowerCase()) {
                case "id":
                    products.sort(Comparator.comparing(Product::getId));
                    break;
                case "name":
                    products.sort(Comparator.comparing(Product::getName));
                    break;
                case "score":
                    products.sort(Comparator.comparing(Product::getOverallScore));
                    break;
                default:
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Invalid filter parameter. Allowed values: id, name, overall score");
            }
        }

        // Сортировка (если указан параметр sort)
        if (sort != null) {
            if ("desc".equalsIgnoreCase(sort)) {
                Collections.reverse(products);
            } else if (!"asc".equalsIgnoreCase(sort)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid sort parameter. Allowed values: asc, desc");
            }
        }

        // Лимит (если указан параметр count)
        if (count != null && count > 0) {
            products = products.stream()
                    .limit(count)
                    .collect(Collectors.toList());
        }

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
        TypeProduct typeProduct = typeProductRepository.findByName(productDto.getTypeProduct());
        if (typeProduct == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid product type");
        }

        Product product = new Product();
        product.setTypeProduct(typeProduct);
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setOverallScore(productDto.getOverallScore());
        product.setAuthor(productDto.getAuthor());
        product.setPhoto(productDto.getPhoto());

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
        if (updateDto.getPhoto() != null) {
            product.setPhoto(updateDto.getPhoto());
        }
        if (updateDto.getTypeProduct() != null) {
            TypeProduct typeProduct = typeProductRepository.findByName(updateDto.getTypeProduct());
            if (typeProduct == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid product type");
            }
            product.setTypeProduct(typeProduct);
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

    private ProductDto convertToDto(Product product, ProductDto productDto) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        List<Comment> comments = commentRepository.findAll(productDto.getComments());

        ProductDto dto = new ProductDto();
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setOverallScore(product.getOverallScore());
        dto.setAuthor(product.getAuthor());
        dto.setTypeProduct(product.getTypeProduct().getName());
        dto.setPhoto(product.getPhoto());
        dto.setComments(comments);

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
        dto.setPhoto(product.getPhoto());
        dto.setTypeProduct(product.getTypeProduct().getName());

        log.trace("Converted product {} to DTO", product.getId());
        return dto;
    }
}
