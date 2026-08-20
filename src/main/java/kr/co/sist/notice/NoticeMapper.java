package kr.co.sist.notice;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeMapper {
	public List<NoticeDomain> selectNotice();

	public NoticeDomain selectNoticeDetail(String noticeNo);
}
