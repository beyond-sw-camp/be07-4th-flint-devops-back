package com.hotel.flint.user.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeToMemberListDto {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
}
