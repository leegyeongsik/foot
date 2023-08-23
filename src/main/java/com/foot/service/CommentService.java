package com.foot.service;

import com.foot.dto.CommentRequestDto;
import com.foot.entity.Comment;
import com.foot.entity.Product;
import com.foot.entity.User;
import com.foot.entity.UserRoleEnum;
import com.foot.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.RejectedExecutionException;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;

    // 댓글 작성
    public void createComment(Long productId, User user, CommentRequestDto requestDto) {
        Product product = productRepository.findById(productId).orElseThrow();
        Comment comment = new Comment(requestDto.getComment(), user, product);

        commentRepository.save(comment);
    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long commentId, User user, CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        // 댓글 작성자 본인 확인 & 관리자 권한 확인
        if(!comment.getUser().equals(user) && !user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new RejectedExecutionException();
        }

        comment.setComment(requestDto.getComment());
    }

    // 댓글 삭제
    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        // 댓글 작성자 본인 확인 & 관리자 권한 확인
        if(!comment.getUser().equals(user) && !user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new RejectedExecutionException();
        }

        commentRepository.delete(comment);
    }
}
