package kr.co.sist.user.address;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressMapper {
	public List<UserDomain> selectAddressList(RangeDTO rDTO);

	public List<GroupsDomain> selectGroup(String userNo);

	public List<OrganizationDomain> selectOrganization(String companyNo);

	public UserDomain selectAddressDetail(String userNo);

	public String selectCompany(String companyNo);

	public List<UserDomain> getContactsByKeyword(String keyword);

	public int insertBookmark(UserDTO uDTO);

	public int deleteBookmark(UserDTO uDTO);

}
