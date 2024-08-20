package com.hotel.flint.user.employee.repository;

import com.hotel.flint.common.enumdir.Department;
import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.user.employee.domain.Employee;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByIdAndDelYN(Long id, Option delYN);
    Optional<Employee> findByEmailAndDelYN(String email, Option delYN);
    Optional<Employee> findByPhoneNumberAndDelYN(String phoneNumber, Option delYN);
    List<Employee> findAll(Specification<Employee> specification);

    List<Employee> findByDepartment(Department department);
}

