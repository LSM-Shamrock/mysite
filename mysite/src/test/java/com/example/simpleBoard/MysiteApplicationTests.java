package com.example.simpleBoard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.simpleBoard.answer.Answer;
import com.example.simpleBoard.answer.AnswerRepository;
import com.example.simpleBoard.question.Question;
import com.example.simpleBoard.question.QuestionRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
class MysiteApplicationTests {
	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private AnswerRepository answerRepository;
	
	@Test
//	@Transactional
	void contextLoads() {
//		Question q;
//		q = new Question();
//		q.setSubject("궁금합니다.");
//		q.setContent("질문 내용입니다.");
//		q.setCreateDate(LocalDateTime.now());		
//		this.questionRepository.save(q);
//		
//		q = new Question();
//		q.setSubject("git공부가 필요합니다..");
//		q.setContent("git에 대한 질문 내용입니다.");
//		q.setCreateDate(LocalDateTime.now());		
//		this.questionRepository.save(q);
		
//		List<Question> all = this.questionRepository.findAll();
//		assertEquals(4, all.size());
//		
//		Question q = all.get(0);
//		assertEquals("궁금합니다.", q.getSubject());
		
//		Optional<Question> op = this.questionRepository.findById(4);
//		if (op.isPresent()) {
//			Question q = op.get();
//			assertEquals("git에 대한 질문 내용입니다.", q.getContent());
//		}


//		Question q = this.questionRepository.findBySubjectAndContent("궁금합니다.", "질문 내용입니다.");
//		assertEquals(3, q.getId());

//		List<Question> qList = this.questionRepository.findBySubjectLike("%니다%");
//		Question q = qList.get(0);
//		assertEquals(3, q.getId());
		
		// 옵셔널 : T타입 필드에 값이 있을 수도, 없을 수도 있을 때 Optional<T>로 반환 
//		Optional<Question> optional = this.questionRepository.findById(3);
//		assertTrue(optional.isPresent());
//		Question question = optional.get(); 
//		question.setSubject("수정된 제목");
//		this.questionRepository.save(question);
		


//		Optional<Question> optional = this.questionRepository.findById(3);
//		assertTrue(optional.isPresent());
//		Question question = optional.get(); 
//
//		
//		Answer answer;
//		answer = new Answer();
//		answer.setContent("질문 내용입니다.");
//		answer.setCreateDate(LocalDateTime.now());
//		answer.setQuestion(question);		
//		this.answerRepository.save(answer);
		

		
//		for (int i=0; i<300; i++) {
//			Question q = new Question();
//			q.setSubject("테스트 코드를 이용해 생성한 내용:[내용"+i+"]");
//			q.setContent("테스트 코드를 이용해 생성한 제목:[제목"+i+"]");
//			q.setCreateDate(LocalDateTime.now());		
//			this.questionRepository.save(q);
//		}
		
//		Optional<Question> optional = this.questionRepository.findById(3);
//		assertTrue(optional.isPresent());
//		Question question = optional.get(); 
//		List<Answer> aList = question.getAnswerList();
//		
//		
//		for (Answer a : aList) {
//			System.out.println("ID:3인 질문의 답글 내용: "+a.getContent());
//		}
//		System.out.println("ID:3인 질문의 답글 개수: "+aList.size());
	}

}
