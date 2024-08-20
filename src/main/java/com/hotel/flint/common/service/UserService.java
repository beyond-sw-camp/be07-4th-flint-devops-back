package com.hotel.flint.common.service;

import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.user.employee.domain.Employee;
import com.hotel.flint.user.employee.repository.EmployeeRepository;
import com.hotel.flint.user.member.domain.Member;
import com.hotel.flint.user.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final MemberRepository memberRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(MemberRepository memberRepository, EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

//    직원, 회원 공통된 부분이므로 여기서 공통화 작업
    public void updatePassword(String email, String newPassword) {
//        이메일 조회 결과로 직원인지 회원인지 판단함.
        Optional<Member> memberOpt = memberRepository.findByEmailAndDelYN(email, Option.N);
        Optional<Employee> employeeOpt = employeeRepository.findByEmailAndDelYN(email, Option.N);

        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            member.modifyUser(passwordEncoder.encode(newPassword));
//            더티 체킹하므로 save 부분 불필요
//            memberRepository.save(member);
        } else if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            employee.modifyEmp(passwordEncoder.encode(newPassword));
//            employeeRepository.save(employee);
        } else {
            throw new EntityNotFoundException("해당 이메일로 가입된 유저가 존재하지 않습니다.");
        }
    }
}
