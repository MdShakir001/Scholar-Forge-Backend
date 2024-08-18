package com.shakir.scholarForge.serviceImpl;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.shakir.scholarForge.exception.JournalNotFoundException;
import com.shakir.scholarForge.model.Journal;
import com.shakir.scholarForge.repository.JournalRepository;
import com.shakir.scholarForge.service.JournalService;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class JournalServiceImpl implements JournalService{
	private final JournalRepository journalRepo;
	@Value("${s3.bucket.name}")
	private String bucketName;
	private final AmazonS3 s3Client;
	@Override
	public List<Journal> getAllJournals() {
		
		return journalRepo.findAll();
	}

	public String uploadFileToSystem(MultipartFile file) throws IllegalStateException, IOException {
		String pathname=System.currentTimeMillis()+"_"+file.getOriginalFilename();
		InputStream inputStream=file.getInputStream();
		long contentLength = file.getSize();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentLength);
        try {
        	s3Client.putObject(new PutObjectRequest(bucketName,pathname,inputStream,metadata));
        }finally {
        	if(inputStream!=null)
        	inputStream.close();
        }
		
		return pathname;
		
	}
	@Override
	public Optional<Journal> getJournalById(Long journalId) {
		Optional<Journal> journal=journalRepo.findById(journalId);
		return journal;
		}

	@Override
	public Journal addJournal(String title,
			String userEmail,
			String authorName,
			String authorEmail,
			LocalDate uploadDate,
			MultipartFile file) throws IllegalStateException, IOException {
		String pathname=uploadFileToSystem(file);
		Journal savedJournal =journalRepo.save(new Journal(title,userEmail,authorName,authorEmail,uploadDate,pathname));
		return savedJournal;
	}

	@Override
	public List<Journal> findByTitle(String title,String status) {
		
		return journalRepo.findByTitleContainingAndStatus(title,status);
	}
	
	public String deleteFile(String fileName) {
		s3Client.deleteObject(bucketName, fileName);
		return fileName +"deletedSuccessFully";
	}

	@Override
	public ResponseEntity<byte[]> downloadFile(String pathname) throws IOException {
		 S3Object s3Object = s3Client.getObject(bucketName, pathname);
	        if (s3Object == null) {
	            return ResponseEntity.notFound().build();
	        }

	        InputStream inputStream = s3Object.getObjectContent();
	        try {
	            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	            byte[] buffer = new byte[1024]; // Adjust buffer size as needed
	            int len;
	            while ((len = inputStream.read(buffer)) != -1) {
	                outputStream.write(buffer, 0, len);
	            }
	            byte[] byteArray = outputStream.toByteArray();

	            return ResponseEntity.ok()
	                    .contentLength(byteArray.length)
	                    .contentType(MediaType.APPLICATION_PDF) // Adjust based on file extension
	                    .cacheControl(CacheControl.noCache())
	                    .header("Content-Disposition", "attachment; filename=" + pathname)
	                    .body(byteArray);
	        } catch (IOException e) {
	            // Handle any IO exceptions
	            // You may want to log the exception or return an error response
	            e.printStackTrace(); // Example of logging the exception
	            throw e; // Rethrow the exception to be handled by the caller
	        } finally {
	            // Close the input stream after the response has been sent or if an exception occurs
	            if (inputStream != null) {
	                inputStream.close();
	            }
	        }
	}

	@Override
	public List<Journal> getJournalsByUser(String email) {
		// TODO Auto-generated method stub
		return journalRepo.findByUserEmail(email);
	}

	@Override
	public void deleteJournalById(Long journalId) throws FileNotFoundException {
		Journal journal=journalRepo.findById(journalId).orElseThrow(
				()-> new JournalNotFoundException("no journal found"));
		String pathname=journal.getPathName();
		try {
			s3Client.deleteObject(new DeleteObjectRequest(bucketName,pathname));
			journalRepo.deleteById(journalId);
		}catch(AmazonS3Exception ase) {
			 // Handle potential exceptions (e.g., file not found)
            if (ase.getStatusCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                // Log or handle the case where the file doesn't exist
                throw new FileNotFoundException("File not found in S3: " + bucketName + "/" + pathname);
            } else {
                throw ase; // Re-throw other exceptions
            }
		}
	
		
	}

	@Override
	public void updateStatusById(Long journalId,String status) {
		Journal journal=journalRepo.findById(journalId).orElseThrow(
				()->new JournalNotFoundException("no journal found with this id "));
		journal.setStatus(status);
		journalRepo.save(journal);
	}

}
