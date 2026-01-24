package com.example.simpleBoard.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.simpleBoard.DataNotFoundException;
import com.example.simpleBoard.answer.Answer;
import com.example.simpleBoard.user.SiteUser;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {
	private final QuestionRepository questionRepository;
	
	public Page<Question> getList(int page, String kw) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		sorts.add(Sort.Order.desc("id"));
		
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		
		Specification<Question> specification = search(kw);
		
		return this.questionRepository.findAll(specification, pageable);
	}
	
	public Question getQuestion(Integer id) {
		Optional<Question> optional = this.questionRepository.findById(id);
		if (optional.isPresent()) {
			return optional.get();
		} 
		else {
			throw new DataNotFoundException("question not found");
		}
	}

	
	// 질문 저장 로직
	public void create(String subject, String content, SiteUser user) {
		Question question = new Question();
		question.setSubject(subject);
		question.setContent(content);
		question.setCreateDate(LocalDateTime.now());
		question.setAuthor(user);
		this.questionRepository.save(question);
	}
	// 질문 수정 로직
	public void modify(Question question, String subject, String content) {
		question.setSubject(subject);
		question.setContent(content);
		question.setModifyDate(LocalDateTime.now());
		this.questionRepository.save(question);
	}
	// 질문 삭제 로직
	public void delete(Question question) {
		this.questionRepository.delete(question);
	}
	
	// 질문 추천
	public void vote(Question question, SiteUser siteUser) {
		
		if (question.getVoter().contains(siteUser)) {
			question.getVoter().remove(siteUser);
		}
		else {
			question.getVoter().add(siteUser);
		}
		this.questionRepository.save(question);
	}
	
	private Specification<Question> search(String kw) {
		return new Specification<Question>() {
			private static final long serialVersionID = 1L;
			
			@Override
			public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
				Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
				Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
				
				return 	cb.or(cb.like(q.get("subject"), "%"+kw+"%"),
						cb.like(q.get("content"), "%"+kw+"%"),
						cb.like(u1.get("username"), "%"+kw+"%"),
						cb.like(a.get("content"), "%"+kw+"%"),
						cb.like(u2.get("username"), "%"+kw+"%"));
			}
		};
	}
}
