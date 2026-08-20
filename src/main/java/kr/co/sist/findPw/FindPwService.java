package kr.co.sist.findPw;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
//import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
@RequiredArgsConstructor
public class FindPwService {
	
	private final FindPwMapper fm;
//	private final JavaMailSender mailSender;
	private final BCryptPasswordEncoder passwordEncoder;
	
	public boolean processVerification(String userId, String email, HttpSession session) {
	    
	    String userNo = fm.selectUserPw(userId, email);
	    
	    if (userNo == null) {
	        return false; 
	    }
	    
	    int randomNum = (int)(Math.random() * 10000);
	    String verificationNo = String.format("%04d", randomNum);
	    
	    Map<String, Object> map = new HashMap<>();
	    map.put("verificationNo", verificationNo);
	    map.put("userNo", userNo);
	    
	    int result = fm.insertCode(map);
	    
	    if (result > 0) {
	        System.out.println("======================================");
	        System.out.println(">>> [테스트용] 인증번호: " + verificationNo);
	        System.out.println("======================================");
	        session.setAttribute("userNo", userNo);
	        return true;
	    }
	    
	    return false;
	}
	
	public int getVerification(String userNo, String verificationNo) {
	    return fm.selectVerification(verificationNo, userNo);
	}
	
	public int setNewPw(String userNo, String newPw) {
		String encodedPassword = passwordEncoder.encode(newPw);
		return fm.updatePw(userNo, encodedPassword); // 암호화된 비밀번호 전달
	}
}