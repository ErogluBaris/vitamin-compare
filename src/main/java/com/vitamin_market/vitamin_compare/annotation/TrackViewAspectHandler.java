package com.vitamin_market.vitamin_compare.annotation;

import com.vitamin_market.vitamin_compare.dto.VitaminDto;
import com.vitamin_market.vitamin_compare.service.VitaminTrackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class TrackViewAspectHandler {

    private final VitaminTrackService vitaminTrackService;

    @AfterReturning(
            pointcut = "@annotation(com.vitamin_market.vitamin_compare.annotation.TrackView)",
            returning = "result"
    )
    public void handle(JoinPoint joinPoint, Object result) {
        try {
            if (result instanceof VitaminDto dto) {
                vitaminTrackService.incrementTrackCount(dto.getId());
            }
        } catch (Exception e) {
            log.error("TrackViewAspectHandler, handle, error occurred.", e);
        }

    }
}
