package com.hotel.flint.user.member.domain;

import com.hotel.flint.common.domain.BaseTimeEntity;
import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.reserve.dining.domain.DiningReservation;
import com.hotel.flint.reserve.room.domain.RoomReservation;
import com.hotel.flint.user.employee.dto.EmployeeToMemberDetailDto;
import com.hotel.flint.user.employee.dto.InfoMemberReserveListResDto;
import com.hotel.flint.user.employee.dto.InfoUserResDto;
import com.hotel.flint.user.member.dto.MemberDetResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, updatable = false)
    private String email;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false, unique = true)
    private String phoneNumber;
    @Column(nullable = false)
    private String nation;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private LocalDate birthday;

    @ColumnDefault("'N'")
    @Enumerated(EnumType.STRING)
    private Option delYN;

    @OneToMany(mappedBy = "memberId", cascade = CascadeType.ALL)
    private List<DiningReservation> diningReservationList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<RoomReservation> roomReservations;



    public InfoUserResDto infoUserEntity(){
        return InfoUserResDto.builder()
                .id(this.id)
                .email(this.email)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .nation(this.nation)
                .birthday(this.birthday)
                .countDiningReservation(this.diningReservationList.size())
                .countRoomReservation(this.roomReservations.size())
                .build();
    }

    public MemberDetResDto detUserEntity(){
        return MemberDetResDto.builder()
                .email(this.email)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .phoneNumber(this.phoneNumber)
                .nation(this.nation)
                .password(this.password)
                .birthday(this.birthday)
                .build();
    }

    public InfoMemberReserveListResDto memberReserveListEntity(){
        List<InfoMemberReserveListResDto.RoomReserveId> roomDto = new ArrayList<>();
        for(RoomReservation dto : this.roomReservations){
            roomDto.add(InfoMemberReserveListResDto.fromRoomEntity(dto));
        }

        List<InfoMemberReserveListResDto.DiningReserveId> diningDto = new ArrayList<>();
        for(DiningReservation dto : this.diningReservationList){
            diningDto.add(InfoMemberReserveListResDto.fromDiningEntity(dto));
        }

        return InfoMemberReserveListResDto.builder()
                .roomReservations(roomDto)
                .diningReservations(diningDto)
                .build();
    }

    public Member deleteUser(){
        this.delYN = Option.Y;
        return this;
    }

    public void modifyUser(String password){
        this.password = password;
    }

    public EmployeeToMemberDetailDto detailFromEntity() {
        EmployeeToMemberDetailDto employeeToMemberDetailDto = EmployeeToMemberDetailDto.builder()
                .id(this.id)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .birthday(this.birthday)
                .nation(this.nation)
                .build();
        return employeeToMemberDetailDto;
    }
}