package com.shakir.scholarForge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shakir.scholarForge.model.User;
import com.shakir.scholarForge.request.LoginRequest;
import com.shakir.scholarForge.response.UserResponse;
import com.shakir.scholarForge.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
public class AuthController {
	private final AuthService authService;
	
	@PostMapping("/register")
	public ResponseEntity<UserResponse> registerNewUser(@RequestBody User user){
		return ResponseEntity.ok(authService.register(user));
	}
	@PostMapping("/login")
	public ResponseEntity<UserResponse> registerNewUser(@RequestBody LoginRequest request){
		return ResponseEntity.ok(authService.login(request));
	}

}
