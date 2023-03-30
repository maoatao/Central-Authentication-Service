package com.maoatao.cas.common.annotation;

import cn.hutool.core.collection.IterUtil;
import com.maoatao.daedalus.core.context.OperatorContextHolder;
import com.maoatao.synapse.lang.exception.SynaException;
import com.maoatao.synapse.lang.util.SynaSafes;
import com.maoatao.synapse.web.response.HttpResponseStatus;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

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
        // 是认证的
        AtomicBoolean isAuthentication = new AtomicBoolean(false);
        // 是人机接口 (Human–Computer Interaction)
        AtomicBoolean isHci = new AtomicBoolean(false);
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        CasAuth methodAnnotation = method.getAnnotation(CasAuth.class);
        preMethodAuthentication(methodAnnotation, isAuthentication, isHci);
        // 以方法注解为主,没有方法注解时尝试使用类注解
        if (methodAnnotation == null) {
            preClassAuthentication(joinPoint, isAuthentication, isHci);
        }
        throwException(isAuthentication.get(), isHci.get());
    }

    private void preMethodAuthentication(CasAuth methodAnnotation, AtomicBoolean isAuthentication, AtomicBoolean isHci) {
        Set<String> methodAuths = getAnnotationValues(methodAnnotation);
        if (methodAnnotation != null) {
            if (IterUtil.isNotEmpty(methodAuths)) {
                isAuthentication.set(anyAuthMatch(methodAuths, SynaSafes.of(OperatorContextHolder.getContext().getPermissions())) ||
                        anyAuthMatch(methodAuths, SynaSafes.of(OperatorContextHolder.getContext().getRoles())));
            } else {
                // 有注解,没权限值 就是机机接口
                isHci.set(false);
            }
        }
    }

    private void preClassAuthentication(JoinPoint joinPoint, AtomicBoolean isAuthentication, AtomicBoolean isHci) {
        CasAuth classAnnotation = joinPoint.getTarget().getClass().getAnnotation(CasAuth.class);
        Set<String> classAuths = getAnnotationValues(classAnnotation);
        if (!isAuthentication.get() && classAnnotation != null) {
            if (IterUtil.isNotEmpty(classAuths)) {
                isAuthentication.set(anyAuthMatch(classAuths, SynaSafes.of(OperatorContextHolder.getContext().getPermissions())) ||
                        anyAuthMatch(classAuths, SynaSafes.of(OperatorContextHolder.getContext().getRoles())));
                isHci.set(true);
            } else {
                isHci.set(false);
            }
        }
    }

    private Set<String> getAnnotationValues(CasAuth casAuth) {
        return casAuth == null ? Set.of() : Arrays.stream(casAuth.value()).collect(Collectors.toSet());
    }

    private boolean anyAuthMatch(@NonNull Set<String> annotationAuths, @NonNull Set<String> operatorAuths) {
        if (IterUtil.isEmpty(operatorAuths)) {
            return false;
        }
        return annotationAuths.stream().anyMatch(operatorAuths::contains);
    }

    private void throwException(boolean isAuthentication, boolean isHci) {
        if (isHci) {
            if (OperatorContextHolder.getContext().isClientCredentials()) {
                // 人机接口 不允许机器访问
                throw new SynaException(HttpResponseStatus.UNAUTHORIZED_HCI);
            }
        } else {
            if (!OperatorContextHolder.getContext().isClientCredentials()) {
                // 机机接口 不允许人访问
                throw new SynaException(HttpResponseStatus.UNAUTHORIZED_CCI);
            }
        }
        if (!isAuthentication) {
            // 没有权限
            throw new SynaException(HttpResponseStatus.UNAUTHORIZED);
        }
    }
}
