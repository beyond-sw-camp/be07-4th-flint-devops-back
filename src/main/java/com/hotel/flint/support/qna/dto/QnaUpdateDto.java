package com.hotel.flint.support.qna.dto;

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
public class QnaUpdateDto {

    // 사용한 서비스, 제목, 내용만 수정 가능
    private Service service;
    private String title;
    private String contents;

}
