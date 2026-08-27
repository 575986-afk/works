package kr.co.sist.user.todo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TodoMapper {
	public String selectName(String userNo);
	public List<TodoDomain> selectTodoList(RangeDTO rDTO);
	public int deleteRepre(String todoNos);
	public List<TodoDomain> selectrepresentativeList(RangeDTO rDTO);
	public int insertTodo(TodoDTO tdDTO);
	public int updateTodo(TodoDTO tdDTO);
	public void insertTodoRepresentative(String todoNo, String representUserNo, String userNo);
	public int updateTodoStatus(String status, String todoNo);
	public int deleteTodos(List<String> todoNos);
	public List<TodoLogDomain> selectTodolog(@Param("todoNo") String todoNo, @Param("userNo") String userNo);
	
}
