package com.hotel.flint.user.employee.dto;

import com.hotel.flint.common.enumdir.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSearchDto {
    private String email;
    private String employeeNumber;
    private Department department;
}
