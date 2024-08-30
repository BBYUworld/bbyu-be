package com.bbyuworld.gagyebbyu.domain.asset.enums;

public enum LoanType {
    개인신용대출, 주택담보대출;

    public static LoanType fromString(String text) {
        for (LoanType b : LoanType.values()) {
            if (b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
