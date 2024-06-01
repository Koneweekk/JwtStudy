package com.example.STOMP_Alarm.service;

import com.example.STOMP_Alarm.dto.Alarm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

// @Controller
@Slf4j
public class AlarmMessageHandler {

    @MessageMapping("/alarm")
    @SendTo("/topic/alarms")
    public String handleAlarm(@Payload Alarm alarm) {
        System.out.println("-----------------------------------------------------------------------");
        log.info("Received alarm: {}", alarm);
        // 알람을 받아서 추가적인 처리 가능
        return "awrghaerhaerhaere";
    }
}
