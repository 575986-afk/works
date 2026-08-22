package kr.co.sist.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityService {
	
	@Autowired(required = false)
	private AuthorityMapper am;
	
	public List<RoleDomain> getRole(String companyNo){
		List<RoleDomain> list=am.selectRole(companyNo);
		return list;
	}
	
	public List<UserDomain> getRoleMember(String companyNo, String roleLevel){
		List<UserDomain> list=am.selectRoleMember(companyNo, roleLevel);
		return list;
	}
	
	public boolean createRole(RoleDTO rDTO) {
		return am.insertRole(rDTO)==1;
	}
	
	public boolean changeRoleName(RoleDTO rDTO) {
		return am.updateRoleName(rDTO)==1;
	}
	
	public boolean deleteRole(String companyNo, String roleNo) {
		return am.deleteRole(companyNo, roleNo)==1;
	}
	
	public boolean changeDelegation(String companyNo, String userNo) {
		return am.updateDelegation(companyNo, userNo)==1;
	}
	
	public List<UserDomain> searchDelegationMember(String companyNo, String keyword) {
	    return am.searchDelegationMember(companyNo, keyword);
	}
	
	public boolean addNewUserRole(String companyNo, String roleName, int roleLevel, String userNo) {
		return am.insertUserRole(companyNo, roleName, roleLevel, userNo)==1;
	}
	
	public boolean deleteUserRole(String companyNo, String roleNo, String userNo) {
		return am.deleteUserRole(companyNo, roleNo, userNo)==1;
	}
}
