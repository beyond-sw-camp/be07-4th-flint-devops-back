package com.hotel.flint.support.qna.dto;

import com.hotel.flint.support.qna.domain.QnA;
import com.hotel.flint.user.employee.domain.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAnswerDto {

    private LocalDateTime answerTime;
    private String answer;

    public QnA toEntity(QnA qnaEntity, Employee employee) {
        return QnA.builder()
                .id(qnaEntity.getId())
                .service(qnaEntity.getService())
                .title(qnaEntity.getTitle())
                .contents(qnaEntity.getContents())
                .member(qnaEntity.getMember())
                .writeTime(qnaEntity.getWriteTime())
                .employee(employee)  // 새로운 답변 작성자 설정
                .respond(qnaEntity.getRespond())
                .answerTime(this.answerTime)  // 새로운 답변 시간 설정
                .answer(this.answer)  // 새로운 답변 내용 설정
                .build();
    }

}
