package kr.co.sist.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {

	@Autowired(required = false)
	private LogDAO lDAO;

	// ----- 캘린더 로그 -----
	public boolean insertCalLog(CalenderLogDTO cDTO) {
		return lDAO.insertCalLog(cDTO) == 1;
	}

	public boolean updateCalLog(CalenderLogDTO cDTO) {
		return lDAO.updateCalLog(cDTO) == 1;
	}

	public boolean deleteCalLog(CalenderLogDTO cDTO) {
		return lDAO.deleteCalLog(cDTO) == 1;
	}

	// 캘린더 삭제 전, 그 캘린더에 달린 로그를 먼저 정리 (FK_CALENDER_TO_CALENDERLOG 위배 방지)
	public void deleteCalLogsByCalenderNo(String calenderNo) {
		lDAO.deleteCalLogsByCalenderNo(calenderNo);
	}

	// ----- 할 일 로그 -----
	public boolean insertTodoLog(TodoLogDTO tDTO) {
		return lDAO.insertTodoLog(tDTO) == 1;
	}

	public boolean updateTodoLog(TodoLogDTO tDTO) {
		return lDAO.updateTodoLog(tDTO) == 1;
	}

	public boolean deleteTodoLog(TodoLogDTO tDTO) {
		return lDAO.deleteTodoLog(tDTO) == 1;
	}

	// ----- 주소록 로그 -----
	public boolean insertAddrLog(AddrLogDTO aDTO) {
		return lDAO.insertAddrLog(aDTO) == 1;
	}

	public boolean updateAddrLog(AddrLogDTO aDTO) {
		return lDAO.updateAddrLog(aDTO) == 1;
	}

	public boolean deleteAddrLog(AddrLogDTO aDTO) {
		return lDAO.deleteAddrLog(aDTO) == 1;
	}

	// ----- 로그인 로그 -----
	public boolean insertLoginLog(LoginLogDTO lDTO) {
		return lDAO.insertLoginLog(lDTO) == 1;
	}
}
