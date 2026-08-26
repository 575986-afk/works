package kr.co.sist.service;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CompanyCalMapper {

    // 일정 목록 및 상세 조회
    public List<CompanyCalDomain> selectCompanyCal(SearchCompanyCalDTO search);
    public CompanyCalDetailDomain selectCalDetail(String calenderNo);

    // 일정 등록
    public int insertCompanyCal(CompanyCalDTO dto);
    public int insertCompanyCalConnection(CompanyCalDTO dto);
    public int insertCompanyCalMembers(CompanyCalDTO dto);

    // 일정 수정
    public int updateCal(CompanyCalDTO dto);

    // 일정 삭제 (논리 삭제: IS_DELETED = 'Y'로 UPDATE)
    public int deleteCompanyCal(String calenderNo);
	
}