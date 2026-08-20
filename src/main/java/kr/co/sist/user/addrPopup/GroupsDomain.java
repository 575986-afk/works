package kr.co.sist.user.addrPopup;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Alias("addrPopupGroupsDomain")
@Data
public class GroupsDomain {
	private String groupsNo, groupsName;
}
