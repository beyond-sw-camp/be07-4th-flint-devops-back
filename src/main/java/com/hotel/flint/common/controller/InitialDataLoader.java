package com.hotel.flint.common.controller;

import com.hotel.flint.common.enumdir.Department;
import com.hotel.flint.common.enumdir.EmployeeRank;
import com.hotel.flint.common.enumdir.Gender;
import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.user.employee.dto.EmployeeMakeDto;
import com.hotel.flint.user.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Component
public class InitialDataLoader implements CommandLineRunner {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //    실행 시점에 office 부서의 유저 한개를 만들겠다.
    @Override
    public void run(String... args) throws Exception{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        if (employeeRepository.findByEmailAndDelYN("flint@gmail.com", Option.N).isEmpty()) {
            LocalDate date = LocalDate.parse("2024/08/21", formatter);
            EmployeeMakeDto dto = EmployeeMakeDto.builder()
                    .firstName("flint")
                    .lastName("hotel")
                    .email("flint@gmail.com")
                    .phoneNumber("02-1111-1111")
                    .department(Department.Office)
                    .password("12341234")
                    .employeeRank(EmployeeRank.사장)
                    .birthday(date)
                    .dateOfEmployment(date)
                    .gender(Gender.FEMALE)
                    .employeeNumber("FL10000001")
                    .build();
            employeeRepository.save(dto.toEntity(passwordEncoder.encode(dto.getPassword())));
        }
        if (employeeRepository.findByEmailAndDelYN("flint_room@gmail.com", Option.N).isEmpty()) {
            LocalDate date = LocalDate.parse("2024/08/21", formatter);
            EmployeeMakeDto dto = EmployeeMakeDto.builder()
                    .firstName("flint")
                    .lastName("Room")
                    .email("flint_room@gmail.com")
                    .phoneNumber("02-1111-2222")
                    .department(Department.Room)
                    .password("12341234")
                    .employeeRank(EmployeeRank.이사)
                    .birthday(date)
                    .dateOfEmployment(date)
                    .gender(Gender.MALE)
                    .employeeNumber("FL11000001")
                    .build();
            employeeRepository.save(dto.toEntity(passwordEncoder.encode(dto.getPassword())));
        }
        if (employeeRepository.findByEmailAndDelYN("flint_kor@gmail.com", Option.N).isEmpty()) {
            LocalDate date = LocalDate.parse("2024/08/21", formatter);
            EmployeeMakeDto dto = EmployeeMakeDto.builder()
                    .firstName("flint")
                    .lastName("KorDining")
                    .email("flint_kor@gmail.com")
                    .phoneNumber("02-1111-3333")
                    .department(Department.KorDining)
                    .password("12341234")
                    .employeeRank(EmployeeRank.이사)
                    .birthday(date)
                    .dateOfEmployment(date)
                    .gender(Gender.FEMALE)
                    .employeeNumber("FL12000001")
                    .build();
            employeeRepository.save(dto.toEntity(passwordEncoder.encode(dto.getPassword())));
        }
        if (employeeRepository.findByEmailAndDelYN("flint_jap@gmail.com", Option.N).isEmpty()) {
            LocalDate date = LocalDate.parse("2024/08/21", formatter);
            EmployeeMakeDto dto = EmployeeMakeDto.builder()
                    .firstName("flint")
                    .lastName("JapDining")
                    .email("flint_jap@gmail.com")
                    .phoneNumber("02-1111-4444")
                    .department(Department.JapDining)
                    .password("12341234")
                    .employeeRank(EmployeeRank.이사)
                    .birthday(date)
                    .dateOfEmployment(date)
                    .gender(Gender.MALE)
                    .employeeNumber("FL13000001")
                    .build();
            employeeRepository.save(dto.toEntity(passwordEncoder.encode(dto.getPassword())));
        }
        if (employeeRepository.findByEmailAndDelYN("flint_chi@gmail.com", Option.N).isEmpty()) {
            LocalDate date = LocalDate.parse("2024/08/21", formatter);
            EmployeeMakeDto dto = EmployeeMakeDto.builder()
                    .firstName("flint")
                    .lastName("ChiDining")
                    .email("flint_chi@gmail.com")
                    .phoneNumber("02-1111-5555")
                    .department(Department.ChiDining)
                    .password("12341234")
                    .employeeRank(EmployeeRank.이사)
                    .birthday(date)
                    .dateOfEmployment(date)
                    .gender(Gender.FEMALE)
                    .employeeNumber("FL14000001")
                    .build();
            employeeRepository.save(dto.toEntity(passwordEncoder.encode(dto.getPassword())));
        }
        if (employeeRepository.findByEmailAndDelYN("flint_lou@gmail.com", Option.N).isEmpty()) {
            LocalDate date = LocalDate.parse("2024/08/21", formatter);
            EmployeeMakeDto dto = EmployeeMakeDto.builder()
                    .firstName("flint")
                    .lastName("Lounge")
                    .email("flint_lou@gmail.com")
                    .phoneNumber("02-1111-6666")
                    .department(Department.Lounge)
                    .password("12341234")
                    .employeeRank(EmployeeRank.이사)
                    .birthday(date)
                    .dateOfEmployment(date)
                    .gender(Gender.MALE)
                    .employeeNumber("FL15000001")
                    .build();
            employeeRepository.save(dto.toEntity(passwordEncoder.encode(dto.getPassword())));
        }
    }
}
