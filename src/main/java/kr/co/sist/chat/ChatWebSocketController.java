package kr.co.sist.chat;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatService cs;
    private final SimpMessagingTemplate messagingTemplate;

    // 클라이언트가 "/pub/chat/message"로 메시지를 보내면 이 메서드가 실행됨
    @MessageMapping("/chat/message/{chatRoomNo}")
    public void send(@DestinationVariable("chatRoomNo") String chatRoomNo, ChatRoomDTO message) {
        // 1. DB에 메시지 저장
        cs.sendMessage(message);

        // 2. 해당 채팅방을 구독(`/sub/chat/room/{chatRoomNo}`)하고 있는 모든 사용자에게 메시지 전송
        messagingTemplate.convertAndSend("/sub/chat/room/" + chatRoomNo, message);
    }
}