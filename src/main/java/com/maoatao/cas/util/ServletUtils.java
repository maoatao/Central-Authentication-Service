package com.maoatao.cas.util;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

/**
 * Servlet工具类
 *
 * @author MaoAtao
 * @date 2022-03-16 09:40:39
 */
public abstract class ServletUtils {
    /**
     * 请求格式为json
     */
    private static final String APPLICATION_JSON = "application/json";
    /**
     * 请求格式为xml
     */
    private static final String XML_HTTP_REQUEST = "XMLHttpRequest";
    /**
     * json后缀
     */
    private static final String JSON_SUFFIX = ".json";
    /**
     * xml后缀
     */
    private static final String XML_SUFFIX = ".xml";

    /**
     * 获取String参数
     *
     * @param name 参数名
     * @return String 参数值
     */
    public static String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    /**
     * 获取String参数
     *
     * @param name         参数名
     * @param defaultValue 默认值
     * @return String 参数值
     */
    public static String getParameter(String name, String defaultValue) {
        return toStr(getRequest().getParameter(name), defaultValue);
    }

    /**
     * 获取Integer参数
     *
     * @param name 参数名
     * @return Integer 参数值
     */
    public static Integer getParameterToInt(String name) {
        return toInt(getRequest().getParameter(name), null);
    }

    /**
     * 获取Integer参数
     *
     * @param name         参数名
     * @param defaultValue 默认值
     * @return Integer 参数值
     */
    public static Integer getParameterToInt(String name, Integer defaultValue) {
        return toInt(getRequest().getParameter(name), defaultValue);
    }

    /**
     * 获取request
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    /**
     * 获取response
     *
     * @return HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    /**
     * 获取session
     *
     * @return HttpSession
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 获取Attributes
     *
     * @return ServletRequestAttributes
     */
    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes)attributes;
    }

    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string   待渲染的字符串
     * @return null
     */
    public static String renderString(HttpServletResponse response, String string) {
        try {
            response.setContentType(APPLICATION_JSON);
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否是Ajax异步请求
     *
     * @param request 客户端请求
     * @return boolean
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        if (accept != null && accept.contains(APPLICATION_JSON)) {
            return true;
        }

        String xRequestedWith = request.getHeader("X-Requested-With");
        if (xRequestedWith != null && xRequestedWith.contains(XML_HTTP_REQUEST)) {
            return true;
        }

        String uri = request.getRequestURI();
        if (inStringIgnoreCase(uri, JSON_SUFFIX, XML_SUFFIX)) {
            return true;
        }

        String ajax = request.getParameter("__ajax");
        return inStringIgnoreCase(ajax, "json", "xml");
    }

    /**
     * 转换为int<br>
     * 如果给定的值为空，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value        被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return Integer
     */
    public static Integer toInt(Object value, Integer defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        if (value instanceof Integer) {
            return (Integer)value;
        }

        if (value instanceof Number) {
            return ((Number)value).intValue();
        }

        final String valueStr = toStr(value, null);
        if (StrUtil.isBlank(valueStr)) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(valueStr.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 转换为字符串<br>
     * 如果给定的值为null，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value        被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return String
     */
    public static String toStr(Object value, String defaultValue) {
        if (null == value) {
            return defaultValue;
        }

        if (value instanceof String) {
            return (String)value;
        }

        return value.toString();
    }

    /**
     * 是否包含字符串(不区分大小写)
     *
     * @param str           验证字符串
     * @param searchStrings 字符串组
     * @return boolean
     */
    public static boolean inStringIgnoreCase(String str, String... searchStrings) {
        if (str != null && searchStrings != null) {
            for (String s : searchStrings) {
                if (str.equalsIgnoreCase(StrUtil.trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取客户端请求的授权信息
     *
     * @return 授权信息(token)
     */
    public static String getAuthorization() {
        return getAuthorization(getRequest());
    }

    /**
     * 获取客户端请求的授权信息
     *
     * @param request 客户端请求
     * @return 授权信息(token)
     */
    public static String getAuthorization(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }
}
