package kr.co.sist.user.addrPopup;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PopupAddressService {
	
	@Autowired
    private PopupAddressMapper pam;

    // 주소록 조회
    public List<UserDomain> getAddressList(RangeDTO rDTO) {
        return pam.selectAddressList(rDTO);
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
    
    public List<UserDomain> searchContactsByKeyword(String keyword){
    	return pam.getContactsByKeyword(keyword);
    }

}
