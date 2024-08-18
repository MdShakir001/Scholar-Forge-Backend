package com.shakir.scholarForge.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Journal {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String userEmail;
	private String title;
	private String authorName;
	private String authorEmail;
	private LocalDate uploadDate;
	private String status="Under-Review";
	private String pathName;
	public Journal(String title,String userEmail, String authorName, String authorEmail, LocalDate uploadDate, String pathName) {
		this.title = title;
		this.userEmail=userEmail;
		this.authorName = authorName;
		this.authorEmail = authorEmail;
		this.uploadDate = uploadDate;
		this.pathName = pathName;
	}
	
	
}
