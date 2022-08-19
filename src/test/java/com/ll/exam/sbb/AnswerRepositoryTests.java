package com.ll.exam.sbb;

import com.ll.exam.sbb.answer.Answer;
import com.ll.exam.sbb.answer.AnswerRepository;
import com.ll.exam.sbb.question.Question;
import com.ll.exam.sbb.question.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AnswerRepositoryTests {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    private int lastSampleDataId;
    @BeforeEach
    void beforeEach() {
        clearData();
        createSampleData();
    }


    public static void clearData(AnswerRepository answerRepository, QuestionRepository questionRepository) {
        QuestionRepositoryTests.clearData(questionRepository);
        answerRepository.deleteAll(); // DELETE FROM question;
        answerRepository.truncateTable();
    }


    //static이 붙지 않은(내부에서만 사용하는) 메서드를 따로 생성하고 싶지 않다면
    // beforeEach()의 clearData()안에 인수로
    // answerRepository, questionRepository를 전달하는 방법도 있음
    private void clearData() {
        clearData(answerRepository, questionRepository);
    }

    private void createSampleData() {
        QuestionRepositoryTests.createSampleData(questionRepository);
        Question q = questionRepository.findById(1L).get(); // 관련 답변이 하나없는 상태에서 쿼리 발생
        Answer a1 = new Answer();
        a1.setContent("sbb는 질문답변 게시판 입니다.");
        a1.setCreateDate(LocalDateTime.now());
        q.addAnswer(a1);

        Answer a2 = new Answer();
        a2.setContent("sbb에서는 주로 스프링부트관련 내용을 다룹니다.");
        a2.setCreateDate(LocalDateTime.now());
        q.addAnswer(a2);

        questionRepository.save(q);
    }

    @Test
    @Transactional
    @Rollback(false)
    void 저장() {
        Question q = questionRepository.findById(2L).get();

        Answer a1 = new Answer();
        a1.setContent("네 자동으로 생성됩니다.");
        a1.setCreateDate(LocalDateTime.now());
        q.addAnswer(a1);

        Answer a2 = new Answer();
        a2.setContent("네네~ 맞아요!");
        a2.setCreateDate(LocalDateTime.now());
        q.addAnswer(a2);

        questionRepository.save(q);
    }

    @Test
    @Transactional
    @Rollback(false)
    void 조회() {
        Answer a = this.answerRepository.findById(1L).get();
        assertThat(a.getContent()).isEqualTo("sbb는 질문답변 게시판 입니다.");
    }
    @Test
    @Transactional
    @Rollback(false)
    void 관련된_question_조회() {
        Answer a = this.answerRepository.findById(1L).get(); // DB통신
        Question q = a.getQuestion(); // DB통신하지 않고 a로부터 question(엄밀히 말하면 Question.id) 바로 가져올 수 있음

        assertThat(q.getId()).isEqualTo(1);
    }

    @Transactional
    @Rollback(false)
    @Test
    void question으로부터_관련된_질문들_조회() {
        Question q = questionRepository.findById(1L).get();

        List<Answer> answerList = q.getAnswerList();

        assertThat(answerList.size()).isEqualTo(2);
        assertThat(answerList.get(0).getContent()).isEqualTo("sbb는 질문답변 게시판 입니다.");
    }
}