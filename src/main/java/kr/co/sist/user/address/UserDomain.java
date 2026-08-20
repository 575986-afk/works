package kr.co.sist.user.address;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Alias("addressUserDomain")
@Data
public class UserDomain {
	private String userNo, userName, positionName, rankName, organizaionName, companyName, email, phoneNumber, profileImage, groupsName, isBookmarked;
}
