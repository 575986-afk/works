package kr.co.sist.setting;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.signup.UserDTO;

@Mapper
public interface SettingMapper {

	UserDTO selectLogin(String userId);
	UserDTO selectProfile(String userNo);
	//타이틀 조회
	TitleDTO selectRankPosition(String userNo);
	int updateProfile(UserDTO uDTO);
	int updatePwChg(String userNo, String newPw);
	
	List<StatusDTO> selectStatusList();
	String selectUserStatus(String userNo);
	int updateUserStatus(String userNo, String statusNo);
//	알람 =======================================================
	AlarmSettingDTO selectAlarmSetting(String userNo);
	int updateAlarmSetting(AlarmSettingDTO asDTO);
//	문의 =======================================================
	List<InquiryDomain> selectInquiry(String inquiryNo);
}
