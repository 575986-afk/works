package kr.co.sist.setting;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InquiryDomain {

	private String inquiry_no, title, content,  files, answer,status, user_no, admin_id;
	private Timestamp inquiry_date,answered_date;
}
