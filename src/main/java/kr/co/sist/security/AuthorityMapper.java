package kr.co.sist.security;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthorityMapper {
	public List<RoleDomain> selectRole(@Param("companyNo") String companyNo);

    public List<UserDomain> selectRoleMember(@Param("companyNo") String companyNo, @Param("roleLevel") String roleLevel);
    
    public int insertRole(RoleDTO rDTO);
    
    public int updateRoleName(RoleDTO rDTO);
    
    public int deleteRole(@Param("companyNo") String companyNo, @Param("roleNo") String roleNo);
    
    public int updateDelegation(@Param("companyNo") String companyNo, @Param("userNo") String userNo);
    
    public List<UserDomain> searchDelegationMember(@Param("companyNo") String companyNo, @Param("keyword") String keyword);
    
    public int insertUserRole(@Param("companyNo") String companyNo, @Param("roleName") String roleName,
            @Param("roleLevel") int roleLevel, @Param("userNo") String userNo);
    
    public int deleteUserRole(@Param("companyNo") String companyNo, @Param("roleNo") String roleNo, @Param("userNo") String userNo);
    
}
