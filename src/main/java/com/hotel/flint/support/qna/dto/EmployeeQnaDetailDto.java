package com.hotel.flint.support.qna.dto;

import com.hotel.flint.common.enumdir.Service;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class EmployeeQnaDetailDto {

    private String title;
    private String contents;
    private String memberEmail;
    private LocalDateTime writeTime;
    private Service service;

    private String answer;
    private String employeeName;
    private LocalDateTime answerTime;


}
