package com.maoatao.cas.core.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.entity.UserEntity;
import com.maoatao.cas.core.service.UserService;
import com.maoatao.cas.core.param.UserParam;
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
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    @GetMapping("/page")
    @Operation(summary = "getPage", description = "分页查询用户列表")
    public Page<UserEntity> getPage(UserParam param) {
        return userService.getPage(param);
    }

    /**
     * 通过id查询
     *
     * @param id 用户id
     * @return 用户
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
     * 新增
     *
     * @param param 参数
     * @return 新增成功返回true
     */
    @PostMapping
    @Operation(summary = "save", description = "新增用户")
    public long save(UserParam param) {
        return userService.save(param);
    }

    /**
     * 更新
     *
     * @param param 参数
     * @return 更新成功返回true
     */
    @PutMapping
    @Operation(summary = "update", description = "修改用户")
    public boolean update(UserParam param) {
        return userService.update(param);
    }

    /**
     * 删除
     *
     * @param id 主键id
     * @return 删除成功返回true
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "remove", description = "通过id删除用户")
    public boolean remove(@PathVariable
                              @Parameter(name = "id", description = "用户id")
                              @NotNull(message = "用户id不能为空")
                              Long id) {
        return userService.remove(id);
    }
}
