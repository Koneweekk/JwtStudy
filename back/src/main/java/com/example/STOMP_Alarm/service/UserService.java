package com.example.STOMP_Alarm.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.example.STOMP_Alarm.dto.UserDto;
import com.example.STOMP_Alarm.entity.UserEntity;
import com.example.STOMP_Alarm.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
  
  private final UserRepository userRepository;

  public List<UserEntity> getAllUsername() {
    List<UserEntity> userList = userRepository.findAll();

    return userList;
  }
}
