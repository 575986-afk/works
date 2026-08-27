package kr.co.sist.calendar;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.sist.log.CalenderLogDTO;
import kr.co.sist.log.LogService;

@Service
public class CalendarService {

	@Autowired(required = false)
	private CalendarDAO cDAO;

	// 다이어그램 상 insert/delete 처럼 로그인한 사용자 정보를 파라미터로
	// 받지 않는 메서드에서, 기존 kr.co.sist.aop.TodoAspect#getUserNoFromSession()와
	// 동일한 방식으로 세션에서 필요한 값을 얻어온다.
	@Autowired(required = false)
	private LogService logService;

	// 개인 월간 일정 조회
	public List<CalendarDTO> selectPersonalMonth(String userId, String yearMonth) {
		return cDAO.selectPersonalMonth(userId, yearMonth);
	}

	// 개인 전체 기간 일정 조회 (전체/중요/범주 일정 보기용)
	public List<CalendarDTO> selectPersonalAll(String userId) {
		return cDAO.selectPersonalAll(userId);
	}

	// 구성원(그룹) 일간 일정 조회 - companyNo는 세션에서 얻어옴
	public List<CalendarDTO> selectMemberDaily(String date) {
		String companyNo = getSessionAttr("companyNo");
		return cDAO.selectMemberDaily(companyNo, date);
	}

	// 일정 한개 조회
	public CalendarDTO selectOne(String calendarId) {
		return cDAO.selectOne(calendarId);
	}

	// 일정 상세 정보 조회(예약 정보 포함)
	public CalendarDTO selectDetail(String calendarId) {
		return cDAO.selectDetail(calendarId);
	}

	// 일정 등록
	@Transactional
	public int insert(CalendarDTO cDTO) {
		int result = cDAO.insert(cDTO);
		if (result == 1) {
			cDTO.setCalendarId(String.valueOf(cDTO.getScheduleNo()));
			cDAO.insertConnection(cDTO.getCalendarId(), cDTO.getUserNo());
			if (logService != null) {
				logService.insertCalLog(toLogDTO("일정 등록", cDTO));
			}
		}
		return result;
	}

	// 즐겨찾기(중요) 등록
	@Transactional
	public int insertFavorite(String calendarId, CalendarDTO cDTO) {
		cDTO.setCalendarId(calendarId);
		cDTO.setFavoriteYn("Y");
		int result = cDAO.insertFavorite(calendarId, cDTO);
		if (result == 1 && logService != null) {
			logService.updateCalLog(toLogDTO("중요 일정으로 표시", cDTO));
		}
		return result;
	}

	// 즐겨찾기(중요) 수정
	@Transactional
	public int updateFavorite(CalendarDTO cDTO, int roleLevel) {
		if (isCompanyEventEditForbidden(cDTO.getScheduleNo(), roleLevel)) {
			return 0;
		}
		int result = cDAO.updateFavorite(cDTO);
		if (result == 1 && logService != null) {
			String duty = "Y".equals(cDTO.getFavoriteYn()) ? "중요 일정으로 표시" : "중요 일정 해제";
			logService.updateCalLog(toLogDTO(duty, cDTO));
		}
		return result;
	}

	// 회사 일정(공개여부='1') 수정/삭제는 role_level 98 이상만 허용
	private static final int COMPANY_EVENT_EDIT_MIN_ROLE_LEVEL = 98;

	private boolean isCompanyEventEditForbidden(String calendarId, int roleLevel) {
		if (roleLevel >= COMPANY_EVENT_EDIT_MIN_ROLE_LEVEL) return false;
		CalendarDTO existing = cDAO.selectOne(calendarId);
		return existing != null && "1".equals(existing.getDisclosureStatus());
	}

	// 일정 수정
	@Transactional
	public int update(CalendarDTO cDTO, int roleLevel) {
		if (isCompanyEventEditForbidden(cDTO.getScheduleNo(), roleLevel)) {
			return 0;
		}
		int result = cDAO.update(cDTO);
		if (result == 1 && logService != null) {
			logService.updateCalLog(toLogDTO("일정 수정", cDTO));
		}
		return result;
	}

	// 일정 삭제
	@Transactional
	public int delete(String scheduleNo, int roleLevel) {
		if (isCompanyEventEditForbidden(scheduleNo, roleLevel)) {
			return 0;
		}
		// CALENDERLOG.CALENDER_NO -> CALENDER.CALENDER_NO FK 때문에, 로그가
		// 남아있으면 CALENDER 삭제가 거부된다(ORA-02292). 먼저 로그를 정리한다.
		// (그 결과 이 일정의 등록/수정 이력도 함께 사라짐 - 알려진 트레이드오프)
		if (logService != null) {
			logService.deleteCalLogsByCalenderNo(String.valueOf(scheduleNo));
		}
		cDAO.deleteConnection(scheduleNo);
		int result = cDAO.delete(scheduleNo);
		// 주의: 삭제가 끝난 뒤에는 이미 없는 CALENDER_NO를 참조하는 로그를 새로
		// 추가할 수 없으므로(같은 FK 위배), "일정 삭제" 로그는 남기지 않는다.
		return result;
	}

	private CalenderLogDTO toLogDTO(String duty, CalendarDTO cDTO) {
		CalenderLogDTO logDTO = new CalenderLogDTO();
		logDTO.setDuty(duty);
		logDTO.setCalenderNo(cDTO.getCalendarId() != null ? cDTO.getCalendarId() : String.valueOf(cDTO.getScheduleNo()));
		logDTO.setUserNo(cDTO.getUserNo() != null ? cDTO.getUserNo() : getSessionAttr("userNo"));
		return logDTO;
	}

	private String getSessionAttr(String key) {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attrs != null) {
			HttpServletRequest request = attrs.getRequest();
			HttpSession session = request.getSession();
			return (String) session.getAttribute(key);
		}
		return null;
	}
}
