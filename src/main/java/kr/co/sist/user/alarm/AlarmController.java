package kr.co.sist.user.alarm;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

@Controller
public class AlarmController {
	
	@Autowired(required = false)
	private AlarmService as;
	
	@GetMapping("/api/noti")
    public ResponseEntity<List<AlarmDomain>> getUnreadNotifications(HttpSession session) {
        
        String userNo = (String) session.getAttribute("userNo");

        List<AlarmDomain> alList = as.getAlarmList(userNo);
        System.out.println(alList);
        return ResponseEntity.ok(alList);
    }


}
