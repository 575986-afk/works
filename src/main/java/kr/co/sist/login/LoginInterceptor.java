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
        
        if (user == null || companyNo == null || userNo == null) {
            
            // 💡 1. AJAX 요청이나 데이터 요청이 아닐 때만 이전 주소 저장 및 리다이렉트 수행
            String ajaxHeader = request.getHeader("X-Requested-With");
            if ("XMLHttpRequest".equals(ajaxHeader) || request.getRequestURI().contains("Data")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            
            // 2. 사용자가 원래 가려던 주소(URI + 파라미터) 조합하기
            String uri = request.getRequestURI();
            String query = request.getQueryString();
            if (query != null && !query.isEmpty()) {
                uri += "?" + query;
            }
            
            // 3. 세션에 'redirectURL' 이름으로 저장
            session.setAttribute("redirectURL", uri);
            
            // 4. 로그인 페이지로 이동
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}