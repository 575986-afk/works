package kr.co.sist.user.address;

import java.util.List;

import lombok.Data;

@Data
public class OrgGroupDomain {
	private List<GroupsDomain> gList;
	private List<OrganizationDomain> oList;
}
