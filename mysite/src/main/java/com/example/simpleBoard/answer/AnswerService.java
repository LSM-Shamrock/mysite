package com.example.simpleBoard.answer;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.simpleBoard.DataNotFoundException;
import com.example.simpleBoard.question.Question;
import com.example.simpleBoard.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnswerService {
	private final AnswerRepository answerRepository;
	
	public void create(Question question, String content, SiteUser author) {
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setCreateDate(LocalDateTime.now());
		answer.setQuestion(question);
		answer.setAuthor(author);
		this.answerRepository.save(answer);
	}

	public Answer getAnswer(Integer id) {
		Optional<Answer> optional = this.answerRepository.findById(id);
		if (optional.isPresent()) {
			return optional.get();
		} 
		else {
			throw new DataNotFoundException("question not found");
		}
	}
	
	// 답변 수정 로직
	public void modify(Answer answer, String content) {
		answer.setContent(content);
		answer.setModifyDate(LocalDateTime.now());
		this.answerRepository.save(answer);
	}
	// 답변 삭제 로직
	public void delete(Answer answer) {
		this.answerRepository.delete(answer);
	}
}
