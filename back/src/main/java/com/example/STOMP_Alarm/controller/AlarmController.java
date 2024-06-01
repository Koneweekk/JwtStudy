package com.example.STOMP_Alarm.controller;

import com.example.STOMP_Alarm.dto.Alarm;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;



@Controller
@RequiredArgsConstructor
public class AlarmController {
    
    @MessageMapping("/send-alarm")
    @SendTo("/topic/alarm")
    public Alarm sendAlarm(@Payload Alarm alarm) {
        return alarm;
    }


    @MessageMapping("/send-alarm-to-user/{username}")
    @SendTo("/queue/alarm/{username}")
    public Alarm sendAlarmToUser(@DestinationVariable String username, @Payload Alarm alarm) {
        return alarm;
    }
}
