package kr.co.sist.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.sist.signup.UserDTO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

	private final LoginService ls;

	//로그인 
    @GetMapping("/login")
    public String login(@CookieValue(value="saveId", required=false) String saveId, Model model) {
        model.addAttribute("saveId",saveId);
       
    	
    	return "/works/login/login";
    }
    
    @PostMapping("/loginProcess")
    public String loginProcess(@RequestParam("id") String userId, 
            @RequestParam("password") String password,
            @RequestParam(value = "saveId", required = false) String saveId,
            HttpServletResponse response,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        UserDTO uDTO = ls.setLoginCheck(userId, password);
        
        if(uDTO == null) {
            redirectAttributes.addFlashAttribute("loginMsg", "해당 아이디 또는 비밀번호의 회원정보가 없습니다."); 
            return "redirect:/login";
        } else {
            session.setAttribute("user", uDTO);
            session.setAttribute("userNo", uDTO.getUserNo());
            session.setAttribute("companyNo", uDTO.getCompanyNo());
            session.setAttribute("role_level", uDTO.getRole_level());
            
            redirectAttributes.addFlashAttribute("loginMsg", uDTO.getName() + "님 환영합니다!");
            
            if(saveId != null) {
                Cookie cookie = new Cookie("saveId", userId);
                cookie.setMaxAge(60 * 60 * 24 * 30); // 30일
                cookie.setPath("/");
                response.addCookie(cookie);
            } else {
                Cookie cookie = new Cookie("saveId", "");
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
        
        String redirectURL = (String) session.getAttribute("redirectURL");
        
        if (redirectURL != null) {
            session.removeAttribute("redirectURL");
            return "redirect:" + redirectURL;
        }
        
        return "redirect:/userDashboard";
    }

}