package kr.co.sist.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/adminUser/service")
public class CompanyCalendarController {
	
	@Autowired(required = false)
	private CompanyCalService ccs;
	
	// 캘린더 목록 조회
	@GetMapping("/calendar")
	public String findCompanyCal(
			@RequestParam(required = false) String year,
			@RequestParam(required = false) String month,
			HttpSession session, Model model) {
		
		String companyNo = (String) session.getAttribute("companyNo");
		
		if (year == null || year.isEmpty()) {
			year = String.valueOf(LocalDate.now().getYear());
		}

		if (month == null || month.isEmpty()) {
			month = String.format("%02d", LocalDate.now().getMonthValue());
		}
		
		SearchCompanyCalDTO search = new SearchCompanyCalDTO();
		search.setCompanyNo(companyNo);
		search.setMonth(month);
		search.setYear(year);
		
		List<CompanyCalDomain> calendarList = ccs.getCompanyCal(search);

		model.addAttribute("calendarList", calendarList);
		return "adminUser/service/calendar";
	}
	
	// 일정 상세 정보 조회
	@GetMapping("/calendar/calDetail")
	public String findCalDetail(
			@RequestParam(value = "calenderNo") String calenderNo,
			Model model) {
		
		CompanyCalDetailDomain calendar = ccs.getCompanyCalDetail(calenderNo);
		model.addAttribute("calendar", calendar);

		return "adminUser/service/calDetail";
	}

	// 캘린더 등록 폼 이동
	@GetMapping("/calendar/addNewCalForm")
	public String addNewCalForm() {
		return "adminUser/service/addNewCalForm";
	}

	// 일정 등록 처리
	@PostMapping("/calendar/addNewCal")
	public String addNewCal(@ModelAttribute CompanyCalDTO companyCalDTO, HttpSession session) {
	    String companyNo = (String) session.getAttribute("companyNo");
	    String userNo = (String) session.getAttribute("userNo");

	    companyCalDTO.setCompanyNo(companyNo);
	    companyCalDTO.setUserNo(userNo);

	    ccs.addCompanyCal(companyCalDTO);
	    
	    return "redirect:/adminUser/service/calendar";
	}

	// 일정 수정 폼 이동
	@GetMapping("/calendar/modifyCalForm")
	public String modifyCalForm(@RequestParam("calenderNo") String calenderNo, Model model) {
		// DB에서 실제 일정 상세 데이터 조회
		CompanyCalDetailDomain calendar = ccs.getCompanyCalDetail(calenderNo);
		
		model.addAttribute("calendar", calendar);
		return "adminUser/service/modifyCalForm";
	}

	// 일정 수정 처리
	@PostMapping("/calendar/modifyCal")
	public String modifyCal(@ModelAttribute CompanyCalDTO companyCalDTO) {
		ccs.modifyCompanyCal(companyCalDTO);
		
		return "redirect:/adminUser/service/calendar";
	}

	// 일정 삭제 처리
	@PostMapping("/calendar/deleteCal")
	public String deleteCal(@RequestParam("calenderNo") String calenderNo) {
		ccs.removeCompanyCal(calenderNo);
		
		return "redirect:/adminUser/service/calendar";
	}
}