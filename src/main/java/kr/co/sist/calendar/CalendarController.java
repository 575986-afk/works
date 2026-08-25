package kr.co.sist.calendar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

/**
 * 다이어그램 상 모든 메서드가 String을 반환하도록 설계되어 있음.
 *
 * - selectPersonalMonth : 최초 페이지 진입(Model + HttpSession 사용) → 뷰 이름 반환
 * - 그 외 조회/등록/수정/삭제 : 화면 JS(calendar.html)가 fetch로 호출하는
 *   AJAX 엔드포인트 → @ResponseBody로 JSON 문자열을 그대로 반환
 *   (다이어그램의 반환형 String과 실제 JSON 문자열 응답을 그대로 일치시킴)
 *
 * 주의: 기존 kr.co.sist.calendar.CalendarPageController(GET /calendar, 뷰만 반환)는
 * 이 컨트롤러로 대체되므로 프로젝트에서 함께 쓰지 말고 제거해야 함(같은 매핑 충돌 남).
 *
 * Jackson(ObjectMapper)이 프로젝트 클래스패스에서 잡히지 않는 환경이라
 * 외부 라이브러리 의존 없이 JSON을 직접 문자열로 만들어 반환한다.
 */
@Controller
public class CalendarController {

	@Autowired(required = false)
	private CalendarService cs;

	// 메뉴(앱 런처) → 캘린더 페이지 최초 진입, 이번 달 개인 일정을 함께 내려줌
	@GetMapping("/calendar")
	public String selectPersonalMonth(
			@RequestParam(value = "yearMonth", required = false) String yearMonth,
			Model model, HttpSession session) {

		String userId = (String) session.getAttribute("userNo");

		if (yearMonth == null || yearMonth.isEmpty()) {
			yearMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
		}

		List<CalendarDTO> eventList = cs.selectPersonalMonth(userId, yearMonth);

		// 최초 렌더링 시 JS가 바로 쓸 수 있도록 JSON도 함께 내려준다.
		model.addAttribute("eventListJson", toJsonArray(eventList));
		model.addAttribute("eventList", eventList);
		model.addAttribute("yearMonth", yearMonth);

		return "works/calendar/calendar";
	}

	// (다이어그램에는 없지만) 월 이동 시 화면 새로고침 없이 JSON만 다시 받기 위한 보조 엔드포인트
	@GetMapping("/calendar/personalMonth")
	@ResponseBody
	public String selectPersonalMonthJson(@RequestParam("yearMonth") String yearMonth, HttpSession session) {
		String userId = (String) session.getAttribute("userNo");
		return toJsonArray(cs.selectPersonalMonth(userId, yearMonth));
	}

	// 전체/중요/범주 일정 보기용 - 월 캐시에 의존하지 않고 전체 기간을 그대로 내려줌
	@GetMapping("/calendar/personalAll")
	@ResponseBody
	public String selectPersonalAllJson(HttpSession session) {
		String userId = (String) session.getAttribute("userNo");
		return toJsonArray(cs.selectPersonalAll(userId));
	}

	// 그룹(구성원) 일간 일정 조회
	@GetMapping("/calendar/memberDaily")
	@ResponseBody
	public String selectMemberDaily(@RequestParam("date") String date, Model model) {
		List<CalendarDTO> list = cs.selectMemberDaily(date);
		return toJsonArray(list);
	}

	// 일정 한개 조회
	@GetMapping("/calendar/one")
	@ResponseBody
	public String selectOne(@RequestParam("calendarId") String calendarId) {
		return toJsonObject(cs.selectOne(calendarId));
	}

	// 일정 상세 정보 조회(예약 정보 포함)
	@GetMapping("/calendar/detail")
	@ResponseBody
	public String selectDetail(@RequestParam("calendarId") String calendarId) {
		return toJsonObject(cs.selectDetail(calendarId));
	}

	// 일정 등록
	@PostMapping("/calendar/insert")
	@ResponseBody
	public String insert(CalendarDTO cDTO, HttpSession session) {
		cDTO.setUserNo((String) session.getAttribute("userNo"));
		int result = cs.insert(cDTO);
		return resultJson(result == 1, cDTO.getScheduleNo());
	}

	// 즐겨찾기(중요) 등록
	@PostMapping("/calendar/insertFavorite")
	@ResponseBody
	public String insertFavorite(@RequestParam("calendarId") String calendarId, CalendarDTO cDTO) {
		int result = cs.insertFavorite(calendarId, cDTO);
		return resultJson(result == 1, cDTO.getScheduleNo());
	}

	// 즐겨찾기(중요) 수정
	@PostMapping("/calendar/updateFavorite")
	@ResponseBody
	public String updateFavorite(CalendarDTO cDTO) {
		int result = cs.updateFavorite(cDTO);
		return resultJson(result == 1, cDTO.getScheduleNo());
	}

	// 일정 수정
	@PostMapping("/calendar/update")
	@ResponseBody
	public String update(CalendarDTO cDTO, HttpSession session) {
		cDTO.setUserNo((String) session.getAttribute("userNo"));
		int result = cs.update(cDTO);
		return resultJson(result == 1, cDTO.getScheduleNo());
	}

	// 일정 삭제
	@PostMapping("/calendar/delete")
	@ResponseBody
	public String delete(@RequestParam("scheduleNo") int scheduleNo) {
		int result = cs.delete(scheduleNo);
		return resultJson(result == 1, scheduleNo);
	}

	// ---------- 수동 JSON 직렬화 (Jackson 미사용) ----------

	private String toJsonArray(List<CalendarDTO> list) {
		if (list == null) return "[]";
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < list.size(); i++) {
			if (i > 0) sb.append(",");
			sb.append(toJsonObject(list.get(i)));
		}
		sb.append("]");
		return sb.toString();
	}

	private String toJsonObject(CalendarDTO d) {
		if (d == null) return "null";
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"scheduleNo\":").append(d.getScheduleNo()).append(",");
		sb.append("\"calendarId\":").append(str(d.getCalendarId())).append(",");
		sb.append("\"title\":").append(str(d.getTitle())).append(",");
		sb.append("\"content\":").append(str(d.getContent())).append(",");
		sb.append("\"startDate\":").append(str(d.getStartDate())).append(",");
		sb.append("\"startTime\":").append(str(d.getStartTime())).append(",");
		sb.append("\"endDate\":").append(str(d.getEndDate())).append(",");
		sb.append("\"endTime\":").append(str(d.getEndTime())).append(",");
		sb.append("\"category\":").append(str(d.getCategory())).append(",");
		sb.append("\"favoriteYn\":").append(str(d.getFavoriteYn())).append(",");
		sb.append("\"userNo\":").append(str(d.getUserNo())).append(",");
		sb.append("\"userName\":").append(str(d.getUserName()));
		sb.append("}");
		return sb.toString();
	}

	private String resultJson(boolean success, int scheduleNo) {
		return "{\"result\":\"" + (success ? "success" : "fail") + "\",\"scheduleNo\":" + scheduleNo + "}";
	}

	// null-safe 문자열을 JSON 문자열 리터럴로 (없으면 null 그대로)
	private String str(String value) {
		if (value == null) return "null";
		String escaped = value
				.replace("\\", "\\\\")
				.replace("\"", "\\\"")
				.replace("\n", "\\n")
				.replace("\r", "\\r")
				.replace("\t", "\\t");
		return "\"" + escaped + "\"";
	}
}
