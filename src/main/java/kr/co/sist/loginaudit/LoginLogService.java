package kr.co.sist.loginaudit;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginLogService {
	
	@Autowired
	private LoginLogMapper llm;
	
	public List<LoginLogListDomain> getAllLoginLogList(LoginLogSearchDTO search){
		return llm.selectAllLoginLog(search);
	}
}
