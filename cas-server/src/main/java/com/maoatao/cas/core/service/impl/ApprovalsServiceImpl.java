package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.entity.ApprovalsEntity;
import com.maoatao.cas.core.bean.entity.ApprovalsScopeEntity;
import com.maoatao.cas.core.bean.entity.ClientEntity;
import com.maoatao.cas.core.bean.entity.ClientSettingEntity;
import com.maoatao.cas.core.bean.entity.ClientUserEntity;
import com.maoatao.cas.core.bean.param.approvals.ApprovalsQueryParam;
import com.maoatao.cas.core.bean.vo.ApprovalsVO;
import com.maoatao.cas.core.mapper.ApprovalsMapper;
import com.maoatao.cas.core.service.ApprovalsScopeService;
import com.maoatao.cas.core.service.ApprovalsService;
import com.maoatao.cas.core.service.ClientService;
import com.maoatao.cas.core.service.ClientSettingService;
import com.maoatao.cas.core.service.ClientUserService;
import com.maoatao.daedalus.data.service.impl.DaedalusServiceImpl;
import com.maoatao.daedalus.data.util.PageUtils;
import com.maoatao.synapse.lang.util.SynaAssert;
import com.maoatao.synapse.lang.util.SynaDates;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CAS 授权批准
 *
 * @author MaoAtao
 * @date 2023-05-06 09:53:13
 */
@Service
public class ApprovalsServiceImpl extends DaedalusServiceImpl<ApprovalsMapper, ApprovalsEntity> implements ApprovalsService {

    private static final String AUTHORITIES_SCOPE_PREFIX = "SCOPE_";

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientSettingService clientSettingService;

    @Autowired
    private ClientUserService clientUserService;

    @Autowired
    private ApprovalsScopeService approvalsScopeService;

    @Override
    public Page<ApprovalsVO> page(ApprovalsQueryParam param) {
        ApprovalsEntity entity = BeanUtil.copyProperties(param, ApprovalsEntity.class);
        Page<ApprovalsEntity> page = super.page(PageUtils.convert(param), Wrappers.query(entity));
        return PageUtils.convert(page, ApprovalsVO.class);
    }

    @Override
    public ApprovalsVO details(Long id) {
        return BeanUtil.toBean(super.getById(id), ApprovalsVO.class);
    }

    @Override
    public List<String> listScopeNamesByClientUser(String clientId, String username) {
        ClientUserEntity clientUserEntity = getClientUser(clientId, username);
        ApprovalsEntity approvalsEntity = getByClientUser(clientId, clientUserEntity.getUserId());
        if (ObjUtil.isNull(approvalsEntity)) {
            return Collections.emptyList();
        }
        List<ApprovalsScopeEntity> approvalsScopeEntities = approvalsScopeService.listByApprovalId(approvalsEntity.getId());
        if (CollectionUtil.isEmpty(approvalsScopeEntities)) {
            return Collections.emptyList();
        }
        return approvalsScopeService.listByApprovalId(approvalsEntity.getId()).stream().map(o -> o.getScope().replaceAll(AUTHORITIES_SCOPE_PREFIX, "")).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        ClientEntity clientEntity = getClient(authorizationConsent.getRegisteredClientId());
        ClientSettingEntity clientSettingEntity = clientSettingService.getByClientId(clientEntity.getClientId());
        SynaAssert.notNull(clientSettingEntity, "未找到客户端配置:{}", authorizationConsent.getRegisteredClientId());
        ClientUserEntity clientUserEntity = getClientUser(clientEntity.getClientId(), authorizationConsent.getPrincipalName());
        ApprovalsEntity approvalsEntity = getByClientUser(clientEntity.getClientId(), clientUserEntity.getUserId());
        if (ObjUtil.isNotNull(approvalsEntity)) {
            // 如果存在旧授权就删除
            SynaAssert.isTrue(remove(approvalsEntity.getId()), "旧的授权同意信息删除失败!");
        }
        ApprovalsEntity entity = ApprovalsEntity.builder()
                .clientId(clientEntity.getClientId())
                .userId(clientUserEntity.getUserId())
                .expiresDate(SynaDates.now().plusSeconds(clientSettingEntity.getApprovalsDuration()))
                .build();
        SynaAssert.isTrue(super.save(entity), "授权同意信息保存失败!");
        if (CollectionUtil.isNotEmpty(authorizationConsent.getAuthorities())) {
            List<ApprovalsScopeEntity> approvalsScopeEntities = authorizationConsent.getAuthorities().stream()
                    .map(grantedAuthority -> ApprovalsScopeEntity.builder().approvalsId(entity.getId()).scope(grantedAuthority.getAuthority()).build())
                    .toList();
            SynaAssert.isTrue(approvalsScopeService.saveBatch(approvalsScopeEntities), "授权同意作用域保存失败!");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean remove(Long approvalsId) {
        List<Long> approvalsScopeIds = approvalsScopeService.listByApprovalId(approvalsId).stream()
                .map(ApprovalsScopeEntity::getId).toList();
        SynaAssert.isTrue(super.removeById(approvalsId), "授权同意信息删除失败!");
        if (CollectionUtil.isNotEmpty(approvalsScopeIds)) {
            SynaAssert.isTrue(approvalsScopeService.remove(approvalsScopeIds), "授权同意作用域删除失败!");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        ClientEntity clientEntity = getClient(authorizationConsent.getRegisteredClientId());
        ClientUserEntity clientUserEntity = getClientUser(clientEntity.getClientId(), authorizationConsent.getPrincipalName());
        SynaAssert.notNull(clientUserEntity, "未找到客户端:{}的用户:{}", authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
        ApprovalsEntity approvalsEntity = getByClientUser(clientUserEntity.getClientId(), clientUserEntity.getUserId());
        SynaAssert.notNull(approvalsEntity, "未找到授权同意信息,客户端:{} 用户:{}", authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
        SynaAssert.isTrue(super.removeById(approvalsEntity.getId()), "授权同意信息删除失败!");
        SynaAssert.isTrue(approvalsScopeService.removeByApprovalId(approvalsEntity.getId()), "授权同意作用域删除失败!");
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        ClientEntity clientEntity = getClient(registeredClientId);
        ClientUserEntity clientUserEntity = getClientUser(clientEntity.getClientId(), principalName);
        SynaAssert.notNull(clientUserEntity, "未找到客户端:{}的用户:{}", registeredClientId, principalName);
        ApprovalsEntity approvalsEntity = getByClientUser(clientUserEntity.getClientId(), clientUserEntity.getUserId());
        if (ObjUtil.isNull(approvalsEntity)) {
            return null;
        }
        if (SynaDates.now().isAfter(approvalsEntity.getExpiresDate())) {
            return null;
        }
        List<ApprovalsScopeEntity> approvalsScopeEntities = approvalsScopeService.listByApprovalId(approvalsEntity.getId());
        OAuth2AuthorizationConsent.Builder builder = OAuth2AuthorizationConsent.withId(clientEntity.getClientId(), principalName);
        approvalsScopeEntities.forEach(o -> builder.authority(new SimpleGrantedAuthority(o.getScope())));
        return builder.build();
    }

    private ApprovalsEntity getByClientUser(String clientId, long userId) {
        return super.getOne(Wrappers.<ApprovalsEntity>lambdaQuery().eq(ApprovalsEntity::getClientId, clientId).eq(ApprovalsEntity::getUserId, userId));
    }

    private ClientUserEntity getClientUser(String clientId, String name) {
        ClientUserEntity clientUserEntity = clientUserService.getByNameAndClient(name, clientId);
        SynaAssert.notNull(clientUserEntity, "未找到客户端:{}登录名:{}的用户", clientId, name);
        return clientUserEntity;
    }

    private ClientEntity getClient(String clientId) {
        // OAuth2AuthorizationConsent 里的 registeredClientId 有时是 客户端主键id 有时是客户端id
        ClientEntity clientEntity = clientService.getById(clientId);
        if (ObjUtil.isNotNull(clientEntity)) {
            return clientEntity;
        }
        clientEntity = clientService.getByClientId(clientId);
        SynaAssert.notNull(clientEntity, "未找到客户端:{}", clientId);
        return clientEntity;
    }
}
