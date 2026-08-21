package kr.co.sist.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.signup.UserDTO;

@RequestMapping("/dashboard")
@Controller
public class MainboardController {
	
	@Autowired(required = false)
	private DashboardService ds;
	
	// 관리자 대시보드 화면
	@GetMapping
	public String showMainboard(HttpSession session, Model model) {
		
		String companyNo = (String)session.getAttribute("companyNo");
		String userNo = (String)session.getAttribute("userNo");
		
        if (companyNo == null || userNo == null) {
            return "redirect:/login";
        }
		
		model.addAttribute("memberCount",ds.getMemberCnt(companyNo));
		model.addAttribute("adminName", ds.getUserName(userNo));
		
		return "adminUser/dashboard/dashboard";
	}
	
}
