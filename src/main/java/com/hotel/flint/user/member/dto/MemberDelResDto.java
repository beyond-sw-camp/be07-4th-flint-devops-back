package com.hotel.flint.user.member.dto;

import com.hotel.flint.common.enumdir.Option;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDelResDto {
    private Option delYn;
}
