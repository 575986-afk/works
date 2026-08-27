package kr.co.sist.login;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        String uri = request.getRequestURI();
        
        if (uri.equals("/favicon.ico") || uri.startsWith("/css/") || uri.startsWith("/js/") || uri.startsWith("/images/") || uri.startsWith("/upload/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return false;
        }

        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
        Object companyNo = session.getAttribute("companyNo");
        Object userNo = session.getAttribute("userNo");
        
        if (user == null || companyNo == null || userNo == null) {
            
            String ajaxHeader = request.getHeader("X-Requested-With");
            if ("XMLHttpRequest".equals(ajaxHeader) || uri.contains("Data")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            
            String query = request.getQueryString();
            if (query != null && !query.isEmpty()) {
                uri += "?" + query;
            }
            
            if (!uri.contains("/login")) {
                session.setAttribute("redirectURL", uri);
            }
            
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}