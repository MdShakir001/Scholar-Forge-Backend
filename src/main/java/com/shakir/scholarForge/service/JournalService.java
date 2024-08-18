package com.shakir.scholarForge.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import com.shakir.scholarForge.model.Journal;

public interface JournalService {

	List<Journal> getAllJournals();

	Optional<Journal> getJournalById(Long journalId);

	Journal addJournal(String title,String userEmail, String authorName, String authorEmail, LocalDate uploadDate, MultipartFile file) throws IllegalStateException, IOException;

	List<Journal> findByTitle(String title, String status);

	ResponseEntity<byte[]> downloadFile(String pathname) throws IOException;

	List<Journal> getJournalsByUser(String email);

	void deleteJournalById(Long journalId) throws FileNotFoundException;

	void updateStatusById(Long journalId,String status);

}
