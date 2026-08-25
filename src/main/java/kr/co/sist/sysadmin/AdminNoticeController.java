package kr.co.sist.sysadmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

/** 전체관리자 - 공지사항 관리 + 1:1 문의 답변 관리 */
@Controller
@RequestMapping("/admin")
public class AdminNoticeController {

	@Autowired(required = false)
	private AdminNoticeService ans;

	// ----- 공지사항 -----

	@GetMapping("/notice")
	public String findNoticesList(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
		model.addAttribute("noticeList", ans.getNoticeList(keyword));
		model.addAttribute("keyword", keyword);
		return "admin/notice";
	}

	@GetMapping("/notice/addForm")
	public String addNoticeForm() {
		return "admin/noticeForm";
	}

	@PostMapping("/notice/add")
	public String addNotice(@ModelAttribute NoticeDTO nDTO, RedirectAttributes rttr) {
		boolean success = ans.addNotice(nDTO);
		rttr.addFlashAttribute("message", success ? "공지사항이 등록되었습니다." : "공지사항 등록에 실패했습니다.");
		return "redirect:/admin/notice";
	}

	@GetMapping("/notice/modifyForm")
	public String modifyNoticeForm(@RequestParam("noticeNo") String noticeNo, Model model) {
		model.addAttribute("notice", ans.getNoticeForEdit(noticeNo));
		return "admin/noticeForm";
	}

	@PostMapping("/notice/modify")
	public String modifyNotice(@ModelAttribute NoticeDTO nDTO, RedirectAttributes rttr) {
		boolean success = ans.modifyNotice(nDTO);
		rttr.addFlashAttribute("message", success ? "공지사항이 수정되었습니다." : "공지사항 수정에 실패했습니다.");
		return "redirect:/admin/notice";
	}

	// ----- 1:1 문의 -----

	@GetMapping("/inquiry")
	public String searchInquiry(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
		model.addAttribute("inquiryList", ans.getInquiryList(keyword));
		model.addAttribute("keyword", keyword);
		return "admin/inquiry";
	}

	@GetMapping("/inquiry/detail")
	public String findInquiryDetail(@RequestParam("inquiryNo") String inquiryNo, Model model) {
		model.addAttribute("inquiry", ans.getInquiryDetail(inquiryNo));
		return "admin/inquiryDetail";
	}

	@PostMapping("/inquiry/answer")
	public String modifyInquiryAnswer(@RequestParam("inquiryNo") String inquiryNo,
			@RequestParam("answer") String answer,
			HttpSession session,
			RedirectAttributes rttr) {
		String adminId = (String) session.getAttribute("sysAdminId");
		boolean success = ans.answerInquiry(inquiryNo, answer, adminId);
		rttr.addFlashAttribute("message", success ? "답변이 등록되었습니다." : "답변 등록에 실패했습니다.");
		return "redirect:/admin/inquiry/detail?inquiryNo=" + inquiryNo;
	}
}
