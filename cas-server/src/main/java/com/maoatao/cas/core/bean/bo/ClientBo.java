package com.maoatao.cas.core.bean.bo;

import com.maoatao.cas.core.bean.entity.ClientAuthenticationMethodEntity;
import com.maoatao.cas.core.bean.entity.ClientEntity;
import com.maoatao.cas.core.bean.entity.ClientGrantTypeEntity;
import com.maoatao.cas.core.bean.entity.ClientRedirectUrlEntity;
import com.maoatao.cas.core.bean.entity.ClientScopeEntity;
import com.maoatao.cas.core.bean.entity.ClientSettingEntity;
import com.maoatao.cas.core.bean.entity.ClientTokenSettingEntity;
import com.maoatao.synapse.core.bean.base.BaseBO;
import java.io.Serial;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

/**
 * 客户端BO
 *
 * @author LiYuanHao
 * @date 2023-04-11 17:23:58
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class ClientBo extends BaseBO {
    @Serial
    private static final long serialVersionUID = 5804517501427373879L;

    /**
     * 客户端信息
     */
    private ClientEntity content;
    /**
     * 验证方法
     */
    private Set<ClientAuthenticationMethodEntity> authenticationMethods;
    /**
     * 授权类型
     */
    private Set<ClientGrantTypeEntity> grantTypes;
    /**
     * 重定向地址
     */
    private Set<ClientRedirectUrlEntity> redirectUrls;
    /**
     * 作用域
     */
    private Set<ClientScopeEntity> scopes;
    /**
     * 配置
     */
    private ClientSettingEntity setting;
    /**
     * 令牌配置
     */
    private ClientTokenSettingEntity tokenSetting;

    @Tolerate
    public ClientBo() {}
}
