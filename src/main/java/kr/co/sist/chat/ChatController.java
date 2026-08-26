package kr.co.sist.chat;

import java.io.File;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.sist.signup.UserDTO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {

	private final ChatService cs;
	private final SimpMessagingTemplate messagingTemplate;

	@GetMapping("/chatting")
	public String chatting(@RequestParam(value = "chatRoomNo", required = false) String chatRoomNo, Model model,
			HttpSession session) {
		UserDTO loginUser = (UserDTO) session.getAttribute("user");
		if (loginUser == null) {
			return "redirect:/login";
		}

		List<ChatRoomDTO> chatroom = cs.getChatRoomList(loginUser.getUserNo());
		model.addAttribute("chatRoomList", chatroom);
		model.addAttribute("currentRoomId", chatRoomNo);

		if (chatRoomNo != null && !chatRoomNo.isEmpty()) {
			for (ChatRoomDTO room : chatroom) {
				if (room.getChatRoomNo().equals(chatRoomNo)) {
					model.addAttribute("currentRoomName", room.getChatRoomName());
					model.addAttribute("currentRoomParticipantCount", room.getParticipantCount());
					break;
				}
			}
		}

		return "works/chat/chatting";
	}

	@GetMapping("/chat/roomList")
	@ResponseBody
	public List<ChatRoomDTO> getChatRoomList(HttpSession session) {

		UserDTO user = (UserDTO) session.getAttribute("user");

		return cs.selectChatRoom(user.getUserNo());
	}

	@GetMapping("/chat/messages")
	@ResponseBody
	public List<ChatRoomDTO> getMessages(@RequestParam("chatRoomNo") String chatRoomNo) {
		return cs.getMessageList(chatRoomNo);
	}

	@PostMapping("/chat/send")
	@ResponseBody
	public String sendMessage(@RequestParam("chatRoomNo") String chatRoomNo, @RequestParam("content") String content,
			HttpSession session) {
		UserDTO loginUser = (UserDTO) session.getAttribute("user");
		if (loginUser == null) {
			return "FAIL";
		}

		ChatRoomDTO cDTO = new ChatRoomDTO();
		cDTO.setChatRoomNo(chatRoomNo);
		cDTO.setContent(content);
		cDTO.setSendUser(loginUser.getUserNo());

		int cnt = cs.sendMessage(cDTO);

		return cnt > 0 ? "SUCCESS" : "FAIL";
	}

	@PostMapping("/chat/create")
	@ResponseBody
	public String createChatRoom(@RequestParam("chatRoomName") String chatRoomName,
			@RequestParam("userNos") List<String> userNos, HttpSession session) {
		UserDTO loginUser = (UserDTO) session.getAttribute("user");

		if (loginUser == null) {
			return "FAIL";
		}

		int result = cs.createChat(chatRoomName, userNos, loginUser.getUserNo());

		return result > 0 ? "SUCCESS" : "FAIL";
	}

	// 사용자 검색
	@GetMapping("/search")
	@ResponseBody
	public List<ChatRoomDTO> search(@RequestParam("keyword") String keyword, HttpSession session) {
		UserDTO loginUser = (UserDTO) session.getAttribute("user");

		String companyNo = loginUser.getCompanyNo();
		String myUserNo = String.valueOf(loginUser.getUserNo());

		return cs.searchUser(companyNo, myUserNo, keyword);
	}

	// 채팅 나기기
	@PostMapping("/chat/leave")
	@ResponseBody
	public String leaveChatRoom(@RequestParam("chatRoomNo") String chatRoomNo, HttpSession session) {
		UserDTO loginUser = (UserDTO) session.getAttribute("user");

		
		int result = cs.leaveChatRoom(loginUser.getUserNo(), chatRoomNo);
		return result > 0 ? "SUCCESS" : "FAIL";
	}

	// 파일첨부
	@PostMapping("/chat/uploadFile")
	@ResponseBody
	public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("chatRoomNo") String chatRoomNo,
			@RequestParam("sendUser") String sendUser, @RequestParam("sendUserName") String sendUserName,
			HttpServletRequest request) {
		if (file.isEmpty()) {
			return "FAIL";
		}
		try {
			String uploadDir = request.getSession().getServletContext().getRealPath("/resources/upload/");
			File dir = new File(uploadDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String originalFilename = file.getOriginalFilename();
			String savedFileName = System.currentTimeMillis() + "_" + originalFilename;
			File target = new File(uploadDir + savedFileName);

			// 1. 서버에 파일 저장
			file.transferTo(target);

			// 2. DB에 저장할 content 규격 설정 ([파일] 원본파일명|서버저장파일명)
			String fileContent = "[파일] " + originalFilename + "|" + savedFileName;

			int result = cs.sendFileMessage(chatRoomNo, sendUser, fileContent);

			if (result > 0) {
				ChatRoomDTO broadcastDto = new ChatRoomDTO();
				broadcastDto.setChatRoomNo(chatRoomNo);
				broadcastDto.setSendUser(sendUser);
				broadcastDto.setSendUserName(sendUserName);
				broadcastDto.setContent(fileContent); // 규격화된 문자열 전송

				messagingTemplate.convertAndSend("/sub/chat/room/" + chatRoomNo, broadcastDto);

				return "SUCCESS";
			} else {
				return "FAIL";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "FAIL";
		}
	}

	// 파일 다운로드 메소드 추가
	@GetMapping("/chat/downloadFile")
	public ResponseEntity<Resource> downloadFile(@RequestParam("fileName") String fileName,
			HttpServletRequest request) {
		try {
			String uploadDir = request.getSession().getServletContext().getRealPath("/resources/upload/");
			Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());

			if (!resource.exists()) {
				return ResponseEntity.notFound().build();
			}

			// 원본 파일명 추출 (시스템 타임스탬프 제거)
			String originalFileName = fileName.substring(fileName.indexOf("_") + 1);
			String encodedFileName = URLEncoder.encode(originalFileName, "UTF-8").replaceAll("\\+", "%20");

			return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
					.body(resource);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
}