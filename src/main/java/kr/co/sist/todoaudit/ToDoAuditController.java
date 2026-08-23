package kr.co.sist.todoaudit;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired(required = false)
    private ToDoLogService tdls;


    // 할 일 로그 페이지
    @GetMapping("/todoAudit")
    public String showToDoLogPage(
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "task", required = false) String task,
            @RequestParam(name = "userName", required = false) String userName,
            HttpSession session,
            Model model) {

        // 기본 조회 기간 : 최근 7일
        if (startDate == null || startDate.trim().isEmpty()) {
            startDate = LocalDate.now()
                    .minusDays(7)
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        if (endDate == null || endDate.trim().isEmpty()) {
            endDate = LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        String companyNo = (String) session.getAttribute("companyNo");

        ToDoLogSearchDTO search = new ToDoLogSearchDTO();

        search.setCompanyNo(companyNo);
        search.setStartDate(startDate);
        search.setEndDate(endDate);
        search.setTitle(title);
        search.setTask(task);
        search.setUserName(userName);

        List<ToDoLogListDomain> logList = tdls.getAllToDoLogList(search);

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("title", title);
        model.addAttribute("task", task);
        model.addAttribute("userName", userName);
        model.addAttribute("logList", logList);

        return "adminUser/audit/todoAudit";
    }


    // 할 일 로그 상세 조회
    @GetMapping("/todoAudit/todoLogDetail")
    public String findToDoLogDetail(
            @RequestParam(name = "logNo", required = false) String logNo,
            HttpSession session,
            Model model) {

        if (logNo == null || logNo.trim().isEmpty()) {
            return "redirect:/adminUser/audit/todoAudit";
        }

        String companyNo = (String) session.getAttribute("companyNo");
        ToDoLogDetailDomain logDetail = tdls.getToDoLogDetail(logNo, companyNo);

        model.addAttribute("logDetail", logDetail);

        return "adminUser/audit/todoLogDetail";
    }


    // 할 일 로그 다운로드
    @GetMapping("/downloadTodoLog")
    public String downloadTodoLog(
            @RequestParam(value = "isProcess", required = false, defaultValue = "false") boolean isProcess,
            @RequestParam(value = "fileName", required = false) String fileName,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "task", required = false) String task,
            @RequestParam(value = "userName", required = false) String userName,
            HttpSession session,
            Model model,
            HttpServletResponse response) {

        String companyNo = (String) session.getAttribute("companyNo");

        // 실제 Excel 다운로드
        if (isProcess) {
            ToDoLogSearchDTO search = new ToDoLogSearchDTO();

            search.setCompanyNo(companyNo);
            search.setStartDate(startDate);
            search.setEndDate(endDate);
            search.setTitle(title);
            search.setTask(task);
            search.setUserName(userName);

            List<ToDoLogListDomain> logList = tdls.getAllToDoLogList(search);

            try {
                // 파일명
                if (fileName == null || fileName.trim().isEmpty()) {
                    fileName = "할일_" +
                            LocalDateTime.now()
                                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
                }
                if (!fileName.toLowerCase().endsWith(".xlsx")) {
                    fileName += ".xlsx";
                }

                String encodedFileName =
                        URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                                .replace("+", "%20");

                // Response 설정
                response.setContentType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setHeader(
                        "Content-Disposition",
                        "attachment; filename*=UTF-8''" + encodedFileName);

                // Excel 생성
                org.apache.poi.xssf.usermodel.XSSFWorkbook workbook =
                        new org.apache.poi.xssf.usermodel.XSSFWorkbook();
                org.apache.poi.ss.usermodel.Sheet sheet =
                        workbook.createSheet("할일 로그");

                // 헤더
                org.apache.poi.ss.usermodel.Row header =
                        sheet.createRow(0);

                header.createCell(0).setCellValue("제목");
                header.createCell(1).setCellValue("과업");
                header.createCell(2).setCellValue("사용자");
                header.createCell(3).setCellValue("이메일");
                header.createCell(4).setCellValue("날짜");

                // 데이터
                int rowNum = 1;
                for (ToDoLogListDomain log : logList) {
                    org.apache.poi.ss.usermodel.Row row =
                            sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(
                            log.getTitle() == null
                                    ? "-"
                                    : log.getTitle()
                    );
                    row.createCell(1).setCellValue(
                            log.getDuty() == null
                                    ? "-"
                                    : log.getDuty()
                    );
                    row.createCell(2).setCellValue(
                            log.getUserName() == null
                                    ? "-"
                                    : log.getUserName()
                    );
                    row.createCell(3).setCellValue(
                            log.getEmail() == null
                                    ? "-"
                                    : log.getEmail()
                    );
                    row.createCell(4).setCellValue(
                            log.getInputDate() == null
                                    ? "-"
                                    : log.getInputDate().toString()
                    );
                }

                // 컬럼 너비
                for (int i = 0; i < 5; i++) {
                    sheet.autoSizeColumn(i);
                    sheet.setColumnWidth(
                            i,
                            Math.min(sheet.getColumnWidth(i) + 1000, 15000)
                    );
                }

                // 응답으로 Excel 전송
                workbook.write(response.getOutputStream());
                workbook.close();
                response.getOutputStream().flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        // 다운로드 모달
        String nowStr =
                LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
        String defaultFileName = "할일_" + nowStr;

        // 검색 조건을 다운로드 URL에 포함
        StringBuilder downloadAction =
                new StringBuilder("/adminUser/audit/downloadTodoLog?");
        downloadAction.append("startDate=")
                .append(encode(startDate));
        downloadAction.append("&endDate=")
                .append(encode(endDate));
        downloadAction.append("&title=")
                .append(encode(title));
        downloadAction.append("&task=")
                .append(encode(task));
        downloadAction.append("&userName=")
                .append(encode(userName));

        model.addAttribute("defaultFileName", defaultFileName);

        // 공통 downloadForm에서 사용
        model.addAttribute("downloadAction", downloadAction.toString());

        return "adminUser/audit/downloadForm";
    }


    // URL 파라미터 UTF-8 인코딩
    private String encode(String value) {
        if (value == null) {
            return "";
        }
        return URLEncoder.encode(
                value,
                StandardCharsets.UTF_8
        );
    }
}