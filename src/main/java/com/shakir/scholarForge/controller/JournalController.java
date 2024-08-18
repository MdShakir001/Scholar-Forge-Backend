package com.shakir.scholarForge.controller;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.shakir.scholarForge.exception.JournalNotFoundException;
import com.shakir.scholarForge.model.Journal;
import com.shakir.scholarForge.service.JournalService;

import lombok.RequiredArgsConstructor;
@RestController
@RequiredArgsConstructor
@RequestMapping("/journal")
@CrossOrigin("http://localhost:5173")
public class JournalController {
	private final JournalService journalService;
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/allJournals")
	public ResponseEntity<List<Journal>> getAllJournals(){
		List<Journal> journals=journalService.getAllJournals();
		return ResponseEntity.ok(journals);
	}
	@PostMapping("/addJournal")
	public ResponseEntity<?> addJournal(@RequestParam("title") String title,
			@RequestParam("userEmail") String userEmail,
			@RequestParam("authorName") String authorName,
			@RequestParam("authorEmail") String authorEmail,
			@RequestParam("uploadDate") LocalDate uploadDate,
			@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException{
		Journal savedJournal=journalService.addJournal(title,
				userEmail,
				authorName,
				authorEmail,
				uploadDate,
				file);
		return ResponseEntity.ok(savedJournal);
	}
	@GetMapping("/searchJournal/{title}")
	public ResponseEntity<List<Journal>> getJournalByTitle(@PathVariable String title,
			@RequestParam("status") String status){
		List<Journal> journals=journalService.findByTitle(title,status);
		return ResponseEntity.ok(journals);
	}
	@GetMapping("/displayJournal/{journalId}")
	public ResponseEntity<Journal> getJournalById(@PathVariable Long journalId){
		Journal journal=journalService.getJournalById(journalId)
				.orElseThrow(()->
		new JournalNotFoundException("no journal found with this id "));
		return ResponseEntity.ok(journal);
	}
	@GetMapping("/downloadPdf/{journalId}")
	public ResponseEntity<byte[]> downloadPdf(@PathVariable Long journalId) throws IOException{
		Journal journal=journalService.getJournalById(journalId)
				.orElseThrow(()->
		new JournalNotFoundException("no journal found with this id "));
		String pathname=journal.getPathName();
		return journalService.downloadFile(pathname);
	}
	@GetMapping("/findAllJournalByEmail/{email}")
	@PreAuthorize("hasRole('ADMIN') or #email==principal.username")
	public ResponseEntity<List<Journal>> getAllJournalsByUser(@PathVariable String email){
		List<Journal> journals=journalService.getJournalsByUser(email);
		return ResponseEntity.ok(journals);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/deleteJournalById/{journalId}")
	public ResponseEntity<Void> deleteJournalById(@PathVariable Long journalId) throws FileNotFoundException{
		journalService.deleteJournalById(journalId);
		return ResponseEntity.noContent().build();
		
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/updateJournalStatus/{journalId}")
	public ResponseEntity<String> updateStatusById(@PathVariable Long journalId,
			@RequestParam("status") String status){
		journalService.updateStatusById(journalId,status);
		return ResponseEntity.ok("Status updated Successfully");
	}
	
	
	

}
