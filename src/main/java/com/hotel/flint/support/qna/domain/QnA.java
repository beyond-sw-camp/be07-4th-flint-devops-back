package com.hotel.flint.support.qna.domain;

import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.common.enumdir.Service;
import com.hotel.flint.support.qna.dto.*;
import com.hotel.flint.user.employee.domain.Employee;
import com.hotel.flint.user.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QnA {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Service service; // 이용한 서비스

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 3000)
    private String contents;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Option respond = Option.N; // 응답 여부

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime writeTime; // qna 작성시간

    private LocalDateTime answerTime; // 답변 작성시간 - 답변 전에는 null일 수 있음

    @Column(length = 3000)
    private String answer; // 답변 전에는 null일 수 있음

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee; // 답변 전에는 null일 수 있음

    public QnaListDto listFromEntity(Long no) {
        QnaListDto qnaListDto = QnaListDto.builder()
                .id(this.id)
                .no(no)
                .title(this.title)
                .memberEmail(this.getMember().getEmail())
                .writeTime(this.writeTime)
                .build();
        return qnaListDto;
    }

    public QnaDetailDto detailFromEntity(String email) {
        QnaDetailDto qnaDetailDto = QnaDetailDto.builder()
                .id(this.id)
                .service(this.service)
                .title(this.title)
                .contents(this.contents)
                .writeTime(this.writeTime)
                .memberEmail(email)
                .respond(this.respond)
                .answer(this.answer)
                .build();
        return qnaDetailDto;
    }

    public QnA updateFromEntity(QnaUpdateDto dto) {

        this.service = dto.getService();
        this.title = dto.getTitle();
        this.contents = dto.getContents();

        return this;
    }


    public void DeleteQna(){

        this.answer = null;
        this.answerTime = null;
        this.respond = Option.N;
        this.employee = null;
    }

    public EmployeeQnaListDto ListEntity(){
        return EmployeeQnaListDto.builder()
                .id(this.id)
                .title(this.title)
                .memberEmail(this.member.getEmail())
                .writeTime(this.writeTime)
                .build();
    }

    public EmployeeQnaDetailDto DetailEntity() {
        return EmployeeQnaDetailDto.builder()
                .title(this.title)
                .contents(this.contents)
                .memberEmail(this.member != null ? this.member.getEmail() : null)
                .service(this.service)
                .writeTime(this.writeTime)
                .answer((this.answer != null && !this.answer.isEmpty()) ? this.answer : null)
                .answerTime(this.answerTime != null ? this.answerTime : null)
                .employeeName((this.employee != null && (this.employee.getFirstName() != null || this.employee.getLastName() != null))
                        ? (this.employee.getFirstName() + this.employee.getLastName())
                        : null)
                .build();
    }

}
