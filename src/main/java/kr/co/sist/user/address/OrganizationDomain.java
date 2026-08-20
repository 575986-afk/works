package kr.co.sist.user.address;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Alias("addressOrganizationDomain")
@Data
public class OrganizationDomain {
	private String organizationNo, organizationName;
}
