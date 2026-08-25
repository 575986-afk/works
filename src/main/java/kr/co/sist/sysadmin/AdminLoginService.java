package kr.co.sist.sysadmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminLoginService {

	@Autowired(required = false)
	private AdminMapper am;

	/**
	 * 로그인 성공 시 AdminDomain, 실패 시 null.
	 * (아이디 없음 / 비밀번호 불일치를 구분하지 않는다 - 무엇이 틀렸는지
	 *  알려주는 건 계정 존재 여부를 노출하는 것과 같아서 지양)
	 *
	 * 주의: 기존 ADMIN 테이블의 PASSWORD 컬럼이 평문으로 저장되어 있어서
	 * (admin01~04 계정 전부 '1234') BCryptPasswordEncoder로는 비교가 안 됨.
	 * 그래서 여기서는 평문 equals로 비교한다. 나중에 이 테이블 비밀번호를
	 * 해시로 마이그레이션하면 이 부분도 BCrypt 비교로 바꿔야 함.
	 */
	public AdminDomain login(AdminDTO aDTO) {
		AdminDomain admin = am.selectAdminById(aDTO.getAdminId());
		if (admin == null) {
			return null;
		}
		if (aDTO.getAdminPw() == null || !aDTO.getAdminPw().equals(admin.getAdminPw())) {
			return null;
		}
		return admin;
	}
}
