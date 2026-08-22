package kr.co.sist.chat;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.signup.UserDTO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {
	
	private final ChatService cs;

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
	    cDTO.setSendUser(loginUser.getName()); 
	    
	    int cnt = cs.sendMessage(cDTO); 
	    
	    return cnt > 0 ? "SUCCESS" : "FAIL";
	}
	
	//채팅방 리스트
	public String chatRoom(String chatRoomNo, Model model) {
		return "";
	}
	
	//사용자 검색
	public String searchUser(String userName) {
		return "";
	}
	
	//채팅방 생성
	public String createChatRoom() {
		return "";
	}
	
	//채팅방 정렬
	public String sortChatRoom() {
		return "";
	}
	
	//채팅 나기기
	public String leaveChatRoom(String chatRoomNo) {
		return "";
	}
	
	//파일첨부
	public String attachFile() {
		return "";
	}
}
