package kr.co.sist.todoaudit;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ToDoLogMapper {
	public List<ToDoLogListDomain> selectAllToDoLog();
}
