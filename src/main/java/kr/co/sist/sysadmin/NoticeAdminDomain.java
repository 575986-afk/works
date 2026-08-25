package kr.co.sist.sysadmin;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("sysNoticeDomain")
@Getter
@Setter
@ToString
public class NoticeAdminDomain {
	private String noticeNo;
	private String noticeTitle;
	private String content;
	private String files;
	private String status;
	private Timestamp createdDate;
}
