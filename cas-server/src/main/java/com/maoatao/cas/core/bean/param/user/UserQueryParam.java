package com.maoatao.cas.core.bean.param.user;

import com.maoatao.daedalus.web.bean.param.BasePaginationParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 用户
 *
 * @author MaoAtao
 * @date 2023-04-21 16:09:13
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "查询CAS 用户参数")
public class UserQueryParam extends BasePaginationParam {

    @Serial
    private static final long serialVersionUID = -4386605265609963211L;

    /**
     * CAS 全局唯一id
     */
    @Schema(description = "CAS 全局唯一id")
    private String openId;
    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String name;
    /**
     * 描述
     */
    @Schema(description = "描述")
    private String description;

    @Tolerate
    public UserQueryParam() {}
}
