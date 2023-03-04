package com.maoatao.cas.core.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.entity.RolePermissionEntity;
import com.maoatao.cas.core.service.RolePermissionService;
import com.maoatao.cas.core.param.RolePermissionParam;
import com.maoatao.cas.security.HttpConstants;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色权限关系管理
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
@RestController
@RequestMapping(HttpConstants.BASE_URL + "/rolepermission")
@Tag(name = "RolePermissionController", description = "角色权限关系管理")
public class RolePermissionController {

    @Autowired
    private RolePermissionService rolePermissionService;

    /**
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    @GetMapping("/page")
    @Operation(summary = "getPage", description = "分页查询角色权限关系列表")
    public Page<RolePermissionEntity> getPage(RolePermissionParam param) {
        return rolePermissionService.getPage(param);
    }

    /**
     * 通过id查询
     *
     * @param id 主键id
     * @return 角色权限关系
     */
    @GetMapping("/{id}")
    @Operation(summary = "getById", description = "通过id查询角色权限关系")
    public RolePermissionEntity getById(@PathVariable
                                        @Parameter(name = "id", description = "角色权限关系id")
                                        @NotNull(message = "角色权限关系id不能为空")
                                        Long id) {
        return rolePermissionService.getById(id);
    }

    /**
     * 新增
     *
     * @param param 参数
     * @return 新增成功返回true
     */
    @PostMapping
    @Operation(summary = "save", description = "新增角色权限关系")
    public boolean save(RolePermissionParam param) {
        return rolePermissionService.save(param);
    }

    /**
     * 更新
     *
     * @param param 参数
     * @return 更新成功返回true
     */
    @PutMapping
    @Operation(summary = "update", description = "修改角色权限关系")
    public boolean update(RolePermissionParam param) {
        return rolePermissionService.update(param);
    }

    /**
     * 删除
     *
     * @param id 主键id
     * @return 删除成功返回true
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "remove", description = "通过id删除角色权限关系")
    public boolean remove(@PathVariable
                          @Parameter(name = "id", description = "角色权限关系id")
                          @NotNull(message = "角色权限关系id不能为空")
                          Long id) {
        return rolePermissionService.remove(id);
    }
}
