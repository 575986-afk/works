package kr.co.sist.userDashboard;

import java.security.Timestamp;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TodoDomain {

	private String todo_no, user_no, title, content, status, bookmark;
	private String start_date, end_date;
}
