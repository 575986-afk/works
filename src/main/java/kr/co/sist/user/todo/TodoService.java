package kr.co.sist.user.todo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.sist.signup.AESUtil;

@Service
public class TodoService {

	@Autowired(required = false)
	private TodoMapper tm;
	
	public String getName(String userNo) {
		return tm.selectName(userNo);
	}
	
	public List<TodoDomain> getTodoList(RangeDTO rDTO) {
	    List<TodoDomain> list = new ArrayList<>();
	    
	    for (TodoDomain todo : tm.selectTodoList(rDTO)) {
	        todo.setUserName(AESUtil.decrypt(todo.getUserName()));
	        
	        todo.setRepresentativeUserNames(decryptMultipleNames(todo.getRepresentativeUserNames()));
	        
	        list.add(todo);
	    }
	    return list;
	}
	
	private String decryptMultipleNames(String encryptedNames) {
	    // null이거나 빈 문자열인 경우 그대로 반환
	    if (encryptedNames == null || encryptedNames.trim().isEmpty()) {
	        return encryptedNames;
	    }
	    
	    // 한 명이든 여러 명이든 split(",")이 알아서 배열로 만들어줌
	    return Arrays.stream(encryptedNames.split(","))
	            .map(String::trim)        // 각 데이터 앞뒤 공백 제거
	            .map(AESUtil::decrypt)    // 각각 복호화 수행
	            .collect(Collectors.joining(", ")); // 다시 쉼표와 공백으로 연결
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
