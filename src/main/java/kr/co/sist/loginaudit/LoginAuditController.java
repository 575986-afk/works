package kr.co.sist.loginaudit;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
public class LoginAuditController {

    @Autowired(required = false)
    private LoginLogService lls;

    // 로그인 로그 페이지
    @GetMapping("/loginAudit")
    public String showLoginLogPage(
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "loginIp", required = false, defaultValue = "") String loginIp,
            @RequestParam(name = "userName", required = false, defaultValue = "") String userName,
            HttpSession session,
            Model model) {

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

        LoginLogSearchDTO search = new LoginLogSearchDTO();

        search.setCompanyNo(companyNo);
        search.setStartDate(startDate);
        search.setEndDate(endDate);
        search.setLoginIp(loginIp);
        search.setUserName(userName);

        List<LoginLogListDomain> logList = lls.getAllLoginLogList(search);

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("loginIp", loginIp);
        model.addAttribute("userName", userName);
        model.addAttribute("logList", logList);

        return "adminUser/audit/loginAudit";
    }

    // 로그인 로그 다운로드
    @GetMapping("/downloadLoginLog")
    public String downloadLoginLog(
            @RequestParam(value = "isProcess", required = false, defaultValue = "false") boolean isProcess,
            @RequestParam(value = "fileName", required = false) String fileName,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "userName", required = false) String userName,
            @RequestParam(name = "loginIp", required = false) String loginIp,
            HttpSession session,
            Model model,
            HttpServletResponse response) {

        String companyNo = (String) session.getAttribute("companyNo");

        // 실제 다운로드
        if (isProcess) {
            LoginLogSearchDTO search = new LoginLogSearchDTO();

            search.setCompanyNo(companyNo);
            search.setStartDate(startDate);
            search.setEndDate(endDate);
            search.setUserName(userName);
            search.setLoginIp(loginIp);

            List<LoginLogListDomain> logList = lls.getAllLoginLogList(search);

            try {
                // 파일명
                if (fileName == null || fileName.trim().isEmpty()) {
                    String nowStr = LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
                    fileName = "로그인_" + nowStr;
                }
                if (!fileName.endsWith(".xlsx")) {
                    fileName += ".xlsx";
                }
                String encodedFileName =
                        URLEncoder.encode(
                                fileName,
                                StandardCharsets.UTF_8
                        ).replace("+", "%20");
                response.setContentType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                );
                response.setHeader(
                        "Content-Disposition",
                        "attachment; filename=\"" + encodedFileName + "\""
                );

                // Excel 생성
                XSSFWorkbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("로그인 로그");

                // 헤더
                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("설명");
                header.createCell(1).setCellValue("사용자");
                header.createCell(2).setCellValue("이메일");
                header.createCell(3).setCellValue("일시");
                header.createCell(4).setCellValue("IP 주소");
                header.createCell(5).setCellValue("접속 상태");

                // 데이터
                int rowNum = 1;
                for (LoginLogListDomain log : logList) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(
                            log.getDescription() == null
                                    ? "-"
                                    : log.getDescription()
                    );
                    row.createCell(1).setCellValue(
                            log.getUserName() == null
                                    ? "-"
                                    : log.getUserName()
                    );
                    row.createCell(2).setCellValue(
                            log.getEmail() == null
                                    ? "-"
                                    : log.getEmail()
                    );
                    row.createCell(3).setCellValue(
                            log.getInputDate() == null
                                    ? "-"
                                    : log.getInputDate().toString()
                    );
                    row.createCell(4).setCellValue(
                            log.getLoginIp() == null
                                    ? "-"
                                    : log.getLoginIp()
                    );
                    row.createCell(5).setCellValue(
                            log.getStatus() == null
                                    ? "-"
                                    : log.getStatus()
                    );
                }
                // 컬럼 너비
                for (int i = 0; i < 6; i++) {
                    sheet.autoSizeColumn(i);
                }

                OutputStream os = response.getOutputStream();
                workbook.write(os);
                workbook.close();
                os.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        // 다운로드 모달
        String nowStr = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
        model.addAttribute(
                "defaultFileName",
                "로그인_" + nowStr
        );

        // 다운로드 요청 페이지의 검색 조건
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("userName", userName);
        model.addAttribute("loginIp", loginIp);

        // 실제 다운로드 URL
        model.addAttribute(
                "downloadAction",
                "/adminUser/audit/downloadLoginLog"
        );

        return "adminUser/audit/downloadForm";
    }
}