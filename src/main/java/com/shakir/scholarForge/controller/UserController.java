package com.shakir.scholarForge.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shakir.scholarForge.exception.UserNotFoundException;
import com.shakir.scholarForge.model.User;
import com.shakir.scholarForge.request.UpdateRequest;
import com.shakir.scholarForge.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@CrossOrigin("http://localhost:5173")
public class UserController {
	private final UserService userService;
	
	@GetMapping("/findByEmail/{email}")
	@PreAuthorize("hasRole('ADMIN') or #email==principal.username ")
	public ResponseEntity<User> findUserByEmail(@PathVariable String email){
		User user=userService.findUserByEmail(email).orElseThrow(
				()->new UserNotFoundException("user not found"+email));
		return ResponseEntity.ok(user);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/updateUser/{email}")
	public ResponseEntity<User> updateUser(@PathVariable String email ,@RequestBody UpdateRequest user){
		User updatedUser=userService.updateUser(email,user);
		return ResponseEntity.ok(updatedUser);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/allUsers")
	public ResponseEntity<List<User>> getAllUsers(){
		List<User> users=userService.getAllUsers();
		return ResponseEntity.ok(users);
		
	}

}
