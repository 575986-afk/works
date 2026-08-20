package kr.co.sist.company;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CompanyMapper {
	public CompanyDomain selectCompanyData(String companyNo);
	
	public int updateCompanyData(@Param("cDTO")CompanyDTO cDTO, String companyNo);
}
