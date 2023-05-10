package com.maoatao.cas.core.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.common.annotation.CasAuth;
import com.maoatao.cas.core.bean.vo.ClientGrantTypeVO;
import com.maoatao.cas.core.bean.param.clientgranttype.ClientGrantTypeQueryParam;
import com.maoatao.cas.core.bean.param.clientgranttype.ClientGrantTypeSaveParam;
import com.maoatao.cas.core.bean.param.clientgranttype.ClientGrantTypeUpdateParam;
import com.maoatao.cas.core.constant.RequestPath;
import com.maoatao.cas.core.service.ClientGrantTypeService;
import com.maoatao.daedalus.web.annotation.OperationLog;
import com.maoatao.daedalus.web.annotation.ResponseHandle;
import com.maoatao.daedalus.web.enums.OperationType;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * CAS 客户端授权类型管理
 *
 * @author MaoAtao
 * @date 2023-04-07 22:48:12
 */
@ResponseHandle
@RestController
@RequestMapping(RequestPath.CAS_CORE + "/client/grant/type")
@Tag(name = "ClientGrantTypeController", description = "CAS 客户端授权类型管理")
public class ClientGrantTypeController {

    @Autowired
    private ClientGrantTypeService clientGrantTypeService;

    /**
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    @PostMapping("/page")
    @CasAuth("cas_clientgranttype_get")
    @Operation(summary = "getPage", description = "分页查询CAS 客户端授权类型列表")
    @OperationLog(type = OperationType.QUERY, content = "分页查询CAS 客户端授权类型", moduleName = "cas")
    public Page<ClientGrantTypeVO> page(@Validated @RequestBody ClientGrantTypeQueryParam param) {
        return clientGrantTypeService.page(param);
    }

    /**
     * 通过id查询
     *
     * @param id CAS 客户端授权类型id
     * @return CAS 客户端授权类型
     */
    @GetMapping("/{id}")
    @CasAuth("cas_clientgranttype_get")
    @Operation(summary = "details", description = "通过id查询CAS 客户端授权类型")
    @OperationLog(type = OperationType.QUERY, content = "查询CAS 客户端授权类型", moduleName = "cas")
    public ClientGrantTypeVO details(@PathVariable @Parameter(name = "id", description = "CAS 客户端授权类型id") Long id) {
        return clientGrantTypeService.details(id);
    }

    /**
     * 新增
     *
     * @param param 参数
     * @return 新增成功返回主键 id
     */
    @PostMapping
    @CasAuth("cas_clientgranttype_add")
    @Operation(summary = "save", description = "新增CAS 客户端授权类型")
    @OperationLog(type = OperationType.ADD, content = "新增CAS 客户端授权类型", moduleName = "cas")
    public long save(@Validated @RequestBody ClientGrantTypeSaveParam param) {
        return clientGrantTypeService.save(param);
    }

    /**
     * 更新
     *
     * @param param 参数
     * @return 更新成功返回true
     */
    @PutMapping
    @CasAuth("cas_clientgranttype_update")
    @Operation(summary = "update", description = "修改CAS 客户端授权类型")
    @OperationLog(type = OperationType.UPDATE, content = "修改CAS 客户端授权类型", moduleName = "cas")
    public boolean update(@Validated @RequestBody ClientGrantTypeUpdateParam param) {
        return clientGrantTypeService.update(param);
    }

    /**
     * 删除
     *
     * @param ids 主键id
     * @return 删除成功返回true
     */
    @DeleteMapping("/{ids}")
    @CasAuth("cas_clientgranttype_delete")
    @Operation(summary = "remove", description = "通过id删除CAS 客户端授权类型(多个 id 使用,分隔)")
    @OperationLog(type = OperationType.DELETE, content = "删除CAS 客户端授权类型", moduleName = "cas")
    public boolean remove(@PathVariable @Parameter(name = "ids", description = "CAS 客户端授权类型id集合") List<Long> ids) {
        return clientGrantTypeService.remove(ids);
    }
}
