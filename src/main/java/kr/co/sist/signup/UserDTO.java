package kr.co.sist.signup;


import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserDTO {

	private String userNo, userId, password, companyNo,name,email,tel,
	passwordConfirm, userType,companyName, companyTel, ip, newPw, accountStatus,workplace, jobtask, rankNo, positionNo,role,profileImage;
	private int role_level;
	private Timestamp signupDate;
	
}
