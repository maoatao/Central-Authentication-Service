package com.maoatao.cas.core.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.entity.RoleEntity;
import com.maoatao.cas.core.param.PageParam;
import com.maoatao.cas.core.service.RoleService;
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
 * 角色管理
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
@RestController
@RequestMapping("/role")
@Tag(name = "RoleController", description = "角色管理")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 分页查询角色列表
     *
     * @param pageParam  分页对象
     * @param roleEntity 角色
     * @return 分页角色列表
     */
    @GetMapping("/page")
    @Operation(summary = "getRolePage", description = "分页查询角色列表")
    public Page<RoleEntity> getRolePage(PageParam pageParam, RoleEntity roleEntity) {
        return roleService.getPage(pageParam, roleEntity);
    }

    /**
     * 通过id查询角色
     *
     * @param id 角色id
     * @return 角色对象
     */
    @GetMapping("/{id}")
    @Operation(summary = "getRoleById", description = "通过id查询角色")
    public RoleEntity getRoleById(@PathVariable
                                  @Parameter(name = "id", description = "角色id")
                                  @NotNull(message = "角色id不能为空")
                                  Long id) {
        return roleService.getById(id);
    }

    /**
     * 新增角色
     *
     * @param roleEntity 角色
     * @return 新增操作结果
     */
    @PostMapping
    @Operation(summary = "addRole", description = "新增角色")
    public Boolean addRole(RoleEntity roleEntity) {
        return roleService.save(roleEntity);
    }

    /**
     * 修改角色
     *
     * @param roleEntity 角色
     * @return 修改操作结果
     */
    @PutMapping
    @Operation(summary = "updateRoleById", description = "修改角色")
    public Boolean updateRoleById(RoleEntity roleEntity) {
        return roleService.updateById(roleEntity);
    }

    /**
     * 通过id删除角色
     *
     * @param id 角色id
     * @return 删除操作结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "deleteRoleById", description = "通过id删除角色")
    public Boolean deleteRoleById(@PathVariable
                                  @Parameter(name = "id", description = "角色id")
                                  @NotNull(message = "角色id不能为空")
                                  Long id) {
        return roleService.removeById(id);
    }
}
