package live.tikgik.expenses.account.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class LoggerAspect {

    @Before(value = "@annotation(ToLog)")
    public void logPointcut(JoinPoint joinPoint) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            String correlationId = request.getHeader("tikGik-correlation-id");

            log.debug(Thread.currentThread().getName());
            log.debug("Executing method: {} with correlation ID: {}",
                    joinPoint.getSignature().getName(), correlationId);
        } else {
            log.debug("No request context available.");
        }
    }
}
