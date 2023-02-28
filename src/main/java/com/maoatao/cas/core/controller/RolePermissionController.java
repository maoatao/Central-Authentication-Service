package com.maoatao.cas.core.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.entity.RolePermissionEntity;
import com.maoatao.cas.core.service.RolePermissionService;
import com.maoatao.cas.core.param.PageParam;
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
@RequestMapping("/rolepermission")
@Tag(name = "RolePermissionController", description = "角色权限关系管理")
public class RolePermissionController {

    @Autowired
    private RolePermissionService rolePermissionService;

    /**
     * 分页查询角色权限关系列表
     *
     * @param pageParam            分页对象
     * @param rolePermissionEntity 角色权限关系
     * @return 分页角色权限关系列表
     */
    @GetMapping("/page")
    @Operation(summary = "getPage", description = "分页查询角色权限关系列表")
    public Page<RolePermissionEntity> getPage(PageParam pageParam, RolePermissionEntity rolePermissionEntity) {
        return rolePermissionService.getPage(pageParam, rolePermissionEntity);
    }

    /**
     * 通过id查询角色权限关系
     *
     * @param id 角色权限关系id
     * @return 角色权限关系对象
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
     * 新增角色权限关系
     *
     * @param rolePermissionEntity 角色权限关系
     * @return 新增操作结果
     */
    @PostMapping
    @Operation(summary = "save", description = "新增角色权限关系")
    public Boolean save(RolePermissionEntity rolePermissionEntity) {
        return rolePermissionService.save(rolePermissionEntity);
    }

    /**
     * 修改角色权限关系
     *
     * @param rolePermissionEntity 角色权限关系
     * @return 修改操作结果
     */
    @PutMapping
    @Operation(summary = "updateById", description = "修改角色权限关系")
    public Boolean updateById(RolePermissionEntity rolePermissionEntity) {
        return rolePermissionService.updateById(rolePermissionEntity);
    }

    /**
     * 通过id删除角色权限关系
     *
     * @param id 角色权限关系id
     * @return 删除操作结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "removeById", description = "通过id删除角色权限关系")
    public Boolean removeById(@PathVariable
                              @Parameter(name = "id", description = "角色权限关系id")
                              @NotNull(message = "角色权限关系id不能为空")
                              Long id) {
        return rolePermissionService.removeById(id);
    }
}
