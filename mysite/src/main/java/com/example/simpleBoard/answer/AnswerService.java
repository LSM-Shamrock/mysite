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
	
	public Answer create(Question question, String content, SiteUser author) {
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setCreateDate(LocalDateTime.now());
		answer.setQuestion(question);
		answer.setAuthor(author);
		this.answerRepository.save(answer);
		return answer;
	}

	// 답변 조회
	public Answer getAnswer(Integer id) {
		Optional<Answer> optional = this.answerRepository.findById(id);
		if (optional.isPresent()) {
			return optional.get();
		} 
		else {
			throw new DataNotFoundException("answer not found");
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

	// 답변 추천
	public void vote(Answer answer, SiteUser siteUser) {
		if (answer.getVoter().contains(siteUser)) {
			answer.getVoter().remove(siteUser);
		}
		else {
			answer.getVoter().add(siteUser);
		}
		this.answerRepository.save(answer);
	}
	
	
}
