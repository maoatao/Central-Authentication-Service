package com.maoatao.cas.common.annotation;

import com.maoatao.cas.common.enums.Api;
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
     * 接口类型
     */
    Api type();

    /**
     * 角色
     */
    String[] role() default SynaStrings.EMPTY;

    /**
     * 权限
     */
    String[] permission() default SynaStrings.EMPTY;
}
