package com.bbyuworld.gagyebbyu.domain.income.entity;

import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;
import com.bbyuworld.gagyebbyu.domain.expense.entity.Category;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "expense")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "couple_id")
    private Couple couple;

    @Column(nullable = false)
    private Long amount;

    @Column(name = "sender")
    private String sender;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime date;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Builder
    public Income(User user, Couple couple, Long amount, String sender, LocalDateTime date, String memo) {
        this.user = user;
        this.couple = couple;
        this.amount = amount;
        this.sender = sender;
        this.date = date != null ? date : LocalDateTime.now();
        this.memo = memo;
    }

    public void updateMemo(String memo) {
        this.memo = memo;
    }

    public void updateAmount(Long amount) {
        this.amount = amount;
    }
}
