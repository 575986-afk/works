package kr.co.sist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyCalService {

	@Autowired(required = false)
	private CompanyCalMapper ccm;
	
	// 일정 목록 조회
	public List<CompanyCalDomain> getCompanyCal(SearchCompanyCalDTO search){
		return ccm.selectCompanyCal(search);
	}
	
	// 일정 상세 조회
	public CompanyCalDetailDomain getCompanyCalDetail(String calenderNo) {
		return ccm.selectCalDetail(calenderNo);
	}

	// 회사 일정 등록 (3개 테이블 일괄 등록)
	@Transactional
	public void addCompanyCal(CompanyCalDTO dto) {
		ccm.insertCompanyCal(dto);
		ccm.insertCompanyCalConnection(dto);
		ccm.insertCompanyCalMembers(dto);
	}

	// 회사 일정 수정
	@Transactional
	public boolean modifyCompanyCal(CompanyCalDTO dto) {
		return ccm.updateCal(dto) > 0;
	}

	// 회사 일정 삭제 (자식 테이블부터 순차 삭제)
	@Transactional
	public boolean removeCompanyCal(String calenderNo) {
	    return ccm.deleteCompanyCal(calenderNo) > 0;
	}
}