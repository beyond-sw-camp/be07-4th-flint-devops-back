package com.hotel.flint.user.employee.service;

import com.hotel.flint.common.enumdir.Department;
import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.dining.domain.Dining;
import com.hotel.flint.reserve.dining.domain.DiningReservation;
import com.hotel.flint.reserve.room.domain.RoomReservation;
import com.hotel.flint.reserve.room.repository.RoomReservationRepository;
import com.hotel.flint.room.domain.RoomInfo;
import com.hotel.flint.room.dto.RoomInfoResDto;
import com.hotel.flint.room.repository.RoomDetailsRepository;
import com.hotel.flint.room.repository.RoomInfoRepository;
import com.hotel.flint.room.repository.RoomPriceRepository;
import com.hotel.flint.user.employee.domain.Employee;
import com.hotel.flint.user.employee.dto.EmployeeModRoomDto;
import com.hotel.flint.user.employee.dto.InfoRoomDetResDto;
import com.hotel.flint.user.employee.dto.InfoRoomResDto;
import com.hotel.flint.user.employee.repository.EmployeeRepository;
import com.hotel.flint.user.member.domain.Member;
import com.hotel.flint.user.member.repository.MemberRepository;
import com.hotel.flint.user.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeRoomService {
    private final RoomPriceRepository roomPriceRepository;
    private final RoomDetailsRepository roomDetailsRepository;
    private final RoomInfoRepository roomInfoRepository;
    private final RoomReservationRepository roomReservationRepository;
    private final EmployeeService employeeService;
    private final MemberRepository memberRepository;
    private final EmployeeRepository employeeRepository;
    private final MemberService memberService;

    @Autowired
    public EmployeeRoomService(RoomPriceRepository roomPriceRepository,
                               RoomDetailsRepository roomDetailsRepository,
                               RoomInfoRepository roomInfoRepository,
                               RoomReservationRepository roomReservationRepository,
                               EmployeeService employeeService,
                               MemberRepository memberRepository, EmployeeRepository employeeRepository, MemberService memberService) {
        this.roomPriceRepository = roomPriceRepository;
        this.roomDetailsRepository = roomDetailsRepository;
        this.roomInfoRepository = roomInfoRepository;
        this.roomReservationRepository = roomReservationRepository;
        this.employeeService = employeeService;
        this.memberRepository = memberRepository;
        this.employeeRepository = employeeRepository;
        this.memberService = memberService;
    }

    private Employee getAuthenticatedEmployee() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            return employeeRepository.findByEmailAndDelYN(email, Option.N)
                    .orElseThrow(() -> new SecurityException("인증되지 않은 사용자입니다."));
        } else {
            throw new SecurityException("인증되지 않은 사용자입니다.");
        }
    }

    public List<RoomInfoResDto> roomInfoList(){
        Employee authenticatedEmployee = getAuthenticatedEmployee();
        if(!authenticatedEmployee.getDepartment().toString().equals("Room")){
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }
        List<RoomInfo> infos = roomInfoRepository.findAll();
        List<RoomInfoResDto> resDtos= new ArrayList<>();

        for(RoomInfo info : infos){
            resDtos.add(info.fromEntity());
        }

        return resDtos;
    }


    public void modRoomPrice(Long id, long newPrice) {
        Employee authenticatedEmployee = getAuthenticatedEmployee();
        if(!authenticatedEmployee.getDepartment().toString().equals("Room")){
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }

        RoomInfo roomInfo = roomInfoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 타입의 객실은 존재하지 않습니다."));

        roomInfo.updateRoomPrice(newPrice);
        roomInfoRepository.save(roomInfo);
    }

    public List<InfoRoomResDto> memberReservationRoomCheck(String email, Pageable pageable) {
        Employee authenticateEmployee = getAuthenticatedEmployee();
        Member member = memberRepository.findByEmailAndDelYN(email, Option.N)
                .orElseThrow(() -> new EntityNotFoundException("해당 고객 email이 존재하지 않습니다."));
        String auth = authenticateEmployee.getDepartment().toString();
        List<InfoRoomResDto> allReservation = new ArrayList<>();
        int pageNumber = 0;
        boolean hasMorePage;
        do {
            pageable = PageRequest.of(pageNumber, 10);
            Page<RoomReservation> roomReservations;
            if(auth.equals(Department.Room.toString())){
                roomReservations = roomReservationRepository.findByMember(pageable, member);
            }else{
                throw new IllegalArgumentException("접근 권한이 없습니다.");
            }

            allReservation.addAll(roomReservations.stream()
                    .map(revs -> revs.toInfoRoomResDto())
                    .collect(Collectors.toList()));
            hasMorePage = roomReservations.hasNext();
            pageNumber++;
        }while(hasMorePage);
        return allReservation;
    }

    public void memberReservationCncRoomByEmployee(InfoRoomDetResDto dto) {
        roomReservationRepository.deleteById(dto.getId());
    }

    /**
     * 고객의 요청 시, 객실 예약 내역 수정
     */
    @Transactional
    public void updateRoomReservation(Long id, EmployeeModRoomDto dto) {

        String employeeEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee employee = employeeRepository.findByEmailAndDelYN(employeeEmail, Option.N).orElseThrow(
                () -> new EntityNotFoundException("해당 이메일의 직원 없음")
        );
        RoomReservation roomReservation = roomReservationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("해당 id의 예약 내역이 없음")
        );

        if (employee.getDepartment().equals(Department.Room)) { // room부서일 경우만 수정 가능
            roomReservation.updateFromEntity(dto);
        } else {
            throw new IllegalArgumentException("객실 담당자만 접근 가능함");
        }
    }
}
