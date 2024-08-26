package com.bbyuworld.gagyebbyu.domain.asset.entity;

import com.bbyuworld.gagyebbyu.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "asset")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @OneToOne(mappedBy = "asset", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private AssetDeposit assetDeposit;

    private Long coupleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;


    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "amount")
    private Long amount;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


}
