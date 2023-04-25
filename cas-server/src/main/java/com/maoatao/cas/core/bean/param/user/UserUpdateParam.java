package com.maoatao.cas.core.bean.param.user;

import com.maoatao.synapse.core.bean.base.BaseUpdateParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "新增CAS 用户参数")
public class UserUpdateParam extends BaseUpdateParam {

    @Serial
    private static final long serialVersionUID = -2859865966647809521L;

    /**
     * 用户名
     */
    @NotNull(message = "name 不能为空")
    @Schema(description = "用户名")
    private String name;
    /**
     * 描述
     */
    @NotNull(message = "description 不能为空")
    @Schema(description = "描述")
    private String description;

    @Tolerate
    public UserUpdateParam() {}
}
