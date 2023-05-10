package com.maoatao.cas.util;

import com.maoatao.synapse.lang.util.SynaStrings;
import java.util.Objects;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

/**
 * 客户端设置工具类
 *
 * @author MaoAtao
 * @date 2022-10-19 13:18:42
 */
public final class ClientSettingUtils {

    private ClientSettingUtils() {}

    /**
     * 客户端别名
     */
    private static final String CLIENT_ALIAS = "settings.client.alias";

    public static void setClientAlias(ClientSettings.Builder builder, String alias) {
        builder.setting(CLIENT_ALIAS, alias);
    }

    public static String getClientAlias(ClientSettings settings) {
        return Objects.requireNonNullElse(settings.getSetting(CLIENT_ALIAS), SynaStrings.EMPTY);
    }
}
