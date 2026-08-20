package kr.co.sist.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/dashboard")
@Controller
public class MainboardController {
	
	@Autowired(required = false)
	private DashboardService ds;
	
	@GetMapping
	public String showMainboard(HttpSession session, Model model) {
		
		String companyNo = (String)session.getAttribute("companyNo");
		String userName = (String)session.getAttribute("userNo");
		
		model.addAttribute("memberCount",ds.getMemberCnt(companyNo));
		model.addAttribute("adminName", ds.getUserName(userName));
		
		return "adminUser/dashboard/dashboard";
	}
	
}
