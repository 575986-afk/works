package kr.co.sist.sysadmin;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * /admin/** (로그인 화면 제외) 접근 시 sysAdminId 세션 여부를 확인.
 * kr.co.sist.login.LoginInterceptor / kr.co.sist.security.RoleLevelInterceptor 와는
 * 완전히 별개로 동작 (회사 관리자 권한과 무관).
 *
 * WebConfig에 아래처럼 등록해야 실제로 동작함:
 *
 *   private final AdminSessionInterceptor adminSessionInterceptor;
 *   ...
 *   registry.addInterceptor(adminSessionInterceptor)
 *           .addPathPatterns("/admin/**")
 *           .excludePathPatterns("/admin/login");
 */
@Component
public class AdminSessionInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		Object sysAdminId = session.getAttribute("sysAdminId");

		if (sysAdminId == null) {
			response.sendRedirect(request.getContextPath() + "/admin/login");
			return false;
		}
		return true;
	}
}
