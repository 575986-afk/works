package kr.co.sist.log;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 도메인별로 흩어져 있던 AOP 기반 로그 기록(kr.co.sist.aop.AopMapper)을
 * 서비스 계층에서 명시적으로 호출하는 방식으로 통합한 매퍼.
 * 캘린더는 이 매퍼를 통해서만 로그를 남긴다.
 */
@Mapper
public interface LogDAO {

	// ----- 캘린더 로그 -----
	public int insertCalLog(CalenderLogDTO cDTO);
	public int updateCalLog(CalenderLogDTO cDTO);
	public int deleteCalLog(CalenderLogDTO cDTO);

	// 캘린더 삭제 전, 그 캘린더에 달린 로그를 먼저 정리 (FK_CALENDER_TO_CALENDERLOG 위배 방지)
	public int deleteCalLogsByCalenderNo(@Param("calenderNo") String calenderNo);

	// ----- 할 일 로그 -----
	public int insertTodoLog(TodoLogDTO tDTO);
	public int updateTodoLog(TodoLogDTO tDTO);
	public int deleteTodoLog(TodoLogDTO tDTO);

	// ----- 주소록 로그 -----
	public int insertAddrLog(AddrLogDTO aDTO);
	public int updateAddrLog(AddrLogDTO aDTO);
	public int deleteAddrLog(AddrLogDTO aDTO);

	// ----- 로그인 로그 -----
	public int insertLoginLog(LoginLogDTO lDTO);
}
