package kr.co.sist.notice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeService {
	
	@Autowired
	private NoticeMapper nm;
	
	public List<NoticeDomain> getNoticeList(){
		List<NoticeDomain> list = nm.selectNotice();
		return list;
	}
	
	public NoticeDomain getNoticeDetail(String noticeNo) {
		NoticeDomain nd = nm.selectNoticeDetail(noticeNo);
		return nd;
	}

}
