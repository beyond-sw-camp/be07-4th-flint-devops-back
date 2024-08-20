package com.hotel.flint.user.employee.dto;

import com.hotel.flint.common.enumdir.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeListResDto {

    private Long id;
    private String empNo;
    private String name;
    private Department department;
}
