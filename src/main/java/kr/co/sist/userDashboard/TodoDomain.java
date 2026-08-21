package kr.co.sist.userDashboard;

import java.security.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TodoDomain {

	private String todo_no, user_no, title, content, status, bookmark;
	private Timestamp start_date, end_date;
}
