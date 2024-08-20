package com.hotel.flint.user.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDetResDto {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String nation;
    private String password;
    private LocalDate birthday;
}

