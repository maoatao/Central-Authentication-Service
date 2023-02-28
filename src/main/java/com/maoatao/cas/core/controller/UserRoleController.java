package com.maoatao.cas.core.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.entity.UserRoleEntity;
import com.maoatao.cas.core.param.PageParam;
import com.maoatao.cas.core.service.UserRoleService;
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
 * 用户角色关系管理
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:22
 */
@RestController
@RequestMapping("/userrole")
@Tag(name = "UserRoleController", description = "用户角色关系管理")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    /**
     * 分页查询用户角色关系列表
     *
     * @param pageParam      分页对象
     * @param userRoleEntity 用户角色关系
     * @return 分页用户角色关系列表
     */
    @GetMapping("/page")
    @Operation(summary = "getUserRolePage", description = "分页查询用户角色关系列表")
    public Page<UserRoleEntity> getUserRolePage(PageParam pageParam, UserRoleEntity userRoleEntity) {
        return userRoleService.getUserRolePage(pageParam, userRoleEntity);
    }

    /**
     * 通过id查询用户角色关系
     *
     * @param id 用户角色关系id
     * @return 用户角色关系对象
     */
    @GetMapping("/{id}")
    @Operation(summary = "getUserRoleById", description = "通过id查询用户角色关系")
    public UserRoleEntity getUserRoleById(@PathVariable @Parameter(name = "id", description = "用户角色关系id") String id) {
        return userRoleService.getUserRoleById(id);
    }

    /**
     * 新增用户角色关系
     *
     * @param userRoleEntity 用户角色关系
     * @return 新增操作结果
     */
    @PostMapping
    @Operation(summary = "addUserRole", description = "新增用户角色关系")
    public Boolean addUserRole(UserRoleEntity userRoleEntity) {
        return userRoleService.addUserRole(userRoleEntity);
    }

    /**
     * 修改用户角色关系
     *
     * @param userRoleEntity 用户角色关系
     * @return 修改操作结果
     */
    @PutMapping
    @Operation(summary = "updateUserRoleById", description = "修改用户角色关系")
    public Boolean updateUserRoleById(UserRoleEntity userRoleEntity) {
        return userRoleService.updateUserRoleById(userRoleEntity);
    }

    /**
     * 通过id删除用户角色关系
     *
     * @param id 用户角色关系id
     * @return 删除操作结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "deleteUserRoleById", description = "通过id删除用户角色关系")
    public Boolean deleteUserRoleById(@PathVariable @Parameter(name = "id", description = "用户角色关系id") String id) {
        return userRoleService.deleteUserRoleById(id);
    }
}
