package com.hotel.flint.common.service;

import com.hotel.flint.user.employee.repository.EmployeeRepository;
import com.hotel.flint.user.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
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

