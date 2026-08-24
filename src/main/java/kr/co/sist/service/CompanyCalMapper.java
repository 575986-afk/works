package kr.co.sist.service;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CompanyCalMapper {

	public List<CompanyCalDomain> selectCompanyCal(SearchCompanyCalDTO search);
}
