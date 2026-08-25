package kr.co.sist.sysadmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** 전체관리자 - 전체 회사 대상 회원 관리 (adminUser/member/** 의 회사별 관리와는 별개) */
@Controller
@RequestMapping("/admin/member")
public class AdminMemberController {

	@Autowired(required = false)
	private AdminMemberService ams;

	// 회원 목록 (검색: 아이디 기준. 이름/이메일은 암호화 저장이라 SQL로 검색 불가)
	@GetMapping("")
	public String memberList(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
		model.addAttribute("memberList", ams.getMemberList(keyword));
		model.addAttribute("keyword", keyword);
		return "admin/member";
	}

	// 회원 상세
	@GetMapping("/detail")
	public String memberDetail(@RequestParam("userNo") String userNo, Model model) {
		model.addAttribute("member", ams.getMemberDetail(userNo));
		return "admin/memberDetail";
	}

	// 비밀번호 강제 재설정
	@PostMapping("/changePassword")
	public String changePassword(@RequestParam("userNo") String userNo,
			@RequestParam("newPassword") String newPassword,
			RedirectAttributes rttr) {
		boolean success = ams.changePassword(userNo, newPassword);
		rttr.addFlashAttribute("message", success ? "비밀번호가 재설정되었습니다." : "비밀번호 재설정에 실패했습니다.");
		return "redirect:/admin/member/detail?userNo=" + userNo;
	}
}
