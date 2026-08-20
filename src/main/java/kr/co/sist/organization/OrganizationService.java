package kr.co.sist.organization;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrganizationService {

    @Autowired(required = false)
    private OrganizationMapper gm;

    // 조직 검색
    public List<OrganizationDomain> getOrganization(String companyNo, String keyword) {
        return gm.selectOrganization(companyNo, keyword);
    }

    // 조직 조회
    public List<OrganizationDomain> getAllOrganization(String companyNo) {
        return gm.selectAllOrganization(companyNo);
    }

    // 조직 추가
    @Transactional
    public boolean createOrganization(OrganizationDTO gDTO) {
    	// 1. 조직 생성 (조직명 등 기본 정보 등록)
        int cnt1 = gm.insertOrganization(gDTO);
        if (cnt1 != 1) {
            return false;
        }

        // 2. 마스터(userNo)가 지정되어 있을 때만 리더 멤버로 추가
        String userNo = gDTO.getUserNo();
        if (userNo != null && !userNo.trim().isEmpty()) {
            int cnt2 = gm.insertOrganizationMember(
                    gDTO.getOrganizationNo(),
                    userNo
            );
            return cnt2 == 1;
        }    

        // 마스터 없이 조직만 생성한 경우 조직 생성 완료(true) 반환
        return true;
    }

    // 조직 정보 조회
    public OrganizationDomain getOrganizationDetail(
            String organizationNo,
            String companyNo) {
        return gm.selectOrganizationDetail(organizationNo, companyNo);
    }

    // 조직 구성원 조회
    public List<OrganizationMemberDomain> getOrganizationMember(String organizationNo) {
        return gm.selectOrganizationMember(organizationNo);
    }

    // 조직 멤버 개별 삭제
    public boolean deleteOrganizationMember(
            String organizationNo,
            String userNo) {
        return gm.deleteOrganizationMember(organizationNo, userNo) == 1;
    }

    // 조직 마스터 변경
    // 상세보기에서 "조직 마스터 변경" 버튼을 눌렀을 때 사용
    @Transactional
    public boolean setOrganizationLeader(String organizationNo, String userNo) {
        int cnt1 = gm.updateOrganizationMemberLeader(organizationNo, userNo);
        int cnt2 = gm.updateOrganizationLeader(organizationNo, userNo);
        return cnt1 == 1 && cnt2 == 1;
    }

    // 조직 삭제
    @Transactional
    public boolean deleteOrganization(String organizationNo) {
        int cnt1 = gm.deleteOrganizationMemberAll(organizationNo);
        int cnt2 = gm.deleteOrganization(organizationNo);
        return cnt1 >= 0 && cnt2 == 1;
    }

    // =========================================================
    // 조직/조직마스터 수정 ("조직/마스터 수정" 모달용)
    // OrganizationSaveDTO를 통해 전달된 항목(조직명, 조직설명, 마스터)을
    // 각각, 둘씩, 또는 모두 전달받아 개별/동적 수정 처리
    // =========================================================
    @Transactional
    public boolean modifyOrganization(OrganizationSaveDTO saveDTO) {
        if (saveDTO == null || saveDTO.getOrganizationNo() == null || saveDTO.getOrganizationNo().trim().isEmpty()) {
            return false;
        }

        // 1. 조직명 또는 조직설명이 전달된 경우 -> 조직 정보 수정
        if ((saveDTO.getOrganizationName() != null && !saveDTO.getOrganizationName().trim().isEmpty())
                || saveDTO.getOrganizationDescription() != null) {

            // Builder 패턴 사용
            OrganizationDTO gDTO = OrganizationDTO.builder()
                    .organizationNo(saveDTO.getOrganizationNo())
                    .organizationName(saveDTO.getOrganizationName())
                    .organizationDescription(saveDTO.getOrganizationDescription())
                    .companyNo(saveDTO.getCompanyNo())
                    .build();

            gm.updateOrganization(gDTO);
        }

        // 2. userNo(조직 마스터)가 전달된 경우 -> 마스터 변경 쿼리 호출
        String leaderNo = saveDTO.getUserNo();
        if (leaderNo != null && !leaderNo.trim().isEmpty()) {

            // Organization 테이블의 user_no 변경
            gm.updateOrganizationLeaderForEdit(saveDTO.getOrganizationNo(), saveDTO.getCompanyNo(), leaderNo);

            // OrganizationMEMBER 테이블의 is_leader 변경
            gm.updateOrganizationMemberLeaderForEdit(saveDTO.getOrganizationNo(), leaderNo);
        }

        return true;
    }


    // =========================================================
    // 조직 멤버 수정
    // =========================================================
    // 멤버 수정 모달에서 "저장"을 눌렀을 때 사용
    //
    // 모달에서 최종적으로 남아 있는 userNo 목록을 전달받고
    // DB의 기존 구성원과 비교하여
    //
    // ① 빠진 사람 → 삭제
    // ② 새로 들어온 사람 → 추가
    //
    // 를 한 번의 Transaction으로 처리한다.
    // =========================================================

    @Transactional
    public boolean saveOrganizationMember(
            OrganizationMemberSaveDTO saveDTO) {
        String organizationNo = saveDTO.getOrganizationNo();
        List<String> newUserNoList =
                saveDTO.getUserNoList();
        // 현재 DB에 들어있는 구성원
        List<OrganizationMemberDomain> currentMemberList =
                gm.selectOrganizationMember(organizationNo);
        // -----------------------------------------------------
        // 1. 기존 구성원 중 모달에서 빠진 사람 삭제
        // -----------------------------------------------------
        for (OrganizationMemberDomain currentMember : currentMemberList) {

            String currentUserNo =
                    currentMember.getUserNo();

            boolean exists = false;

            if (newUserNoList != null) {

                for (String newUserNo : newUserNoList) {

                    if (currentUserNo.equals(newUserNo)) {
                        exists = true;
                        break;
                    }
                }
            }
            // DB에는 있는데 모달 최종 목록에는 없다
            // → X를 눌러 제거된 구성원
            if (!exists) {
                gm.deleteOrganizationMember(
                        organizationNo,
                        currentUserNo
                );
            }
        }
        // -----------------------------------------------------
        // 2. 모달에서 새로 추가된 구성원 추가
        // -----------------------------------------------------
        if (newUserNoList != null) {
            for (String newUserNo : newUserNoList) {
                boolean exists = false;
                for (OrganizationMemberDomain currentMember : currentMemberList) {
                    if (newUserNo.equals(
                            currentMember.getUserNo())) {
                        exists = true;
                        break;
                    }
                }
                // DB에 없던 사람
                // → 주소록에서 새로 추가된 구성원
                if (!exists) {
                    gm.insertOrganizationMemberForEdit(
                            organizationNo,
                            newUserNo
                    );
                }
            }
        }

        return true;
    }
}