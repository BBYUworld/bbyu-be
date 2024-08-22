package com.bbyuworld.gagyebbyu.domain.user.repository;

import com.bbyuworld.gagyebbyu.domain.user.entity.User;

public interface UserRepositoryCustom {
    User findUserById(Long userId);
    User findUserByEmail(String email);
}
