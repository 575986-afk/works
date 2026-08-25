package kr.co.sist.log;

import org.apache.ibatis.type.Alias;

import lombok.Data;

/** 기존 kr.co.sist.aop.AopMapper#insertAddressbookLog(duty, target, userNo) 와 동일 항목 */
@Alias("addrLogDTO")
@Data
public class AddrLogDTO {
	private String duty;
	private String target;
	private String userNo;
}
