package kr.co.sist.user.addrPopup;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Alias("addrPopupUserDomain")
@Data
public class UserDomain {
	private String userNo, userName, positionName, rankName, organizaionName, companyName, email, phoneNumber, profileImage, groupsName;
	
}
