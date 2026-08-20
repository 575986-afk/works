package kr.co.sist.notice;

import java.time.LocalDateTime;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Alias("NoticeDomain")
@Data
public class NoticeDomain {
	private String noticeNo, noticeTitle, content, files;
	private LocalDateTime createdDate;

}
