package com.hotel.flint.reserve.room.service;


import com.hotel.flint.common.enumdir.Department;
import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.common.enumdir.RoomView;
import com.hotel.flint.common.enumdir.Season;
import com.hotel.flint.reserve.dining.domain.DiningReservation;
import com.hotel.flint.reserve.room.controller.RoomSSEController;
import com.hotel.flint.reserve.room.domain.*;
import com.hotel.flint.reserve.room.dto.PossibleRoomDto;
import com.hotel.flint.reserve.room.dto.RoomReservedDetailDto;
import com.hotel.flint.reserve.room.dto.RoomReservedDto;
import com.hotel.flint.reserve.room.dto.RoomReservedListDto;
import com.hotel.flint.reserve.room.repository.*;
import com.hotel.flint.room.domain.RoomDetails;
import com.hotel.flint.room.domain.RoomInfo;
import com.hotel.flint.room.domain.RoomPrice;
import com.hotel.flint.room.repository.RoomDetailsRepository;
import com.hotel.flint.room.repository.RoomInfoRepository;
import com.hotel.flint.room.repository.RoomPriceRepository;
import com.hotel.flint.user.employee.dto.InfoRoomDetResDto;
import com.hotel.flint.user.employee.dto.memberDiningResDto;
import com.hotel.flint.user.employee.domain.Employee;
import com.hotel.flint.user.employee.repository.EmployeeRepository;
import com.hotel.flint.user.member.domain.Member;
import com.hotel.flint.user.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.security.Security;
import java.sql.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional(readOnly = true)
@Slf4j
public class RoomReservedService {

    private final RoomReservationRepository roomReservationRepository;
    private final RoomDetailsRepository roomDetailsRepository;
    private final RoomPriceRepository roomPriceRepository;
    private final RoomInfoRepository roomInfoRepository;
    private final MemberRepository memberRepository;
    private final CheckReservedDateRepository checkReservedDateRepository;

    private final HolidayService holidayService;
    private final SeasonService seasonService;
    // sse controller
    private final RoomSSEController roomSSEController;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public RoomReservedService (RoomReservationRepository roomReservationRepository,
                                RoomDetailsRepository roomDetailsRepository,
                                RoomPriceRepository roomPriceRepository,
                                RoomInfoRepository roomInfoRepository,
                                MemberRepository memberRepository,
                                CheckReservedDateRepository checkReservedDateRepository,
                                HolidayService holidayService,
                                SeasonService seasonService, RoomSSEController roomSSEController, EmployeeRepository employeeRepository) {
        this.roomReservationRepository = roomReservationRepository;
        this.roomDetailsRepository = roomDetailsRepository;
        this.roomPriceRepository = roomPriceRepository;
        this.roomInfoRepository = roomInfoRepository;
        this.memberRepository = memberRepository;
        this.checkReservedDateRepository = checkReservedDateRepository;

        this.holidayService = holidayService;
        this.seasonService = seasonService;
        this.roomSSEController = roomSSEController;
        this.employeeRepository = employeeRepository;
    }

    /**
     * 룸 예약 진행
     */
    @Transactional
    public long roomReservation(RoomReservedDto dto) {

        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // user 찾기
        Member member = memberRepository.findByEmailAndDelYN(memberEmail, Option.N).orElseThrow(
                () -> new EntityNotFoundException("해당 email에 대한 회원이 존재하지 않습니다.")
        );
        // RoomDetail 찾아오기
        RoomDetails roomDetails = roomDetailsRepository.findById(dto.getRoomId()).orElseThrow(
                () -> new EntityNotFoundException("해당 id의 방이 존재하지 않습니다.")
        );

        // 수용할 수 있는 최대 인원수 체크하기 => 객실 조회에도 추가
        if (roomDetails.getMaxOccupancy() < dto.getAdultCnt() + dto.getChildCnt()) {
            throw new IllegalArgumentException("최대 수용 가능한 인원 수를 초과하였습니다.");
        }

        // 해당 날짜에 해당 객실을 예약할 수 있는지 날짜별로 확인 ⭐⭐
        if (!checkReserved(dto, roomDetails)) {
            throw new IllegalArgumentException("선택하신 날짜에 해당 객실을 이용하실 수 없습니다.");
        }

        RoomReservation roomReservation = dto.toEntity(member, roomDetails);
        log.info("toEntity넘어감");
        RoomReservation savedRoomReservation = roomReservationRepository.save(roomReservation);

        // 주문 된 후 알림 - Room부서인 직원 List 가져오기
        List<Employee> roomEmployeeList = employeeRepository.findByDepartment(Department.Room);
        roomSSEController.publishMessage(savedRoomReservation.detailFromEntity(), roomEmployeeList);
        // 날짜 가져가서 계산
        long totalPrice = calculatePrice(dto);

        return totalPrice;

    }

    /**
     * 예약 가능한 날짜 및 객실인지 확인
     */
    private boolean checkReserved(RoomReservedDto dto, RoomDetails roomDetails) {

        // 체크인,아웃 날짜
        LocalDate checkInDate = dto.getCheckInDate();
        LocalDate checkOutDate = dto.getCheckOutDate();

        while (checkInDate.isBefore(checkOutDate)) { // checkInDate < checkOutDate
            if (checkReservedDateRepository.findByDateAndRooms(checkInDate, roomDetails).isPresent()) {
                log.info("이 날짜 안됨 : " + checkInDate);
                return false;
            }
            checkInDate = checkInDate.plusDays(1);
        }

        // 모든 날짜에 대해 예약이 가능한 경우 예약 데이터 저장
        checkInDate = dto.getCheckInDate();
        while (checkInDate.isBefore(checkOutDate)) {
            ReservedRoom reservedRoom = dto.toEntity(checkInDate, roomDetails);
            checkReservedDateRepository.save(reservedRoom);
            checkInDate = checkInDate.plusDays(1);
        }
        return true;
    }

    /**
     * 객실 가격 조회용
     */
    public long getPrice(RoomReservedDto dto) {
        return calculatePrice(dto);
    }


    /**
     * 예약 룸 총액 계산
     */
    private long calculatePrice(RoomReservedDto dto) {

        // 해당 방의 base 가격 가져오기
        long roomBasePrice = getBasePrice(dto.getRoomId());
        long total = 0;

        // 체크인,아웃 날짜
        LocalDate checkInDate= dto.getCheckInDate();
        LocalDate checkOutDate = dto.getCheckOutDate();

        // 체크인 체크아웃 날짜를 비교해서 holiday, season 결과 가져오기
        // 뷰 찾기
        Long roomId = dto.getRoomId();
        RoomDetails roomDetails = roomDetailsRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("해당 id의 방이 없습니다."));
        while (checkInDate.isBefore(checkOutDate)) { // checkInDate <= checkOutdate
            boolean isHoliday = holidayService.isHoliday(checkInDate);
            boolean isSeason = seasonService.isSeason(checkInDate);
            log.info("Checking date: {} isHoliday: {}, isSeason: {}", checkInDate, isHoliday, isSeason);

            double percentage = getAdditionalPercentage(roomDetails.getId(), roomDetails.getRoomInfo(), isHoliday, isSeason);

            // 총액 계산
            log.info("roomBasePrice " + roomBasePrice);
            log.info("percentage " + percentage);
            total += (long)(roomBasePrice * percentage);
            log.info("total :" + total);
            checkInDate = checkInDate.plusDays(1); // 체크인날짜 +1 (체크아웃 전까지)
        }

        // 조식 금액 추가하기
//        int adultBfCnt = dto.getAdultBfCnt();
//        int childBfCnt = dto.getChildBfCnt();
//
//        double bf_total = (adultBfCnt * 50000) + (childBfCnt * 35000);
//        log.info("조식 총가격 :" + bf_total);
//
//        total += bf_total;
//        log.info("조식 + 객실 total :" + total);

        return total;
    }

    /**
     * 룸의 원가 가져오기
     */
    private long getBasePrice(Long id) {

        // room_id를 가지고 해당 방의 detail 정보를 반환
        RoomDetails room = roomDetailsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 방이 없습니다."));

        return room.getRoomInfo().getRoomTypePrice();
    }

    /**
     * 각 방의 정보 확인해서 퍼센티지 가져오기
     */
    private double getAdditionalPercentage(Long roomId, RoomInfo roomInfo, boolean isHoliday, boolean isSeason) {
        log.info("isHoliday: " + isHoliday);
        Option holiday = isHoliday ? Option.Y : Option.N;
        Season season = isSeason ? Season.PEAK : Season.ROW;
        RoomDetails findRoom = roomDetailsRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("해당 id의 방이 없습니다."));
        RoomView view = findRoom.getRoomView();
        log.info("holiday : " + holiday);
        log.info("season : " + season);
        log.info("view : " + view);

        RoomPrice percentage = roomPriceRepository.findByIsHolidayAndSeasonAndRoomViewAndRoomInfo(holiday, season, view, roomInfo);

        return percentage != null ? percentage.getAdditionalPercentage() : 1.0;

    }

//    예약 단건 조회
    public InfoRoomDetResDto roomDetail(Long id){
        RoomReservation roomReservation = roomReservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 예약 ID 내역이 없습니다."));

        return roomReservation.toInfoRoomDetResEntity();
    }

    /**
     * 객실 예약 취소
     */
    @Transactional
    public void delete(Long roomReservedId) {

        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        // user 찾기
        Member member = memberRepository.findByEmailAndDelYN(memberEmail, Option.N).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 없음")
        );
        RoomReservation roomReservation = roomReservationRepository.findByIdAndMember(roomReservedId, member).orElseThrow(
                () -> new IllegalArgumentException("해당 id의 예약 내역 없음")
        );
        // 객실 예약 내역 취소
        roomReservationRepository.delete(roomReservation);

        // 예약된 객실 테이블에서도 삭제
        LocalDate checkInDate = roomReservation.getCheckInDate();
        LocalDate checkOutDate = roomReservation.getCheckOutDate();
        RoomDetails roomDetails = roomReservation.getRooms();

        while (checkInDate.isBefore(checkOutDate)) {
            checkReservedDateRepository.deleteByDateAndRooms(checkInDate, roomDetails);
            checkInDate = checkInDate.plusDays(1);
        }
    }

    /**
     * 객실 예약 내역 조회 - 목록 (내 예약 목록)
     */
    public Page<RoomReservedListDto> roomReservedList(Pageable pageable) {

        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // user 찾기
        Member member = memberRepository.findByEmailAndDelYN(memberEmail, Option.N).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 없음")
        );

        Page<RoomReservation> reservations = roomReservationRepository.findByMember(pageable, member);

        log.info("Total reservations found: {}", reservations.getTotalElements());

        // no구하기
        AtomicInteger start = new AtomicInteger((int) pageable.getOffset());

        Page<RoomReservedListDto> roomReservedListDtos = reservations.map(a -> a.listFromEntity(start.incrementAndGet()));

        return roomReservedListDtos;
    }

    /**
     *  객실 예약 내역 조회 - 단 건 상세내역
     */
    public RoomReservedDetailDto roomReservedDetail(Long roomReservationId) {

        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        // user 찾기
        Member member = memberRepository.findByEmailAndDelYN(memberEmail, Option.N).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 없음")
        );
        RoomReservation detail = roomReservationRepository.findByIdAndMember(roomReservationId, member).orElseThrow(
                () -> new IllegalArgumentException("해당 id의 예약 내역이 없음")
        );

        RoomReservedDetailDto roomReservedDetailDto = detail.detailFromEntity();
        return roomReservedDetailDto;
    }

    /**
     * 원하는 날짜에 남은 객실이 있는지 목록 조회
     */
    public List<PossibleRoomDto> checkRemainRoom(LocalDate checkInDate, LocalDate checkOutDate,
                                                 int adultCnt, int childCnt) {

        List<RoomDetails> allRooms = roomDetailsRepository.findAll(); // 모든 방 리스트 찾아오기
        List<PossibleRoomDto> possibleRoomDtos = new ArrayList<>();

        // 인원수 제한 걸기
        int people = adultCnt + childCnt;

        for (RoomDetails room : allRooms) {
            if (people <= room.getMaxOccupancy()) { // 예약하려는 인원과 방의 수용 가능한 인원수 비교 추가
                boolean possible = true;
                LocalDate date = checkInDate;

                while (date.isBefore(checkOutDate)) { // checkInDate < checkOutDate
                    if (checkReservedDateRepository.findByDateAndRooms(date, room).isPresent()) {
                        possible = false;
                        break;
                    }
                    date = date.plusDays(1);
                }
                if (possible) {
                    possibleRoomDtos.add(room.possibleListFromEntity());
                }
            }
        }

        return possibleRoomDtos;
    }
}