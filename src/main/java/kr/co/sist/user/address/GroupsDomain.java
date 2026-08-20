package kr.co.sist.user.address;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Alias("addressGroupsDomain")
@Data
public class GroupsDomain {
	private String groupsNo, groupsName;
}
