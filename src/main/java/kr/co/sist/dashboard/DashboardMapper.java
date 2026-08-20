package kr.co.sist.dashboard;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DashboardMapper {
	public int selectMemberCnt(String companyNo);
	
	public String selectUserName(String userNo);
}
