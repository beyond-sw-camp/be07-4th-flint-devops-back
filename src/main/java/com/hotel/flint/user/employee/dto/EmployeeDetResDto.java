package com.hotel.flint.user.employee.dto;

import com.hotel.flint.common.enumdir.Department;
import com.hotel.flint.common.enumdir.EmployeeRank;
import com.hotel.flint.common.enumdir.Gender;
import com.hotel.flint.common.enumdir.Option;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 직원의 상세정보를 반환해주는 Dto
public class EmployeeDetResDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String employeeNumber;
    private EmployeeRank employeeRank;
    private String email;
    private String phoneNumber;
    private LocalDate birthday;
    private LocalDate dateOfEmployment;
    private Gender gender;
    private Department department;
    private Option delYN;
}