package kr.co.sist.userDashboard;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.setting.AlarmSettingDTO;
import kr.co.sist.setting.StatusDTO;
import kr.co.sist.setting.TitleDTO;
import kr.co.sist.signup.AESUtil;
import kr.co.sist.signup.UserDTO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserDashboardController {
	
	private final UserDashboardService uds;

	@GetMapping("/userDashboard")
	public String userDashboard(HttpSession session, Model model) {
	    UserDTO loginUser = (UserDTO) session.getAttribute("user");
	    if (loginUser == null) {
	        return "redirect:/login"; 
	    }
	    String userName = loginUser.getName();
	    model.addAttribute("userName", userName != null ? userName : "사용자");
	    
	    // 알람 설정 정보
	    AlarmSettingDTO alarmDTO = uds.getAlarm(loginUser.getUserNo());
	    int isAlarmOn = (alarmDTO != null) ? alarmDTO.getIsAlarmOn() : 1;
	    model.addAttribute("isAlarmOn", isAlarmOn);
	    
	    String currentStatusName = uds.getCurrentStatusName(loginUser.getUserNo());
	    model.addAttribute("currentStatusName", currentStatusName); 
	    
	    //직책/직급
	    TitleDTO rankPosition=uds.selectRankPosition(loginUser.getUserNo());
	    model.addAttribute("rank", rankPosition.getRankName()); 
	    model.addAttribute("position", rankPosition.getPositionName()); 
	    
	    //할 일 
	    List<TodoDomain> list=uds.getTodo(loginUser.getUserNo());
	    model.addAttribute("todoList",list);
	    //조직도 
	    List<OrganizationDomain> list2=uds.getOrganization(loginUser.getUserNo());
	    model.addAttribute("organizationList",list2);
	    
	    return "works/userDashboard";
	}
	
	
 // 알람 클릭 시 온/오프 
    @PostMapping("/AlarmSetting")
    @ResponseBody
    public int alarmSetting(@RequestParam("isAlarmOn") int isAlarmOn, HttpSession session, String userNo,AlarmSettingDTO asDTO) {
        UserDTO loginUser = (UserDTO) session.getAttribute("user");
        if (loginUser == null) {
            return 0; 
        }
        
        asDTO.setUserNo(loginUser.getUserNo());
        asDTO.setIsAlarmOn(isAlarmOn);
        
		int cnt = uds.setAlarm(asDTO);
        System.out.println("디버깅 -> userNo: [" + asDTO.getUserNo() + "], isAlarmOn: " + asDTO.getIsAlarmOn());
        return cnt;
    }
    
}