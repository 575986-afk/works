package kr.co.sist.addraudit;

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
public class AddrAuditController {

    @Autowired(required = false)
    private AddrLogService als;


    // 주소록 로그 페이지
    @GetMapping("/addrAudit")
    public String showAddrLogPage(
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "duty", required = false) String duty,
            @RequestParam(name = "userName", required = false) String userName,
            @RequestParam(name = "targetName", required = false) String targetName,
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

        AddrLogSearchDTO search = new AddrLogSearchDTO();

        search.setCompanyNo(companyNo);
        search.setStartDate(startDate);
        search.setEndDate(endDate);
        search.setDuty(duty);
        search.setUserName(userName);
        search.setTargetName(targetName);

        List<AddrLogListDomain> logList = als.getAllAddrLogList(search);

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("duty", duty);
        model.addAttribute("userName", userName);
        model.addAttribute("targetName", targetName);
        model.addAttribute("logList", logList);

        return "adminUser/audit/addrAudit";
    }


    // 주소록 로그 다운로드
    @GetMapping("/downloadAddrLog")
    public String downloadAddrLog(
            @RequestParam(value = "isProcess", required = false, defaultValue = "false") boolean isProcess,
            @RequestParam(value = "fileName", required = false) String fileName,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "duty", required = false) String duty,
            @RequestParam(name = "userName", required = false) String userName,
            @RequestParam(name = "targetName", required = false) String targetName,
            HttpSession session,
            Model model,
            HttpServletResponse response) {

        String companyNo = (String) session.getAttribute("companyNo");

        // 실제 다운로드
        if (isProcess) {
            AddrLogSearchDTO search = new AddrLogSearchDTO();
            search.setCompanyNo(companyNo);
            search.setStartDate(startDate);
            search.setEndDate(endDate);
            search.setDuty(duty);
            search.setUserName(userName);
            search.setTargetName(targetName);

            List<AddrLogListDomain> logList = als.getAllAddrLogList(search);

            try {
                // 파일명
                if (fileName == null || fileName.trim().isEmpty()) {
                    String nowStr =
                            LocalDateTime.now()
                                    .format(
                                        DateTimeFormatter.ofPattern(
                                            "yyyyMMdd_HHmm"
                                        )
                                    );
                    fileName = "주소록_" + nowStr;
                }
                if (!fileName.endsWith(".xlsx")) {
                    fileName += ".xlsx";
                }
                String encodedFileName =
                        URLEncoder.encode(
                                fileName,
                                StandardCharsets.UTF_8
                        ).replace("+", "%20");

                // 응답 설정
                response.setContentType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                );
                response.setHeader(
                        "Content-Disposition",
                        "attachment; filename=\"" +
                        encodedFileName +
                        "\""
                );

                // Excel 생성
                XSSFWorkbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("주소록 로그");

                // 헤더
                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("대상");
                header.createCell(1).setCellValue("대상 이메일");
                header.createCell(2).setCellValue("과업");
                header.createCell(3).setCellValue("사용자");
                header.createCell(4).setCellValue("사용자 이메일");
                header.createCell(5).setCellValue("일시");

                // 데이터
                int rowNum = 1;
                for (AddrLogListDomain log : logList) {
                    Row row = sheet.createRow(rowNum++);
                    // 대상
                    row.createCell(0)
                            .setCellValue(
                                log.getTargetName() == null
                                    ? "전체"
                                    : log.getTargetName()
                            );
                    // 대상 이메일
                    row.createCell(1)
                            .setCellValue(
                                log.getTargetEmail() == null
                                    ? "-"
                                    : log.getTargetEmail()
                            );
                    // 과업
                    row.createCell(2)
                            .setCellValue(
                                log.getDuty() == null
                                    ? "-"
                                    : log.getDuty()
                            );
                    // 사용자
                    row.createCell(3)
                            .setCellValue(
                                log.getUserName() == null
                                    ? "-"
                                    : log.getUserName()
                            );
                    // 사용자 이메일
                    row.createCell(4)
                            .setCellValue(
                                log.getEmail() == null
                                    ? "-"
                                    : log.getEmail()
                            );
                    // 일시
                    row.createCell(5)
                            .setCellValue(
                                log.getInputDate() == null
                                    ? "-"
                                    : log.getInputDate().toString()
                            );
                }

                // 컬럼 너비
                for (int i = 0; i < 6; i++) {
                    sheet.autoSizeColumn(i);
                }

                // Excel 출력
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
        String nowStr =
                LocalDateTime.now()
                        .format(
                            DateTimeFormatter.ofPattern(
                                "yyyyMMdd_HHmm"
                            )
                        );
        model.addAttribute(
                "defaultFileName",
                "주소록_" + nowStr
        );

        // 다운로드 요청 페이지의 검색 조건
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("duty", duty);
        model.addAttribute("userName", userName);
        model.addAttribute("targetName", targetName);

        // 실제 다운로드 URL
        model.addAttribute("downloadAction", "/adminUser/audit/downloadAddrLog");

        return "adminUser/audit/downloadForm";
    }
}