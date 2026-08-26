package kr.co.sist.findPw;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FindPwController {

	private final FindPwService fps;

	@GetMapping("/findPw")
	public String findId() {
		return "works/login/findPw";
	}

	@PostMapping("/checkUser")
	public String checkPwUser(HttpSession session, String userId, String email, Model model) {
		boolean isSuccess = fps.processVerification(userId, email, session);

		if (isSuccess) {
			return "works/login/verificationChk";
		}

		model.addAttribute("errorMessage", "인증번호 생성이 실패했습니다.");
		return "works/login/findPw";
	}

	@PostMapping("/verificationChk")
	public String verificationChk() {
		return "works/login/verificationChk";
	}

	@PostMapping("/verifyCodeProcess")
	public String verifyCodeProcess(@RequestParam String verificationNo, HttpSession session, Model model) {
		String userNo = (String) session.getAttribute("userNo");

		if (userNo == null) {
			model.addAttribute("errorMessage", "세션이 만료되었습니다. 다시 시도해주세요.");
			return "works/login/findPw";
		}

		int isValid = fps.getVerification(userNo, verificationNo);

		if (isValid > 0) {
			return "works/login/pwUpdate"; // 인증 성공 시 비밀번호 재설정 화면으로
		} else {
			model.addAttribute("errorMessage", "인증번호가 일치하지 않습니다.");
		}

		return "works/login/verificationChk"; // 실패 시 다시 인증번호 입력 화면으로
	}

	@PostMapping("/pwUpdate")
	public String pwUpdate() {
		return "works/login/pwUpdate";
	}

	@PostMapping("/resetPwProcess")
	public String resetPwProcess(@RequestParam String newPw, @RequestParam String confirmPw, HttpSession session,
			Model model) {
		String userNo = (String) session.getAttribute("userNo");

		if (userNo == null) {
			return "redirect:/login?error=expired";
		}

		if (!newPw.equals(confirmPw)) {
			model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
			return "works/login/pwUpdate"; // 다시 비밀번호 재설정 페이지로
		}

		// 비밀번호 변경 서비스 호출
		int isUpdated = fps.setNewPw(userNo, newPw);

		if (isUpdated == 1) {
			session.invalidate(); // 세션 초기화
			return "redirect:/login?success=true"; // 성공 파라미터와 함께 로그인 화면으로 리다이렉트
		} else {
			model.addAttribute("errorMessage", "비밀번호 변경에 실패했습니다.");
			return "works/login/pwUpdate";
		}
	}
}