package kr.co.sist.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
	
	@Autowired(required = false)
	private DashboardMapper dm;
	
	public int getMemberCnt(String companyNo) {
		int cnt=dm.selectMemberCnt(companyNo);
		return cnt;
	}
	
	public String getUserName(String userNo) {
		String userName=dm.selectUserName(userNo);
		return userName;
	}
	
}
