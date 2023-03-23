package com.maoatao.cas.security.bean;

import com.maoatao.synapse.core.bean.base.BaseVO;
import com.maoatao.synapse.lang.util.SynaAssert;
import com.maoatao.synapse.lang.util.SynaDates;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;

import java.io.Serial;
import java.util.Set;

/**
 * 自定义访问令牌
 *
 * @author MaoAtao
 * @date 2023-03-05 00:58:55
 */
@Slf4j
@Getter
@Builder
@ToString
@Schema(description = "自定义访问令牌")
public class CustomAccessToken extends BaseVO {
    @Serial
    private static final long serialVersionUID = 7546337242964121016L;

    /**
     * 访问令牌值
     */
    @Schema(description = "访问令牌值")
    private String accessToken;
    /**
     * 刷新令牌值
     */
    @Schema(description = "刷新令牌值")
    private String refreshToken;
    /**
     * 授权范围
     */
    @Schema(description = "授权范围")
    private Set<String> scope;
    /**
     * 令牌类型
     */
    @Schema(description = "令牌类型")
    private String tokenType;
    /**
     * 过期时间
     */
    @Schema(description = "过期时间")
    private long expiresIn;

    public static CustomAccessToken of(OAuth2AccessTokenAuthenticationToken accessTokenAuthentication) {
        OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
        SynaAssert.notNull(accessToken, "令牌生成失败!");
        OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
        CustomAccessToken.CustomAccessTokenBuilder customAccessTokenBuilder = CustomAccessToken.builder();
        long now = SynaDates.now(SynaDates.DateType.MILLI_SECOND);
        long expiresAt = accessTokenAuthentication.getAccessToken().getExpiresAt().toEpochMilli();
        // 求差值去毫秒
        long expiresIn = (expiresAt - now) / 1000;
        if (expiresIn <= 0) {
            log.warn("令牌生成成功,但已过期? issuedAt:{},expiresAt:{},now:{}", accessTokenAuthentication.getAccessToken().getIssuedAt().toEpochMilli(), expiresAt, now);
        }
        customAccessTokenBuilder.accessToken(accessToken.getTokenValue()).scope(accessToken.getScopes()).expiresIn(expiresIn);
        if (accessToken.getTokenType() != null) {
            customAccessTokenBuilder.tokenType(accessToken.getTokenType().getValue());
        }
        if (refreshToken != null) {
            customAccessTokenBuilder.refreshToken(refreshToken.getTokenValue());
        }
        return customAccessTokenBuilder.build();
    }
}
