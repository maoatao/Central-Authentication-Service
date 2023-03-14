package com.maoatao.cas.core.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.entity.RoleEntity;
import com.maoatao.cas.core.service.RoleService;
import com.maoatao.cas.core.param.RoleParam;
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

import java.util.List;

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
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    @GetMapping("/page")
    @Operation(summary = "getPage", description = "分页查询角色列表")
    public Page<RoleEntity> getPage(RoleParam param) {
        return roleService.getPage(param);
    }

    /**
     * 通过id查询
     *
     * @param id 主键id
     * @return 角色
     */
    @GetMapping("/{id}")
    @Operation(summary = "getById", description = "通过id查询角色")
    public RoleEntity getById(@PathVariable
                              @Parameter(name = "id", description = "角色id")
                              @NotNull(message = "角色id不能为空")
                              Long id) {
        return roleService.getById(id);
    }

    /**
     * 新增
     *
     * @param param 参数
     * @return 新增成功返回主键 id
     */
    @PostMapping
    @Operation(summary = "save", description = "新增角色")
    public long save(RoleParam param) {
        return roleService.save(param);
    }

    /**
     * 更新
     *
     * @param param 参数
     * @return 更新成功返回true
     */
    @PutMapping
    @Operation(summary = "update", description = "修改角色")
    public boolean update(RoleParam param) {
        return roleService.update(param);
    }

    /**
     * 删除
     *
     * @param ids 主键id
     * @return 删除成功返回true
     */
    @DeleteMapping("/{ids}")
    @Operation(summary = "remove", description = "通过id删除角色(多个 id 使用,分隔)")
    public boolean remove(@PathVariable
                          @Parameter(name = "id", description = "角色id集合")
                          @NotNull(message = "角色id不能为空")
                          List<Long> ids) {
        return roleService.remove(ids);
    }
}
