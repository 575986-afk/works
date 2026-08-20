package kr.co.sist.userDashboard;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.sist.setting.AlarmSettingDTO;
import kr.co.sist.setting.StatusDTO;
import kr.co.sist.setting.TitleDTO;

@Mapper
public interface UserDashboardMapper {
	
	TitleDTO selectRankPosition(String userNo);
	List<StatusDTO> selectStatusList();
	String selectUserStatus(String userNo);
	String selectUserStatusName(String userNo);
	AlarmSettingDTO selectAlarm(String userNo);
	int updateAlarm(AlarmSettingDTO asDTO);
	List<TodoDomain> selectTodo(String todoNo);
	List<OrganizationDomain> selectOrganization(String userNo);
	
}
