package com.maoatao.cas.openapi.matcher;

import cn.hutool.core.collection.IterUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Daedalus 请求匹配
 *
 * @author MaoAtao
 * @date 2023-03-26 16:29:12
 */
public class DaedalusRequestMatcher {

    private static final String MATCH_ALL = "/**";

    private final DaedalusMatcher matcher;

    private final String pattern;

    private final HttpMethod httpMethod;

    public static DaedalusRequestMatcher antMatcher(HttpMethod method, String pattern) {
        Assert.notNull(method, "method cannot be null");
        Assert.hasText(pattern, "pattern cannot be empty");
        return new DaedalusRequestMatcher(pattern, method);
    }

    public DaedalusRequestMatcher(String pattern) {
        this(pattern, null);
    }

    public DaedalusRequestMatcher(String pattern, HttpMethod httpMethod) {
        this(pattern, httpMethod, true);
    }


    public DaedalusRequestMatcher(String pattern, HttpMethod httpMethod, boolean caseSensitive) {
        this.pattern = pattern;
        this.httpMethod = httpMethod;
        this.matcher = new DaedalusMatcher(pattern, caseSensitive);
    }

    public boolean isMatch(HttpServletRequest request) {
        if (this.httpMethod != null && StringUtils.hasText(request.getMethod())
                && !this.httpMethod.name().equals(request.getMethod())) {
            return false;
        }
        if (this.pattern.equals(MATCH_ALL)) {
            return true;
        }
        String url = getRequestPath(request);
        return this.matcher.matches(url);
    }

    private String getRequestPath(HttpServletRequest request) {
        String url = request.getServletPath();
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            url = StringUtils.hasLength(url) ? url + pathInfo : pathInfo;
        }
        return url;
    }

    /**
     * @return 请求匹配器构建器
     */
    public static RequestMatchersBuilder builder() {
        return new RequestMatchersBuilder();
    }

    /**
     * 是否存在请求匹配
     *
     * @return 有任意匹配返回 true, matchers 为空返回 false
     */
    public static boolean anyMatch(List<DaedalusRequestMatcher> matchers, HttpServletRequest request) {
        if (IterUtil.isEmpty(matchers)) {
            return false;
        }
        for (DaedalusRequestMatcher requestMatcher : matchers) {
            if (requestMatcher.isMatch(request)) {
                return true;
            }
        }
        return false;
    }

    public static class RequestMatchersBuilder {
        private final List<DaedalusRequestMatcher> matchers;

        public RequestMatchersBuilder() {
            this.matchers = new ArrayList<>();
        }

        /**
         * 构建matchers
         *
         * @param httpMethod  请求方法
         * @param antPatterns 匹配 url
         * @return RequestMatchersBuilder
         */
        public RequestMatchersBuilder antMatchers(HttpMethod httpMethod, String... antPatterns) {
            addMatchers(httpMethod, antPatterns);
            return this;
        }

        public List<DaedalusRequestMatcher> build() {
            return this.matchers;
        }

        private void addMatchers(HttpMethod httpMethod, String... antPatterns) {
            for (String pattern : antPatterns) {
                this.matchers.add(new DaedalusRequestMatcher(pattern, httpMethod));
            }
        }
    }
}
