package com.maoatao.cas.core.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.entity.UserEntity;
import com.maoatao.cas.core.service.UserService;
import com.maoatao.cas.core.param.PageParam;
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
 * 用户管理
 *
 * @author MaoAtao
 * @date 2021-05-13 13:28:20
 */
@RestController
@RequestMapping("/user")
@Tag(name = "UserController", description = "用户管理")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 分页查询用户列表
     *
     * @param pageParam 分页对象
     * @param entity    请求参数
     * @return 分页用户列表
     */
    @GetMapping("/page")
    @Operation(summary = "getUserPage", description = "分页查询用户列表")
    public Page<UserEntity> getUserPage(PageParam pageParam, UserEntity entity) {
        return userService.getUserPage(pageParam, entity);
    }

    /**
     * 通过id查询用户
     *
     * @param id 用户id
     * @return 用户对象
     */
    @GetMapping("/{id}")
    @Operation(summary = "getUserById", description = "通过id查询用户")
    public UserEntity getUserById(@PathVariable @Parameter(name = "id", description = "用户id") String id) {
        return userService.getUserById(id);
    }

    /**
     * 新增用户
     *
     * @param entity 请求参数
     * @return 新增操作结果
     */
    @PostMapping
    @Operation(summary = "addUser", description = "新增用户")
    public Boolean addUser(UserEntity entity) {
        return userService.addUser(entity);
    }

    /**
     * 修改用户
     *
     * @param entity 请求参数
     * @return 修改操作结果
     */
    @PutMapping
    @Operation(summary = "updateUserById", description = "修改用户")
    public Boolean updateUserById(UserEntity entity) {
        return userService.updateUserById(entity);
    }

    /**
     * 通过id删除用户
     *
     * @param id 用户id
     * @return 删除操作结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "deleteUserById", description = "通过id删除用户")
    public Boolean deleteUserById(@PathVariable @Parameter(name = "id", description = "用户id") String id) {
        return userService.deleteUserById(id);
    }
}