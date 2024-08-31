package com.bbyuworld.gagyebbyu.domain.couple.repository;

import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.bbyuworld.gagyebbyu.domain.couple.entity.QCouple.couple;

@Repository
@RequiredArgsConstructor
public class CoupleRepositoryImpl implements CoupleRepositoryCustom {
    private final JPAQueryFactory queryFactory;


    @Override
    public Couple findCoupleByCoupleId(Long coupleId) {
        return queryFactory.selectFrom(couple)
                .where(couple.coupleId.eq(coupleId))
                .fetchOne();
    }
}
