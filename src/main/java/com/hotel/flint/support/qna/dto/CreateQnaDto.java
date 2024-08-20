package com.hotel.flint.support.qna.dto;

import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.common.enumdir.Service;
import com.hotel.flint.support.qna.domain.QnA;
import com.hotel.flint.user.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateQnaDto {

    private Service service; // 이용하는 서비스 (=qna를 남길 서비스)
    private String title;
    private String contents;
    private Option response = Option.N;
    private LocalDateTime writeTime;

    public QnA toEntity(Member member) {
        QnA qna = QnA.builder()
                .service(this.service)
                .title(this.title)
                .contents(this.contents)
                .respond(this.response)
                .writeTime(this.writeTime)
                .member(member)
                .build();
        return qna;
    }

}
