package kr.co.sist.findId;

import org.springframework.stereotype.Service;

import kr.co.sist.signup.AESUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FindIdService {
	
	private final FindIdMapper fDAO;
	
	 public String selectFindId(String name, String email) {

	        String userName = name;
	        String userEmail = email;

	        return fDAO.selectFindId(userName, userEmail);
	    }

}
