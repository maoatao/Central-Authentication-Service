package com.maoatao.cas.core.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.common.annotation.CasAuth;
import com.maoatao.cas.core.bean.vo.ApprovalsVO;
import com.maoatao.cas.core.bean.param.approvals.ApprovalsQueryParam;
import com.maoatao.cas.core.bean.param.approvals.ApprovalsSaveParam;
import com.maoatao.cas.core.bean.param.approvals.ApprovalsUpdateParam;
import com.maoatao.cas.core.service.ApprovalsService;
import com.maoatao.daedalus.web.annotation.OperationLog;
import com.maoatao.daedalus.web.annotation.ResponseHandle;
import com.maoatao.daedalus.web.enums.OperationType;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * CAS 授权批准管理
 *
 * @author MaoAtao
 * @date 2023-05-06 09:53:13
 */
@ResponseHandle
@RestController
@RequestMapping("/core/approvals")
@Tag(name = "ApprovalsController", description = "CAS 授权批准管理")
public class ApprovalsController {

    @Autowired
    private ApprovalsService approvalsService;

    /**
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    @PostMapping("/page")
    @CasAuth("cas_approvals_get")
    @Operation(summary = "getPage", description = "分页查询CAS 授权批准列表")
    @OperationLog(type = OperationType.QUERY, content = "分页查询CAS 授权批准", moduleName = "cas")
    public Page<ApprovalsVO> page(@Validated @RequestBody ApprovalsQueryParam param) {
        return approvalsService.page(param);
    }

    /**
     * 通过id查询
     *
     * @param id CAS 授权批准id
     * @return CAS 授权批准
     */
    @GetMapping("/{id}")
    @CasAuth("cas_approvals_get")
    @Operation(summary = "details", description = "通过id查询CAS 授权批准")
    @OperationLog(type = OperationType.QUERY, content = "查询CAS 授权批准", moduleName = "cas")
    public ApprovalsVO details(@PathVariable @Parameter(name = "id", description = "CAS 授权批准id") Long id) {
        return approvalsService.details(id);
    }

    /**
     * 新增
     *
     * @param param 参数
     * @return 新增成功返回主键 id
     */
    @PostMapping
    @CasAuth("cas_approvals_add")
    @Operation(summary = "save", description = "新增CAS 授权批准")
    @OperationLog(type = OperationType.ADD, content = "新增CAS 授权批准", moduleName = "cas")
    public long save(@Validated @RequestBody ApprovalsSaveParam param) {
        return approvalsService.save(param);
    }

    /**
     * 更新
     *
     * @param param 参数
     * @return 更新成功返回true
     */
    @PutMapping
    @CasAuth("cas_approvals_update")
    @Operation(summary = "update", description = "修改CAS 授权批准")
    @OperationLog(type = OperationType.UPDATE, content = "修改CAS 授权批准", moduleName = "cas")
    public boolean update(@Validated @RequestBody ApprovalsUpdateParam param) {
        return approvalsService.update(param);
    }

    /**
     * 删除
     *
     * @param ids 主键id
     * @return 删除成功返回true
     */
    @DeleteMapping("/{ids}")
    @CasAuth("cas_approvals_delete")
    @Operation(summary = "remove", description = "通过id删除CAS 授权批准(多个 id 使用,分隔)")
    @OperationLog(type = OperationType.DELETE, content = "删除CAS 授权批准", moduleName = "cas")
    public boolean remove(@PathVariable @Parameter(name = "ids", description = "CAS 授权批准id集合") List<Long> ids) {
        return approvalsService.remove(ids);
    }
}
