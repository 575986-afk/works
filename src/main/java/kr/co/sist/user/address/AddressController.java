package kr.co.sist.user.address;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

@Controller
public class AddressController {
	
	@Autowired(required = false)
	private AddressService as;

	@GetMapping("addressBook")
    public String showAddressPage(HttpSession session, RangeDTO rDTO, Model model) {
        rDTO.setUserNo((String)session.getAttribute("userNo"));
        rDTO.setCompanyNo((String)session.getAttribute("companyNo"));
        List<UserDomain> userList = as.getAddressList(rDTO);
        
        model.addAttribute("users", userList);
        model.addAttribute("company", as.getCompany(rDTO.getCompanyNo()));
        model.addAttribute("groups", as.getGroup(rDTO.getUserNo()));
        model.addAttribute("organizations", as.getOrganization(rDTO.getCompanyNo()));
        
        return "user/addressBook";
    }
    
    // 자동완성 검색용 AJAX 매핑 추가
    @GetMapping("/address/main/search")
    @ResponseBody
    public List<UserDomain> searchContacts(String keyword) {
        return as.searchContactsByKeyword(keyword);
    }
    
    @GetMapping("addressDetail")
    public String showAddressDetail(HttpSession session, String userNo) {
        return "user/addressBook";
    }
    
    @ResponseBody
    @PostMapping("/toggleFavorite")
    public String modifyBookmark(HttpSession session, String targetNo, String action) {
                                     
        String userNo = (String)session.getAttribute("userNo");
        
        // DTO에 본인 사번과 대상 사번 세팅 (DTO 이름은 프로젝트에 맞게 수정하세요)
        UserDTO dto = new UserDTO();
        dto.setUserNo(userNo);
        dto.setTargetNo(targetNo);
        
        try {
            if ("add".equals(action)) {
                as.addBookmark(dto); // Service의 insert 쿼리 호출
            } else if ("remove".equals(action)) {
                as.removeBookmark(dto); // Service의 delete 쿼리 호출
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

}
