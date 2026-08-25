package kr.co.sist.sysadmin;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.sist.setting.InquiryDomain;

@Mapper
public interface AdminNoticeDAO {

	// ----- 공지사항 -----
	public List<NoticeAdminDomain> selectNoticeList(@Param("keyword") String keyword);

	public NoticeAdminDomain selectNoticeForEdit(@Param("noticeNo") String noticeNo);

	public int insertNotice(NoticeDTO nDTO);

	public int updateNotice(NoticeDTO nDTO);

	// ----- 문의 (기존 kr.co.sist.setting.InquiryDomain 재사용 - INQUIRY 테이블과 필드명이 그대로 일치함) -----
	public List<InquiryDomain> selectInquiryList(@Param("keyword") String keyword);

	public InquiryDomain selectInquiryDetail(@Param("inquiryNo") String inquiryNo);

	public int updateInquiryAnswer(
			@Param("inquiryNo") String inquiryNo,
			@Param("answer") String answer,
			@Param("adminId") String adminId);
}
