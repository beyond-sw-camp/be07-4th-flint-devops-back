package com.hotel.flint.user.member.dto;

import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.user.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberSignUpDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String nation;
    private String password;
    private String birthday; // LocalDate를 String으로 변환

    public Member toEntity(String password){
        Member member = Member.builder()
                .email(this.email)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .birthday(LocalDate.parse(this.birthday)) // String을 LocalDate로 변환
                .phoneNumber(this.phoneNumber)
                .password(password)
                .nation(this.nation)
                .delYN(Option.N)
                .build();
        return member;
    }
}