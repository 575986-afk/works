package kr.co.sist.user.addrPopup;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.signup.AESUtil;


@Service
public class PopupAddressService {
	
	@Autowired
    private PopupAddressMapper pam;

    // 주소록 조회
    public List<UserDomain> getAddressList(RangeDTO rDTO) {
    	List<UserDomain> list = new ArrayList<UserDomain>();
    	UserDomain temp = null;
    	for (UserDomain user : pam.selectAddressList(rDTO)) {
    		temp = user;
    		temp.setUserName(AESUtil.decrypt(temp.getUserName()));
    		temp.setEmail(AESUtil.decrypt(temp.getEmail()));
    		temp.setPhoneNumber(AESUtil.decrypt(temp.getPhoneNumber()));
    		list.add(temp);
    	}
        return list;
    }

    // 그룹 조회
    public List<GroupsDomain> getGroup(String userNo) {
        return pam.selectGroup(userNo);
    }
    
    // 그룹 조회
    public String getCompany(String companyNo) {
    	return pam.selectCompany(companyNo);
    }

    // 조직 조회
    public List<OrganizationDomain> getOrganization(String userNo) {
        return pam.selectOrganization(userNo);
    }

    // 선택된 멤버 조회
    public List<String> getSelectedMember(String[] userNo) {
        // 선택된 유저 번호 배열을 처리하는 비즈니스 로직
        return null;
    }
    
    public List<UserDomain> searchContactsByKeyword(String keyword, String companyNo){
        // 1. DB에서 같은 회사 직원만 모두 가져오기
        List<UserDomain> allUsers = pam.getContactsByKeyword(companyNo);
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
                
                // 검색 조건에 부합하는 사용자만 결과 리스트에 추가
                if (matchName || matchPhone || matchOrg || matchGroup) {
                    filteredResult.add(user);
                }
            }
        }
        return filteredResult;
    }

}
