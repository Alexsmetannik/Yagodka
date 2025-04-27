package org.yagodka.comment.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.yagodka.comment.dto.CommentDto;
import org.yagodka.comment.dto.CommentSummaryDto;
import org.yagodka.comment.dto.CommentUpdateDto;
import org.yagodka.comment.entity.Comment;
import org.yagodka.comment.repository.CommentRepository;
import org.yagodka.product.dto.ProductUpdateDto;
import org.yagodka.product.entity.Product;
import org.yagodka.product.entity.TypeProduct;
import org.yagodka.product.repository.ProductRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {
    private static final Logger log = LogManager.getLogger(CommentService.class);
    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, ProductRepository productRepository) {
        this.commentRepository = commentRepository;
        this.productRepository = productRepository;
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

    @Transactional(readOnly = true)
    public List<CommentSummaryDto> getAllComments(Integer count) {
        List<Comment> comments = commentRepository.findAll();

        // Лимит (если указан параметр count)
        if (count != null && count > 0) {
            comments = comments.stream()
                    .limit(count)
                    .collect(Collectors.toList());
        }

        if (comments.isEmpty()) {
            log.warn("No comments found in database");
            return Collections.emptyList();
        }

        log.debug("Found {} comments", comments.size());

        return comments.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    public Long createComment(CommentDto commentDto) {
        Optional<Product> product = productRepository.findById(commentDto.getProductId());
        if (product.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid product id");
        }

        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setProductId(product.orElse(null));
        comment.setDignities(commentDto.getDignities());
        comment.setDisadvantages(commentDto.getDisadvantages());
        comment.setResult(commentDto.getResult());
        comment.setMyScore(commentDto.getMyScore());
        comment.setAuthor(commentDto.getAuthor());

        return commentRepository.save(comment).getId();
    }

    public void updateComment(Long id, CommentUpdateDto commentDto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        log.info("Updating comment {} with data: {}", id, commentDto);

        if (commentDto.getDignities() != null) {
            comment.setDignities(commentDto.getDignities());
        }
        if (commentDto.getDisadvantages() != null) {
            comment.setDisadvantages(commentDto.getDisadvantages());
        }
        if (commentDto.getResult() != null) {
            comment.setResult(commentDto.getResult());
        }
        if (commentDto.getMyScore() != null) {
            comment.setMyScore(commentDto.getMyScore());
        }

        commentRepository.save(comment);
    }

    public HttpStatus deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
        }
        commentRepository.deleteById(id);
        return HttpStatus.NO_CONTENT;
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

    private CommentSummaryDto convertToSummaryDto(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("Comments cannot be null");
        }

        CommentSummaryDto dto = new CommentSummaryDto();
        dto.setId(comment.getId());
        dto.setProductId(comment.getProductId().getId());
        dto.setDignities(comment.getDignities());
        dto.setDisadvantages(comment.getDisadvantages());
        dto.setResult(comment.getResult());
        dto.setMyScore(comment.getMyScore());
        dto.setAuthor(comment.getAuthor());

        log.trace("Converted comments {} to DTO", comment.getId());
        return dto;
    }
}
