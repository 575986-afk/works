package kr.co.sist.user.todo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TodoMapper {
	public List<TodoDomain> selectTodoList(RangeDTO rDTO);
	public List<TodoDomain> selectrepresentativeList(RangeDTO rDTO);
	public int insertTodo(TodoDTO tdDTO);
	public void insertTodoRepresentative(String todoNo, String representUserNo, String userNo);
	public int updateTodoStatus(String status, String todoNo);
	public int deleteTodoRepresentatives(List<String> todoNos);
	public int deleteTodos(List<String> todoNos);
	
}
