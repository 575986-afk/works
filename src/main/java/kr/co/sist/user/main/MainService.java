package kr.co.sist.user.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainService {
	
	@Autowired(required = false)
	private MainMapper mm;
	
	public UserDomain getUser(String userNo) {
		UserDomain ud = mm.selectUserInfo(userNo);
		return ud;
	}

}
