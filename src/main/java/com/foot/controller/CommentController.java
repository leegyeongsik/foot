package com.foot.controller;

import com.foot.dto.ApiResponseDto;
import com.foot.dto.CommentRequestDto;
import com.foot.security.UserDetailsImpl;
import com.foot.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.RejectedExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class CommentController {

    private final CommentService commentService;

    //댓글 조회 -> 상품 조회 Controller 에서 조회하는게 어떨지?
    @GetMapping("/{productId}/comment")
    public void getComment(@PathVariable Long productId) {

    }

    //댓글 작성 -> 구매한 사용자만 댓글을 달게 해야되지 않을까..?
    @PostMapping("/{productId}/comment")
    public ResponseEntity<ApiResponseDto> createComment(@PathVariable Long productId, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CommentRequestDto requestDto) {
        commentService.createComment(productId, userDetails.getUser(), requestDto);

        return ResponseEntity.ok().body(new ApiResponseDto("댓글이 작성되었습니다", HttpStatus.CREATED.value()));
    }

    //댓글 수정
    @PutMapping("/{commentId}/comment")
    public ResponseEntity<ApiResponseDto> updateComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CommentRequestDto requestDto) {
        try {
            commentService.updateComment(commentId, userDetails.getUser(), requestDto);
            return ResponseEntity.ok().body(new ApiResponseDto("댓글 수정이 완료되었습니다",HttpStatus.OK.value()));
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("작성자만 수정할 수 있습니다,",HttpStatus.BAD_REQUEST.value()));
        }
    }

    //댓글 삭제
    @DeleteMapping("/{commentId}/comment")
    public ResponseEntity<ApiResponseDto> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            commentService.deleteComment(commentId, userDetails.getUser());
            return ResponseEntity.ok().body(new ApiResponseDto("댓글 삭제가 완료되었습니다", HttpStatus.OK.value()));
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("작성자만 삭제할 수 있습니다",HttpStatus.BAD_REQUEST.value()));
        }
    }

}
