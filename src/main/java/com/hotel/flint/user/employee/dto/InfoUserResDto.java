package com.hotel.flint.user.employee.dto;

import com.hotel.flint.reserve.dining.domain.DiningReservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
// 직원이 member의 정보를 보기 위한 dto
public class InfoUserResDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String nation;
    private LocalDate birthday;
    private int countRoomReservation;
    private int countDiningReservation;
}
