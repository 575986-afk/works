package kr.co.sist.sysadmin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminMemberService {

	@Autowired(required = false)
	private AdminMemberDAO amDAO;

	@Autowired(required = false)
	private BCryptPasswordEncoder passwordEncoder;

	public List<AdminUserDomain> getMemberList(String keyword) {
		return amDAO.selectMemberList(keyword);
	}

	public AdminUserDomain getMemberDetail(String userNo) {
		return amDAO.selectMemberDetail(userNo);
	}

	/**
	 * 관리자가 특정 사용자의 비밀번호를 강제로 재설정한다.
	 * 새 비밀번호는 반드시 여기서 BCrypt로 해시한 뒤 저장한다 (평문 저장 금지).
	 */
	@Transactional
	public boolean changePassword(String userNo, String newPw) {
		String hashed = passwordEncoder.encode(newPw);
		return amDAO.updatePassword(userNo, hashed) == 1;
	}
}
