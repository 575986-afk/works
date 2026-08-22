package kr.co.sist.security;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.sist.signup.UserDTO;

@Component
public class RoleLevelInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        System.out.println("======================================");
        System.out.println(">>> RoleLevelInterceptor 실행");
        System.out.println(">>> 요청 URL : " + request.getRequestURI());

        HttpSession session = request.getSession();

        // 현재 로그인 사용자 확인
        UserDTO user = (UserDTO) session.getAttribute("user");

        if (user == null) {
            System.out.println(">>> 로그인 사용자 없음");
            System.out.println(">>> 로그인 페이지로 이동");
            response.sendRedirect(
                request.getContextPath() + "/login"
            );
            return false;
        }

        // 현재 사용자의 role_level
        int roleLevel = user.getRole_level();
        System.out.println(">>> 현재 role_level : " + roleLevel);

        // 요청 URL
        String requestUri = request.getRequestURI();

        // contextPath 제거
        String path = requestUri.substring(
            request.getContextPath().length()
        );

        // 필요한 최소 권한
        int requiredRoleLevel = getRequiredRoleLevel(path);
        System.out.println(
            ">>> 필요한 최소 role_level : " + requiredRoleLevel
        );

        // 권한 설정이 없는 페이지는 통과
        if (requiredRoleLevel == -1) {
            System.out.println(">>> 권한 설정 없음 → 통과");
            System.out.println("======================================");

            return true;
        }

        // 현재 권한이 부족하면 차단
        if (roleLevel < requiredRoleLevel) {
            System.out.println(">>> 권한 부족");
            System.out.println(">>> 접근 차단");

            response.sendError(
                HttpServletResponse.SC_FORBIDDEN
            );

            return false;
        }

        System.out.println(">>> 권한 확인 완료 → 통과");
        System.out.println("======================================");

        return true;
    }


    /**
     * 페이지별 최소 role_level
     */
    private int getRequiredRoleLevel(String path) {
        // 회사설정
        if (path.equals("/adminUser/company/info")) {
            return 99;
        }
        // 구성원
        if (path.equals("/adminUser/member/member")) {
            return 50;
        }
        if (path.equals("/adminUser/member/organization")) {
            return 50;
        }
        if (path.equals("/adminUser/member/group")) {
            return 50;
        }
        if (path.equals("/adminUser/member/title")) {
            return 98;
        }
        // 보안
        if (path.equals("/adminUser/security/authority")) {
            return 99;
        }
        // 서비스
        if (path.equals("/adminUser/service/calendar")) {
            return 98;
        }
        // 감사
        if (path.equals("/adminUser/audit/loginAudit")) {
            return 99;
        }
        if (path.equals("/adminUser/audit/calAudit")) {
            return 99;
        }
        if (path.equals("/adminUser/audit/todoAudit")) {
            return 99;
        }
        if (path.equals("/adminUser/audit/addrAudit")) {
            return 99;
        }
        // 권한 설정이 없는 URL
        return -1;
    }
}