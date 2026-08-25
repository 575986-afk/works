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

    @MessageMapping("/chat/message/{chatRoomNo}")
    public void send(
            @DestinationVariable("chatRoomNo") String chatRoomNo,
            ChatRoomDTO message) {

        message.setChatRoomNo(chatRoomNo);

        int result = cs.sendMessage(message);

        if (result > 0) {
            messagingTemplate.convertAndSend(
                    "/sub/chat/room/" + chatRoomNo,
                    message
            );
        }
    }
}