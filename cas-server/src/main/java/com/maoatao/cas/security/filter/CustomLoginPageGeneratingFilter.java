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
        sb.append("    <title>CAS - 中央认证服务</title>\n");
        sb.append("    <style>\n");
        sb.append("      * {\n");
        sb.append("          margin: 0;\n");
        sb.append("          padding: 0;\n");
        sb.append("          box-sizing: border-box;\n");
        sb.append("          user-select: none;\n");
        sb.append("      }\n");
        sb.append("      html, body {\n");
        sb.append("          height: 100%;\n");
        sb.append("      }\n");
        sb.append("      .container {\n");
        sb.append("          height: 100%;\n");
        sb.append("          background-image: linear-gradient(to right, #fbc2eb, #a6c1ee);\n");
        sb.append("      }\n");
        sb.append("      .login-wrapper {\n");
        sb.append("          background-color: #fff;\n");
        sb.append("          width: 358px;\n");
        sb.append("          height: 588px;\n");
        sb.append("          border-radius: 15px;\n");
        sb.append("          padding: 0 50px;\n");
        sb.append("          position: relative;\n");
        sb.append("          left: 50%;\n");
        sb.append("          top: 50%;\n");
        sb.append("          transform: translate(-50%, -50%);\n");
        sb.append("      }\n");
        sb.append("      .header {\n");
        sb.append("          font-size: 38px;\n");
        sb.append("          font-weight: bold;\n");
        sb.append("          text-align: center;\n");
        sb.append("          line-height: 200px;\n");
        sb.append("      }\n");
        sb.append("      .input-item {\n");
        sb.append("          display: block;\n");
        sb.append("          width: 100%;\n");
        sb.append("          margin-bottom: 20px;\n");
        sb.append("          border: 0;\n");
        sb.append("          padding: 10px;\n");
        sb.append("          border-bottom: 1px solid rgb(128, 125, 125);\n");
        sb.append("          font-size: 15px;\n");
        sb.append("          outline: none;\n");
        sb.append("      }\n");
        sb.append("      .input-item::placeholder {\n");
        sb.append("          text-transform: uppercase;\n");
        sb.append("      }\n");
        sb.append("      .btn {\n");
        sb.append("          text-align: center;\n");
        sb.append("          padding: 10px;\n");
        sb.append("          width: 100%;\n");
        sb.append("          margin-top: 40px;\n");
        sb.append("          background-image: linear-gradient(to right, #a6c1ee, #fbc2eb);\n");
        sb.append("          color: #fff;\n");
        sb.append("          border: 0;\n");
        sb.append("      }\n");
        sb.append("      .msg {\n");
        sb.append("          text-align: center;\n");
        sb.append("          height: 80px;\n");
        sb.append("      }\n");
        sb.append("      .msg.succes {\n");
        sb.append("          color: #abc1ee;\n");
        sb.append("      }\n");
        sb.append("      .msg.error {\n");
        sb.append("          color: #f56c6c;\n");
        sb.append("      }\n");
        sb.append("    </style>\n");
        sb.append("  </head>\n");
        sb.append("  <body>\n");
        sb.append("    <div class=\"container\">\n");
        sb.append("      <div class=\"login-wrapper\">\n");
        sb.append("      <div class=\"header\">登 录</div>\n");
        sb.append(createError(loginError, errorMsg));
        sb.append(createLogoutSuccess(logoutSuccess));
        sb.append("        <div class=\"form-wrapper\">\n");
        sb.append("          <form method=\"post\" action=\"").append(contextPath).append(LOGIN_URL).append("\">\n");
        sb.append("            <label>\n");
        sb.append("              <input type=\"text\" id=\"clientId\" name=\"clientId\" class=\"input-item\" placeholder=\"客户端ID\" required>\n");
        sb.append("            </label>\n");
        sb.append("            <label>\n");
        sb.append("              <input type=\"text\" id=\"username\" name=\"username\" class=\"input-item\" placeholder=\"用户名\" required autofocus>\n");
        sb.append("            </label>\n");
        sb.append("            <label>\n");
        sb.append("              <input type=\"password\" id=\"password\" name=\"password\" class=\"input-item\" placeholder=\"密码\" required>\n");
        sb.append("            </label>\n");
        sb.append("            <button class=\"btn\" type=\"submit\">登 录</button>\n");
        sb.append("          </form>\n");
        sb.append("        </div>\n");
        sb.append("      </div>\n");
        sb.append("    </div>\n");
        sb.append("  </body>");
        sb.append("</html>");
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

    private String createError(boolean isError, String message) {
        if (!isError) {
            return "";
        }
        return "<div class=\"msg error\">" + HtmlUtils.htmlEscape(message) + "</div>";
    }

    private String createLogoutSuccess(boolean isLogoutSuccess) {
        if (!isLogoutSuccess) {
            return "";
        }
        return "<div class=\"msg success\">You have been signed out</div>";
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