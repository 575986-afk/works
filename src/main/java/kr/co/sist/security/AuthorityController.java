package kr.co.sist.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/adminUser/security")
public class AuthorityController {
	
	@Autowired(required = false)
	private AuthorityService as;
	
	// 화면 조회
	@GetMapping("/authority")
	public String showPage(HttpSession session, Model model) {
	    return "adminUser/security/authority";
	}
	
	//역할 조회 (운영관리자, 최고관리자)
	@GetMapping("/findRole")
	public String findRole(HttpSession session, Model model) {
		String companyNo = (String) session.getAttribute("companyNo");
		List<RoleDomain> roleList = as.getRole(companyNo);
        model.addAttribute("roleList", roleList);
        
        return "adminUser/security/roleFragment";
    }
	
	//권한 조회
	@GetMapping("/findAuthority")
	public String findAuthority(@RequestParam(name="roleName", required=false) String roleName, Model model) {
	    return "adminUser/security/authorityFragment";
	}
	
    // 권한 구성원 조회
 	@GetMapping("/findRoleMember")
 	public String findRoleMember(@RequestParam(name="roleName") String roleName,
 								HttpSession session, Model model) {
 		String companyNo=(String)session.getAttribute("companyNo");
 		
 		List<UserDomain> roleMemberList=as.getRoleMember(companyNo, roleName);
 		model.addAttribute("roleMemberList", roleMemberList);
 		
 		return "adminUser/security/memberFragment";
 	}
	
	// 권한 추가
 	@PostMapping("/addRole")
	@ResponseBody
	public String addRole(@RequestParam(name="roleName") String roleName, HttpSession session) {
 		String companyNo = (String) session.getAttribute("companyNo");

		RoleDTO rDTO = RoleDTO.builder()
				.roleName(roleName)
				.companyNo(companyNo)
				.roleLevel(50)
				.build();

		boolean result = as.createRole(rDTO);
        
		return result ? "success" : "fail";
	}
	
	//권한 추가 폼
	@GetMapping("/addRoleForm")
    public String addRoleForm(Model model) {
        return "adminUser/security/addRoleForm";
    }

	// 권한명 수정
	@PostMapping("/changeRoleName")
	@ResponseBody
	public String changeRoleName(@RequestParam(name="roleNo") String roleNo, 
								 @RequestParam(name="roleName") String roleName,
								 HttpSession session) {
		String companyNo = (String) session.getAttribute("companyNo");

		RoleDTO rDTO = RoleDTO.builder()
				.roleNo(roleNo)
				.roleName(roleName)
				.companyNo(companyNo)
				.build();

		boolean result = as.changeRoleName(rDTO);

		return result ? "success" : "fail";
	}

	// 권한명 수정 폼
	@GetMapping("/changeRoleNameForm")
	public String changeRoleNameForm(@RequestParam(name="roleNo") String roleNo, Model model) {
		model.addAttribute("roleNo", roleNo);
		return "adminUser/security/changeRoleNameForm";
	}

	// 권한 삭제
	@PostMapping("/removeRole")
	@ResponseBody
	public String removeRole(@RequestParam(name="roleNo") String roleNo, HttpSession session) {
		String companyNo = (String) session.getAttribute("companyNo");

		boolean result = as.deleteRole(companyNo, roleNo);

		return result ? "success" : "fail";
	}
	
	// 권한 위임
	@PostMapping("/changeDelegation")
	@ResponseBody
	public String changeDelegation(@RequestParam(name="selectedUserNo") String selectedUserNo
								, HttpSession session) {
		String companyNo = (String) session.getAttribute("companyNo");

		boolean result = as.changeDelegation(companyNo, selectedUserNo);

		return result ? "success" : "fail";
	}
	
	// 권한 위임 시 검색
	@GetMapping("/searchDelegationMember")
	public String searchDelegationMember(
	        @RequestParam(name = "keyword") String keyword,
	        HttpSession session,
	        Model model) {
	    String companyNo = (String) session.getAttribute("companyNo");

	    List<UserDomain> memberList =
	            as.searchDelegationMember(companyNo, keyword);

	    model.addAttribute("memberList", memberList);

	    return "adminUser/security/delegationMemberFragment";
	}
	
	//권한 위임 폼
    @GetMapping("/changeDelegationForm")
    public String changeDelegationForm(Model model) {
        
        // DB에서 현재 보낸 위임 요청이 있는지 확인 (있으면 true, 없으면 false)
        boolean isWaiting = false; // 테스트용 (DB 연동 시 조회 결과에 따라 true/false)

        if (isWaiting) {
            // 위임 요청이 존재하면 2단계(대기 화면)용 데이터 전달
            model.addAttribute("isWaiting", true);
            model.addAttribute("senderInfo", "홍길동 testtest@practice-6.by-works.com");
            model.addAttribute("receiverName", "김철수");
            model.addAttribute("receiverInfo", "김철수 test1@practice-6.by-works.net");
            model.addAttribute("deadlineDate", "2026년 8월 27일까지 수락 필요");
        } else {
            // 위임 요청이 없으면 1단계(처음 화면) 표시
            model.addAttribute("isWaiting", false);
        }

        return "adminUser/security/changeDelegation";
    }
	
	// 사용자 권한 추가
	@PostMapping("/addNewUserRole")
	@ResponseBody
	public String addNewUserRole(HttpSession session,
	        @RequestParam(name = "roleName") String roleName,
	        @RequestParam(name = "roleLevel") int roleLevel,
	        @RequestParam(name = "userNo") String userNo) {

	    String companyNo = (String) session.getAttribute("companyNo");

	    System.out.println(">>> addNewUserRole - companyNo: " + companyNo 
	                       + ", roleName: " + roleName 
	                       + ", roleLevel: " + roleLevel 
	                       + ", userNo: " + userNo);
	    // DB에 Insert
	    boolean result = as.addNewUserRole(companyNo, roleName, roleLevel, userNo);
	    return result ? "success" : "fail";
	}

	// 사용자 권한 삭제
    @PostMapping("/removeUserRole")
	@ResponseBody
	public String removeUserRole(@RequestParam(name = "roleNo") String roleNo,
			@RequestParam(name = "userNo") String userNo, HttpSession session) {
		String companyNo = (String) session.getAttribute("companyNo");

		boolean result =
				as.deleteUserRole(
						companyNo,
						roleNo,
						userNo);

		return result ? "success" : "fail";
	}
}
