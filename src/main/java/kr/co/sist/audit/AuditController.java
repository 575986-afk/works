package kr.co.sist.audit;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/adminUser/audit")
public class AuditController {

	// 인증 페이지
    @GetMapping("/audit")
    public String showAuditPage(HttpSession session) {
        return "adminUser/audit/audit";
    }
    
    // 인증 번호 폼
    public String auditNumForm() {
    	return "";
    }

    //인증번호 발송 요청
    public String checkAuthority(Model model) {
    	return "";
    }

    //인증번호 확인
    public String checkAuthorityNumber(String num) {
    	return "";
    }
}
