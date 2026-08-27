package kr.co.sist.security;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthorityMapper {
	public List<RoleDomain> selectRole(@Param("companyNo") String companyNo);

    public List<UserDomain> selectRoleMember(@Param("companyNo") String companyNo, 
    		@Param("roleLevel") String roleLevel);
    
    public int insertRole(RoleDTO rDTO);
    
    public int updateRoleName(RoleDTO rDTO);
    
//    public int deleteRole(@Param("companyNo") String companyNo, @Param("roleName") String roleName);
    public int updateUserRoleToDefault(@Param("companyNo") String companyNo, @Param("roleName") String roleName);
    public int deleteRoleTemplate(@Param("companyNo") String companyNo, @Param("roleName") String roleName);
    
    public int updateDelegationReceiver(
            @Param("receiverUserNo") String receiverUserNo,
            @Param("companyNo") String companyNo);
    public int updateDelegationSender(
    		@Param("senderUserNo") String senderUserNo,
    		@Param("companyNo") String companyNo);

    public RoleDomain selectCurrentAdmin(
            @Param("companyNo") String companyNo);

    public UserDomain selectDelegationReceiver(
            @Param("companyNo") String companyNo,
            @Param("userNo") String userNo);
    
    public List<UserDomain> searchDelegationMember(@Param("companyNo") String companyNo, 
    		@Param("keyword") String keyword);
    
    public int insertUserRole(@Param("companyNo") String companyNo,
    		@Param("roleName") String roleName,
    	    @Param("roleLevel") int roleLevel,
    	    @Param("userNoList") List<String> userNoList);

    public int insertUserRoleDirect(@Param("companyNo") String companyNo, 
    		@Param("roleName") String roleName, 
    		@Param("roleLevel") int roleLevel, 
    		@Param("userNoList") List<String> userNoList);
    
    public int deleteUserRole(@Param("companyNo") String companyNo, @Param("userNo") String userNo);
    
}
