package org.yagodka.comment.services;

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
import org.yagodka.product.dto.ProductDto;
import org.yagodka.product.entity.Product;
import org.yagodka.product.repository.ProductRepository;
import org.yagodka.product.repository.TypeProductRepository;
import org.yagodka.product.services.ProductService;

@Service
@Transactional
public class CommentService {
    private static final Logger log = LogManager.getLogger(ProductService.class);
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional(readOnly = true)
    public CommentDto getCommentById(Long id) {
        log.info("Fetching comment with id: {}", id);
        return commentRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> {
                    log.error("Comment not found with id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
                });
    }









    private CommentDto convertToDto(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("Comment cannot be null");
        }

        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setProductId(comment.getProductId().getId());
        dto.setDignities(comment.getDignities());
        dto.setDisadvantages(comment.getDisadvantages());
        dto.setResult(comment.getResult());
        dto.setMyScore(comment.getMyScore());
        dto.setAuthor(comment.getAuthor());

        log.debug("Converted Comment to DTO: {}", dto); // Добавьте логирование
        return dto;
    }
}
