package com.example.simpleBoard.question;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.example.simpleBoard.answer.AnswerForm;
import com.example.simpleBoard.user.SiteUser;
import com.example.simpleBoard.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/question")
public class QuestionController {	
	private final QuestionService questionService;
	private final UserService userService;

// region list
	@GetMapping("/list")
	public String list(
			Model model, 
			UserDetails userDetails,
			@RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="kw", defaultValue="") String kw
	) {
		if (userDetails != null) {
			SiteUser user = userService.getUser(userDetails.getUsername());
			model.addAttribute("profileImage", user.getImageUrl());
		}
		
		Page<Question> paging = this.questionService.getList(page, kw);
		model.addAttribute("paging", paging);
		return "question_list";
	}
// endregion
	
// region detail
	@GetMapping("/detail/{id}")
	public String detail(
		Model model, 
		UserDetails userDetails,
		@PathVariable("id") Integer id, 
		AnswerForm answerForm,
		Principal principal
	) {
		if (userDetails != null) {
			SiteUser user = userService.getUser(userDetails.getUsername());
			model.addAttribute("profileImage", user.getImageUrl());
		}
		
		Question question = this.questionService.getQuestion(id);
		SiteUser siteUser = principal == null ? null : this.userService.getUser(principal.getName());
		model.addAttribute("question", question);
		model.addAttribute("siteUser", siteUser);
		return "question_detail";
	}
// endregion

// region 질문등록
	// 질문 등록 get
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String questionCreate(QuestionForm questionForm) {
		return "question_form";
	}
	
	// 질문 등록 post
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String questionCreate(
			@Valid QuestionForm questionForm, 
			BindingResult bindingResult, 
			Principal principal // 유저 이름 가져오는 용도
	) {
		if (bindingResult.hasErrors()) {
			return "question_form";
		}
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
		return "redirect:/question/list";
	}
// endregion 	
	
// region 질문 수정
	// 질문 수정 get
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String questionModify(
			QuestionForm questionForm, 
			@PathVariable("id") Integer id, 
			Principal principal
	) {
		Question question = this.questionService.getQuestion(id);
		if (!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		questionForm.setSubject(question.getSubject());
		questionForm.setContent(question.getContent());
		return "question_form";
	}
	// 질문 수정 post
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String questionModify(
			@Valid QuestionForm questionForm, 
			BindingResult bindingResult, 
			@PathVariable("id") Integer id, 
			Principal principal
	) {
		Question question = this.questionService.getQuestion(id);
		if (!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
		
		return String.format("redirect:/question/detail/%s", id);
	}
// endregion
	
// region 질문 삭제
	// 질문 삭제 get
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String questionDelete(
			@PathVariable("id") Integer id, 
			Principal principal
	) {
		Question question = this.questionService.getQuestion(id);
		if (!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
		}
		this.questionService.delete(question);
		
		return "redirect:/question/list";
		
	}
// #endregion
	
// #region 질문 추천
	// 질문 추천
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}")
	public String questionVote(
			Principal principal,
			@PathVariable("id") Integer id
	) {
		Question question = this.questionService.getQuestion(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.questionService.vote(question, siteUser);
		return String.format("redirect:/question/detail/%s", id);
	}
// #endregion
}
