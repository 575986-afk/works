package kr.co.sist.calaudit;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalLogService {
	
	@Autowired(required = false)
	private CalLogMapper clm;
	
	public List<CalLogListDomain> getAllCalLogList(CalLogSearchDTO search){
		return clm.selectAllCalLog(search);
	}
	
	public CalLogDetailDomain getCalLogDetail(String logNo){
		return clm.selectCalLogDetail(logNo);
	}
}
