package com.hotel.flint.user.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
// 직원 비밀번호 수정 Dto
public class EmployeeModResDto {
    private String beforePassword;
    private String afterPassword;
}