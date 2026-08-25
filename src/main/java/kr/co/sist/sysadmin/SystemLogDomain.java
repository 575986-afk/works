package kr.co.sist.sysadmin;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** 전체관리자 대시보드용 시스템(로그인) 로그. 기존 LOGINLOG 테이블을 회사 구분 없이 조회. */
@Alias("systemLogDomain")
@Getter
@Setter
@ToString
public class SystemLogDomain {
	private String logNo;
	private String description;
	private String loginIp;
	private String status;       // CONNECTION_STATUS
	private String userName;     // 복호화 필요 (EncryptTypeHandler)
	private String companyName;
	private Timestamp inputDate;
}
