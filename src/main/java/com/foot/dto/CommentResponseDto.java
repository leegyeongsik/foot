package com.foot.dto;

import com.foot.entity.Comment;

import java.time.LocalDateTime;

public class CommentResponseDto {
    private String username;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommentResponseDto(Comment comment) {
        this.username = comment.getUser().getName();
        this.comment = comment.getComments();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }
}
