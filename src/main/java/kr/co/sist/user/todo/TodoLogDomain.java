package kr.co.sist.user.todo;

import java.security.Timestamp;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Alias("todoLogDomain")
@Data
public class TodoLogDomain {
	private String duty, representativeNo, userName;
	private Timestamp inputDate;
}
