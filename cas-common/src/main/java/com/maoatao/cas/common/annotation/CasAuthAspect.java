package com.maoatao.cas.common.annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 权限切面
 *
 * @author MaoAtao
 * @date 2023-03-29 14:17:52
 */
@Aspect
@Component
public class CasAuthAspect {

    /**
     * 设置操作日志切入点
     */
    @Pointcut("@annotation(com.maoatao.cas.common.annotation.CasAuth) && execution(public * *..controller.*Controller.*(..)))")
    public void casAuthPoinCut() {
    }

    /**
     * 切入点执行前执行该方法
     *
     * @param joinPoint 切入点
     */
    @Before(value = "casAuthPoinCut()", argNames = "joinPoint")
    public void beforeCasAuth(JoinPoint joinPoint) {
        System.out.println(joinPoint);
        // throw new SynaException(HttpResponseStatus.UNAUTHORIZED);
    }
}
