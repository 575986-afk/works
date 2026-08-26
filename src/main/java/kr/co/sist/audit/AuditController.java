package kr.co.sist.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.mail.MailService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/adminUser/audit")
public class AuditController {
	
	@Autowired(required = false)
	private AuditService as;
	@Autowired(required = false)
	private MailService ms;

	// 인증 페이지
    @GetMapping("/audit")
    public String showAuditPage(HttpSession session, Model model) {
    	String maskedEmail = as.getMaskedEmail(session);

        model.addAttribute("maskedEmail", maskedEmail);
        
        return "adminUser/audit/audit";
    }
    
    // 인증 번호 폼
    @GetMapping("/number")
    public String auditNumForm(HttpSession session, Model model) {
        String maskedEmail = as.getMaskedEmail(session);

        model.addAttribute("maskedEmail", maskedEmail);

        return "adminUser/audit/send";
    }

    //인증번호 발송 요청
    @PostMapping("/send")
    public String checkAuthority(HttpSession session, Model model) {
        boolean result = as.processVerification(session);

        if (!result) {
            model.addAttribute(
                "msg",
                "인증번호 발송에 실패했습니다."
            );
            return "adminUser/audit/audit";
        }
        return "redirect:/adminUser/audit/number";
    }
    
    //인증번호 재전송
    @PostMapping("/resend")
    @ResponseBody
    public String resendCode(HttpSession session) {
        boolean result = as.processVerification(session);
        if (result) {
            return "success";
        }

        return "fail";
    }

    //인증번호 확인
    @PostMapping("/verify")
    public String checkAuthorityNumber(
            @RequestParam("num") String num,
            HttpSession session,
            Model model) {

        System.out.println("======================================");
        System.out.println(">>> /adminUser/audit/verify 호출");
        System.out.println(">>> 입력 인증번호 : " + num);
        System.out.println(">>> Session ID : " + session.getId());

        boolean result = as.verifyCode(session, num);
        System.out.println(">>> 인증번호 검증 결과 : " + result);

        if (!result) {
            System.out.println(">>> 인증번호 불일치");
            model.addAttribute(
                "msg",
                "인증번호가 올바르지 않습니다."
            );
            String maskedEmail = as.getMaskedEmail(session);
            model.addAttribute("maskedEmail", maskedEmail);

            return "adminUser/audit/send";
        }

        // 감사 기능 2단계 인증 완료
        session.setAttribute(
            "audit2FAAuthenticated",
            true
        );

        System.out.println(">>> audit2FAAuthenticated = true");
        String targetUrl = (String) session.getAttribute("auditTargetUrl");
        System.out.println(">>> auditTargetUrl = " + targetUrl);

        if (targetUrl != null && !targetUrl.isEmpty()) {
            session.removeAttribute("auditTargetUrl");
            System.out.println(">>> 감사 페이지로 이동 : " + targetUrl);
            return "redirect:" + targetUrl;
        }

        return "redirect:/adminUser/audit/loginAudit";
    }
}
