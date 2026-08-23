package kr.co.sist.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.sist.signup.UserDTO;

@Aspect
@Component
public class LoginAspect {

	@Autowired(required = false)
	private AopMapper am;

	@Around("execution(* kr.co.sist.login.LoginController.loginProcess(..))")
	public Object logLoginAttempt(ProceedingJoinPoint pjp) throws Throwable {

		// 1. IP 주소를 가져오기 위해 Request 객체 추출
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		String clientIp = getClientIp(request);
		String rawUserAgent = request.getHeader("User-Agent");
		String browser = getBrowserName(rawUserAgent);
		String userId = (String) pjp.getArgs()[0];

		Object result = pjp.proceed();
		String returnView = (String) result;

		String description = "";
		String connectionStatus = browser;
		String userNo = null;

		// 4. 리턴 뷰 값을 보고 성공/실패 여부 판단
		if (returnView != null && returnView.contains("/userDashboard")) {
			// [로그인 성공]
			HttpSession session = request.getSession(false);
			if (session != null && session.getAttribute("userNo") != null) {
				userNo = session.getAttribute("userNo").toString();
			}
			description = "로그인 성공 (ID: " + userId + ")";

		} else if (returnView != null && returnView.contains("/login")) {
			// [로그인 실패]
			description = "로그인 실패 (시도한 ID: " + userId + ")";
		}

		// 5. LoginMapper를 통해 DB에 INSERT 실행
		if (description != null && !description.isEmpty()) {
			am.insertLoginLog(description, clientIp, connectionStatus, userNo, userId);
		}

		// 6. 원래 컨트롤러가 줘야 할 결과값을 정상적으로 반환
		return result;
	}

	// 클라이언트의 실제 IP를 추출하는 메서드
	private String getClientIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	private String getBrowserName(String userAgent) {
		if (userAgent == null)
			return "Unknown";

		String agent = userAgent.toLowerCase();

		if (agent.contains("edg")) {
			return "Edge";
		} else if (agent.contains("chrome")) {
			return "Chrome";
		} else if (agent.contains("safari") && !agent.contains("chrome")) {
			return "Safari";
		} else if (agent.contains("firefox")) {
			return "Firefox";
		} else if (agent.contains("trident") || agent.contains("msie")) {
			return "Internet Explorer";
		} else if (agent.contains("opr") || agent.contains("opera")) {
			return "Opera";
		} else {
			return "Other";
		}
	}// getBrowserName

	@Around("execution(* kr.co.sist.setting.SettingController.pwChg(..))")
	public Object logPasswordChange(ProceedingJoinPoint pjp) throws Throwable {

		// 1. IP 및 브라우저 정보 추출
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		String clientIp = getClientIp(request);
		String rawUserAgent = request.getHeader("User-Agent");
		String browser = getBrowserName(rawUserAgent);


		// 컨트롤러 실행
		Object result = pjp.proceed();

		// 리턴타입이 int이므로 Integer로 캐스팅
		Integer updateResult = (Integer) result;

		String userNo = null;
		String description = "";
		String connectionStatus = browser; // 작성하신 방식대로 브라우저 정보를 활용
		String userId = ""; // Mapper 파라미터용 변수 (아래에서 세팅)

		// 2. 세션에서 로그인한 사용자의 실제 아이디(userId) 가져오기
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("user") != null) {
			userNo = session.getAttribute("userNo").toString();
			UserDTO loginUser = (UserDTO) session.getAttribute("user");

			userId = loginUser.getUserId();
		}

		// 3. 리턴 값(업데이트된 행 수)을 보고 성공/실패 여부 판단
		if (updateResult != null && updateResult > 0) {
			// [비밀번호 변경 성공]
			description = "비밀번호 변경 성공 (회원번호: " + userNo + ")";
		} else {
			// [비밀번호 변경 실패] 비로그인 상태이거나 실패
			description = "비밀번호 변경 실패 (회원번호: " + userNo + ")";
		}

		// 4. 기존 작성하신 am(Mapper)을 통해 DB에 INSERT 실행
		if (description != null && !description.isEmpty()) {
			am.insertLoginLog(description, clientIp, connectionStatus, userNo, userId);
		}
		System.out.println("+++======================================");
		// 5. 원래 컨트롤러가 줘야 할 결과값(int)을 정상적으로 반환
		return result;
	}// logPasswordChange
}
