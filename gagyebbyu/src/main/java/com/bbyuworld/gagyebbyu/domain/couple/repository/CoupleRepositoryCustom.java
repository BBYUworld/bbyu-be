package com.bbyuworld.gagyebbyu.domain.couple.repository;

import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;

public interface CoupleRepositoryCustom {
    Couple findCoupleByCoupleId(Long coupleId);
}
