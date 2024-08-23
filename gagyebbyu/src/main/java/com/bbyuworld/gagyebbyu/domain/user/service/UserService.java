package com.bbyuworld.gagyebbyu.domain.user.service;

import com.bbyuworld.gagyebbyu.domain.user.dto.UserDto;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;
import com.bbyuworld.gagyebbyu.domain.user.repository.UserRepository;
import com.bbyuworld.gagyebbyu.global.error.ErrorCode;
import com.bbyuworld.gagyebbyu.global.error.type.BadRequestException;
import com.bbyuworld.gagyebbyu.global.error.type.UserNotFoundException;
import com.bbyuworld.gagyebbyu.global.jwt.JwtToken;
import com.bbyuworld.gagyebbyu.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public boolean regist(UserDto dto){
        User user = userRepository.findUserByEmail(dto.getEmail());
        if(user!=null)throw new BadRequestException(ErrorCode.USER_ALREADY_EXIST);
        user = User.fromDto(dto);
        userRepository.save(user);
        return true;
    }
    public boolean login(UserDto dto){
        User user = userRepository.findUserByEmail(dto.getEmail());
        if(user.getPassword().equals(dto.getPassword())){
            log.info("Login success");
            JwtToken token = jwtTokenProvider.createToken(user.getUserId());
            user.setAccessToken(token.getAccessToken());
            user.setRefreshToken(token.getRefreshToken());
            user.setLogin(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean logout(Long userId){
        User user = userRepository.findUserById(userId);
        if(user == null)throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
        user.setLogin(false);
        user.setAccessToken(null);
        user.setRefreshToken(null);
        userRepository.save(user);
        return true;
    }
}
