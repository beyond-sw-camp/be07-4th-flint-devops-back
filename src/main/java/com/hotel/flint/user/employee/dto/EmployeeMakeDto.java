package com.hotel.flint.user.employee.dto;

import com.hotel.flint.common.enumdir.Department;
import com.hotel.flint.common.enumdir.EmployeeRank;
import com.hotel.flint.common.enumdir.Gender;
import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.user.employee.domain.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeMakeDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private Department department;
    private EmployeeRank employeeRank;
    private LocalDate dateOfEmployment;
    private String password;
    private LocalDate birthday;
    private Gender gender;
    private String employeeNumber;

    public Employee toEntity(String password){
        Employee employee = Employee.builder()
                .email(this.email)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .birthday(this.birthday)
                .phoneNumber(this.phoneNumber)
                .password(password)
                .employeeRank(this.employeeRank)
                .dateOfEmployment(this.dateOfEmployment)
                .gender(this.gender)
                .employeeNumber(this.employeeNumber)
                .department(this.department)
                .delYN(Option.N)
                .build();
        return employee;
    }
}
