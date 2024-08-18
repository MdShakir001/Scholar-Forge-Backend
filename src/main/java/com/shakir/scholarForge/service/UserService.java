package com.shakir.scholarForge.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.shakir.scholarForge.exception.UserNotFoundException;
import com.shakir.scholarForge.model.User;
import com.shakir.scholarForge.repository.UserRepository;
import com.shakir.scholarForge.request.UpdateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepo;
	
	public Optional<User> findUserByEmail(String email){
		return userRepo.findByEmail(email);
	}

	public User updateUser(String userEmail, UpdateRequest user) {
		User updatedUser=userRepo.findByEmail(userEmail).orElseThrow(()->
			new UserNotFoundException("user not found"));
		if(user.getFirstName()!=null) {
			updatedUser.setFirstName(user.getFirstName());
		}
		if(user.getLastName()!=null) {
			updatedUser.setLastName(user.getLastName());
		}
		if(user.getContactNo()!=null) {
			updatedUser.setContactNo(user.getContactNo());
		}
		if(user.getOccupation()!=null) {
			updatedUser.setOccupation(user.getOccupation());
		}
		
		return userRepo.save(updatedUser);
	}

	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		return userRepo.findAll();
	}

}
