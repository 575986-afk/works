package kr.co.sist.audit;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.mail.MailService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditMapper am;
    private final MailService ms;

    // 이메일 마스크 처리
    public String getMaskedEmail(HttpSession session) {
        String userNo = (String) session.getAttribute("userNo");
        if (userNo == null) {
            return null;
        }

        AuditEmailDomain emailDomain = am.selectEmail(userNo);
        if (emailDomain == null || emailDomain.getEmail() == null) {
            return "";
        }

        String email = emailDomain.getEmail();
        if (email.isEmpty()) {
            return "";
        }

        int atIndex = email.indexOf("@");
        if (atIndex <= 0) {
            return email;
        }

        String id = email.substring(0, atIndex);
        String domain = email.substring(atIndex);

        if (id.length() <= 2) {
            return id.charAt(0) + "*" + domain;
        }

        return id.substring(0, 2) + "******" + domain;
    }


    // 감사 페이지 2단계 인증번호 발송
    public boolean processVerification(HttpSession session) {
        String userNo = (String) session.getAttribute("userNo");
        if (userNo == null) {
            return false;
        }

        // 현재 사용자의 이메일 조회
        AuditEmailDomain emailDomain = am.selectEmail(userNo);
        if (emailDomain == null || emailDomain.getEmail() == null) {
            return false;
        }

        String email = emailDomain.getEmail();
        if (email.isEmpty()) {
            return false;
        }

        // 4자리 인증번호 생성
        int randomNum = (int) (Math.random() * 10000);
        String verificationNo = String.format("%04d", randomNum);

        // DB 저장
        Map<String, Object> map = new HashMap<>();
        map.put("verificationNo", verificationNo);
        map.put("userNo", userNo);

        int result = am.insertCode(map);

        if (result > 0) {
        	//이메일 전송
//        	boolean mailResult = ms.sendVerificationMail(email, verificationNo);
//            if (!mailResult) {
//                return false;
//            }

            // 개발 단계에서는 Console로 인증번호 확인
            System.out.println("======================================");
            System.out.println(">>> [감사 2단계 인증]");
            System.out.println(">>> 이메일: " + email);
            System.out.println(">>> 인증번호: " + verificationNo);
            System.out.println("======================================");

            return true;
        }

        return false;
    }


    // 감사 페이지 2단계 인증번호 확인
    public boolean verifyCode(HttpSession session, String verificationNo) {
        String userNo = (String) session.getAttribute("userNo");
        if (userNo == null || verificationNo == null) {
            return false;
        }

        int result = am.selectNumber(userNo, verificationNo);

        return result > 0;
    }
}