package kr.co.sist.sysadmin;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminMemberDAO {

	// 전체 회사 대상 회원 목록 (keyword가 있으면 아이디로 검색)
	public List<AdminUserDomain> selectMemberList(@Param("keyword") String keyword);

	// 회원 상세
	public AdminUserDomain selectMemberDetail(@Param("userNo") String userNo);

	// 비밀번호 재설정 (해시된 값으로)
	public int updatePassword(@Param("userNo") String userNo, @Param("newPw") String newPw);
}
