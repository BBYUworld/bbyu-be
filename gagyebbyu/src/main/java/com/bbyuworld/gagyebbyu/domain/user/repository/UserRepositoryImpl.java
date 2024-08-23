package com.bbyuworld.gagyebbyu.domain.user.repository;

import com.bbyuworld.gagyebbyu.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.bbyuworld.gagyebbyu.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{
    private final JPAQueryFactory queryFactory;


    @Override
    public User findUserById(Long userId) {
        return queryFactory.selectFrom(user)
                .where(user.userId.eq(userId))
                .fetchOne();
    }

    @Override
    public User findUserByEmail(String email) {
        return queryFactory.selectFrom(user)
                .where(user.email.eq(email))
                .fetchOne();
    }
}
