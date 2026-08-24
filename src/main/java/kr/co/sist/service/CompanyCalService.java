package kr.co.sist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyCalService {

	@Autowired(required = false)
	private CompanyCalMapper ccm;
	
	public List<CompanyCalDomain> getCompanyCal(SearchCompanyCalDTO search){
		return ccm.selectCompanyCal(search);
	}
}
