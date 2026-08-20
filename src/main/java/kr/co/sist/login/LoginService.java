package kr.co.sist.login;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.co.sist.signup.UserDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {

	private final LoginMapper lDAO;
	private final PasswordEncoder passwordEncoder;
	
	public UserDTO setLoginCheck(String userId, String password) {
		// DB에서 사용자 정보 조회
		UserDTO uDTO = lDAO.selectLogin(userId);

		if (uDTO == null) {
			return null;
		}

		// 만약 DB에 비밀번호가 평문으로 저장되어 있다면 아래와 같이 직접 비교해야 합니다.
		// (스프링 시큐리티 표준 방식은 passwordEncoder.matches(password, uDTO.getPassword()) 입니다.)
		boolean isMatch = passwordEncoder.matches(password, uDTO.getPassword());
		
		// 만약 DB 비밀번호가 평문이라 matches가 계속 실패한다면 아래 주석처럼 비교해볼 수 있습니다.
		// boolean isMatch = passwordEncoder.matches(password, passwordEncoder.encode(uDTO.getPassword())) 또는 평문 직접 비교:
		// boolean isMatch = passwordEncoder.matches(password, uDTO.getPassword()) || password.equals(uDTO.getPassword());

		if (isMatch) {
			return uDTO;
		}

		return null;
	}
}