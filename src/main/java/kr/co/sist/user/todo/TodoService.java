package kr.co.sist.user.todo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoService {

	@Autowired(required = false)
	private TodoMapper tm;
	
	public List<TodoDomain> getTodoList(RangeDTO rDTO) {
		return tm.selectTodoList(rDTO);
	}
	
	public TodoDomain getTodoDetail(String userNo, String todoNo) {
		TodoDomain td = null;
		return td;
	}
	
	public void createTodo(TodoDTO tdDTO) {

		tm.insertTodo(tdDTO);
		String generatedTodoNo = tdDTO.getTodoNo();

		List<String> representList = tdDTO.getRepresentUserNo();
		if (representList != null && !representList.isEmpty()) {
			for (String representUserNo : representList) {
				// 매퍼 호출 시 파라미터를 2개 넘겨야 하므로 map이나 어노테이션(@Param)을 사용해야 할 수 있습니다.
				tm.insertTodoRepresentative(generatedTodoNo, representUserNo, tdDTO.getUserNo());
			}
		}
	}
	
	@Transactional
	public boolean deleteTodos(List<String> todoNos) {
		if (todoNos != null && !todoNos.isEmpty()) {
			tm.deleteTodoRepresentatives(todoNos);
			tm.deleteTodos(todoNos);
			return true;
		}
		return false;
	}
	
	public boolean changeTodoStatus(String status, String todoNo) {
		boolean flag = tm.updateTodoStatus(status, todoNo) == 1;
		return flag;
	}
	
}
