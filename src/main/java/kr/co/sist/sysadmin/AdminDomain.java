package kr.co.sist.sysadmin;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** SYS_ADMIN 테이블 조회 결과 (비밀번호 포함 - 매칭 후 서비스단에서 절대 세션에 담지 않음) */
@Alias("sysAdminDomain")
@Getter
@Setter
@ToString(exclude = "adminPw")
public class AdminDomain {
	private long adminNo;
	private String adminId;
	private String adminPw;   // ADMIN 테이블은 평문 저장 - AdminLoginService에서 평문 비교함
	private String adminName;
	private String email;
	private String phoneNumber;
	private Timestamp regDate;
}
