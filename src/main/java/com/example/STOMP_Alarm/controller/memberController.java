package com.example.STOMP_Alarm.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class memberController {
  
  @GetMapping("/user")
  public ResponseEntity<String> getMethodName() {
    return ResponseEntity.status(201).body(SecurityContextHolder.getContext().getAuthentication().getName());
  }
  
}
