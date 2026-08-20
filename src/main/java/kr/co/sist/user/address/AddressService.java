package kr.co.sist.user.address;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

	@Autowired(required = false)
	private AddressMapper am;
	 
	public List<UserDomain> getAddressList(RangeDTO rDTO) {
        return am.selectAddressList(rDTO);
    }
	
	public List<GroupsDomain> getGroup(String userNo) {
        return am.selectGroup(userNo);
    }
    
    public List<OrganizationDomain> getOrganization(String companyNo) {
        return am.selectOrganization(companyNo);
    }
    
    public String getCompany(String companyNo) {
        return am.selectCompany(companyNo);
    }
	
    public List<UserDomain> searchContactsByKeyword(String keyword) {
        return am.getContactsByKeyword(keyword);
    }
    
    public int addBookmark(UserDTO uDTO) {
        return am.insertBookmark(uDTO);
    }
    
    public int removeBookmark(UserDTO uDTO) {
    	return am.deleteBookmark(uDTO);
    }
	


	
}
