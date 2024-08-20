package com.hotel.flint.reserve.room.repository;

import com.hotel.flint.reserve.room.domain.RoomReservation;
import com.hotel.flint.user.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomReservationRepository extends JpaRepository<RoomReservation, Long> {

    Optional<RoomReservation> findByMember(Member member);
    Page<RoomReservation> findByMember(Pageable pageable, Member member);
    Optional<RoomReservation> findByIdAndMember(Long id, Member member);
}