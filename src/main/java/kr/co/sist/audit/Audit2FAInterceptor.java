package kr.co.sist.audit;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class Audit2FAInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        System.out.println("======================================");
        System.out.println(">>> Audit2FAInterceptor 실행");
        System.out.println(">>> 요청 URL : " + request.getRequestURI());

        HttpSession session = request.getSession();

        Boolean authenticated =
                (Boolean) session.getAttribute("audit2FAAuthenticated");

        System.out.println(">>> audit2FAAuthenticated = " + authenticated);

        // 이미 인증했다면 통과
        if (Boolean.TRUE.equals(authenticated)) {

            System.out.println(">>> 이미 인증됨 → 통과");

            return true;
        }

        // 현재 접근하려던 감사 페이지 저장
        String targetUrl = request.getRequestURI();

        if (request.getQueryString() != null) {
            targetUrl += "?" + request.getQueryString();
        }

        session.setAttribute("auditTargetUrl", targetUrl);

        System.out.println(">>> 인증 필요");
        System.out.println(">>> auditTargetUrl = " + targetUrl);

        // 2단계 인증 선택 페이지
        response.sendRedirect(
            request.getContextPath()
                + "/adminUser/audit/audit"
        );

        return false;
    }
}