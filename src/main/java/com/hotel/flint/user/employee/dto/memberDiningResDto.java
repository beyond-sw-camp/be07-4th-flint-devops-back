package com.hotel.flint.user.employee.dto;

import com.hotel.flint.common.enumdir.DiningName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class memberDiningResDto {
    private LocalDateTime reservationDateTime;
    private Long id;
    private String firstname;
    private String lastname;
    private DiningName diningName;
    private String email;
    private String phoneNumber;
    private int adult;
    private int child;
    private String comment;

}
