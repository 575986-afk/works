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
import kr.co.sist.mail.MailService;

@Controller
@RequestMapping("/adminUser/security")
public class AuthorityController {
	
	@Autowired(required = false)
	private AuthorityService as;
	@Autowired(required = false)
	private MailService ms;
	
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
 	public String findRoleMember(@RequestParam(name="roleLevel") String roleLevel,
 								HttpSession session, Model model) {
 		String companyNo=(String)session.getAttribute("companyNo");
 		
 		List<UserDomain> roleMemberList=as.getRoleMember(companyNo, roleLevel);
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
	public String removeRole(@RequestParam(name = "roleName") String roleName, HttpSession session) {
		String companyNo = (String) session.getAttribute("companyNo");

		boolean result = as.deleteRole(companyNo, roleName);

		return result ? "success" : "fail";
	}
	
	// 권한 위임
	@PostMapping("/changeDelegation")
	@ResponseBody
	public String changeDelegation(
	        @RequestParam(name = "selectedUserNo") String selectedUserNo,
	        HttpSession session) {

	    String companyNo = (String) session.getAttribute("companyNo");

	    UserDomain receiver = as.getDelegationReceiver(companyNo, selectedUserNo);
	    if (receiver == null) {
	        return "fail";
	    }

	    session.setAttribute("delegationWaiting", true);
	    session.setAttribute("delegationReceiverUserNo", selectedUserNo);

	    return "success";
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
    public String changeDelegationForm(HttpSession session, Model model) {
        Boolean isWaiting = (Boolean) session.getAttribute("delegationWaiting");
        if (isWaiting == null) {
            isWaiting = false;
        }

        model.addAttribute("isWaiting", isWaiting);

        if (isWaiting) {
            String companyNo = (String) session.getAttribute("companyNo");
            String receiverUserNo = (String) session.getAttribute("delegationReceiverUserNo");

            RoleDomain currentAdmin = as.getCurrentAdmin(companyNo);
            UserDomain receiver = as.getDelegationReceiver(companyNo, receiverUserNo);

            if (currentAdmin != null && receiver != null) {
                model.addAttribute("senderInfo", currentAdmin.getUserName() + " " + currentAdmin.getEmail());
                model.addAttribute("receiverName", receiver.getUserName());
                model.addAttribute("receiverInfo", receiver.getUserName() + " " + receiver.getEmail());
            } else {
                model.addAttribute("isWaiting", false);
            }
        }
        return "adminUser/security/changeDelegation";
    }
    // 권한 위임 다음으로 눌러서 2단계 열고 이메일 보내고 권한 위임
    @PostMapping("/startDelegation")
    @ResponseBody
    public String startDelegation(HttpSession session) {
        String companyNo = (String) session.getAttribute("companyNo");

        String receiverUserNo = (String) session.getAttribute("delegationReceiverUserNo");
        if (companyNo == null || receiverUserNo == null) {
            return "fail";
        }

        UserDomain receiver = as.getDelegationReceiver(companyNo, receiverUserNo);
        if (receiver == null) {
            return "fail";
        }

        // 1. 실제 권한 위임
        boolean delegationResult = as.changeDelegation(receiverUserNo, companyNo);
        if (!delegationResult) {
            return "fail";
        }

        // 2. 권한 위임 성공 후 안내 메일 발송
        boolean mailResult = ms.sendDelegationMail(receiver.getEmail(),receiver.getUserName());

        // 3. 세션 정리
        session.removeAttribute("delegationWaiting");
        session.removeAttribute("delegationReceiverUserNo");

        if (!mailResult) {
            return "mailFail";
        }

        return "success";
    }
    
    @PostMapping("/cancelDelegation")
    @ResponseBody
    public String cancelDelegation(HttpSession session) {

        session.removeAttribute("delegationWaiting");
        session.removeAttribute("delegationReceiverUserNo");

        return "success";
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
	    
	    boolean result = as.addNewUserRole(companyNo, roleName, roleLevel, userNo);
	    return result ? "success" : "fail";
	}

	// 사용자 권한 삭제
	@PostMapping("/removeUserRole")
	@ResponseBody
	public String removeUserRole(@RequestParam(name = "userNo") String userNo, 
			HttpSession session) {
	    String companyNo = (String) session.getAttribute("companyNo");

	    boolean result = as.deleteUserRole(companyNo, userNo);

	    return result ? "success" : "fail";
	}
}
