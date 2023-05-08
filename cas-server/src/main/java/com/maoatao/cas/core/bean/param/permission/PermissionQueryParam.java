package com.maoatao.cas.core.bean.param.permission;

import com.maoatao.daedalus.web.bean.param.BasePaginationParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 权限
 *
 * @author MaoAtao
 * @date 2023-04-07 21:23:37
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "查询CAS 权限参数")
public class PermissionQueryParam extends BasePaginationParam {

    @Serial
    private static final long serialVersionUID = -6019231050732079457L;

    /**
     * 客户端 id
     */
    @Schema(description = "客户端 id")
    private String clientId;
    /**
     * 权限名
     */
    @Schema(description = "权限名")
    private String name;

    @Tolerate
    public PermissionQueryParam() {}
}
