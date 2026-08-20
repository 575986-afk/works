package kr.co.sist.group;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GroupService {

    @Autowired(required = false)
    private GroupMapper gm;

    // 그룹 검색
    public List<GroupDomain> getGroup(String companyNo, String keyword) {
        return gm.selectGroup(companyNo, keyword);
    }

    // 그룹 조회
    public List<GroupDomain> getAllGroup(String companyNo) {
        return gm.selectAllGroup(companyNo);
    }

    // 그룹 추가
    @Transactional
    public boolean createGroup(GroupDTO gDTO) {
    	// 1. 그룹 생성 (그룹명 등 기본 정보 등록)
        int cnt1 = gm.insertGroup(gDTO);
        if (cnt1 != 1) {
            return false;
        }

        // 2. 마스터(userNo)가 지정되어 있을 때만 리더 멤버로 추가
        String userNo = gDTO.getUserNo();
        if (userNo != null && !userNo.trim().isEmpty()) {
            int cnt2 = gm.insertGroupMember(
                    gDTO.getGroupNo(),
                    userNo
            );
            return cnt2 == 1;
        }    

        // 마스터 없이 그룹만 생성한 경우 그룹 생성 완료(true) 반환
        return true;
    }

    // 그룹 정보 조회
    public GroupDomain getGroupDetail(
            String groupNo,
            String companyNo) {
        return gm.selectGroupDetail(groupNo, companyNo);
    }

    // 그룹 구성원 조회
    public List<GroupMemberDomain> getGroupMember(String groupNo) {
        return gm.selectGroupMember(groupNo);
    }

    // 그룹 멤버 개별 삭제
    public boolean deleteGroupMember(
            String groupNo,
            String userNo) {
        return gm.deleteGroupMember(groupNo, userNo) == 1;
    }

    // 그룹 마스터 변경
    // 상세보기에서 "그룹 마스터 변경" 버튼을 눌렀을 때 사용
    @Transactional
    public boolean setGroupLeader(String groupNo, String userNo) {
        int cnt1 = gm.updateGroupMemberLeader(groupNo, userNo);
        int cnt2 = gm.updateGroupLeader(groupNo, userNo);
        return cnt1 == 1 && cnt2 == 1;
    }

    // 그룹 삭제
    @Transactional
    public boolean deleteGroup(String groupNo) {
        int cnt1 = gm.deleteGroupMemberAll(groupNo);
        int cnt2 = gm.deleteGroup(groupNo);
        return cnt1 >= 0 && cnt2 == 1;
    }

    // =========================================================
    // 그룹/그룹마스터 수정 ("그룹/마스터 수정" 모달용)
    // GroupSaveDTO를 통해 전달된 항목(그룹명, 그룹설명, 마스터)을
    // 각각, 둘씩, 또는 모두 전달받아 개별/동적 수정 처리
    // =========================================================
    @Transactional
    public boolean modifyGroup(GroupSaveDTO saveDTO) {
        if (saveDTO == null || saveDTO.getGroupNo() == null || saveDTO.getGroupNo().trim().isEmpty()) {
            return false;
        }

        // 1. 그룹명 또는 그룹설명이 전달된 경우 -> 그룹 정보 수정
        if ((saveDTO.getGroupName() != null && !saveDTO.getGroupName().trim().isEmpty())
                || saveDTO.getGroupDescription() != null) {

            // Builder 패턴 사용
            GroupDTO gDTO = GroupDTO.builder()
                    .groupNo(saveDTO.getGroupNo())
                    .groupName(saveDTO.getGroupName())
                    .groupDescription(saveDTO.getGroupDescription())
                    .companyNo(saveDTO.getCompanyNo())
                    .build();

            gm.updateGroup(gDTO);
        }

        // 2. userNo(그룹 마스터)가 전달된 경우 -> 마스터 변경 쿼리 호출
        String leaderNo = saveDTO.getUserNo();
        if (leaderNo != null && !leaderNo.trim().isEmpty()) {

            // GROUPS 테이블의 user_no 변경
            gm.updateGroupLeaderForEdit(saveDTO.getGroupNo(), saveDTO.getCompanyNo(), leaderNo);

            // GROUPSMEMBER 테이블의 is_leader 변경
            gm.updateGroupMemberLeaderForEdit(saveDTO.getGroupNo(), leaderNo);
        }

        return true;
    }


    // =========================================================
    // 그룹 멤버 수정
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
    public boolean saveGroupMember(
            GroupMemberSaveDTO saveDTO) {
        String groupNo = saveDTO.getGroupNo();
        List<String> newUserNoList =
                saveDTO.getUserNoList();
        // 현재 DB에 들어있는 구성원
        List<GroupMemberDomain> currentMemberList =
                gm.selectGroupMember(groupNo);
        // -----------------------------------------------------
        // 1. 기존 구성원 중 모달에서 빠진 사람 삭제
        // -----------------------------------------------------
        for (GroupMemberDomain currentMember : currentMemberList) {

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
                gm.deleteGroupMember(
                        groupNo,
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
                for (GroupMemberDomain currentMember : currentMemberList) {
                    if (newUserNo.equals(
                            currentMember.getUserNo())) {
                        exists = true;
                        break;
                    }
                }
                // DB에 없던 사람
                // → 주소록에서 새로 추가된 구성원
                if (!exists) {
                    gm.insertGroupMemberForEdit(
                            groupNo,
                            newUserNo
                    );
                }
            }
        }

        return true;
    }
}