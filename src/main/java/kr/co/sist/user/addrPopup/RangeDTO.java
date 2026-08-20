package kr.co.sist.user.addrPopup;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Alias("addrPopupRangeDTO")
@Data
public class RangeDTO {
	private String userNo, phoneNumber, groupsNo, organizationNo, companyNo, type;
	
}
