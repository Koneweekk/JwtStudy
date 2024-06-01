package com.example.STOMP_Alarm.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.STOMP_Alarm.dto.UserDto;
import com.example.STOMP_Alarm.entity.UserEntity;
import com.example.STOMP_Alarm.service.UserService;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequiredArgsConstructor
public class memberController {

  private final UserService userService;
  
  @GetMapping("/user")
  public ResponseEntity<String> getUsername() {
    return ResponseEntity.status(201).body(SecurityContextHolder.getContext().getAuthentication().getName());
  }

  @GetMapping("/userlist")
  public ResponseEntity<List<UserDto>> getAllUsername() {
    String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

    List<UserDto> userDtoList = new ArrayList<>();

    for (UserEntity userEntity : userService.getAllUsername()) {
      if (currentUsername.equals(userEntity.getUsername())) continue;
      UserDto userDto = new UserDto(userEntity.getUsername());
      userDtoList.add(userDto);
    }

    return ResponseEntity.status(201).body(userDtoList);
  }
  
}
