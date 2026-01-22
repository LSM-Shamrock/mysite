package com.example.simpleBoard.answer;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.example.simpleBoard.question.Question;
import com.example.simpleBoard.question.QuestionForm;
import com.example.simpleBoard.question.QuestionService;
import com.example.simpleBoard.user.SiteUser;
import com.example.simpleBoard.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/answer")
@Controller
@RequiredArgsConstructor
public class AnswerController {

	private final UserService userService;
    private final AnswerService answerService;
	private final QuestionService questionService;

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{id}")
	public String createAnswer(
			Model model, 
			@PathVariable("id") Integer id, 
			@Valid AnswerForm answerForm, 
			BindingResult bindingResult,
			Principal principal // 유저 이름 가져오는 용도
	) {
		Question question = this.questionService.getQuestion(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		if (bindingResult.hasErrors()) {
			model.addAttribute(question);
			return "question_detail";
		}
		this.answerService.create(question, answerForm.getContent(), siteUser);
		return "redirect:/question/detail/"+id;
	}
	
	
	// 답변 수정 get
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String answerModify(
			AnswerForm answerForm, 
			@PathVariable("id") Integer id, 
			Principal principal
	) {
		Answer answer = this.answerService.getAnswer(id);
		if (!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		answerForm.setContent(answer.getContent());
		return "answer_form";
	}
	// 답변 수정 post
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String answerModify(
			@Valid AnswerForm answerForm, 
			BindingResult bindingResult, 
			@PathVariable("id") Integer id, 
			Principal principal
	) {
		Answer answer = this.answerService.getAnswer(id);
		if (!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		this.answerService.modify(answer, answerForm.getContent());

		int questionId = answer.getQuestion().getId();
		return String.format("redirect:/question/detail/%s", questionId);
	}
	
	// 답변 삭제 get
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String answerDelete(
			@PathVariable("id") Integer id, 
			Principal principal
	) {
		Answer answer = this.answerService.getAnswer(id);
		if (!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
		}
		this.answerService.delete(answer);
		
		int questionId = answer.getQuestion().getId();
		return String.format("redirect:/question/detail/%s", questionId);
		
	}
}
