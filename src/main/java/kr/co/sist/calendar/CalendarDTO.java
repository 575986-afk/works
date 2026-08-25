package kr.co.sist.calendar;

import org.apache.ibatis.type.Alias;

import lombok.Data;

/**
 * CALENDER 테이블과 매핑되는 일정 DTO.
 *
 * scheduleNo : CALENDER_NO(number) 원본 PK. delete(int scheduleNo)처럼
 *              숫자 그대로 받아야 하는 경우에 사용.
 * calendarId : 위와 같은 PK를 문자열로 다루는 조회용 파라미터(String).
 *              화면/쿼리스트링에서는 문자열로 넘어오기 때문에 별도로 둠.
 *              insert 시에는 비워두고 넘기면 DB(SEQUENCE)에서 채워서
 *              selectKey로 되돌려준다.
 */
@Alias("calendarDTO")
@Data
public class CalendarDTO {

	private int scheduleNo;       // CALENDER_NO (PK, number)
	private String calendarId;    // CALENDER_NO를 문자열로 다룰 때 사용

	private String title;         // 제목
	private String content;       // 내용/메모

	private String startDate;     // 'YYYY-MM-DD'
	private String startTime;     // 'HH:mm'
	private String endDate;       // 'YYYY-MM-DD'
	private String endTime;       // 'HH:mm'

	private String category;      // 캘린더 구분 (default/todo/company/커스텀 id)
	private String favoriteYn;    // 중요(즐겨찾기) 일정 여부 'Y'/'N'

	private String userNo;        // 작성자(사용자) 사번
	private String userName;      // 조회 시 표시용 (복호화된 이름)
}
