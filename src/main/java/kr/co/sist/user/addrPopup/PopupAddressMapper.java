package kr.co.sist.user.addrPopup;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PopupAddressMapper {
	public List<UserDomain> selectAddressList(RangeDTO rDTO);

	public List<GroupsDomain> selectGroup(String userNo);
	public String selectCompany(String companyNo);

	public 	List<OrganizationDomain> selectOrganization(String userNo);
	
	public 	List<UserDomain> getContactsByKeyword(String keyword);
}
