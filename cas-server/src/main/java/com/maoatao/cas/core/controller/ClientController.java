package com.maoatao.cas.core.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.common.annotation.CasAuth;
import com.maoatao.cas.core.bean.vo.ClientVO;
import com.maoatao.cas.core.bean.param.client.ClientQueryParam;
import com.maoatao.cas.core.bean.param.client.ClientSaveParam;
import com.maoatao.cas.core.bean.param.client.ClientUpdateParam;
import com.maoatao.cas.core.service.ClientService;
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
 * CAS 客户端管理
 *
 * @author MaoAtao
 * @date 2023-04-07 22:48:12
 */
@ResponseHandle
@RestController
@RequestMapping("/client")
@Tag(name = "ClientController", description = "CAS 客户端管理")
public class ClientController {

    @Autowired
    private ClientService clientService;

    /**
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    @GetMapping("/page")
    @CasAuth("cas_client_get")
    @Operation(summary = "getPage", description = "分页查询CAS 客户端列表")
    @OperationLog(type = OperationType.QUERY, content = "分页查询CAS 客户端", moduleName = "cas")
    public Page<ClientVO> page(@Validated ClientQueryParam param) {
        return clientService.page(param);
    }

    /**
     * 通过id查询
     *
     * @param id CAS 客户端id
     * @return CAS 客户端
     */
    @GetMapping("/{id}")
    @CasAuth("cas_client_get")
    @Operation(summary = "details", description = "通过id查询CAS 客户端")
    @OperationLog(type = OperationType.QUERY, content = "查询CAS 客户端", moduleName = "cas")
    public ClientVO details(@PathVariable @Parameter(name = "id", description = "CAS 客户端id") Long id) {
        return clientService.details(id);
    }

    /**
     * 新增
     *
     * @param param 参数
     * @return 新增成功返回主键 id
     */
    @PostMapping
    @CasAuth("cas_client_add")
    @Operation(summary = "save", description = "新增CAS 客户端")
    @OperationLog(type = OperationType.ADD, content = "新增CAS 客户端", moduleName = "cas")
    public long save(@Validated @RequestBody ClientSaveParam param) {
        return clientService.save(param);
    }

    /**
     * 更新
     *
     * @param param 参数
     * @return 更新成功返回true
     */
    @PutMapping
    @CasAuth("cas_client_update")
    @Operation(summary = "update", description = "修改CAS 客户端")
    @OperationLog(type = OperationType.UPDATE, content = "修改CAS 客户端", moduleName = "cas")
    public boolean update(@Validated @RequestBody ClientUpdateParam param) {
        return clientService.update(param);
    }

    /**
     * 删除
     *
     * @param ids 主键id
     * @return 删除成功返回true
     */
    @DeleteMapping("/{ids}")
    @CasAuth("cas_client_delete")
    @Operation(summary = "remove", description = "通过id删除CAS 客户端(多个 id 使用,分隔)")
    @OperationLog(type = OperationType.DELETE, content = "删除CAS 客户端", moduleName = "cas")
    public boolean remove(@PathVariable @Parameter(name = "ids", description = "CAS 客户端id集合") List<Long> ids) {
        return clientService.remove(ids);
    }
}
