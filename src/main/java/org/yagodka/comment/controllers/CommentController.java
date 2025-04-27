package org.yagodka.comment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yagodka.comment.dto.CommentDto;
import org.yagodka.comment.services.CommentService;
import org.yagodka.product.dto.ProductDto;
import org.yagodka.product.services.ProductService;

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
}
