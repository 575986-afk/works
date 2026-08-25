package kr.co.sist.sysadmin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.setting.InquiryDomain;

@Service
public class AdminNoticeService {

	@Autowired(required = false)
	private AdminNoticeDAO anDAO;

	// ----- 공지사항 -----
	public List<NoticeAdminDomain> getNoticeList(String keyword) {
		return anDAO.selectNoticeList(keyword);
	}

	public NoticeAdminDomain getNoticeForEdit(String noticeNo) {
		return anDAO.selectNoticeForEdit(noticeNo);
	}

	public boolean addNotice(NoticeDTO nDTO) {
		return anDAO.insertNotice(nDTO) == 1;
	}

	public boolean modifyNotice(NoticeDTO nDTO) {
		return anDAO.updateNotice(nDTO) == 1;
	}

	// ----- 문의 -----
	public List<InquiryDomain> getInquiryList(String keyword) {
		return anDAO.selectInquiryList(keyword);
	}

	public InquiryDomain getInquiryDetail(String inquiryNo) {
		return anDAO.selectInquiryDetail(inquiryNo);
	}

	public boolean answerInquiry(String inquiryNo, String answer, String adminId) {
		return anDAO.updateInquiryAnswer(inquiryNo, answer, adminId) == 1;
	}
}
