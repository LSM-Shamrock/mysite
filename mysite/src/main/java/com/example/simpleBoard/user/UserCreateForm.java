package com.example.simpleBoard.user;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {	
	
	@Size(min=3, max=25)
	@NotEmpty(message="사용자 ID는 필수 항목입력니다.")
	private String username;

	@NotEmpty(message="비밀번호는 항목입력니다.")
	private String password;

	@NotEmpty(message="비밀번호 확인은 필수 항목입력니다.")
	private String password2;

	@NotEmpty(message="이메일은 필수 항목입력니다.")
	@Email
	private String email;

	private MultipartFile imageFile;
}
