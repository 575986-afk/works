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
        
        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
        Object companyNo = session.getAttribute("companyNo");
        Object userNo = session.getAttribute("userNo");
        
        if (user == null) {
            String ajaxHeader = request.getHeader("X-Requested-With");
            if ("XMLHttpRequest".equals(ajaxHeader) || request.getRequestURI().contains("Data")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            
            response.sendRedirect("/login");
            return false;
        }
        if (companyNo == null ||userNo == null) {
        	String ajaxHeader = request.getHeader("X-Requested-With");
        	if ("XMLHttpRequest".equals(ajaxHeader) || request.getRequestURI().contains("Data")) {
        		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        		return false;
        	}
        	
        	response.sendRedirect("/login");
        	return false;
        }

        return true;
    }
}