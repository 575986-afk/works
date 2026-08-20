package kr.co.sist.dashboard;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.signup.UserDTO;

@Mapper
public interface DashboardMapper {
	public int selectMemberCnt(String companyNo);
	
	public UserDTO selectUserName(String userNo);
}
