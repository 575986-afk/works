package kr.co.sist.log;

import org.apache.ibatis.type.Alias;

import lombok.Data;

/** 기존 kr.co.sist.aop.AopMapper#insertLoginLog(description, loginIp, connectionStatus, userNo, attemptId) 와 동일 항목 */
@Alias("loginLogDTO")
@Data
public class LoginLogDTO {
	private String description;
	private String loginIp;
	private String connectionStatus;
	private String userNo;
	private String attemptId;
}
