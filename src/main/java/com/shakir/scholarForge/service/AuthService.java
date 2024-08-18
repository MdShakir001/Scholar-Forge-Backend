package com.shakir.scholarForge.service;

import java.util.HashMap;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shakir.scholarForge.exception.UserAlreadyExistException;
import com.shakir.scholarForge.jwt.JwtService;
import com.shakir.scholarForge.model.Role;
import com.shakir.scholarForge.model.User;
import com.shakir.scholarForge.repository.UserRepository;
import com.shakir.scholarForge.request.LoginRequest;
import com.shakir.scholarForge.response.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authManager;
	public UserResponse login(LoginRequest request) {
		authManager.authenticate(new UsernamePasswordAuthenticationToken(
				request.getEmail(),
				request.getPassword()
				));
		User user=userRepo.findByEmail(request.getEmail()).orElseThrow(
				()->new UsernameNotFoundException("username not found"));
		HashMap<String,Object> hs=new HashMap<>();
		hs.put("role", user.getRole());
		String token=jwtService.generateTokenForUser(hs, user);
		return UserResponse.builder()
				.token(token)
				.build();
	}

	public UserResponse register(User user) {
		if(userRepo.existsByEmail(user.getEmail())) {
			throw new UserAlreadyExistException("email already exist please login");
		}
		User newUser=User.builder()
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.email(user.getEmail())
				.password(passwordEncoder.encode(user.getPassword()))
				.role(Role.USER)
				.build();
		userRepo.save(newUser);
		HashMap<String,Object> hs=new HashMap<>();
		hs.put("role", Role.USER);
		String token=jwtService.generateTokenForUser(hs, user);
		return UserResponse.builder()
				.token(token)
				.build();
	}

}
