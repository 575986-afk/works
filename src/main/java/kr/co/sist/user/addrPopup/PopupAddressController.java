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

	@GetMapping("popupAddr")
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
	    
	    // 받아온 userNos 배열 확인
	    System.out.println("전달받은 사원 번호들: " + userNos);
	    
	    // DB 저장 로직 수행...
	    
	    return ResponseEntity.ok("success");
	}
	
	@GetMapping("/address/search")
	@ResponseBody
	public List<UserDomain> searchContacts(String keyword) {
	    return pas.searchContactsByKeyword(keyword); 
	}
	
	

}
