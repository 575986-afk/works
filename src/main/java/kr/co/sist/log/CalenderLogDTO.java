package kr.co.sist.log;

import org.apache.ibatis.type.Alias;

import lombok.Data;

/**
 * CALENDERLOG 테이블 기록용 DTO.
 * 기존 calaudit(CalLogMapper) 조회 쿼리와 짝을 맞추려면
 * CALENDERLOG 테이블에 USER_NO 컬럼이 있어야 한다.
 * (현재 companyCalMapper/calLogMapper.xml 기준으로는 없어서 조회 시
 *  CALENDERCONNECTION을 거쳐 USER_NO를 얻고 있음 — 실제 "누가 이 로그를
 *  남겼는지"를 정확히 남기려면 USER_NO 컬럼 추가를 권장.)
 */
@Alias("calenderLogDTO")
@Data
public class CalenderLogDTO {
	private String duty;        // 수행한 작업 (일정 등록/수정/삭제 등)
	private String calenderNo;  // CALENDER_NO
	private String userNo;      // 작업을 수행한 사용자 사번
}
