package kr.co.sist.organization;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrganizationMapper {

	// 조직 검색
	public List<OrganizationDomain> selectOrganization(@Param("companyNo") String companyNo, @Param("keyword") String keyword);

	// 조직 전체 조회
	public List<OrganizationDomain> selectAllOrganization(@Param("companyNo") String companyNo);

	// 조직 추가
	public int insertOrganization(OrganizationDTO organizationDTO);
	//조직 추가시 조직멤버추가
	public int insertOrganizationMember(@Param("organizationNo") String organizationNo, @Param("userNo") String userNo);

	// 기존 조직에 멤버 추가
    // 조직 멤버 수정에서 사용
    public int insertOrganizationMemberForEdit(@Param("organizationNo") String organizationNo,@Param("userNo") String userNo);
	
	// 조직 상세 정보 조회
	public OrganizationDomain selectOrganizationDetail(@Param("organizationNo") String organizationNo, @Param("companyNo") String companyNo);

	// 조직 구성원 조회
	public List<OrganizationMemberDomain> selectOrganizationMember(@Param("organizationNo") String organizationNo);

	// 조직 구성원 삭제
	public int deleteOrganizationMember(@Param("organizationNo") String organizationNo,@Param("userNo") String userNo);

	// 조직장 변경 - organizationMEMBER
	public int updateOrganizationMemberLeader(@Param("organizationNo") String organizationNo, @Param("userNo") String userNo);

	// 조직장 변경 - organization
	public int updateOrganizationLeader(@Param("organizationNo") String organizationNo, @Param("userNo") String userNo);

	// [수정] 조직 삭제 전 구성원 전체 삭제 (단건/다중 통합)
	public int deleteOrganizationMemberAll(@Param("organizationNos") List<String> organizationNos);

	// [수정] 조직 삭제 (단건/다중 통합)
	public int deleteOrganization(@Param("organizationNos") List<String> organizationNos);
	
	//조직 수정
	public int updateOrganization(OrganizationDTO gDTO);

	// 조직/조직마스터 수정용
    // organization의 user_no 변경
    public int updateOrganizationLeaderForEdit(@Param("organizationNo") String organizationNo,@Param("companyNo") String companyNo,@Param("userNo") String userNo);

    // 조직/조직마스터 수정용
    // organizationMEMBER의 is_leader 변경
    public int updateOrganizationMemberLeaderForEdit(@Param("organizationNo") String organizationNo,@Param("userNo") String userNo);

	
}