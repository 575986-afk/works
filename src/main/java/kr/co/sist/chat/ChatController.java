package kr.co.sist.chat;

import java.io.File;
import java.util.List;

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
	public String chatting(@RequestParam(value = "chatRoomNo", required = false) String chatRoomNo, Model model, HttpSession session) {
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
	public String sendMessage(@RequestParam("chatRoomNo") String chatRoomNo, 
	                          @RequestParam("content") String content, 
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
	                             @RequestParam("userNos") List<String> userNos,
	                             HttpSession session) {
		UserDTO loginUser = (UserDTO) session.getAttribute("user");

	    if (loginUser == null) {
	        return "FAIL";
	    }

	    int result = cs.createChat(
	            chatRoomName,
	            userNos,
	            loginUser.getUserNo()
	    );

	    
	    return result > 0 ? "SUCCESS" : "FAIL";
	}
	
//	@GetMapping("/popup/PopupAddr")
//	public String addGroupForm() {
//	    return "popup/PopupAddr"; 
//	}
	
//	@PostMapping("/chat/setRoomName")
//	@ResponseBody
//	public String setTempRoomName(@RequestParam("chatRoomName") String chatRoomName, HttpSession session) {
//	    session.setAttribute("tempChatRoomName", chatRoomName);
//	    return "SUCCESS";
//	}
	
	
	
	//사용자 검색
	@GetMapping("/search")
	@ResponseBody
	public List<ChatRoomDTO> search(@RequestParam("keyword") String keyword, HttpSession session) {
	    UserDTO loginUser = (UserDTO) session.getAttribute("user");
	    if (loginUser == null) {
	        return null;
	    }
	    
	    String companyNo = loginUser.getCompanyNo(); 
	    String myUserNo = String.valueOf(loginUser.getUserNo());
	    
	    return cs.searchUser(companyNo, myUserNo, keyword);
	}
	
	
	
	//채팅 나기기
	@PostMapping("/chat/leave")
	@ResponseBody
	public String leaveChatRoom(@RequestParam("chatRoomNo") String chatRoomNo, HttpSession session) {
	    UserDTO loginUser = (UserDTO) session.getAttribute("user");
	    if (loginUser == null) {
	        return "FAIL";
	    }
	    
	    int result = cs.leaveChatRoom(loginUser.getUserNo(), chatRoomNo);
	    return result > 0 ? "SUCCESS" : "FAIL";
	}
	
	// 파일첨부
		@PostMapping("/chat/uploadFile")
		@ResponseBody
		public String uploadFile(@RequestParam("file") MultipartFile file,
		                         @RequestParam("chatRoomNo") String chatRoomNo,
		                         @RequestParam("sendUser") String sendUser,
		                         @RequestParam("sendUserName") String sendUserName,
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

		        // 2. DB의 chatting 테이블(content)에 파일 전송 내역 저장
		        int result = cs.sendFileMessage(chatRoomNo, sendUser, originalFilename);

		        if (result > 0) {
		            // ★ 핵심: 파일 전송 성공 시 웹소켓 구독자들에게 실시간 브로드캐스트 전송
		            ChatRoomDTO broadcastDto = new ChatRoomDTO();
		            broadcastDto.setChatRoomNo(chatRoomNo);
		            broadcastDto.setSendUser(sendUser);
		            broadcastDto.setSendUserName(sendUserName);
		            broadcastDto.setContent("[파일] " + originalFilename);
		            
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
}