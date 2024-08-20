package com.hotel.flint.user.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeToMemberDetailDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String nation;
    private LocalDate birthday;
}
