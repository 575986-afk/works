package kr.co.sist.sysadmin;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminDashBoardDAO {

	// 전체 회사 수
	public int selectTotalCompanyCount();

	// 전체 사용자 수
	public int selectTotalUserCount();

	// 활성 사용자 수 (ACCOUNT_STATUS = '활성')
	public int selectActiveUserCount();

	// 오늘 신규가입 수
	public int selectDailyCount();

	// 최근 7일 일별 신규가입 수
	public List<DailyCountDomain> selectWeeklyCount();

	// 최근 6개월 월별 신규가입 수
	public List<DailyCountDomain> selectMonthlyCount();

	// 최근 시스템(로그인) 로그 (전체 회사, 최근 50건)
	public List<SystemLogDomain> selectSystemLogList();
}
