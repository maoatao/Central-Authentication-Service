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
        sb.append("    <link rel=\"icon\" href=\"data:image/png;base64,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\">\n");
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