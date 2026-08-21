package kr.co.sist.chat;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.signup.UserDTO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {
	
	private final ChatService cs;

	@GetMapping("/chatting")
	public String chatting(String chatRoomNo, Model model, HttpSession session) {
	    UserDTO loginUser = (UserDTO) session.getAttribute("user");
	    if (loginUser != null) {
	        
	        List<ChatRoomDTO> chatroom = cs.getChatRoomList(chatRoomNo); 
	        model.addAttribute("chatRoomList", chatroom);
	    }
	    
	    model.addAttribute("currentRoomId", chatRoomNo);
	    
	    return "works/chat/chatting";
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
