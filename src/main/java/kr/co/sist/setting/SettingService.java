package kr.co.sist.setting;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.sist.signup.AESUtil;
import kr.co.sist.signup.UserDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SettingService {
	
	private final SettingMapper settingMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder; 

    public boolean checkPassword(String userId, String rawPassword) {
        UserDTO user = settingMapper.selectLogin(userId);
        
        if (user == null) {
            return false;
        }

        return bCryptPasswordEncoder.matches(rawPassword, user.getPassword());
    }

   public UserDTO selectProfile(String userNo) {
	   return settingMapper.selectProfile(userNo);
   }
   public TitleDTO selectRankPosition(String userNo) {
	   return settingMapper.selectRankPosition(userNo);
   }
   
   	@Transactional
    public int modifyProfile(UserDTO uDTO) {
    	
    	if (uDTO.getTel() != null && !uDTO.getTel().isEmpty()) {
            uDTO.setTel(uDTO.getTel());
        }
        if (uDTO.getEmail() != null && !uDTO.getEmail().isEmpty()) {
            uDTO.setEmail(uDTO.getEmail());
        }
        return settingMapper.updateProfile(uDTO);
    }
   
    
    public int updatePwChg(String userNo, String newPw) {
    	BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
    	String encodePw=encoder.encode(newPw);
    	
    	int cnt=settingMapper.updatePwChg(userNo, encodePw);
    	return cnt;
    }
    
    
    public List<StatusDTO> getStatusList() {
        return settingMapper.selectStatusList();
    }

    public String getUserStatus(String userNo) {
        return settingMapper.selectUserStatus(userNo);
    }

    public int updateUserStatus(String userNo, String statusNo) {
        return settingMapper.updateUserStatus(userNo, statusNo);
    }
//	=================================알람 ==============================================
	
	public AlarmSettingDTO getAlarm(String userNo) {
		return settingMapper.selectAlarmSetting(userNo);
	}
	
	public int setAlarmSetting(AlarmSettingDTO asDTO) {
		return settingMapper.updateAlarmSetting(asDTO);
	}
	
//	==================================문의 =============================================
	public List<InquiryDomain> showInquiry(String inquiryNo) {
		return settingMapper.selectInquiry(inquiryNo);
	}
	
	
}
