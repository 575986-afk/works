package kr.co.sist.todoaudit;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ToDoLogService {
	
	@Autowired(required = false)
	private ToDoLogMapper tdlm;
	
	public List<ToDoLogListDomain> getAllToDoLogList(){
		return tdlm.selectAllToDoLog();
	}
}
