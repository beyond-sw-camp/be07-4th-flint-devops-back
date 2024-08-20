package com.hotel.flint.reserve.dining.repository;

import com.hotel.flint.dining.domain.Dining;
import com.hotel.flint.reserve.dining.domain.DiningReservation;
import com.hotel.flint.user.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiningReservationRepository extends JpaRepository<DiningReservation, Long> {
    List<DiningReservation> findByMemberId(Member member);
    Page<DiningReservation> findAll(Pageable pageable);
    Page<DiningReservation> findByMemberIdAndDiningId(Member memberId, Dining diningId, Pageable pageable);
    Page<DiningReservation> findAllByMemberId(Member member ,Pageable pageable);
}