package com.hotel.flint.user.employee.dto;


import com.hotel.flint.common.enumdir.EmployeeRank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 직원의 직급 수정 Dto
public class EmployeeRankModResDto {
    private Long targetId;
    private EmployeeRank employeeRank;
}