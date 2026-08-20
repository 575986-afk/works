package kr.co.sist.signup;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller 
@RequiredArgsConstructor
public class SignupController {
	
	private final SignupService ss;
	
	// 가입유형 선택 화면
	@GetMapping("/signupChk")
	public String signupChk() {
		return "works/login/signupChk"; 
	}
	// 사용자 회원가입
	@GetMapping("/userJoinForm")
	public String userJoinForm(Model model) {
		model.addAttribute("memberForm",new UserDTO());
		model.addAttribute("formAction","/userJoin");
		return "works/login/joinForm"; 
	}

    // 관리자 회원가입
    @GetMapping("/managerJoinForm")
    public String managerJoinForm(Model model) {
    	model.addAttribute("memberForm",new UserDTO());
    	model.addAttribute("formAction","/managerJoin");
        return "works/login/joinForm";
    }
    
    
    
    
    @PostMapping("/idDup")
    @ResponseBody
    public int idDup(String id) {
    	return ss.idDup(id);
    }
    
    @PostMapping("/userJoin")
    public String userjoin(UserDTO uDTO, HttpServletRequest request,RedirectAttributes redirectAttributes) {
    	uDTO.setUserType("USER");
    	uDTO.setIp(request.getRemoteAddr());
    	int result=ss.insertUserJoin(uDTO);
    	
    	if(result>0) {
    		redirectAttributes.addFlashAttribute(
    	            "signupMsg", "회원가입이 성공하였습니다!"
    	        );
    	}else {
    		redirectAttributes.addFlashAttribute(
    	            "signupMsg", "회원가입에 실패하였습니다."
    	        );
    	}
    	
    	return "redirect:/login";
    	
    }
    
    @PostMapping("/managerJoin")
    public String managerJoin(UserDTO uDTO,HttpServletRequest request,RedirectAttributes redirectAttributes) {
    	uDTO.setUserType("MANAGER");
    	uDTO.setIp(request.getRemoteAddr());
    	int result=ss.insertManagerJoin(uDTO);
    	
    	if(result>0) {
    		redirectAttributes.addFlashAttribute(
    	            "signupMsg", "회원가입이 성공하였습니다!"
    	        );
    	}else {
    		redirectAttributes.addFlashAttribute(
    	            "signupMsg", "회원가입에 실패하였습니다."
    	        );
    	}
    	return "redirect:/login";
    	
    }
}
