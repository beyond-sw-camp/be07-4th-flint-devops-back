package com.hotel.flint.support.qna.dto;

import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.common.enumdir.Service;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QnaDetailDto {

    private Long id;
    private Service service;
    private String title;
    private String contents;
    private LocalDateTime writeTime;
    private String memberEmail; // 작성자

    // 답변부분 추가
    private Option respond;
    private String answer;

}
