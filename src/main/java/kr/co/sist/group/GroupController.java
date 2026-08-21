package kr.co.sist.group;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/adminUser/member")
public class GroupController {

    @Autowired(required = false)
    private GroupService gs;


    // 그룹 조회
    @GetMapping("/group")
    public String showAllGroup(Model model, HttpSession session) {

        String companyNo = (String) session.getAttribute("companyNo");
        List<GroupDomain> groupList = gs.getAllGroup(companyNo);

        model.addAttribute("groupList", groupList);

        return "adminUser/member/group";
    }


    // 그룹 검색
    @GetMapping("/searchGroup")
    public String findGroup(
            @RequestParam(
                    value = "keyword",
                    required = false,
                    defaultValue = "") String keyword,
            Model model,
            HttpSession session) {

        String companyNo = (String) session.getAttribute("companyNo");

        List<GroupDomain> groupList;

        if (keyword.trim().isEmpty()) {
            groupList = gs.getAllGroup(companyNo);
        } else {
            groupList = gs.getGroup(companyNo, keyword);
        }

        model.addAttribute("groupList", groupList);
        model.addAttribute("keyword", keyword);

        return "adminUser/member/group";
    }


    // 그룹 추가/수정 폼
    @GetMapping("/addGroupForm")
    public String addGroupForm(
            @RequestParam(value = "groupNo", required = false) String groupNo,
            Model model,
            HttpSession session) {
        if (groupNo != null && !groupNo.trim().isEmpty()) {
            String companyNo = (String) session.getAttribute("companyNo");
            GroupDomain groupDetail = gs.getGroupDetail(groupNo, companyNo);
            model.addAttribute("group", groupDetail);
        }
        return "adminUser/member/addGroupForm :: addGrpForm";
    }


    // 그룹 추가
    @PostMapping("/addGroup")
    @ResponseBody
    public String addGroup(
            @RequestParam("groupName") String groupName,
            @RequestParam(value = "groupDescription", required = false) String groupDescription,
            @RequestParam(value = "userNo", required = false) String userNo,
            HttpSession session) {

        String companyNo = (String) session.getAttribute("companyNo");

        GroupDTO gDTO = GroupDTO.builder()
                .groupName(groupName)
                .groupDescription(groupDescription)
                .companyNo(companyNo)
                .userNo(userNo)
                .build();

        boolean result = gs.createGroup(gDTO);

        return result ? "success" : "fail";
    }


    // 그룹 수정
    // 그룹/마스터 수정 모달에서 사용
    // 그룹명 + 그룹설명 + 그룹마스터를 한 번에 수정
    @PostMapping("/modifyGroup")
    @ResponseBody
    public boolean modifyGroup(GroupSaveDTO saveDTO, HttpSession session) {
        String companyNo = (String) session.getAttribute("companyNo");
        saveDTO.setCompanyNo(companyNo);

        return gs.modifyGroup(saveDTO);
    }


    // 그룹 정보 상세 조회
    @GetMapping("/groupdetail")
    public String findGroupDetail(
            @RequestParam("groupNo") String groupNo,
            Model model,
            HttpSession session) {

        String companyNo = (String) session.getAttribute("companyNo");

        GroupDomain groupDetail = gs.getGroupDetail(groupNo, companyNo);
        List<GroupMemberDomain> memberList = gs.getGroupMember(groupNo);

        model.addAttribute("groupDetail", groupDetail);
        model.addAttribute("memberList", memberList);

        return "adminUser/member/groupDetail";
    }


    // 그룹 멤버 개별 삭제
    // 상세보기에서 구성원의 ... → 삭제를 눌렀을 때 사용
    // ※ 멤버 수정 모달의 X 삭제와는 별개
    @PostMapping("/deleteGroupMember")
    @ResponseBody
    public String deleteGroupMember(
            @RequestParam("groupNo") String groupNo,
            @RequestParam("userNo") String userNo) {

        boolean result = gs.deleteGroupMember(groupNo, userNo);

        return result ? "success" : "fail";
    }


    // 그룹 마스터 변경
    // 상세보기의 "그룹 마스터 변경" 모달에서 사용
    @PostMapping("/changeGroupLeader")
    @ResponseBody
    public String setGroupLeader(
            @RequestParam("groupNo") String groupNo,
            @RequestParam("userNo") String userNo) {

        boolean result = gs.setGroupLeader(groupNo, userNo);

        return result ? "success" : "fail";
    }


    // 그룹 삭제 (단건/다중 통합)
    @PostMapping("/deleteGroup")
    @ResponseBody
    public String deleteGroup(
            @RequestParam(value = "groupNo", required = false) List<String> groupNos) {

        if (groupNos == null || groupNos.isEmpty()) {
            return "fail";
        }

        boolean result = gs.deleteGroup(groupNos);

        return result ? "success" : "fail";
    }


    // 그룹 멤버 수정
    // 멤버 수정 모달에서 저장 버튼을 눌렀을 때 사용
    // groupNo       → 현재 수정 중인 그룹
    // userNo[]      → 저장 시점에 모달에 남아 있는 최종 멤버들
    // Service에서 DB의 기존 멤버와 비교하여
    // 기존 DB에 있고 최종 목록에 없음
    //      → 삭제
    // 기존 DB에 없고 최종 목록에 있음
    //      → 추가
    // 기존 DB에도 있고 최종 목록에도 있음
    //      → 유지
    @PostMapping("/changeGroupMember")
    @ResponseBody
    public String changeGroupMember(
            @RequestParam("groupNo") String groupNo,
            @RequestParam(
                    value = "userNo",
                    required = false) String[] userNo) {

        GroupMemberSaveDTO saveDTO = new GroupMemberSaveDTO();
        saveDTO.setGroupNo(groupNo);

        if (userNo != null) {
            saveDTO.setUserNoList(List.of(userNo));
        } else {
            saveDTO.setUserNoList(List.of());
        }

        boolean result = gs.saveGroupMember(saveDTO);

        return result ? "success" : "fail";
    }

}