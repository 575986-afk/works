package kr.co.sist.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.signup.UserDTO;

@Service
public class DashboardService {
	
	@Autowired(required = false)
	private DashboardMapper dm;
	
	public int getMemberCnt(String companyNo) {
		int cnt=dm.selectMemberCnt(companyNo);
		return cnt;
	}
	
	public String getUserName(String userNo) {
		UserDTO user = dm.selectUserName(userNo);
        return user.getName();
	}
	
}
