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
	
	@GetMapping("user/inquiry")
	public String showInquiry() {
		return "inquiry/inquiry";
	}
	
	@PostMapping("inquiryProcess")
	public String addInquiry(@RequestParam(value = "uploadFile", required = false)MultipartFile uploadFile ,HttpSession session, InquiryDTO iDTO, Model model) {
		
		iDTO.setUserNo((String)session.getAttribute("userNo"));
		System.out.println(iDTO);
		model.addAttribute("msg", is.createInquiry(uploadFile, iDTO));
		return "inquiry/inquiryResult";
	}

}
