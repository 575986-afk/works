package kr.co.sist.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/adminUser/service")
public class CompanyCalendarController {
	
	//캘린더 조회
	@GetMapping("/calendar")
	public String findCompanyCal(HttpSession session, Model model) {
		// List<CalendarDTO> calendarList = calendarService.selectCalendarList();
		// model.addAttribute("calendarList", calendarList);
		
		// 임시 데이터 (테스트용)
		List<Map<String, String>> calendarList = new ArrayList<Map<String,String>>();
		Map<String, String> c1 = new HashMap<String, String>();
		c1.put("calendarId", "1");
		c1.put("scheduleNo", "101");
		c1.put("title", "주간 팀 회의");
		c1.put("startDate", "2026-07-16");
		c1.put("startTime", "16:13");
		c1.put("endDate", "2026-07-16");
		c1.put("endTime", "17:00");
		c1.put("content", "주간 업무 진행 상황 점검");
		calendarList.add(c1);

		model.addAttribute("calendarList", calendarList);
		return "adminUser/service/calendar";
	}
	
	// 한개 일정 상제 정보 조회(예약 정보)
	@GetMapping("/calendar/calDetail")
	public String findCalDetail(@RequestParam(value = "calendarId", required = false, defaultValue = "1") String calendarId, Model model) {
		// CalendarDTO dto = calendarService.selectOneCalendar(calendarId);
		// model.addAttribute("calendar", dto);

		// 임시 데이터
		Map<String, String> calendar = new HashMap<>();
		calendar.put("calendarId", calendarId);
		calendar.put("scheduleNo", "101");
		calendar.put("title", "주간 팀 회의");
		calendar.put("startDate", "2026-07-16");
		calendar.put("startTime", "16:13");
		calendar.put("endDate", "2026-07-16");
		calendar.put("endTime", "17:00");
		calendar.put("content", "주간 업무 진행 상황 점검");

		model.addAttribute("calendar", calendar);
		return "adminUser/service/calDetail";
	}

	// 일정 등록
	@PostMapping("/calendar/addNewCal")
	public String addNewCal(/* CalendarDTO cDTO */) {
		// calendarService.insertCalendar(cDTO);
		System.out.println("일정 등록 실행");
		
		// 등록 완료 후 캘린더 목록 페이지로 리다이렉트
		return "redirect:/adminUser/service/calendar";
	}
	
	// 캘린더 등록 폼
	@GetMapping("/calendar/addNewCalForm")
	public String addNewCalForm() {
		// "adminUser/service/addNewCalForm :: addNewCalForm"
		return "adminUser/service/addNewCalForm";
	}

	// 일정 수정
	@PostMapping("/calendar/modifyCal")
	public String modifyCal(/* CalendarDTO cDTO, */@RequestParam("calendarId") String calendarId) {
		// calendarService.updateCalendar(cDTO);
		System.out.println("일정 수정 실행: " + calendarId);
		
		return "redirect:/adminUser/service/calendar";
	}
	
	// 일정 수정 폼
	@GetMapping("/calendar/modifyCalForm")
	public String modifyCalForm(@RequestParam("calendarId") String calendarId, Model model) {
	    // DB에서 해당 일정 데이터 조회 (테스트 데이터)
	    Map<String, String> calendar = new HashMap<>();
	    calendar.put("calendarId", calendarId);
	    calendar.put("scheduleNo", "101");
	    calendar.put("title", "123");
	    calendar.put("startDate", "2026. 8. 6. (목)");
	    calendar.put("startTime", "오후 06:00");
	    calendar.put("endDate", "2026. 8. 6. (목)");
	    calendar.put("endTime", "오후 07:00");
	    calendar.put("content", "메모를 작성하세요.");

	    model.addAttribute("calendar", calendar);
	    return "adminUser/service/modifyCalForm";
	}

	// 일정 삭제
	@PostMapping("/calendar/deleteCal")
	public String deleteCal(@RequestParam("scheduleNo") int scheduleNo) {
		// calendarService.deleteCalendar(scheduleNo);
		System.out.println("삭제 번호 : " + scheduleNo);
		
		return "redirect:/adminUser/service/calendar";
	}
}
