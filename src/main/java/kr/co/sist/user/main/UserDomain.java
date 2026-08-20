package kr.co.sist.user.main;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Alias("MainUserDomain")
@Data
public class UserDomain {
	private String userName, role, profileImage;

}
