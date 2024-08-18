package com.shakir.scholarForge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shakir.scholarForge.model.Journal;

public interface JournalRepository extends JpaRepository<Journal,Long> {

	List<Journal> findByTitleContainingAndStatus(String title,String status);
	List<Journal> findByUserEmail(String userEmail);
	

}
