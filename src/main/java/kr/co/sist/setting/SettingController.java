package kr.co.sist.setting;


import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.signup.UserDTO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SettingController {
	
	private final SettingService ss;
	
	@Value("${user.upload-dir}")
	private String uploadDir;
	
	//설정 메인화면 출력 
	@GetMapping("/userInfo")
    public String userInfo(Model model,HttpSession session) {
    	UserDTO loginUser = (UserDTO) session.getAttribute("user");
    	if (loginUser != null) {
    		model.addAttribute("userId", loginUser.getUserId());
    	}
        model.addAttribute("currentMenu", "profile"); // 개인정보 페이지
        return "works/settings/userInfo";
    }
	//비밀번호 확인 
	@PostMapping("/verifyPassword")
	@ResponseBody
	public boolean verifyPassword(@RequestParam("password") String password, HttpSession session) {
	    UserDTO loginUser = (UserDTO) session.getAttribute("user");
	    if (loginUser == null) {
	        return false;
	    }

	    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	    boolean isMatch = encoder.matches(password, loginUser.getPassword());

	    return isMatch; 
	}
	//비번 창 아이디 
	@GetMapping("/userInfoData")
	@ResponseBody
	public ResponseEntity<String> userInfoData(HttpSession session) {
	    UserDTO loginUser = (UserDTO) session.getAttribute("user");
	    
	    if (loginUser == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	    }
	    
	    String userId = loginUser.getUserId();
	    
	    return ResponseEntity.ok(userId != null ? userId : "");
	}
	//개인정보 변경 
	@PostMapping("/updateProfile")
	public String updateProfile(UserDTO uDTO,HttpSession session,RedirectAttributes rttr) {
		
		UserDTO loginUser = (UserDTO) session.getAttribute("user");
		
		uDTO.setUserNo(loginUser.getUserNo());
		
		int result = ss.modifyProfile(uDTO);
		
		if (result > 0) {
			rttr.addFlashAttribute("message", "회원정보 수정이 완료되었습니다.");
			
			UserDTO updatedUser = ss.selectProfile(loginUser.getUserNo()); 
			if (updatedUser != null) {
				session.setAttribute("user", updatedUser);
			}
		} else {
			rttr.addFlashAttribute("message", "회원정보 수정에 실패했습니다.");
		}
		return "redirect:/userInfo";
	}
	//개인정보수정화면 
    @GetMapping("/profileChg")
    public String profileChg(Model model, HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("user");
        
        UserDTO currentUser = ss.selectProfile(loginUser.getUserNo());
        TitleDTO currentTitle = ss.selectRankPosition(loginUser.getUserNo());
        
        if (currentUser != null) {
            model.addAttribute("userName", currentUser.getName());
            model.addAttribute("Tel", currentUser.getTel());
            model.addAttribute("Email", currentUser.getEmail());
            model.addAttribute("workplace", currentUser.getWorkplace());
            model.addAttribute("jobtask", currentUser.getJobtask());
            model.addAttribute("companyName", currentUser.getCompanyName());
            model.addAttribute("profileImage", currentUser.getProfileImage());
        } else {
            model.addAttribute("userName", loginUser.getName());
            model.addAttribute("Tel", loginUser.getTel());
            model.addAttribute("Email", loginUser.getEmail());
            model.addAttribute("workplace", loginUser.getWorkplace());
            model.addAttribute("jobtask", loginUser.getJobtask());
            model.addAttribute("companyName", "회사가 없습니다.");
            model.addAttribute("profileImage", loginUser.getProfileImage());
        }
        
        if(currentTitle != null) {
        	model.addAttribute("rank", currentTitle.getRankName());
        	model.addAttribute("position", currentTitle.getPositionName());
        } else {
        	model.addAttribute("rank", "직급이 없습니다.");
        	model.addAttribute("position", "직책이 없습니다.");
        }
        
        return "works/settings/profileChg";
    }
    
    @PostMapping("/updatePw")
    @ResponseBody
    public int pwChg(String userNo, String newPw,HttpSession session) {
    	UserDTO loginUser = (UserDTO) session.getAttribute("user");
        if (loginUser == null) {
            return 0; 
        }
    	return ss.updatePwChg(loginUser.getUserNo(), newPw);
    }
    
    
    @PostMapping("/setting/updateProfileImage")
    @ResponseBody
    public String updateProfileImage(@RequestParam("profileImgFile") MultipartFile file, 
                                     HttpSession session) {
        
        UserDTO loginUser = (UserDTO) session.getAttribute("user");
        if (loginUser == null) {
            return "FAIL_UNAUTHORIZED";
        }
        
        String userNo = loginUser.getUserNo();

        if (file.isEmpty()) {
            return "FAIL_EMPTY_FILE";
        }

        try {
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs(); 
            }

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String savedFilename = UUID.randomUUID().toString() + extension;

            File dest = new File(uploadDir + savedFilename);
            file.transferTo(dest);

            int cnt = ss.updateProfileImage(userNo, savedFilename);

            if (cnt > 0) {
                loginUser.setProfileImage(savedFilename);
                session.setAttribute("user", loginUser);
                
                return "SUCCESS";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "FAIL";
    }

 // 프로필 이미지 출력 매핑
    @GetMapping("/images/profile/{fileName}")
    @ResponseBody
    public ResponseEntity<Resource> getProfileImage(@org.springframework.web.bind.annotation.PathVariable("fileName") String fileName) {
        try {
            if (fileName == null || fileName.isEmpty() || fileName.equals("null")) {
                return ResponseEntity.notFound().build();
            }

            Path path = Paths.get(uploadDir + fileName);
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) 
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    //상태 설정화면 
    @GetMapping("/statusSetting")
    public String statusSetting(Model model, HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("user");
        
        List<StatusDTO> statusList = ss.getStatusList();
        
        String currentStatusNo = ss.getUserStatus(loginUser.getUserNo());
        
        model.addAttribute("statusList", statusList);
        model.addAttribute("currentStatusNo", currentStatusNo);
        model.addAttribute("currentMenu", "status"); 
        
        return "works/settings/statusSetting";
    }
    
    @PostMapping("/userStatus")
    @ResponseBody
    public int userStatus(@RequestParam("statusNo") String statusNo, HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("user");
        if (loginUser == null) {
            return 0; 
        }
        
        if ("".equals(statusNo) || "none".equals(statusNo)) {
            statusNo = null;
        }
        
        return ss.updateUserStatus(loginUser.getUserNo(), statusNo);
    }
    
    //-------------------------------------------------------------------------------------
    //알람 설정 
    @GetMapping("/alarmSetting")
    public String alarmSetting(Model model,HttpSession session) {
    	UserDTO loginUser = (UserDTO) session.getAttribute("user");
        
        AlarmSettingDTO alarmDTO = ss.getAlarm(loginUser.getUserNo());
        
        if (alarmDTO == null) {
            alarmDTO = new AlarmSettingDTO();
            alarmDTO.setUserNo(loginUser.getUserNo());
        }
        
        model.addAttribute("alarmDTO", alarmDTO);
        model.addAttribute("currentMenu", "alarm"); // 알림 페이지
        return "works/settings/alarmSetting";
    }
    //알람 설정 저장
    @PostMapping("/updateAlarmSetting")
    @ResponseBody
    public int updateAlarmSetting(@RequestBody AlarmSettingDTO alarmDTO, HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("user");
        if (loginUser == null) {
            return 0;
        }
        
        alarmDTO.setUserNo(loginUser.getUserNo());
        return ss.setAlarmSetting(alarmDTO);
    }
    
    //-------------------------------------------------------------------------------------
    //문의 리스트 화면 
    @GetMapping("/inquiry")
    public String inquiry(Model model,HttpSession session) {
    	UserDTO loginUser = (UserDTO) session.getAttribute("user");
        
       List<InquiryDomain> inquiryList=ss.showInquiry(loginUser.getUserNo());
        
       model.addAttribute("inquiryList",inquiryList);
    	model.addAttribute("currentMenu", "inquiry"); 
    	return "works/settings/inquiry";
    }
    
    
 // 파일 다운로드
    @GetMapping("/inquiry/image")
    @ResponseBody
    public ResponseEntity<Resource> getInquiryImage(@RequestParam("inquiryNo") String inquiryNo, HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("user");

        try {
            String fileName = ss.getInquiryFilePath(inquiryNo); 
            if (fileName == null || fileName.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Path path = Paths.get(uploadDir, "inquiry", fileName);
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) 
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // 2. 파일 다운로드
    @GetMapping("/inquiry/download")
    public ResponseEntity<Resource> downloadInquiryFile(@RequestParam("inquiryNo") String inquiryNo, HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("user");
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            String fileName = ss.getInquiryFilePath(inquiryNo);
            if (fileName == null || fileName.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Path path = Paths.get(uploadDir, fileName);

            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    
}