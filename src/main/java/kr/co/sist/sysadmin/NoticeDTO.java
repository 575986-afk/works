package kr.co.sist.sysadmin;

import org.apache.ibatis.type.Alias;

import lombok.Data;

/** 전체관리자 - 공지사항 등록/수정 폼 바인딩용 (기존 kr.co.sist.notice 의 사용자 조회 기능과는 별개) */
@Alias("sysNoticeDTO")
@Data
public class NoticeDTO {
	private String noticeNo;   // 수정 시에만 사용
	private String noticeTitle;
	private String content;
	private String files;
	private String status;     // '게시중' / '숨김' 등
}
