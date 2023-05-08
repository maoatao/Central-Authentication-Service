package com.maoatao.cas.core.bean.param.role;

import com.maoatao.daedalus.web.bean.param.BasePaginationParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 角色
 *
 * @author MaoAtao
 * @date 2023-04-07 21:23:37
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "查询CAS 角色参数")
public class RoleQueryParam extends BasePaginationParam {

    @Serial
    private static final long serialVersionUID = 2605561592641795545L;

    /**
     * 客户端 id
     */
    @Schema(description = "客户端 id")
    private String clientId;
    /**
     * 角色名
     */
    @Schema(description = "角色名")
    private String name;

    @Tolerate
    public RoleQueryParam() {}
}
