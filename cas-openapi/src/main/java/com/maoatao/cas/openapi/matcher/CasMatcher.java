package com.maoatao.cas.openapi.matcher;

import java.util.Map;
import org.springframework.util.AntPathMatcher;

/**
 * @author MaoAtao
 * @date 2023-03-26 16:29:12
 */
public class CasMatcher {

    private final AntPathMatcher antMatcher;

    private final String pattern;

    public CasMatcher(String pattern, boolean caseSensitive) {
        this.pattern = pattern;
        this.antMatcher = createMatcher(caseSensitive);
    }

    public boolean matches(String path) {
        return this.antMatcher.match(this.pattern, path);
    }

    public Map<String, String> extractUriTemplateVariables(String path) {
        return this.antMatcher.extractUriTemplateVariables(this.pattern, path);
    }

    private AntPathMatcher createMatcher(boolean caseSensitive) {
        AntPathMatcher matcher = new AntPathMatcher();
        matcher.setTrimTokens(false);
        matcher.setCaseSensitive(caseSensitive);
        return matcher;
    }
}
