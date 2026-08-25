package kr.co.sist.sysadmin;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** 전체관리자용 - 회사 구분 없이 전체 USERS 조회 결과 */
@Alias("adminUserDomain")
@Getter
@Setter
@ToString
public class AdminUserDomain {
	private String userNo;
	private String userName;   // 복호화 필요
	private String userId;
	private String email;      // 복호화 필요
	private String phone;      // 복호화 필요
	private String companyNo;
	private String companyName;
	private int roleLevel;
	private String accountStatus;
	private Timestamp signupDate;
}
