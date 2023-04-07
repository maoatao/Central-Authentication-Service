package com.maoatao.cas.core.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.common.annotation.CasAuth;
import com.maoatao.cas.core.bean.vo.UserRoleVO;
import com.maoatao.cas.core.bean.param.userrole.UserRoleQueryParam;
import com.maoatao.cas.core.bean.param.userrole.UserRoleSaveParam;
import com.maoatao.cas.core.bean.param.userrole.UserRoleUpdateParam;
import com.maoatao.cas.core.service.UserRoleService;
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
 * CAS 用户角色关系管理
 *
 * @author MaoAtao
 * @date 2023-04-07 22:48:12
 */
@ResponseHandle
@RestController
@RequestMapping("/userrole")
@Tag(name = "UserRoleController", description = "CAS 用户角色关系管理")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    /**
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    @GetMapping("/page")
    @CasAuth("cas_userrole_get")
    @Operation(summary = "getPage", description = "分页查询CAS 用户角色关系列表")
    @OperationLog(type = OperationType.QUERY, content = "分页查询CAS 用户角色关系", moduleName = "cas")
    public Page<UserRoleVO> page(@Validated UserRoleQueryParam param) {
        return userRoleService.page(param);
    }

    /**
     * 通过id查询
     *
     * @param id CAS 用户角色关系id
     * @return CAS 用户角色关系
     */
    @GetMapping("/{id}")
    @CasAuth("cas_userrole_get")
    @Operation(summary = "details", description = "通过id查询CAS 用户角色关系")
    @OperationLog(type = OperationType.QUERY, content = "查询CAS 用户角色关系", moduleName = "cas")
    public UserRoleVO details(@PathVariable @Parameter(name = "id", description = "CAS 用户角色关系id") Long id) {
        return userRoleService.details(id);
    }

    /**
     * 新增
     *
     * @param param 参数
     * @return 新增成功返回主键 id
     */
    @PostMapping
    @CasAuth("cas_userrole_add")
    @Operation(summary = "save", description = "新增CAS 用户角色关系")
    @OperationLog(type = OperationType.ADD, content = "新增CAS 用户角色关系", moduleName = "cas")
    public long save(@Validated @RequestBody UserRoleSaveParam param) {
        return userRoleService.save(param);
    }

    /**
     * 更新
     *
     * @param param 参数
     * @return 更新成功返回true
     */
    @PutMapping
    @CasAuth("cas_userrole_update")
    @Operation(summary = "update", description = "修改CAS 用户角色关系")
    @OperationLog(type = OperationType.UPDATE, content = "修改CAS 用户角色关系", moduleName = "cas")
    public boolean update(@Validated @RequestBody UserRoleUpdateParam param) {
        return userRoleService.update(param);
    }

    /**
     * 删除
     *
     * @param ids 主键id
     * @return 删除成功返回true
     */
    @DeleteMapping("/{ids}")
    @CasAuth("cas_userrole_delete")
    @Operation(summary = "remove", description = "通过id删除CAS 用户角色关系(多个 id 使用,分隔)")
    @OperationLog(type = OperationType.DELETE, content = "删除CAS 用户角色关系", moduleName = "cas")
    public boolean remove(@PathVariable @Parameter(name = "ids", description = "CAS 用户角色关系id集合") List<Long> ids) {
        return userRoleService.remove(ids);
    }
}
