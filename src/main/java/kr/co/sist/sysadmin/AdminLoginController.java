package kr.co.sist.sysadmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * 전체(시스템) 관리자 로그인.
 * adminUser/** (회사 관리자, role_level)와는 완전히 별개의 계정/세션 체계.
 * 세션 키를 "sysAdminXxx"로 두어 기존 로그인(user/userNo/companyNo)과 절대 겹치지 않게 함.
 */
@Controller
@RequestMapping("/admin")
public class AdminLoginController {

	@Autowired(required = false)
	private AdminLoginService als;

	// 전체관리자 로그인 화면
	@GetMapping("/login")
	public String adminLoginForm() {
		return "admin/login";
	}

	// 전체관리자 로그인 처리
	@PostMapping("/login")
	public String adminLogin(@ModelAttribute AdminDTO aDTO, Model model, HttpServletRequest request) {
		AdminDomain admin = als.login(aDTO);

		if (admin == null) {
			model.addAttribute("errorMessage", "아이디 또는 비밀번호가 올바르지 않습니다.");
			return "admin/login";
		}

		// 세션 고정(Session Fixation) 공격 방지: 로그인 성공 시 세션 ID를 재발급
		HttpSession session = request.getSession(true);
		request.changeSessionId();
		session.setAttribute("sysAdminNo", admin.getAdminNo());
		session.setAttribute("sysAdminId", admin.getAdminId());
		session.setAttribute("sysAdminName", admin.getAdminName());

		return "redirect:/admin/main";
	}

	// 전체관리자 로그아웃
	@GetMapping("/logout")
	public String adminLogout(HttpSession session) {
		session.removeAttribute("sysAdminNo");
		session.removeAttribute("sysAdminId");
		session.removeAttribute("sysAdminName");
		return "redirect:/admin/login";
	}
}
