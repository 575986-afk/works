package kr.co.sist.loginaudit;

import java.io.IOException;
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
public class LoginAuditController {

	// 로그인 로그 페이지
    @GetMapping("/loginAudit")
    public String showLoginLogPage(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "ip", required = false, defaultValue = "") String ip,
            @RequestParam(value = "userName", required = false, defaultValue = "") String userName,
            HttpSession session, 
            Model model) {

        if (startDate == null || startDate.trim().isEmpty()) {
            startDate = LocalDate.now().minusDays(7).format(DateTimeFormatter.ofPattern("yyyy. MM. dd"));
        }
        if (endDate == null || endDate.trim().isEmpty()) {
            endDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy. MM. dd"));
        }

        // 임시 데이터
        List<Map<String, String>> logList = new ArrayList<>();

        Map<String, String> log1 = new HashMap<>();
        log1.put("description", "새로운 브라우저에서의 로그인");
        log1.put("userName", "홍길동");
        log1.put("userEmail", "test1@practice-6.by-works.net");
        log1.put("date", "2026-07-17");
        log1.put("time", "T16:53:51+09:00");
        log1.put("ip", "121.133.55.252");

        Map<String, String> log2 = new HashMap<>();
        log2.put("description", "로그인 성공");
        log2.put("userName", "홍길동");
        log2.put("userEmail", "test1@practice-6.by-works.net");
        log2.put("date", "2026-07-17");
        log2.put("time", "T16:53:50+09:00");
        log2.put("ip", "121.133.55.252");

        Map<String, String> log3 = new HashMap<>();
        log3.put("description", "새로운 브라우저에서의 로그인");
        log3.put("userName", "김철수");
        log3.put("userEmail", "test2@practice-6.by-works.net");
        log3.put("date", "2026-07-16");
        log3.put("time", "T16:13:37+09:00");
        log3.put("ip", "1.234.165.103");

        logList.add(log1);
        logList.add(log2);
        logList.add(log3);

        model.addAttribute("logList", logList);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("userName", userName);
        model.addAttribute("ip", ip);

        return "adminUser/audit/loginAudit";
    }
    
    //접속 주소(IP) 로그 다운로드 - 파일 이름 할일_날짜로 고정
    //파라미터 받아서 response로 
    @GetMapping("/downloadLoginLog")
    public String downloadLoginLog(
            @RequestParam(value = "isProcess", required = false, defaultValue = "false") boolean isProcess,
            @RequestParam(value = "fileName", required = false) String fileName,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "userName", required = false) String userName,
            @RequestParam(name = "userEmail", required = false) String userEmail,
            @RequestParam(name = "ip", required = false) String ip,
            Model model,
            HttpServletResponse response) {

        if (isProcess) {
            try {
                if (fileName == null || fileName.trim().isEmpty()) {
                    String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
                    fileName = "login_" + nowStr;
                }
                if (!fileName.endsWith(".csv")) fileName += ".csv";

                String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");

                response.setContentType("text/csv; charset=UTF-8");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");

                StringBuilder csvContent = new StringBuilder();
                csvContent.append("\uFEFF");
                csvContent.append("설명,사용자 이름,이메일,일시,IP 주소\n");
                csvContent.append("새로운 브라우저에서의 로그인,홍길동,test1@practice-6.by-works.net,2026-07-17 T16:53:51+09:00,121.133.55.252\n");
                csvContent.append("로그인 성공,홍길동,test1@practice-6.by-works.net,2026-07-17 T16:53:50+09:00,121.133.55.252\n");
                csvContent.append("새로운 브라우저에서의 로그인,김철수,test2@practice-6.by-works.net,2026-07-16 T16:13:37+09:00,1.234.165.103\n");

                OutputStream os = response.getOutputStream();
                os.write(csvContent.toString().getBytes(StandardCharsets.UTF_8));
                os.flush();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
        
        model.addAttribute("defaultFileName", "login_" + nowStr);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("userName", userName);
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("ip", ip);

        return "adminUser/audit/downloadForm";
    }
}
