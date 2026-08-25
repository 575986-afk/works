package kr.co.sist.sysadmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

/** 전체관리자 메인(대시보드) 화면. AdminSessionInterceptor가 /admin/** 을 이미 보호하므로 여기서는 세션 체크 없이 값만 꺼내 쓴다. */
@Controller
@RequestMapping("/admin")
public class AdminDashBoardController {

	@Autowired(required = false)
	private AdminDashBoardService ads;

	@GetMapping("/main")
	public String adminMainForm(Model model, HttpSession session) {
		model.addAttribute("sysAdminName", session.getAttribute("sysAdminName"));

		model.addAttribute("totalCompanyCount", ads.getTotalCompanyCount());
		model.addAttribute("totalUserCount", ads.getTotalUserCount());
		model.addAttribute("activeUserCount", ads.getActiveUserCount());
		model.addAttribute("dailyCount", ads.getDailyCount());
		model.addAttribute("weeklyCount", ads.getWeeklyCount());
		model.addAttribute("monthlyCount", ads.getMonthlyCount());
		model.addAttribute("logList", ads.getLogList());

		return "admin/main";
	}
}
