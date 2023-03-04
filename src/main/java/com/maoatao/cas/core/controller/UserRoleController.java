package com.maoatao.cas.core.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.entity.UserRoleEntity;
import com.maoatao.cas.core.service.UserRoleService;
import com.maoatao.cas.core.param.UserRoleParam;
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
 * 用户角色关系管理
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:22
 */
@RestController
@RequestMapping(HttpConstants.BASE_URL + "/userrole")
@Tag(name = "UserRoleController", description = "用户角色关系管理")
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
    @Operation(summary = "getPage", description = "分页查询用户角色关系列表")
    public Page<UserRoleEntity> getPage(UserRoleParam param) {
        return userRoleService.getPage(param);
    }

    /**
     * 通过id查询
     *
     * @param id 主键id
     * @return 用户角色关系
     */
    @GetMapping("/{id}")
    @Operation(summary = "getById", description = "通过id查询用户角色关系")
    public UserRoleEntity getById(@PathVariable
                                  @Parameter(name = "id", description = "用户角色关系id")
                                  @NotNull(message = "用户角色关系id不能为空")
                                  Long id) {
        return userRoleService.getById(id);
    }

    /**
     * 新增
     *
     * @param param 参数
     * @return 新增成功返回true
     */
    @PostMapping
    @Operation(summary = "save", description = "新增用户角色关系")
    public boolean save(UserRoleParam param) {
        return userRoleService.save(param);
    }

    /**
     * 更新
     *
     * @param param 参数
     * @return 更新成功返回true
     */
    @PutMapping
    @Operation(summary = "update", description = "修改用户角色关系")
    public boolean update(UserRoleParam param) {
        return userRoleService.update(param);
    }

    /**
     * 删除
     *
     * @param id 主键id
     * @return 删除成功返回true
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "remove", description = "通过id删除用户角色关系")
    public boolean remove(@PathVariable
                          @Parameter(name = "id", description = "用户角色关系id")
                          @NotNull(message = "用户角色关系id不能为空")
                          Long id) {
        return userRoleService.remove(id);
    }
}
