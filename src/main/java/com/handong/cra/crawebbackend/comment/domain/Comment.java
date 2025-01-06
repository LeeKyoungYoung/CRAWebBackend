package com.handong.cra.crawebbackend.comment.domain;

import com.handong.cra.crawebbackend.board.domain.Board;
import com.handong.cra.crawebbackend.comment.dto.CreateCommentDto;
import com.handong.cra.crawebbackend.comment.dto.UpdateCommentDto;
import com.handong.cra.crawebbackend.common.domain.BaseEntity;
import com.handong.cra.crawebbackend.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
//@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parentComment; // 상위 댓글

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList; // 대댓글

    @Column(length = 512, nullable = false)
    private String content;

    @Column(name = "like_count")
    private Long likeCount;

    public Comment(User user, Board board, CreateCommentDto createCommentDto) {
        this.user = user;
        this.board = board;
        this.content = createCommentDto.getContent();
        likeCount = 0L;
    }

    // TODO: 수정할 데이터 추가
    public void update(UpdateCommentDto updateCommentDto) {
        this.content = updateCommentDto.getContent();
    }
}
