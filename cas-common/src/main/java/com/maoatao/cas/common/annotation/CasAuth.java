package com.maoatao.cas.common.annotation;

import com.maoatao.daedalus.core.context.DaedalusOperatorContext;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CAS 身份验证 注解
 * <p>
 * <p>
 * 【机机接口】默认注解或权限编号填0:
 * <pre>
 * &#064;CasAuth
 * &#064;CasAuth("0")
 * </pre>
 * 【人机接口】使用权限编号:
 * <pre>
 * &#064;CasAuth("001")
 * </pre>
 * 【人机接口】使用多个权限编号:
 * <pre>
 * &#064;CasAuth({"001","002"})
 * </pre>
 * 【人机接口和机机接口】使用多个权限编号:
 * <pre>
 * &#064;CasAuth({"001","0"})
 * </pre>
 * 权限编号 {@link DaedalusOperatorContext#getPermissions()}
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
     * 机机交互权限
     */
    String CCI = "0";

    /**
     * 权限编号,默认为0(机机接口)
     * <p>
     * 人机接口必填
     */
    String[] value() default CCI;
}
