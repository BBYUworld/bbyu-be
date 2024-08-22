package com.bbyuworld.gagyebbyu.global.jwt;

public class UserContext {
    private  static final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();

    public static void setUserId(Long id){
        userIdHolder.set(id);
    }

    public static Long getUserId(){
        return userIdHolder.get();
    }

    public static void clear(){
        userIdHolder.remove();
    }
}
