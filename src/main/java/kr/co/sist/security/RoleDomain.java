package kr.co.sist.security;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("roleDomain")
@Setter
@Getter
@ToString
public class RoleDomain {
	private String roleNo, roleName, userNo, userName, email;
	private int roleLevel;
}
