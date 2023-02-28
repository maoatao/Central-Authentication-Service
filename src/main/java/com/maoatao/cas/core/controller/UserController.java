package com.maoatao.cas.core.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.entity.UserEntity;
import com.maoatao.cas.core.service.UserService;
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
    @Operation(summary = "getPage", description = "分页查询用户列表")
    public Page<UserEntity> getPage(PageParam pageParam, UserEntity entity) {
        return userService.getPage(pageParam, entity);
    }

    /**
     * 通过id查询用户
     *
     * @param id 用户id
     * @return 用户对象
     */
    @GetMapping("/{id}")
    @Operation(summary = "getById", description = "通过id查询用户")
    public UserEntity getById(@PathVariable
                              @Parameter(name = "id", description = "用户id")
                              @NotNull(message = "用户id不能为空")
                              Long id) {
        return userService.getById(id);
    }

    /**
     * 新增用户
     *
     * @param entity 请求参数
     * @return 新增操作结果
     */
    @PostMapping
    @Operation(summary = "save", description = "新增用户")
    public Boolean save(UserEntity entity) {
        return userService.save(entity);
    }

    /**
     * 修改用户
     *
     * @param entity 请求参数
     * @return 修改操作结果
     */
    @PutMapping
    @Operation(summary = "updateById", description = "修改用户")
    public Boolean updateById(UserEntity entity) {
        return userService.updateById(entity);
    }

    /**
     * 通过id删除用户
     *
     * @param id 用户id
     * @return 删除操作结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "removeById", description = "通过id删除用户")
    public Boolean removeById(@PathVariable
                              @Parameter(name = "id", description = "用户id")
                              @NotNull(message = "用户id不能为空")
                              Long id) {
        return userService.removeById(id);
    }
}