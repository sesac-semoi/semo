package com.semoi.semo.board.entity;

//import com.semoi.semo.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "boards")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id", nullable = false)
    private Long boardId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "content", nullable = false, length = 4000)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user; // 작성자 (회원)

    @Column(name = "user_id")
    private String user;

    @Column(name = "hit", nullable = false)
    private Integer hit;

    @Column(name = "recruitment_type", nullable = false, length = 255)
    private String recruitmentType;

    @Column(name = "recruitment_count", nullable = false)
    private Integer recruitmentCount;

    @Column(name = "recruitment_field", nullable = false, length = 255)
    private String recruitmentField;

    @Column(name = "recruitment_method", nullable = false, length = 255)
    private String recruitmentMethod;

    @Column(name = "recruitment_deadline", nullable = false)
    private LocalDateTime recruitmentDeadline;

    @Column(name = "progress_period", nullable = false, length = 255)
    private String progressPeriod;
}
