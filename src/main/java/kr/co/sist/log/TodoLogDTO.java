package kr.co.sist.log;

import org.apache.ibatis.type.Alias;

import lombok.Data;

/** 기존 kr.co.sist.aop.AopMapper#insertTodoLog(duty, representativeNo, todoNo, userNo) 와 동일 항목 */
@Alias("todoLogDTO")
@Data
public class TodoLogDTO {
	private String duty;
	private String representativeNo;
	private String todoNo;
	private String userNo;
}
