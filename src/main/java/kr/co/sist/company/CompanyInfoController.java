package kr.co.sist.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/adminUser/company")
public class CompanyInfoController {

	@Autowired(required = false)
	private CompanyInfoService cis;
	
    // 화면 열기
    @GetMapping("/info")
    public String findCompanyData(HttpSession session, Model model) {
    	String companyNo = (String) session.getAttribute("companyNo");

    	model.addAttribute("companyData", cis.getCompanyData(companyNo));
    	
        return "adminUser/company/info";
    }

    // 회사 정보 수정
    @PostMapping("/changeData")
	public String changeCompanyData(CompanyDTO company, HttpSession session) {
    	String companyNo = (String) session.getAttribute("companyNo");

    	cis.setCompanyData(company, companyNo);
    	
    	return "redirect:/adminUser/company/info?success=true";
    }
}
