package kr.co.sist.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.company.CompanyDomain;
import kr.co.sist.company.CompanyInfoService;

@Controller
@RequestMapping("/adminUser/member")
public class MemberAddController {

	@Autowired(required = false)
	private MemberService ms;
	
	@Autowired(required = false)
	private CompanyInfoService cis;
	
    // 페이지 조회
    @GetMapping("/member")
    public String memberList(
            @RequestParam(value = "organizationNo", required = false, defaultValue = "ALL")
            String organizationNo,
            HttpSession session,
            Model model) {
        String companyNo = (String) session.getAttribute("companyNo");

        // 회사 정보
        CompanyDomain company = cis.getCompanyData(companyNo);
        model.addAttribute("company", company);
        
        // 조직 목록
        model.addAttribute("organizationList",ms.getOrganizationList(companyNo));

        // 구성원 목록
        model.addAttribute("memberList",ms.getMemberList(companyNo, organizationNo));

        // 회사 구성원 수
        model.addAttribute("memberCount",ms.getCompanyMemberCount(companyNo));

        // 현재 선택된 조직
        model.addAttribute("organizationNo", organizationNo);

        return "adminUser/member/member";
    }
    
    // 구성원 추가 폼
	@GetMapping("/addMemberForm")
    public String addMemberForm(@RequestParam(value = "userNo", required = false) String userNo,
    		HttpSession session,
            Model model) {

        String companyNo = (String) session.getAttribute("companyNo");

        model.addAttribute("rankList",ms.getRankList(companyNo));
        model.addAttribute("positionList",ms.getPositionList(companyNo));
        model.addAttribute("deptList",ms.getOrganizationList(companyNo));
        
        // 수정 모드인 경우 회원 정보 조회
        if (userNo != null && !userNo.isEmpty()) {
            MemberDomain member = ms.getMemberDetail(companyNo, userNo);
            model.addAttribute("member", member);
            model.addAttribute("mode", "modify");
        } else {
            model.addAttribute("mode", "add");
        }

    	return "adminUser/member/addMemberForm :: addMemberForm";
    }
	
	// 아이디 확인
	@ResponseBody
    @GetMapping("/checkId")
	public MemberDomain checkID(@RequestParam("userId") String userId) {
        return ms.getUserById(userId);
    }
    
    // 구성원 추가
	@ResponseBody
	@PostMapping("/addMember")
	public String addMember(
            MemberDTO memberDTO,
            HttpSession session) {
		String companyNo = (String) session.getAttribute("companyNo");
	    
	    memberDTO.setCompanyNo(companyNo);
        ms.addMember(memberDTO);

        return "success";
    }
    
    // 구성원 조회
    @GetMapping("/detail")
	public String findMemberDetail(@RequestParam("userNo") String userNo,
            HttpSession session,
            Model model) {
        String companyNo = (String) session.getAttribute("companyNo");
        
        MemberDomain member = ms.getMemberDetail(companyNo, userNo);
        model.addAttribute("member", member);

        return "adminUser/member/memberDetail";
    }

    // 구성원 수정
    @ResponseBody
    @PostMapping("/modifyMember")
    public String modifyMember(
            MemberDTO memberDTO,
            HttpSession session) {
        String companyNo = (String) session.getAttribute("companyNo");

        memberDTO.setCompanyNo(companyNo);
        ms.modifyMember(memberDTO);

        return "success";
    }

    // 구성원 삭제
    @PostMapping("/deleteMember")
    public String deleteMember(
            @RequestParam("userNo") String userNo,
            HttpSession session) {
        String companyNo = (String) session.getAttribute("companyNo");
        ms.deleteMember(userNo, companyNo);

        return "redirect:/adminUser/member/member";
    }
}
