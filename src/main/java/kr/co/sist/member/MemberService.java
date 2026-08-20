package kr.co.sist.member;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.sist.organization.OrganizationDomain;
import kr.co.sist.title.PositionDomain;
import kr.co.sist.title.RankDomain;

@Service
public class MemberService {
	
	@Autowired(required = false)
	private MemberMapper mm;
	
	// 조직 조회
    public List<OrganizationDomain> getOrganizationList(String companyNo) {
        return mm.selectOrganization(companyNo);
    }

    // 구성원 리스트 조회
    public List<MemberDomain> getMemberList(String companyNo, String organizationNo) {
        return mm.selectMemberList(companyNo, organizationNo);
    }

    // 회사 구성원 수 조회
    public int getCompanyMemberCount(String companyNo) {
        return mm.selectCompanyMemberCount(companyNo);
    }

    // 구성원 상세 조회
    public MemberDomain getMemberDetail(String companyNo, String userNo) {
        return mm.selectMemberDetail(companyNo, userNo);
    }

    // 아이디 확인
    public MemberDomain getUserById(String userId) {
        return mm.selectUserById(userId);
    }

    // 직급 목록 조회
    public List<RankDomain> getRankList() {
        return mm.selectRankList();
    }

    // 직책 목록 조회
    public List<PositionDomain> getPositionList() {
        return mm.selectPositionList();
    }

    // 구성원 추가
    @Transactional
    public void addMember(MemberDTO memberDTO) {

        // 1. USERS 정보 수정
        mm.updateUserForMember(memberDTO);

        // 2. TITLE에 직급/직책 등록
        mm.insertMemberTitle(memberDTO);

        // 3. ORGANIZATIONMEMBER에 다중 조직 등록 (선택된 조직이 있을 때만 실행)
        if (memberDTO.getOrganizationNos() != null && !memberDTO.getOrganizationNos().isEmpty()) {
            mm.insertMemberOrganizations(memberDTO);
        } else if (memberDTO.getOrganizationNo() != null && !memberDTO.getOrganizationNo().isEmpty()) {
            // 단일 선택만 넘어왔을 때의 예외 방지용
            mm.insertMemberOrganization(memberDTO);
        }
    }

    // 구성원 정보 수정
    @Transactional
    public void modifyMember(MemberDTO memberDTO) {
    	// 1. USERS 테이블 정보 수정
        mm.updateUsers(memberDTO);

        // 2. TITLE 테이블 정보 수정
        mm.updateMemberTitle(memberDTO);

        // 3. 기존 소속 조직 전체 삭제
        mm.deleteMemberOrganizations(memberDTO.getUserNo());

        // 4. 새롭게 선택된 다중 조직 목록 일괄 재등록
        if (memberDTO.getOrganizationNos() != null && !memberDTO.getOrganizationNos().isEmpty()) {
            mm.insertMemberOrganizations(memberDTO);
        }

    }

    // 구성원 삭제
    @Transactional
    public void deleteMember(String userNo, String companyNo) {
        mm.deleteMember(userNo, companyNo);
    }
}
