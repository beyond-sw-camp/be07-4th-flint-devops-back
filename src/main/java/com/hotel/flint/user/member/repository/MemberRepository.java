package com.hotel.flint.user.member.repository;


import com.hotel.flint.common.enumdir.Option;

import com.hotel.flint.user.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndDelYN(String email, Option delYN);
    Optional<Member> findByPhoneNumberAndDelYN(String phoneNumber, Option delYN);
    Optional<Member> findByIdAndDelYN(Member memberId, Option delYN);

    Optional<Member> findById(Member memberId);
}
