package kr.co.sist.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public boolean sendDelegationMail(String receiverEmail, String receiverName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            // 실제 받을 사람
            message.setTo("day305@naver.com");
            // 네이버 SMTP 계정과 동일한 주소
            message.setFrom("day305@naver.com");
            message.setSubject("최고관리자 권한 위임 확인");
            message.setText(
                    receiverName + "님,\n\n"
                    + "최고관리자 권한 위임 요청이 도착했습니다.\n\n"
                    + "권한 위임 페이지에서 권한을 확인해 주세요.\n"
                    + "감사합니다."
            );
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 감사 페이지 2단계 인증번호 메일 발송
    public boolean sendVerificationMail(String receiverEmail, String verificationNo) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            // 실제 인증번호를 받을 사람
            message.setTo("day305@naver.com");
            // SMTP 계정과 동일한 주소
            message.setFrom("day305@naver.com");
            message.setSubject("감사 페이지 2단계 인증번호");
            message.setText(
                    "안녕하세요.\n\n"
                    + "감사 페이지 접근을 위한 2단계 인증번호입니다.\n\n"
                    + "인증번호 : " + verificationNo + "\n\n"
                    + "인증번호는 10분 이내에 입력해주세요.\n"
                    + "감사합니다."
            );
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}