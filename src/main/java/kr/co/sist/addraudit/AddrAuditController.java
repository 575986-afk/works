package kr.co.sist.addraudit;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
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
public class AddrAuditController {

	// 주소록 로그 페이지
	@GetMapping("/addrAudit")
    public String showAddrLogPage(
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "target", required = false) String target,
            @RequestParam(name = "targetEmail", required = false) String targetEmail,
            @RequestParam(name = "task", required = false) String task,
            @RequestParam(name = "userName", required = false) String userName,
            HttpSession session, 
            Model model) {

        if (startDate == null || startDate.trim().isEmpty()) {
            startDate = LocalDate.now().minusDays(7).format(DateTimeFormatter.ofPattern("yyyy. MM. dd"));
        }
        if (endDate == null || endDate.trim().isEmpty()) {
            endDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy. MM. dd"));
        }

        List<Map<String, String>> logList = new ArrayList<>();

        Map<String, String> log1 = new HashMap<>();
        log1.put("target", "홍길동");
        log1.put("targetEmail", "hong@example.com");
        log1.put("task", "주소록 등록/수정");
        log1.put("userName", "12");
        log1.put("userEmail", "test1@practice-6.by-works.net");
        log1.put("date", "2026-07-16");
        log1.put("time", "T16:13:36+09:00");
        logList.add(log1);

        Map<String, String> log2 = new HashMap<>();
        log2.put("target", "김철수");
        log2.put("targetEmail", "chulsoo@example.com");
        log2.put("task", "주소록 삭제");
        log2.put("userName", "1234");
        log2.put("userEmail", "test3@practice-6.by-works.net");
        log2.put("date", "2026-07-16");
        log2.put("time", "T16:07:02+09:00");
        logList.add(log2);

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("target", target);
        model.addAttribute("targetEmail", targetEmail);
        model.addAttribute("task", task);
        model.addAttribute("userName", userName);
        model.addAttribute("logList", logList);

        return "adminUser/audit/addrAudit";
    }
    
    //주소록 로그 다운로드 - 파일 이름 주소록_날짜로 고정
    //파라미터 받아서 response로 
	@GetMapping("/downloadAddrLog")
    public String downloadAddrLog(
            @RequestParam(value = "isProcess", required = false, defaultValue = "false") boolean isProcess,
            @RequestParam(value = "fileName", required = false) String fileName,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "target", required = false) String target,
            @RequestParam(name = "targetEmail", required = false) String targetEmail,
            @RequestParam(name = "task", required = false) String task,
            @RequestParam(name = "userName", required = false) String userName,
            Model model,
            HttpServletResponse response) {

        // CSV 파일 다운로드
        if (isProcess) {
            try {
                if (fileName == null || fileName.trim().isEmpty()) {
                    String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
                    fileName = "addressbook_" + nowStr;
                }
                if (!fileName.endsWith(".csv")) fileName += ".csv";

                String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");

                response.setContentType("text/csv; charset=UTF-8");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");

                StringBuilder csvContent = new StringBuilder();
                csvContent.append("\uFEFF");
                csvContent.append("대상,대상 이메일,과업,작업자,작업자 이메일,날짜,시간\n");
                csvContent.append("홍길동,hong@example.com,주소록 등록/수정,12,test1@practice-6.by-works.net,2026-07-16,T16:13:36+09:00\n");
                csvContent.append("김철수,chulsoo@example.com,주소록 삭제,1234,test3@practice-6.by-works.net,2026-07-16,T16:07:02+09:00\n");

                OutputStream os = response.getOutputStream();
                os.write(csvContent.toString().getBytes(StandardCharsets.UTF_8));
                os.flush();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        // 다운로드 버튼 클릭
        String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
        
        model.addAttribute("defaultFileName", "addressbook_" + nowStr);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("target", target);
        model.addAttribute("targetEmail", targetEmail);
        model.addAttribute("task", task);
        model.addAttribute("userName", userName);

        return "adminUser/audit/downloadForm";
    }
}
