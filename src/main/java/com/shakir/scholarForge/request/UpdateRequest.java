package com.shakir.scholarForge.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequest {
	private String firstName;
	private String lastName;
	private String email;
	private String contactNo;
	private String occupation;
	

}
