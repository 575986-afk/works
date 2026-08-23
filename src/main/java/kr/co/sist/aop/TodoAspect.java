package kr.co.sist.aop;

import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.sist.user.todo.TodoDTO;
import kr.co.sist.user.todo.TodoMapper;

@Aspect
@Component
public class TodoAspect {

	@Autowired(required = false)
	private AopMapper am;

	@AfterReturning("execution(* kr.co.sist.user.todo.TodoService.createTodo(..))")
	public void logAfterCreateTodo(JoinPoint joinPoint) {

		// 1. 가로챈 메서드의 파라미터(인자)들을 가져옴
		Object[] args = joinPoint.getArgs();

		for (Object arg : args) {
			// 2. 파라미터 중 TodoDTO 객체를 찾음
			if (arg instanceof TodoDTO) {
				TodoDTO tdDTO = (TodoDTO) arg;

				// 3. MyBatis 연산이 끝났으므로 tdDTO 안에 새로 생성된 todoNo가 존재함
				String todoNo = tdDTO.getTodoNo();
				String userNo = tdDTO.getUserNo();

				// 4. 로그 DB에 저장 (담당자가 없는 기본 할 일 생성 로그)
				am.insertTodoLog("할 일 생성", "", todoNo, userNo);

				if (tdDTO.getRepresentUserNo() != null) {
					for (String repNo : tdDTO.getRepresentUserNo()) {
						am.insertTodoLog("할 일 담당자 지정", repNo, todoNo, userNo);
					}
				}

			}
		}
	}// logAfterCreateTodo

	@AfterReturning(pointcut = "execution(* kr.co.sist.user.todo.TodoService.deleteTodos(..))", returning = "result")
	public void logAfterDeleteTodo(JoinPoint joinPoint, boolean result) {
		// 삭제가 성공(true)했을 때만 로그를 남김
		if (result) {
			// 첫 번째 파라미터인 List<String> todoNos를 가져옴[cite: 3]
			@SuppressWarnings("unchecked")
			List<String> todoNos = (List<String>) joinPoint.getArgs()[0];
			String userNo = getUserNoFromSession(); // 세션에서 userNo 획득

			if (todoNos != null && userNo != null) {
				for (String todoNo : todoNos) {
					am.insertTodoLog("할 일 삭제", "", todoNo, userNo);
				}
			}
		}
	}// logAfterDeleteTodo

	@AfterReturning(pointcut = "execution(* kr.co.sist.user.todo.TodoService.changeTodoStatus(..))", returning = "result")
	public void logAfterChangeTodoStatus(JoinPoint joinPoint, boolean result) {
		// 상태 변경이 성공(true)했을 때만 로그를 남김
		if (result) {
			String status = (String) joinPoint.getArgs()[0]; // 파라미터: status[cite: 3]
			String todoNo = (String) joinPoint.getArgs()[1]; // 파라미터: todoNo[cite: 3]
			String userNo = getUserNoFromSession();

			if (userNo != null) {
				// 컨트롤러 로직에 따라 0이면 미완료이므로 duty를 분기 처리[cite: 2]
				String duty = "0".equals(status) ? "할 일 미완료로 변경" : "할 일 완료로 변경";
				am.insertTodoLog(duty, "", todoNo, userNo);
			}
		}
	}// logAfterChangeTodoStatus

	private String getUserNoFromSession() {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attrs != null) {
			HttpServletRequest request = attrs.getRequest();
			HttpSession session = request.getSession();
			return (String) session.getAttribute("userNo");
		}
		return null;
	}// getUserNoFromSession
}
