package com.example.STOMP_Alarm.controller;

import com.example.STOMP_Alarm.dto.Alarm;
import com.example.STOMP_Alarm.dto.AlarmToUser;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;


// @Controller
@RequiredArgsConstructor
public class AlarmController {

    private final SimpMessagingTemplate messagingTemplate;
    // 이렇게 코드가 있으면 클라이언트는
    // 한 마디로 @MessageMapping 브로커라고 이해하면 편하다(simpleBroker 아님)
    
    @MessageMapping("/send-alarm")
    @SendTo("/topic/alarm")
    public Alarm sendAlarm(@Payload Alarm alarm) {
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println(alarm.toString());
        // messagingTemplate.convertAndSend("/topic/alarm", alarm);
        return null;
    }


    @MessageMapping("/send-alarm-to-user")
    public void sendAlarmToUser(@Payload AlarmToUser alarmToUser) {
        // 여기서는 Alarm 객체에 recipient 필드를 추가한다고 가정합니다.
        String username = alarmToUser.getRecipient();
        messagingTemplate.convertAndSendToUser(username, "/queue/alarms", alarmToUser);
    }
}
