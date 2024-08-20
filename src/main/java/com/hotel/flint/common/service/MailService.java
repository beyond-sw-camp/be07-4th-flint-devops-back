package com.hotel.flint.common.service;

import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.user.employee.repository.EmployeeRepository;
import com.hotel.flint.user.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;
import java.util.Random;

@Transactional
@RequiredArgsConstructor
@Service
public class MailService {
    private final JavaMailSender emailSender;
    private final RedisUtil redisUtil;
    private final UserService userService;
    private final MemberRepository memberRepository;
    private final EmployeeRepository employeeRepository;

    public void authEmail(String email, Object signUpDto) {
        Random random = new Random();
//        6자리 랜덤 코드 발송
        String authKey = String.valueOf(random.nextInt(888888) + 111111);

        sendAuthEmail(email, authKey);
        // 인증 유효시간 5분 redis에 email, authKey 저장
        redisUtil.setDataExpire(email, authKey, 60 * 5L);
        // 회원 가입 정보를 redis에 5분간 저장
        redisUtil.setDataExpire(email + ":data", signUpDto, 60 * 5L);
    }

    private void sendAuthEmail(String email, String authKey) {
        String subject = "Flint Authorization";
        String text = "인증번호는 " + authKey + "입니다.";
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(text, true); // 포함된 텍스트가 HTML이라는 의미로 true.
            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendTempPassword(String email) {
        if(!memberRepository.findByEmailAndDelYN(email, Option.N).isPresent()
                && !employeeRepository.findByEmailAndDelYN(email, Option.N).isPresent()){
            throw new EntityNotFoundException("해당 이메일은 존재하지 않습니다");
        }
        // 10자리 임시 비밀번호 생성
        String tempPassword = generateTempPassword(10);

        // 임시 비밀번호 이메일 발송
        sendTempPasswordEmail(email, tempPassword);

        // 데이터베이스에 인코딩된 임시 비밀번호 저장
        userService.updatePassword(email, tempPassword);
    }

    private void sendTempPasswordEmail(String email, String tempPassword) {
        String subject = "임시 비밀번호 발급";
        String text = "임시 비밀번호는 " + tempPassword + "입니다. 로그인 후 비밀번호를 변경해주세요.";
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(text, true);
            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

//    length 길이만큼의 임시 비밀번호 생성
    private String generateTempPassword(int length) {
//    대소문자, 숫자로 구성된 임시 비밀번호 생성
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
//            chars의 랜덤한 인덱스를 sb에 저장
            sb.append(chars[random.nextInt(chars.length)]);
        }
        return sb.toString();
    }

//    이메일 정보와 인증 코드 일치 여부를 판단함
    public boolean verifyAuthCode(String email, String authCode) {
        String storedAuthCode = redisUtil.getData(email, String.class);
        return authCode.equals(storedAuthCode);
    }

//    Redis에 email:data로 저장된 dto를 불러오는 메서드
    public <T> T getSignUpData(String email, Class<T> clazz) {
        return redisUtil.getData(email + ":data", clazz);
    }
}

