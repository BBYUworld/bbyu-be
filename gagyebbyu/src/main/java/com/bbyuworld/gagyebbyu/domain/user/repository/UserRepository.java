package com.bbyuworld.gagyebbyu.domain.user.repository;

import com.bbyuworld.gagyebbyu.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
}
