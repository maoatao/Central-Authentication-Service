package com.maoatao.cas.core.param;

import com.maoatao.synapse.core.bean.BaseParam;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 分页参数
 *
 * @author MaoAtao
 * @date 2023-02-15 17:27:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageParam extends BaseParam {
    @Serial
    private static final long serialVersionUID = 3465282147815463165L;

    /**
     * 当前页数
     */
    @NotNull(message = "当前页数不能为空")
    @Min(value = 1, message = "当前页数不能小于{value}")
    private Long pageNo;

    /**
     * 每页条数
     */
    @NotNull(message = "每页条数不能为空")
    @Min(value = 1, message = "每页条数不能小于{value}")
    @Max(value = 1000, message = "每页条数不能大于{value}")
    private Long pageSize;
}