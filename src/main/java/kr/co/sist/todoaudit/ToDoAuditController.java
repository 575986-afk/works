package kr.co.sist.todoaudit;

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
public class ToDoAuditController {

	// 할일 로그 페이지
	@GetMapping("/todoAudit")
    public String showToDoLogPage(
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "title", required = false) String title,
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

        // 임시 데이터
        List<Map<String, String>> logList = new ArrayList<>();

        Map<String, String> log1 = new HashMap<>();
        log1.put("logNo", "LOG_1001");
        log1.put("title", "요청 할 일 테스트");
        log1.put("task", "할 일 미완료 변경");
        log1.put("userName", "홍길동");
        log1.put("userEmail", "test1@practice-6.by-works.net");
        log1.put("date", "2026-07-16");
        log1.put("time", "T15:51:44+09:00");
        logList.add(log1);

        Map<String, String> log2 = new HashMap<>();
        log2.put("logNo", "LOG_1002");
        log2.put("title", "요청 할 일 테스트");
        log2.put("task", "할 일 완료");
        log2.put("userName", "홍길동");
        log2.put("userEmail", "test1@practice-6.by-works.net");
        log2.put("date", "2026-07-16");
        log2.put("time", "T15:49:50+09:00");
        logList.add(log2);

        Map<String, String> log3 = new HashMap<>();
        log3.put("logNo", "LOG_1003");
        log3.put("title", "요청 할 일 테스트");
        log3.put("task", "할 일 수정");
        log3.put("userName", "홍길동");
        log3.put("userEmail", "test1@practice-6.by-works.net");
        log3.put("date", "2026-07-16");
        log3.put("time", "T15:49:08+09:00");
        logList.add(log3);

        Map<String, String> log4 = new HashMap<>();
        log4.put("logNo", "LOG_1004");
        log4.put("title", "1234");
        log4.put("task", "할 일 삭제");
        log4.put("userName", "홍길동");
        log4.put("userEmail", "test1@practice-6.by-works.net");
        log4.put("date", "2026-07-16");
        log4.put("time", "T15:24:49+09:00");
        logList.add(log4);

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("title", title);
        model.addAttribute("task", task);
        model.addAttribute("userName", userName);
        model.addAttribute("logList", logList);

        return "adminUser/audit/todoAudit";
    }
    
    //할일 로그 상세 조회
	@GetMapping("/todoAudit/todoLogDetail")
    public String findToDoLogDetail(
            @RequestParam(name = "logNo", required = false) String logNo, 
            Model model) {

        // 임시 상세 데이터
        Map<String, String> logDetail = new HashMap<>();
        logDetail.put("logNo", logNo != null ? logNo : "LOG_1001");
        logDetail.put("time", "2026-07-16 T15:51:44+09:00");
        logDetail.put("targetList", "프로젝트 A 할일 리스트");
        logDetail.put("task", "할 일 미완료 변경");
        logDetail.put("userName", "홍길동");
        logDetail.put("userEmail", "test1@practice-6.by-works.net");
        logDetail.put("requester", "김철수");
        logDetail.put("title", "요청 할 일 테스트");
        logDetail.put("content", "요청 할 일 상태를 미완료로 변경함");

        model.addAttribute("logDetail", logDetail);

        return "adminUser/audit/todoLogDetail";
    }
    
    //할일 로그 다운로드 - 파일 이름 할일_날짜로 고정
    //파라미터 받아서 response로 
	@GetMapping("/downloadTodoLog")
    public String downloadTodoLog(
            @RequestParam(value = "isProcess", required = false, defaultValue = "false") boolean isProcess,
            @RequestParam(value = "fileName", required = false) String fileName,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "task", required = false) String task,
            @RequestParam(name = "userName", required = false) String userName,
            Model model,
            HttpServletResponse response) {

        if (isProcess) {
            try {
                if (fileName == null || fileName.trim().isEmpty()) {
                    String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
                    fileName = "todo_" + nowStr;
                }
                if (!fileName.endsWith(".csv")) fileName += ".csv";

                String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");

                response.setContentType("text/csv; charset=UTF-8");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");

                StringBuilder csvContent = new StringBuilder();
                csvContent.append("\uFEFF");
                csvContent.append("제목,과업,사용자,날짜\n");
                csvContent.append("요청 할 일 테스트,할 일 미완료 변경,홍길동(test1@practice-6.by-works.net),2026-07-16 T15:51:44+09:00\n");
                csvContent.append("요청 할 일 테스트,할 일 완료,홍길동(test1@practice-6.by-works.net),2026-07-16 T15:49:50+09:00\n");
                csvContent.append("1234,할 일 삭제,홍길동(test1@practice-6.by-works.net),2026-07-16 T15:24:49+09:00\n");

                OutputStream os = response.getOutputStream();
                os.write(csvContent.toString().getBytes(StandardCharsets.UTF_8));
                os.flush();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
        
        model.addAttribute("defaultFileName", "todo_" + nowStr);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("title", title);
        model.addAttribute("task", task);
        model.addAttribute("userName", userName);

        return "adminUser/audit/downloadForm";
    }
}
