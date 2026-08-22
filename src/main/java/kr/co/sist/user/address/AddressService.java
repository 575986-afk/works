package kr.co.sist.user.address;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.signup.AESUtil;

@Service
public class AddressService {

	@Autowired(required = false)
	private AddressMapper am;
	 
	public List<UserDomain> getAddressList(RangeDTO rDTO) {
		List<UserDomain> list = new ArrayList<UserDomain>();
    	UserDomain temp = null;
    	for (UserDomain user : am.selectAddressList(rDTO)) {
    		temp = user;
    		temp.setUserName(AESUtil.decrypt(temp.getUserName()));
    		temp.setEmail(AESUtil.decrypt(temp.getEmail()));
    		temp.setPhoneNumber(AESUtil.decrypt(temp.getPhoneNumber()));
    		list.add(temp);
    	}
        return list;
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
	
    public List<UserDomain> searchContactsByKeyword(String keyword, String companyNo) {
        // 1. DB에서 같은 회사 직원만 모두 가져오기 (companyNo 전달)
        List<UserDomain> allUsers = am.getContactsByKeyword(companyNo);
        List<UserDomain> filteredResult = new ArrayList<>();
        
        if (allUsers != null && keyword != null) {
            String searchKeyword = keyword.trim();
            
            for (UserDomain user : allUsers) {
                // 2. 복호화
                String decName = AESUtil.decrypt(user.getUserName());
                String decPhone = AESUtil.decrypt(user.getPhoneNumber());
                
                user.setUserName(decName);
                user.setPhoneNumber(decPhone);
                
                // 3. Java 단에서 키워드 포함 여부 필터링
                boolean matchName = (decName != null && decName.contains(searchKeyword));
                boolean matchPhone = (decPhone != null && decPhone.contains(searchKeyword));
                boolean matchOrg = (user.getOrganizaionName() != null && user.getOrganizaionName().contains(searchKeyword));
                boolean matchGroup = (user.getGroupsName() != null && user.getGroupsName().contains(searchKeyword));
                
                if (matchName || matchPhone || matchOrg || matchGroup) {
                    filteredResult.add(user);
                }
            }
        }
        return filteredResult;
    }
    
    public int addBookmark(UserDTO uDTO) {
        return am.insertBookmark(uDTO);
    }
    
    public int removeBookmark(UserDTO uDTO) {
    	return am.deleteBookmark(uDTO);
    }
	


	
}
