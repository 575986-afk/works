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
	
	public UserDTO setLoginCheck(String userId,String password) {
		 UserDTO uDTO = lDAO.selectLogin(userId);

			 if (uDTO == null) {
		            return null;
		        }
		 
		    if (uDTO != null && passwordEncoder.matches(password, uDTO.getPassword())) {
		        return uDTO;
		    }

		    return null;
	}
}
