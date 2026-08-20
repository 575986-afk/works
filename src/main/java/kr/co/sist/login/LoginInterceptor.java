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
        
        // 세션에 유저 정보가 없으면
        if (user == null) {
            // AJAX 요청이거나 'Data'가 포함된 경로인 경우 (예: /userInfoData 등)
            String ajaxHeader = request.getHeader("X-Requested-With");
            if ("XMLHttpRequest".equals(ajaxHeader) || request.getRequestURI().contains("Data")) {
                // 리다이렉트 대신 401 Unauthorized 에러 코드 전송
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            
            // 일반 페이지 요청일 경우 로그인 페이지로 리다이렉트
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}