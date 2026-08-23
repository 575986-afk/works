package kr.co.sist.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.member.MemberDTO;

@Aspect
@Component
public class AddrAspect {

	@Autowired(required = false)
	private AopMapper am;

	@Pointcut("execution(* kr.co.sist.user.address.AddressController.showAddressPage(..)) || "
			+ "execution(* kr.co.sist.user.addrPopup.PopupAddressController.showAddrPopupPage(..)) || "
			+ "execution(* kr.co.sist.user.address.AddressController.logAddressDetailClick(..)) || "
			+ "execution(* kr.co.sist.member.MemberAddController.modifyMember(..))")
	public void addressBookMethods() {
	}

	@AfterReturning("addressBookMethods()")
	public void logAddressBookAccess(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();

		String loginUserNo = null;
		String targetUserNo = "";

		for (Object arg : args) {
			// 1. 세션에서 접속한 사용자(행위자) 사번 추출
			if (arg instanceof HttpSession) {
				HttpSession session = (HttpSession) arg;
				loginUserNo = (String) session.getAttribute("userNo");
			}
			// 2. 상세조회(AJAX) 시 파라미터에서 대상자 사번 추출
			if (methodName.equals("logAddressDetailClick") && arg instanceof String) {
				targetUserNo = (String) arg;
			}
			// 3. 구성원 수정 시 MemberDTO 객체에서 수정 대상자 사번 추출
			if (methodName.equals("modifyMember") && arg instanceof MemberDTO) {
				MemberDTO dto = (MemberDTO) arg;
				targetUserNo = dto.getUserNo(); 
			}
		}

		if (loginUserNo == null)
			return;

		// duty와 target 분기 처리
		String duty = "구성원 정보 조회"; // 기본값
		String target = "";

		if (methodName.equals("showAddressPage") || methodName.equals("showAddrPopupPage")) {
			target = "all";
		} else if (methodName.equals("logAddressDetailClick")) {
			target = targetUserNo; 
		} else if (methodName.equals("modifyMember")) {
			duty = "구성원 정보 수정"; // 수정 메소드일 경우 duty 변경
			target = targetUserNo; // 수정 대상자 사번을 target에 저장
		}
		
		am.insertAddressbookLog(duty, target, loginUserNo);
	}
	
	
	
}
