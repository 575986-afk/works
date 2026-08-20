package kr.co.sist.user.todo;

import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Alias("todoDTO")
@Data
public class TodoDTO {
	private String title, content, userNo, endDate, todoNo;
	private List<String> representUserNo;
}
