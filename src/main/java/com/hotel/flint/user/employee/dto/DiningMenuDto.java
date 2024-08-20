package com.hotel.flint.user.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiningMenuDto {
    private Long menuId;
    private String menuName;
    private Integer cost;

}
