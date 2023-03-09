package com.maoatao.cas.core.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.entity.PermissionEntity;
import com.maoatao.cas.core.service.PermissionService;
import com.maoatao.cas.core.param.PermissionParam;
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
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    @GetMapping("/page")
    @Operation(summary = "getPage", description = "分页查询权限列表")
    public Page<PermissionEntity> getPage(PermissionParam param) {
        return permissionService.getPage(param);
    }

    /**
     * 通过id查询
     *
     * @param id 主键id
     * @return 权限
     */
    @GetMapping("/{id}")
    @Operation(summary = "getById", description = "通过id查询权限")
    public PermissionEntity getById(@PathVariable
                                    @Parameter(name = "id", description = "权限id")
                                    @NotNull(message = "权限id不能为空")
                                    Long id) {
        return permissionService.getById(id);
    }

    /**
     * 新增
     *
     * @param param 参数
     * @return 新增成功返回true
     */
    @PostMapping
    @Operation(summary = "save", description = "新增权限")
    public boolean save(PermissionParam param) {
        return permissionService.save(param);
    }

    /**
     * 更新
     *
     * @param param 参数
     * @return 更新成功返回true
     */
    @PutMapping
    @Operation(summary = "update", description = "修改权限")
    public boolean update(PermissionParam param) {
        return permissionService.update(param);
    }

    /**
     * 删除
     *
     * @param id 主键id
     * @return 删除成功返回true
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "remove", description = "通过id删除权限")
    public boolean remove(@PathVariable
                          @Parameter(name = "id", description = "权限id")
                          @NotNull(message = "权限id不能为空")
                          Long id) {
        return permissionService.remove(id);
    }

}
