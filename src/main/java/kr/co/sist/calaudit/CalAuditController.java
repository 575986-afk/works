package kr.co.sist.calaudit;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/adminUser/audit")
public class CalAuditController {

	// 캘린더 로그 페이지
	@GetMapping("/calAudit")
    public String showCalLogPage(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "task", required = false) String task,
            @RequestParam(value = "userName", required = false) String userName,
            HttpSession session, Model model) {

        // 기본 검색 기간 설정
        if (startDate == null || startDate.isEmpty()) {
            startDate = "2026. 01. 19";
        }
        if (endDate == null || endDate.isEmpty()) {
            endDate = "2026. 07. 17";
        }

        // 임시 데이터
        List<Map<String, String>> logList = new ArrayList<>();

        Map<String, String> log1 = new HashMap<>();
        log1.put("logNo", "1");
        log1.put("title", "1234");
        log1.put("task", "캘린더 등록/수정");
        log1.put("userName", "1234");
        log1.put("userEmail", "test@practice-6.by-works.net");
        log1.put("date", "2026-07-16");
        log1.put("time", "T16:13:36+09:00");
        log1.put("calId", "c_300302240_72a62600-4eb0-4c69-bda6-4107008420b5");
        logList.add(log1);

        Map<String, String> log2 = new HashMap<>();
        log2.put("logNo", "2");
        log2.put("title", "12");
        log2.put("task", "캘린더 등록/수정");
        log2.put("userName", "12");
        log2.put("userEmail", "test1@practice-6.by-works.net");
        log2.put("date", "2026-07-16");
        log2.put("time", "T16:07:02+09:00");
        log2.put("calId", "c_300302240_2edbca6f-e153-4dce-a98e-264a7848530");
        logList.add(log2);

        Map<String, String> log3 = new HashMap<>();
        log3.put("logNo", "3");
        log3.put("title", "연습");
        log3.put("task", "캘린더 등록/수정");
        log3.put("userName", "홍길동");
        log3.put("userEmail", "test22@practice-6.by-works.net");
        log3.put("date", "2026-07-16");
        log3.put("time", "T15:47:02+09:00");
        log3.put("calId", "c_0_d0e3d3ce-be72-47d2-a9e6-d7e5aab44944");
        logList.add(log3);

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("title", title);
        model.addAttribute("task", task);
        model.addAttribute("userName", userName);
        model.addAttribute("logList", logList);

        return "adminUser/audit/calAudit";
    }
    
    //캘린더 로그 상세 조회
	@GetMapping("/calAudit/calLogDetail")
    public String findCalLogDetail(@RequestParam("logNo") String logNo, Model model) {
        Map<String, String> logDetail = new HashMap<>();
        logDetail.put("logNo", logNo);
        logDetail.put("title", "1232131");
        logDetail.put("startDate", "2026-07-21T14:00:00+09:00");
        logDetail.put("endDate", "2026-07-21T14:30:00+09:00");

        model.addAttribute("logDetail", logDetail);

        return "adminUser/audit/calLogDetail";
    }
    
    //캘린더 로그 다운로드 - 파일 이름 캘린더_날짜로 고정
    //파라미터 받아서 response로 
	@GetMapping("/downloadCalLog")
    public String downloadCalLog(
            @RequestParam(value = "isProcess", required = false, defaultValue = "false") boolean isProcess,
            @RequestParam(value = "fileName", required = false) String fileName,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "task", required = false) String task,
            @RequestParam(value = "userName", required = false) String userName,
            Model model,
            HttpServletResponse response) throws IOException {

        // 확인 버튼 클릭
        if (isProcess) {
            if (fileName == null || fileName.trim().isEmpty()) {
                String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
                fileName = "calendar_" + nowStr;
            }
            if (!fileName.endsWith(".csv")) fileName += ".csv";

            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");

            PrintWriter writer = response.getWriter();
            writer.write("\uFEFF");
            writer.write("제목,과업,사용자,날짜,캘린더 ID\n");
            writer.write("1234,캘린더 등록/수정,1234(test1@practice-6.by-works.net),2026-07-16 T16:13:36+09:00,c_300302240_72a62600-4eb0-4c69-bda6-4107008420b5\n");
            writer.flush();
            writer.close();

            return null;
        }

        // 다운로드 버튼 클릭
        String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
        
        model.addAttribute("defaultFileName", "calendar_" + nowStr);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("title", title);
        model.addAttribute("task", task);
        model.addAttribute("userName", userName);

        return "adminUser/audit/downloadForm";
    }
}
