package kr.co.sist.calendar;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CalendarDAO {

	// 개인 월간 일정 조회
	public List<CalendarDTO> selectPersonalMonth(
			@Param("userNo") String userNo,
			@Param("yearMonth") String yearMonth);

	// 개인 전체 기간 일정 조회 (전체/중요/범주 일정 보기용 - 월 캐시에 의존하지 않음)
	public List<CalendarDTO> selectPersonalAll(@Param("userNo") String userNo);

	// 그룹(구성원) 일간 일정 조회
	public List<CalendarDTO> selectMemberDaily(
			@Param("companyNo") String companyNo,
			@Param("date") String date);

	// 일정 한개 조회
	public CalendarDTO selectOne(@Param("calendarId") String calendarId);

	// 일정 상세 정보 조회(예약 정보 포함)
	public CalendarDTO selectDetail(@Param("calendarId") String calendarId);

	// 일정 등록 (성공 시 dTO.scheduleNo 에 생성된 PK가 채워짐)
	public int insert(CalendarDTO cDTO);

	// CALENDERCONNECTION에 작성자를 연결 (다이어그램에는 없지만 개인 캘린더
	// 조회가 CALENDERCONNECTION 조인에 의존하므로 insert() 직후 반드시 호출해야 함)
	public int insertConnection(
			@Param("calendarId") String calendarId,
			@Param("userNo") String userNo);

	// 즐겨찾기(중요) 등록
	public int insertFavorite(
			@Param("calendarId") String calendarId,
			@Param("cDTO") CalendarDTO cDTO);

	// 즐겨찾기(중요) 수정
	public int updateFavorite(CalendarDTO cDTO);

	// 일정 수정
	public int update(CalendarDTO cDTO);

	// 일정 삭제
	public int delete(@Param("scheduleNo") String scheduleNo);

	// CALENDERCONNECTION에 FK ON DELETE CASCADE가 없는 경우를 대비해
	// delete() 이전에 명시적으로 먼저 지워준다.
	public int deleteConnection(@Param("scheduleNo") String scheduleNo);
}
