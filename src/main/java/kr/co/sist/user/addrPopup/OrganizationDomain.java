package kr.co.sist.user.addrPopup;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Alias("addrPopupOrganizationDomain")
@Data
public class OrganizationDomain {
	private String organizationNo, organizationName;
}
