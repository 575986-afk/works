package kr.co.sist.member;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.sist.organization.OrganizationDomain;
import kr.co.sist.title.PositionDomain;
import kr.co.sist.title.RankDomain;

@Mapper
public interface MemberMapper {

    // 조직 조회
    public List<OrganizationDomain> selectOrganization(@Param("companyNo") String companyNo);

    // 구성원 리스트 조회
    public List<MemberDomain> selectMemberList(@Param("companyNo") String companyNo, @Param("organizationNo") String organizationNo);

    // 회사 구성원 수 조회
    public int selectCompanyMemberCount(@Param("companyNo") String companyNo);

    // 구성원 상세 조회
    public MemberDomain selectMemberDetail(@Param("companyNo") String companyNo, @Param("userNo") String userNo);

    // 아이디 확인
    public MemberDomain selectUserById(@Param("userId") String userId);

    // 직급 조회
    public List<RankDomain> selectRankList(@Param("companyNo") String companyNo);

    // 직책 조회
    public List<PositionDomain> selectPositionList(@Param("companyNo") String companyNo);
    
    // 구성원 등록
    // 구성원 정보 등록
    public int updateUserForMember(MemberDTO memberDTO);
    // 직급 / 직책 등록
    public int insertMemberTitle(MemberDTO memberDTO);
    // 조직 등록
    public int insertMemberOrganization(MemberDTO memberDTO);

    // 구성원 정보 수정
    public int updateUsers(MemberDTO memberDTO);
    public int updateMemberTitle(MemberDTO memberDTO);
    public int deleteMemberOrganizations(@Param("userNo") String userNo);
    public int insertMemberOrganizations(MemberDTO memberDTO);

    // 구성원 삭제
    public int deleteMember(@Param("userNo") String userNo, @Param("companyNo") String companyNo);
}