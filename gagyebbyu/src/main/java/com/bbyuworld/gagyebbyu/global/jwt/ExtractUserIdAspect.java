package com.bbyuworld.gagyebbyu.global.jwt;

import com.bbyuworld.gagyebbyu.global.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@RequiredArgsConstructor
@Aspect
@Slf4j
public class ExtractUserIdAspect {
    private final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtTokenProvider jwtTokenProvider;
    private final ThreadPoolTaskExecutorBuilder threadPoolTaskExecutorBuilder;

    @Around("@annotation(requireJwtToken)")
    public Object before(ProceedingJoinPoint pjp, RequireJwtToken requireJwtToken) throws Throwable {
        log.info("AOP require Jwt Token Before에 도착");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String accessToken = extractToken(request);
        log.info("Access Token = {}", accessToken);
        if (accessToken == null || !jwtTokenProvider.validateToken(accessToken)) {
            throw new UnauthorizedException("Invalid or missing JWT token");
        }
        Long memberId = jwtTokenProvider.getUserId(accessToken);
        log.info("memberId = {}", memberId);
        UserContext.setUserId(memberId);
        try {
            return pjp.proceed();
        } catch (Throwable e) {
            throw e;
        } finally {
            UserContext.clear();
        }
    }
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
