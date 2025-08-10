package com.board.domain.comment.entity;

import com.board.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"ancestor_id", "descendant_id"}))
public class CommentHierarchy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ancestor_id")
    private Comment ancestor;

    @ManyToOne
    @JoinColumn(name = "descendant_id")
    private Comment descendant;

    private int depth;

    @Builder
    public CommentHierarchy(Long id, Comment ancestor, Comment descendant, int depth) {
        this.id = id;
        this.ancestor = ancestor;
        this.descendant = descendant;
        this.depth = depth;
    }

    public static CommentHierarchy of(Comment ancestor, Comment descendant, int depth) {
        return CommentHierarchy.builder()
                .ancestor(ancestor)
                .descendant(descendant)
                .depth(depth)
                .build();
    }
    
}
