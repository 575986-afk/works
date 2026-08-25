package kr.co.sist.chat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    @MessageMapping("/chat/message/{chatRoomNo}")
    public void send(
            @DestinationVariable("chatRoomNo") String chatRoomNo,
            ChatRoomDTO message) {

        message.setChatRoomNo(chatRoomNo);
        
        // ★★★ 전송 시간 세팅 (클라이언트가 화면에 표시할 수 있도록) ★★★
        message.setSendDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));

        int result = cs.sendMessage(message);

        if (result > 0) {
            messagingTemplate.convertAndSend(
                    "/sub/chat/room/" + chatRoomNo,
                    message
            );
        }
    }
}