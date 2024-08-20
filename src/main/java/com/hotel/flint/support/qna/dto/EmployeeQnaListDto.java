package com.hotel.flint.support.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeQnaListDto { // 직원이 보는 Qna 전체 목록 조회

    private Long id;
    private String title;
    private String memberEmail;
    private LocalDateTime writeTime;
}
