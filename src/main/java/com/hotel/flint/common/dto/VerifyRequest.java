package com.hotel.flint.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
// 가입 시 Body에 인증받은 이메일과 코드를 담아서 보내야함
public class VerifyRequest {
    private String email;
    private String authCode;
}
