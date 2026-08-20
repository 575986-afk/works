package kr.co.sist.user.todo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

@Controller
public class TodoController {
	
	@Autowired
	private TodoService ts;
	
	@GetMapping("todo")
	public String showTodoPage(RangeDTO rDTO, HttpSession session, Model model) {
		String userNo = (String) session.getAttribute("userNo");
		
		if (userNo != null) {
	        // 별도의 검색 조건이 넘어오지 않았다면 내 관련 할일만 조회되도록 기본값 설정
	        if (rDTO.getUserNo() == null && rDTO.getRepresentativeUserNo() == null) {
	            rDTO.setUserNo(userNo);
	            rDTO.setRepresentativeUserNo(userNo);
	        }
	    }
		
		List<TodoDomain> todoList = ts.getTodoList(rDTO);
		
		int totalCount = todoList.size();
	    long incompleteCount = todoList.stream()
	                                   .filter(todo -> "0".equals(todo.getStatus()))
	                                   .count();
	    model.addAttribute("todoList", todoList);
	    model.addAttribute("totalCount", totalCount);
	    model.addAttribute("incompleteCount", incompleteCount);
		return "user/todo";
	}
	
	@PostMapping("addTodo")
	public String  addTodo(TodoDTO tdDTO, HttpSession session, Model model) {
		tdDTO.setUserNo((String) session.getAttribute("userNo"));
		ts.createTodo(tdDTO);
		return "redirect:/todo";
	}
	
	
	@PostMapping("deleteTodos")
	@ResponseBody
	public ResponseEntity<String> deleteTodos(@RequestBody List<String> todoNos) {
		try {
			boolean result = ts.deleteTodos(todoNos);
			if (result) {
				return ResponseEntity.ok("success");
			} else {
				return ResponseEntity.badRequest().body("fail");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("error");
		}
	}
	
	@PostMapping("modifyTodoStatus")
	public String modifyTodoStatus(String status, String todoNo, HttpSession sesseion) {
		ts.changeTodoStatus(status, todoNo);
		return "redirect:/todo";
	}

}
