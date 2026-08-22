package kr.co.sist.todoaudit;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ToDoLogListDomain {
	private String duty, userName, email;
	private Timestamp inputDate;
}
