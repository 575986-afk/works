package kr.co.sist.todoaudit;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ToDoLogMapper {
	public List<ToDoLogListDomain> selectAllToDoLog(ToDoLogSearchDTO search);
	public ToDoLogDetailDomain selectToDoLogDetail(@Param("logNo") String logNo, @Param("companyNo") String companyNo);
}
