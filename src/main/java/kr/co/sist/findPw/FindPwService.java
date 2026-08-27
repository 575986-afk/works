package kr.co.sist.findPw;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
@RequiredArgsConstructor
public class FindPwService {

	private final FindPwMapper fm;
	private final JavaMailSender mailSender;
	private final BCryptPasswordEncoder passwordEncoder;

	public boolean processVerification(String userId, String email, HttpSession session) {

		String userNo = fm.selectUserPw(userId, email);

		if (userNo == null) {
			return false;
		}

		int randomNum = (int) (Math.random() * 10000);
		String verificationNo = String.format("%04d", randomNum);

		Map<String, Object> map = new HashMap<>();
		map.put("verificationNo", verificationNo);
		map.put("userNo", userNo);

		session.setAttribute("userNo", userNo);
		int result = fm.insertCode(map);

		if (result != 0) {
			System.out.println("======================================");
			System.out.println(">>> [테스트용] 인증번호: " + verificationNo);
			System.out.println("======================================");
//			try {
//	            SimpleMailMessage message = new SimpleMailMessage();
//	            message.setTo("day305@naver.com");
//	            message.setFrom("day305@naver.com");
//	            message.setSubject("[WORKS] 비밀번호 찾기 인증번호");
//	            message.setText("인증번호는 [" + verificationNo + "] 입니다. \n10분 이내에 입력해주세요.");
//	            mailSender.send(message);
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	            return false;
//	        }
			return true;
		}

		return false;
	}

	public int getVerification(String userNo, String verificationNo) {
		return fm.selectVerification(verificationNo, userNo);
	}

	public int setNewPw(String userNo, String newPw) {
		String encodedPassword = passwordEncoder.encode(newPw);
		return fm.updatePw(userNo, encodedPassword); 
	}
}