package kr.co.sist.sysadmin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminMapper {

	// 아이디로 관리자 1건 조회 (비밀번호는 서비스단에서 BCrypt로 매칭)
	public AdminDomain selectAdminById(@Param("adminId") String adminId);
}
