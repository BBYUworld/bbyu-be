package com.bbyuworld.gagyebbyu.domain.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private String userId;
    private String userName;
    private String institutionCode;
    private String userKey;
    private String created;
    private String modified;

    @Override
    public String toString() {
        return "UserResponseDto{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", institutionCode='" + institutionCode + '\'' +
                ", userKey='" + userKey + '\'' +
                ", created='" + created + '\'' +
                ", modified='" + modified + '\'' +
                "}\n";
    }
}
