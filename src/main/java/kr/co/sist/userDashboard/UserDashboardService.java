package kr.co.sist.userDashboard;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.sist.organization.OrganizationDomain;
import kr.co.sist.setting.AlarmSettingDTO;
import kr.co.sist.setting.StatusDTO;
import kr.co.sist.setting.TitleDTO;
import kr.co.sist.signup.SignupMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDashboardService {
	
	private final UserDashboardMapper udMapper;
	
	public TitleDTO selectRankPosition(String userNo) {
		return udMapper.selectRankPosition(userNo);
	}
	
	public List<StatusDTO> getStatusList() {
	    return udMapper.selectStatusList();
	}

	public String getUserStatus(String userNo) {
	    return udMapper.selectUserStatus(userNo);
	}

	public String getCurrentStatusName(String userNo) {
	    return udMapper.selectUserStatusName(userNo);
	}
	
	public AlarmSettingDTO getAlarm(String userNo) {
		return udMapper.selectAlarm(userNo);
	}
	
	public int setAlarm(int isAlarmOn,String userNo) {
		int cnt=udMapper.updateAlarm(isAlarmOn,userNo);
		return cnt;
	}
    //to do ----------------------------------------------------------------------------------
	public List<TodoDomain> getTodo(String userNo){
		List<TodoDomain> list=udMapper.selectTodo(userNo);
		return list;
	}
    //조직도 ------------------------------------------------------------------------------------
	public List<OrganizationDomain> getOrganization(String userNo){
		List<OrganizationDomain> list=udMapper.selectOrganization(userNo);
		return list;
	}
	
	
}
