package com.bbyuworld.gagyebbyu.domain.user.service;

import com.bbyuworld.gagyebbyu.domain.user.dto.UserDto;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;
import com.bbyuworld.gagyebbyu.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public boolean login(UserDto dto){
        User user = userRepository.findUserByEmail(dto.getEmail());
        return false;
    }
}
