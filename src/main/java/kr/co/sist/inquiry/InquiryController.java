package kr.co.sist.inquiry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

@Controller
public class InquiryController {

	@Autowired
	private InquiryService is;

	@GetMapping("/user/inquiry")
	public String showInquiry() {
		return "inquiry/inquiry";
	}

	@PostMapping("/inquiryProcess")
	public String addInquiry(@RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile,
			InquiryDTO iDTO, Model model, HttpSession session) {

		// 기존에는 이 값이 채워지지 않아 INQUIRY.USER_NO가 항상 NULL로 저장되고
		// 있었음 -> 그 결과 "내 문의 목록"(SettingController)과 전체관리자 문의
		// 관리 화면 양쪽에서 작성자를 제대로 알 수 없었음.
		iDTO.setUserNo((String) session.getAttribute("userNo"));

		model.addAttribute("msg", is.createInquiry(uploadFile, iDTO));
		return "inquiry/inquiryResult";
	}

}
