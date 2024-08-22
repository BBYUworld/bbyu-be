package com.bbyuworld.gagyebbyu.domain.user.dto;

import com.bbyuworld.gagyebbyu.domain.user.entity.Gender;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private Long userId;
    private Long coupleId;
    private String name;
    private Gender gender;
    private Integer age;
    private Long monthlyIncome;
    private String ratingName;
    private boolean isDeleted;
    private String phone;
    private boolean isLogin;
    private String email;
    private String nickname;
    private Integer monthlyTargetAmount;
    private String refreshToken;
    private String accessToken;

    @Builder
    public UserDto(Long userId, Long coupleId, String name, Gender gender, Integer age, Long monthlyIncome,
                   String ratingName, boolean isDeleted, String phone, boolean isLogin, String email,
                   String nickname, Integer monthlyTargetAmount, String refreshToken, String accessToken) {
        this.userId = userId;
        this.coupleId = coupleId;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.monthlyIncome = monthlyIncome;
        this.ratingName = ratingName;
        this.isDeleted = isDeleted;
        this.phone = phone;
        this.isLogin = isLogin;
        this.email = email;
        this.nickname = nickname;
        this.monthlyTargetAmount = monthlyTargetAmount;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }
}
