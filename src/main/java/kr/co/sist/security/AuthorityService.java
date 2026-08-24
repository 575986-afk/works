package kr.co.sist.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorityService {
	
	@Autowired(required = false)
	private AuthorityMapper am;
	
	// 권한 조회
	public List<RoleDomain> getRole(String companyNo){
		List<RoleDomain> list=am.selectRole(companyNo);
		return list;
	}
	
	// 권한 구성원 조회
	public List<UserDomain> getRoleMember(String companyNo, String roleLevel){
		List<UserDomain> list=am.selectRoleMember(companyNo, roleLevel);
		return list;
	}
	
	// 권한명 생성
	public boolean createRole(RoleDTO rDTO) {
		return am.insertRole(rDTO)==1;
	}
	
	// 권한명 수정
	public boolean changeRoleName(RoleDTO rDTO) {
		return am.updateRoleName(rDTO)==1;
	}
	
	// 권한 삭제
	public boolean deleteRole(String companyNo, String roleName) {
	    if ("최고운영자".equals(roleName) || "운영관리자".equals(roleName)) {
	        return false;
	    }
	    return am.deleteRole(companyNo, roleName) > 0;
	}
	
	// 권한 위임
	@Transactional
	public boolean changeDelegation(
	        String receiverUserNo,
	        String companyNo) {

	    // 1. 현재 최고관리자 조회
	    RoleDomain currentAdmin = am.selectCurrentAdmin(companyNo);
	    if (currentAdmin == null) {
	        return false;
	    }
	    String senderUserNo = currentAdmin.getUserNo();

	    // 2. 위임받을 사람 조회
	    UserDomain receiver = am.selectDelegationReceiver(companyNo, receiverUserNo);
	    if (receiver == null) {
	        return false;
	    }

	    // 3. 현재 최고운영자를 일반사용자로 변경
        int senderResult = am.updateDelegationSender(senderUserNo, companyNo);
        if (senderResult != 1) {
            return false;
        }
        
        // 4. 위임받는 사람을 최고운영자로 변경
        int receiverResult = am.updateDelegationReceiver(receiverUserNo, companyNo);

        return receiverResult == 1;
	}
	
	// 현재 최고운영자 조회
	public RoleDomain getCurrentAdmin(String companyNo) {
	    return am.selectCurrentAdmin(companyNo);
	}

	// 위임받을 사람 조회
	public UserDomain getDelegationReceiver(String companyNo, String userNo) {
	    return am.selectDelegationReceiver(companyNo, userNo);
	}
	
	// 위임받을 구성원 검색
	public List<UserDomain> searchDelegationMember(String companyNo, String keyword) {
	    return am.searchDelegationMember(companyNo, keyword);
	}
	
	// 구성원에게 권한 추가
	public boolean addNewUserRole(String companyNo, String roleName, int roleLevel, String userNo) {
		return am.insertUserRole(companyNo, roleName, roleLevel, userNo)==1;
	}
	
	// 구성원 권한 삭제
    public boolean deleteUserRole(String companyNo, String userNo) {
        return am.deleteUserRole(companyNo,userNo) == 1;
    }
}
