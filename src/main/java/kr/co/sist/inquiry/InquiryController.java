package kr.co.sist.inquiry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class InquiryController {
	
	@Autowired
	private InquiryService is;
	
	@GetMapping("user/inquiry")
	public String showInquiry() {
		return "inquiry/inquiry";
	}
	
	@PostMapping("inquiryProcess")
	public String addInquiry(MultipartFile uploadFile , InquiryDTO iDTO, Model model) {
		
		model.addAttribute("msg", is.createInquiry(uploadFile, iDTO));
		return "inquiry/inquiryResult";
	}

}
