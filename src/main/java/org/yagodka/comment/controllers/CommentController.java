package org.yagodka.comment.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.yagodka.comment.dto.CommentDto;
import org.yagodka.comment.dto.CommentSummaryDto;
import org.yagodka.comment.services.CommentService;
import org.yagodka.product.dto.ProductDto;
import org.yagodka.product.dto.ProductSummaryDto;
import org.yagodka.responces.StandardResponse;

import java.util.List;

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
}
