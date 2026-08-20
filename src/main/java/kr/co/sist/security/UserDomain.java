package kr.co.sist.security;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("authorityUserDomain")
@Setter
@Getter
@ToString
public class UserDomain {
	private String userId, userNo, userName, email, position, rank, roleNo, roleName;
	private int roleLevel;
}
