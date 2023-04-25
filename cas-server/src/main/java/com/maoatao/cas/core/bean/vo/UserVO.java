package com.maoatao.cas.core.bean.vo;

import com.maoatao.synapse.core.bean.base.BaseVO;
import com.maoatao.synapse.lang.util.SynaDates;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * CAS 用户
 *
 * @author MaoAtao
 * @date 2023-04-21 16:09:13
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "CAS 用户数据")
public class UserVO extends BaseVO {

    @Serial
    private static final long serialVersionUID = 5861184563320996350L;

    /**
     * 主键id(自增)
     */
    @Schema(description = "主键id(自增)")
    private Long id;
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
    /**
     * 创建人 ID
     */
    @Schema(description = "创建人 ID")
    private String createdById;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = SynaDates.DATE_TIME_FORMAT, timezone = SynaDates.CN_TIME_ZONE)
    @Schema(description = "创建时间")
    private LocalDateTime createdDate;
    /**
     * 更新人 ID
     */
    @Schema(description = "更新人 ID")
    private String updatedById;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = SynaDates.DATE_TIME_FORMAT, timezone = SynaDates.CN_TIME_ZONE)
    @Schema(description = "更新时间")
    private LocalDateTime updatedDate;

    @Tolerate
    public UserVO() {}
}
