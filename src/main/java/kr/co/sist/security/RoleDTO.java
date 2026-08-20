package kr.co.sist.security;

import org.apache.ibatis.type.Alias;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Alias("roleDTO")
@Getter
@Builder
@ToString
public class RoleDTO {
	private String roleNo, roleName, companyNo, userNo;
	private int roleLevel;
}
