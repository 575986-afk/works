package kr.co.sist.notice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NoticeController {
	
	@Autowired
	private NoticeService ns;
	
	@GetMapping("user/notices")
	public String showNotice(Model model) {
		model.addAttribute("notices", ns.getNoticeList());
		return "notice/notices";
	}
	
	@GetMapping("notice/notice")
	public String showNoticeDetail(Model model, String noticeNo) {
		model.addAttribute("notice", ns.getNoticeDetail(noticeNo));
		return "notice/notice";
	}

}
