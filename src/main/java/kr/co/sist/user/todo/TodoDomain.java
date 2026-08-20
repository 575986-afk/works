package kr.co.sist.user.todo;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Alias("usertodoDomain")
@Data
public class TodoDomain {
	private String userName, todoNo, title, content, status, representativeUserNames;
	private Timestamp startDate, endDate;
}
