package com.hotel.flint.support.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QnaListDto {

    private Long id;
    private Long no;
    private String title;
    private String memberEmail;
    private LocalDateTime writeTime;
}
