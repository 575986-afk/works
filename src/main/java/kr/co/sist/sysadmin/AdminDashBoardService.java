package kr.co.sist.sysadmin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminDashBoardService {

	@Autowired(required = false)
	private AdminDashBoardDAO adDAO;

	public int getTotalCompanyCount() {
		return adDAO.selectTotalCompanyCount();
	}

	public int getTotalUserCount() {
		return adDAO.selectTotalUserCount();
	}

	public int getActiveUserCount() {
		return adDAO.selectActiveUserCount();
	}

	public int getDailyCount() {
		return adDAO.selectDailyCount();
	}

	public List<DailyCountDomain> getWeeklyCount() {
		return adDAO.selectWeeklyCount();
	}

	public List<DailyCountDomain> getMonthlyCount() {
		return adDAO.selectMonthlyCount();
	}

	public List<SystemLogDomain> getLogList() {
		return adDAO.selectSystemLogList();
	}
}
