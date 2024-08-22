package com.bbyuworld.gagyebbyu.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bbyuworld.gagyebbyu.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
