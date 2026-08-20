package kr.co.sist.organization;

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
@RequestMapping("/adminUser/member")
public class OrganizationController {
	
	@Autowired(required = false)
	private OrganizationService os;
	
    // 조직 조회
    @GetMapping("/organization")
    public String showAllOrganization(Model model, HttpSession session) {
        
    	String companyNo =
                (String) session.getAttribute("companyNo");

        List<OrganizationDomain> organizationList =
        		os.getAllOrganization(companyNo);

        model.addAttribute("organizationList", organizationList);
        
        return "adminUser/member/organization";
    }
    
    // 조직 검색
    @GetMapping("/searchOrganization")
    public String findOrganization(@RequestParam(
		            value = "keyword",
		            required = false,
		            defaultValue = "") String keyword,
		    Model model,
		    HttpSession session) {
		
		String companyNo =
		        (String) session.getAttribute("companyNo");
		
		List<OrganizationDomain> organizationList;
		
		if (keyword.trim().isEmpty()) {
		
		    organizationList =
		    		os.getAllOrganization(companyNo);
		
		} else {
		
		    organizationList =
		    		os.getOrganization(companyNo, keyword);
		}
		
		model.addAttribute("organizationList", organizationList);
		model.addAttribute("keyword", keyword);
		
		return "adminUser/member/organization";
    }
    
    // 조직 추가/수정 폼
    @GetMapping("/addOrganizationForm")
    public String addOrganizationForm(@RequestParam(value = "organizationNo", required = false) String organizationNo,
            Model model,
            HttpSession session) {
        if (organizationNo != null && !organizationNo.trim().isEmpty()) {
            String companyNo = (String) session.getAttribute("companyNo");
            OrganizationDomain organizationDetail = os.getOrganizationDetail(organizationNo, companyNo);
            model.addAttribute("organization", organizationDetail);
        }
        return "adminUser/member/addOrganizationForm :: addOrgForm";
    }
    
    // 조직 추가
    @PostMapping("/addOrganization")
    @ResponseBody
    public String addOrganization(
            @RequestParam("organizationName") String organizationName,
            @RequestParam(value = "organizationDescription", required = false) String organizationDescription,
            @RequestParam(value = "organizationLeaderNo", required = false) String organizationLeaderNo,
            HttpSession session) {

        String companyNo =
                (String) session.getAttribute("companyNo");

        OrganizationDTO gDTO = OrganizationDTO.builder()
                .organizationName(organizationName)
                .organizationDescription(organizationDescription)
                .companyNo(companyNo)
                .userNo(organizationLeaderNo)
                .build();

        boolean result =
        		os.createOrganization(gDTO);

        return result ? "success" : "fail";
    }
    
    // 조직 수정
    @PostMapping("/modifyOrganization")
    @ResponseBody
    public boolean modifyOrganization(OrganizationSaveDTO saveDTO, HttpSession session) {
        String companyNo = (String) session.getAttribute("companyNo");
        saveDTO.setCompanyNo(companyNo);

        return os.modifyOrganization(saveDTO);
    }
    
    // 조직 상세 조회
    @GetMapping("/organizationdetail")
    public String findOrganizationDetail(
            @RequestParam("organizationNo") String organizationNo,
            Model model,
            HttpSession session) {

        String companyNo =
                (String) session.getAttribute("companyNo");

        OrganizationDomain organizationDetail =
        		os.getOrganizationDetail(organizationNo, companyNo);

        List<OrganizationMemberDomain> memberList =
        		os.getOrganizationMember(organizationNo);

        model.addAttribute("organizationDetail", organizationDetail);
        model.addAttribute("memberList", memberList);

        return "adminUser/member/organizationDetail";
    }
    
    // 조직 멤버 개별 삭제
    // 상세보기에서 구성원의 ... → 삭제를 눌렀을 때 사용
    // ※ 멤버 수정 모달의 X 삭제와는 별개
    @PostMapping("/deleteOrganizationMember")
    @ResponseBody
    public String deleteOrganizationMember(
            @RequestParam("organizationNo") String organizationNo,
            @RequestParam("userNo") String userNo) {

        boolean result =
        		os.deleteOrganizationMember(organizationNo, userNo);

        return result ? "success" : "fail";
    }
    
    // 조직 삭제
    @PostMapping("/deleteOrganization")
    @ResponseBody
    public String deleteOrganization(
            @RequestParam("organizationNo") String organizationNo) {

        boolean result =
        		os.deleteOrganization(organizationNo);

        return result ? "success" : "fail";
    }
    
    // 조직 멤버 수정
    // 멤버 수정 모달에서 저장 버튼을 눌렀을 때 사용
    // organizationNo       → 현재 수정 중인 조직
    // userNo[]      → 저장 시점에 모달에 남아 있는 최종 멤버들
    // Service에서 DB의 기존 멤버와 비교하여
    // 기존 DB에 있고 최종 목록에 없음
    //      → 삭제
    // 기존 DB에 없고 최종 목록에 있음
    //      → 추가
    // 기존 DB에도 있고 최종 목록에도 있음
    //      → 유지
    @PostMapping("/changeOrganizationMember")
    @ResponseBody
    public String changeOrganizationMember(
            @RequestParam("organizationNo") String organizationNo,
            @RequestParam(
                    value = "userNo",
                    required = false) String[] userNo) {

        OrganizationMemberSaveDTO saveDTO =
                new OrganizationMemberSaveDTO();

        saveDTO.setOrganizationNo(organizationNo);

        if (userNo != null) {

            saveDTO.setUserNoList(
                    List.of(userNo)
            );

        } else {

            saveDTO.setUserNoList(
                    List.of()
            );
        }

        boolean result =
        		os.saveOrganizationMember(saveDTO);

        return result ? "success" : "fail";
    }
    
    // 조직장 변경
    // 조직 마스터 변경
    // 상세보기의 "조직 마스터 변경" 모달에서 사용
    @PostMapping("/changeOrganizationLeader")
    @ResponseBody
    public String setOrganizationLeader(
            @RequestParam("organizationNo") String organizationNo,
            @RequestParam("userNo") String userNo) {

        boolean result =
        		os.setOrganizationLeader(organizationNo, userNo);

        return result ? "success" : "fail";
    }
}