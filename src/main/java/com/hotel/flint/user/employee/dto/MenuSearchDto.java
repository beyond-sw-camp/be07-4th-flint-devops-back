package com.hotel.flint.user.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuSearchDto {
    private String menuName;
    private Long id;
}
