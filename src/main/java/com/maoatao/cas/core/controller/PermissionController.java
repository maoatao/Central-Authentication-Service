package com.maoatao.cas.core.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.entity.PermissionEntity;
import com.maoatao.cas.core.param.PageParam;
import com.maoatao.cas.core.service.PermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限管理
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
@RestController
@RequestMapping("/permission")
@Tag(name = "PermissionController", description = "权限管理")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 分页查询权限列表
     *
     * @param pageParam        分页对象
     * @param permissionEntity 权限
     * @return 分页权限列表
     */
    @GetMapping("/page")
    @Operation(summary = "getPermissionPage", description = "分页查询权限列表")
    public Page<PermissionEntity> getPermissionPage(PageParam pageParam, PermissionEntity permissionEntity) {
        return permissionService.getPermissionPage(pageParam, permissionEntity);
    }

    /**
     * 通过id查询权限
     *
     * @param id 权限id
     * @return 权限对象
     */
    @GetMapping("/{id}")
    @Operation(summary = "getPermissionById", description = "通过id查询权限")
    public PermissionEntity getPermissionById(@PathVariable @Parameter(name = "id", description = "权限id") String id) {
        return permissionService.getPermissionById(id);
    }

    /**
     * 新增权限
     *
     * @param permissionEntity 权限
     * @return 新增操作结果
     */
    @PostMapping
    @Operation(summary = "addPermission", description = "新增权限")
    public Boolean addPermission(PermissionEntity permissionEntity) {
        return permissionService.addPermission(permissionEntity);
    }

    /**
     * 修改权限
     *
     * @param permissionEntity 权限
     * @return 修改操作结果
     */
    @PutMapping
    @Operation(summary = "updatePermissionById", description = "修改权限")
    public Boolean updatePermissionById(PermissionEntity permissionEntity) {
        return permissionService.updatePermissionById(permissionEntity);
    }

    /**
     * 通过id删除权限
     *
     * @param id 权限id
     * @return 删除操作结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "deletePermissionById", description = "通过id删除权限")
    public Boolean deletePermissionById(@PathVariable @Parameter(name = "id", description = "权限id") String id) {
        return permissionService.deletePermissionById(id);
    }

}
