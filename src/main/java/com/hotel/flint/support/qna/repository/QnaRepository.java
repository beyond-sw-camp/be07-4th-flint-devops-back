package com.hotel.flint.support.qna.repository;


import com.hotel.flint.support.qna.domain.QnA;
import com.hotel.flint.user.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QnaRepository extends JpaRepository<QnA, Long> {

    Page<QnA> findByMember(Pageable pageable, Member member);
    Optional<QnA> findByIdAndMember(Long id, Member member);
    Page<QnA> findAll(Specification specification, Pageable pageable);
}
