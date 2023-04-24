package com.maoatao.cas.common.annotation;

import com.maoatao.synapse.lang.util.SynaStrings;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CAS 身份验证 注解
 *
 * @author MaoAtao
 * @date 2023-03-28 16:44:08
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface CasAuth {

    /**
     * 权限名称,默认为空(机机接口)
     * <p>
     * 人机接口必填
     */
    String[] value() default SynaStrings.EMPTY;
}
