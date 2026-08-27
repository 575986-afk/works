package kr.co.sist.user.addrPopup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

@Controller
public class PopupAddressController {
	
	@Autowired
	private PopupAddressService pas;

	@GetMapping("/popupAddr")
	public String showAddrPopupPage(HttpSession session, Model model, RangeDTO rDTO) {
		rDTO.setCompanyNo((String)session.getAttribute("companyNo"));
		rDTO.setUserNo((String)session.getAttribute("userNo"));
		
		
		model.addAttribute("users", pas.getAddressList(rDTO));
		model.addAttribute("company", pas.getCompany((String)session.getAttribute("companyNo")));
		model.addAttribute("groups", pas.getGroup((String)session.getAttribute("userNo")));
		model.addAttribute("organizations", pas.getOrganization((String)session.getAttribute("userNo")));
		
		return "popup/PopupAddr";
	}
	
	@PostMapping("/api/users/save")
	@ResponseBody
	public ResponseEntity<String> saveSelectedUsers(@RequestBody Map<String, List<String>> requestData) {
	    
	    List<String> userNos = requestData.get("userNos");
	    
	    return ResponseEntity.ok("success");
	}
	
	@GetMapping("/address/search")
	@ResponseBody
	public List<UserDomain> searchContacts(HttpSession session, String keyword) {
	    String companyNo = (String) session.getAttribute("companyNo"); // 세션에서 회사 번호 가져오기
	    return pas.searchContactsByKeyword(keyword, companyNo); // 서비스로 함께 전달
	}
	
	

}
