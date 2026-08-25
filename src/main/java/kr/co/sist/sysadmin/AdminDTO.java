package kr.co.sist.sysadmin;

import org.apache.ibatis.type.Alias;

import lombok.Data;

/**
 * 전체(시스템) 관리자 로그인 폼 바인딩용 DTO.
 * adminUser/** 의 회사별 관리자(role_level)와는 완전히 별개의 계정 체계.
 */
@Alias("sysAdminDTO")
@Data
public class AdminDTO {
	private String adminId;
	private String adminPw;
}
