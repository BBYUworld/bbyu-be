package com.bbyuworld.gagyebbyu.domain.asset.entity;

import com.bbyuworld.gagyebbyu.domain.user.entity.Gender;

import java.util.Locale;

public enum Type {
    CARD,
    DEPOSIT;
    public static Type getType(String tpye) {
        return Type.valueOf(tpye.toUpperCase(Locale.ENGLISH));
    }
}
