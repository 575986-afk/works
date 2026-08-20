package kr.co.sist.title;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/adminUser/member")
public class TitleController {

	@Autowired(required = false)
	private TitleService ts;
	
    // 첫 화면
    @GetMapping("/title")
    public String titleView(HttpSession session, Model model) {
        return "adminUser/member/title";
    }

    // 직책 목록 조회
    @GetMapping("/position/list")
    @ResponseBody
    public List<PositionDomain> findPosition(HttpSession session, Model model) {
    	
    	String companyNo = (String) session.getAttribute("companyNo");
    	
        return ts.getPosition(companyNo);
    }

    // 직책 우선순위 수정
    @PostMapping("/position/priority")
    @ResponseBody
    public String changePositionPriority(@RequestBody List<String> positionNoList, HttpSession session) {
        String companyNo = (String) session.getAttribute("companyNo");
        ts.changePositionPriority(positionNoList, companyNo);
        return "success";
    }

    // 직책명 수정
    @PostMapping("/position/name")
    @ResponseBody
    public String changePositionName(@RequestParam String positionNo, @RequestParam("name") String positionName, HttpSession session) {
		String companyNo = (String) session.getAttribute("companyNo");
		boolean result = ts.changePositionName(positionNo, positionName, companyNo);
		return result ? "success" : "fail";
	}

    // 직책 삭제
    @PostMapping("/position/remove")
    @ResponseBody
    public String removePosition(HttpSession session, @RequestParam String positionNo) {
        String companyNo = (String) session.getAttribute("companyNo");
        return ts.deletePosition(positionNo, companyNo);
    }

    // 직책 추가
    @PostMapping("/position/add")
    @ResponseBody
    public String addPosition(@RequestParam("name") String positionName, HttpSession session) {
        String companyNo = (String) session.getAttribute("companyNo");
        int currentSize = ts.getPosition(companyNo).size();

        PositionDTO pDTO = PositionDTO.builder()
                .positionName(positionName)
                .companyNo(companyNo)
                .priority(currentSize + 1)
                .build();

        boolean result = ts.createPosition(pDTO);
        return result ? "success" : "fail";
    }

    // 직급 목록 조회
    @GetMapping("/rank/list")
    @ResponseBody
    public List<RankDomain> findRank(HttpSession session, Model model) {
    	String companyNo = (String) session.getAttribute("companyNo");
    	
        return ts.getRank(companyNo);
    }

    // 직급 우선순위 수정
    @PostMapping("/rank/priority")
    @ResponseBody
    public String changeRankPriority(@RequestBody List<String> rankNoList, HttpSession session) {
        String companyNo = (String) session.getAttribute("companyNo");
        ts.changeRankPriority(rankNoList, companyNo);
        return "success";
    }

    // 직급명 수정
    @PostMapping("/rank/name")
    @ResponseBody
    public String changeRankName(@RequestParam String rankNo, @RequestParam("name") String rankName, HttpSession session) {
		String companyNo = (String) session.getAttribute("companyNo");
		boolean result = ts.changeRankName(rankNo, rankName, companyNo);
		return result ? "success" : "fail";
	}

    // 직급 삭제
    @PostMapping("/rank/remove")
    @ResponseBody
    public String removeRank(@RequestParam String rankNo, HttpSession session) {
        String companyNo = (String) session.getAttribute("companyNo");
        return ts.deleteRank(rankNo, companyNo);
    }

    // 직급 추가
    @PostMapping("/rank/add")
    @ResponseBody
    public String addRank(@RequestParam("name") String rankName, HttpSession session) {
        String companyNo = (String) session.getAttribute("companyNo");
        int currentSize = ts.getRank(companyNo).size();

        RankDTO rDTO = RankDTO.builder()
                .rankName(rankName)
                .companyNo(companyNo)
                .priority(currentSize + 1)
                .build();

        boolean result = ts.createRank(rDTO);
        return result ? "success" : "fail";
    }
    
    // 현재 상태 저장
    @PostMapping("/position/save")
    @ResponseBody
    public String savePosition(@RequestBody List<TitleSaveDTO> saveList, HttpSession session) {

        String companyNo = (String) session.getAttribute("companyNo");

        return ts.savePosition(saveList, companyNo);
    }
    
    @PostMapping("/rank/save")
    @ResponseBody
    public String saveRank(@RequestBody List<TitleSaveDTO> saveList, HttpSession session) {

        String companyNo = (String) session.getAttribute("companyNo");

        return ts.saveRank(saveList, companyNo);
    }

}