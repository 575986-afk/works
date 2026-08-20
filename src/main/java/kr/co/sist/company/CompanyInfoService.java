package kr.co.sist.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyInfoService {

	@Autowired(required = false)
	private CompanyMapper cm;
	
	public CompanyDomain getCompanyData(String companyNo){
		CompanyDomain cDomain=cm.selectCompanyData(companyNo);
		return cDomain;
	}
	
	public boolean setCompanyData(CompanyDTO cDTO, String companyNo) {
	    return cm.updateCompanyData(cDTO, companyNo) == 1;
	}
}
