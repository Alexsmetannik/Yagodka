package org.yagodka.comment.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.yagodka.comment.dto.CommentDto;
import org.yagodka.comment.dto.CommentSummaryDto;
import org.yagodka.comment.dto.CommentUpdateDto;
import org.yagodka.comment.services.CommentService;
import org.yagodka.product.dto.ProductDto;
import org.yagodka.product.dto.ProductSummaryDto;
import org.yagodka.product.dto.ProductUpdateDto;
import org.yagodka.responces.StandardResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/public/product/{productId}/comment")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{commentId}")
    public CommentDto getComment(@PathVariable Long commentId) {
        return commentService.getCommentById(commentId);
    }

    @GetMapping("/all")
    public List<CommentSummaryDto> getAllComments(
            @RequestParam(required = false) Integer count) {
        return commentService.getAllComments(count);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public StandardResponse createComment(@Valid @RequestBody CommentDto commentDto) {
        return new StandardResponse("success", null, commentService.createComment(commentDto));
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

    @PutMapping("/{commentId}")
    public ResponseEntity<StandardResponse> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateDto commentDto) {
        commentService.updateComment(commentId, commentDto);
        return ResponseEntity.ok(
                new StandardResponse("success", null, commentId)
        );
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        HttpStatus status = commentService.deleteComment(commentId);
        return ResponseEntity.status(status).build();
    }
}
