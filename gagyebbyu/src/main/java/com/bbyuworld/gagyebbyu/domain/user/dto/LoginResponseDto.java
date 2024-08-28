package com.bbyuworld.gagyebbyu.domain.user.dto;

import com.bbyuworld.gagyebbyu.global.jwt.JwtToken;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    private JwtToken token;
    private boolean is_first_login;
    private Long userId;
}
