package com.example.simpleBoard.answer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.simpleBoard.question.Question;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
	List<Answer> findByQuestion(Question question);
}
