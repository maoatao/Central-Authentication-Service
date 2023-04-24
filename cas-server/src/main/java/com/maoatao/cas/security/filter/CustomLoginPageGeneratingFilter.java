package com.maoatao.cas.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 自定义登录页面
 * <p>
 * Customized by {@link org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter}
 *
 * @author MaoAtao
 * @date 2023-03-10 19:15:54
 */
public class CustomLoginPageGeneratingFilter extends GenericFilterBean {

    private static final String LOGIN_URL = "/login";
    private static final String ERROR_PARAMETER_NAME = "error";
    private static final String LOGOUT_URL = LOGIN_URL.concat("?logout");
    private static final String FAILURE_URL = LOGIN_URL.concat("?").concat(ERROR_PARAMETER_NAME);

    public CustomLoginPageGeneratingFilter() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        boolean loginError = isErrorPage(request);
        boolean logoutSuccess = isLogoutSuccess(request);
        if (isLoginUrlRequest(request) || loginError || logoutSuccess) {
            String loginPageHtml = generateLoginPageHtml(request, loginError, logoutSuccess);
            response.setContentType("text/html;charset=UTF-8");
            response.setContentLength(loginPageHtml.getBytes(StandardCharsets.UTF_8).length);
            response.getWriter().write(loginPageHtml);
            return;
        }
        chain.doFilter(request, response);
    }

    private String generateLoginPageHtml(HttpServletRequest request, boolean loginError, boolean logoutSuccess) {
        String errorMsg = "Invalid credentials";
        if (loginError) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                AuthenticationException ex = (AuthenticationException) session
                        .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
                errorMsg = (ex != null) ? ex.getMessage() : "Invalid credentials";
            }
        }
        String contextPath = request.getContextPath();
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html lang=\"en\">\n");
        sb.append("  <head>\n");
        sb.append("    <meta charset=\"utf-8\">\n");
        sb.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n");
        sb.append("    <meta name=\"description\" content=\"\">\n");
        sb.append("    <meta name=\"author\" content=\"\">\n");
        sb.append("    <title>Please sign in</title>\n");
        // sb.append("    <link href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M\" crossorigin=\"anonymous\">\n");
        // sb.append("    <link href=\"https://getbootstrap.com/docs/4.0/examples/signin/signin.css\" rel=\"stylesheet\" crossorigin=\"anonymous\"/>\n");
        sb.append("  </head>\n");
        sb.append("  <body>\n");
        sb.append("     <div class=\"container\">\n");
        sb.append("      <form class=\"form-signin\" method=\"post\" action=\"").append(contextPath).append(LOGIN_URL).append("\">\n");
        sb.append("        <h2 class=\"form-signin-heading\">Please sign in</h2>\n");
        sb.append(createError(loginError, errorMsg));
        sb.append(createLogoutSuccess(logoutSuccess));
        sb.append("        <p>\n");
        sb.append("          <label for=\"clientId\" class=\"sr-only\">Client Id</label>\n");
        sb.append("          <input type=\"text\" id=\"clientId\" name=\"clientId\" class=\"form-control\" placeholder=\"Client Id\" required>\n");
        sb.append("        </p>\n");
        sb.append("        <p>\n");
        sb.append("          <label for=\"username\" class=\"sr-only\">Username</label>\n");
        sb.append("          <input type=\"text\" id=\"username\" name=\"username\" class=\"form-control\" placeholder=\"Username\" required autofocus>\n");
        sb.append("        </p>\n");
        sb.append("        <p>\n");
        sb.append("          <label for=\"password\" class=\"sr-only\">Password</label>\n");
        sb.append("          <input type=\"password\" id=\"password\" name=\"password\" class=\"form-control\" placeholder=\"Password\" required>\n");
        sb.append("        </p>\n");
        sb.append("        <button class=\"btn btn-lg btn-primary btn-block\" type=\"submit\">Sign in</button>\n");
        sb.append("      </form>\n");
        sb.append("</div>\n");
        sb.append("</body></html>");
        return sb.toString();
    }

    private boolean isLogoutSuccess(HttpServletRequest request) {
        return matches(request, LOGOUT_URL);
    }

    private boolean isLoginUrlRequest(HttpServletRequest request) {
        return matches(request, LOGIN_URL);
    }

    private boolean isErrorPage(HttpServletRequest request) {
        return matches(request, FAILURE_URL);
    }

    private static String createError(boolean isError, String message) {
        if (!isError) {
            return "";
        }
        return "<div class=\"alert alert-danger\" role=\"alert\">" + HtmlUtils.htmlEscape(message) + "</div>";
    }

    private static String createLogoutSuccess(boolean isLogoutSuccess) {
        if (!isLogoutSuccess) {
            return "";
        }
        return "<div class=\"alert alert-success\" role=\"alert\">You have been signed out</div>";
    }

    private boolean matches(HttpServletRequest request, String url) {
        if (!HttpMethod.GET.name().equals(request.getMethod()) || url == null) {
            return false;
        }
        String uri = request.getRequestURI();
        int pathParamIndex = uri.indexOf(';');
        if (pathParamIndex > 0) {
            uri = uri.substring(0, pathParamIndex);
        }
        if (request.getQueryString() != null) {
            uri += "?" + request.getQueryString();
        }
        if ("".equals(request.getContextPath())) {
            return uri.equals(url);
        }
        return uri.equals(request.getContextPath() + url);
    }
}