package kr.co.sist.user.alarm;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlarmService {

	@Autowired(required = false)
	private AlarmMapper am;
	 
	public List<AlarmDomain> getAlarmList(String userNo){
		return am.selectAlarmList(userNo);
	}
	
}
