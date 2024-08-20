package com.hotel.flint.common.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {
    @Value("${spring.mail.host}") // Gmail SMTP 서버 호스트
    private String host;

    @Value("${spring.mail.port}") // SMTP 서버의 포트 번호
    private int port;

    @Value("${spring.mail.username}") // 이메일 보내는 용으로 사용되는 이름
    private String username;

    @Value("${spring.mail.password}") // 앱 비밀번호. 분실 시 기존 키 삭제 후 재생성 후 재할당 필요
    private String password;

    @Value("${spring.mail.properties.mail.smtp.auth}") // SMTP 서버에 인증이 필요한 경우 사용함. Gmail SMTP는 인증 필수
    private boolean auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}") // SMTP 서버가 TLS를 사용하여 안전한 연결이 필요한 경우 True
    private boolean starttlsEnable;

    @Value("${spring.mail.properties.mail.smtp.starttls.required}")
    private boolean starttlsRequired;

    @Value("${spring.mail.properties.mail.smtp.connectiontimeout}") // SMTP 서버와 연결을 설정하는 데 대기하는 시간
    private int connectionTimeout;                                  // 너무 크게 설정할 경우 전송 속도가 느려짐

    @Value("${spring.mail.properties.mail.smtp.timeout}") // 서버에서 응답이 오지 않는 경우 응답을 대기하는 시간을 제한하는 데 사용함.
    private int timeout;

    @Value("${spring.mail.properties.mail.smtp.writetimeout}") // SMTP 서버로 전송하는 데 걸리는 시간을 제한하는데 사용함.
    private int writeTimeout;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setDefaultEncoding("UTF-8");
        mailSender.setJavaMailProperties(getMailProperties());

        return mailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.starttls.enable", starttlsEnable);
        properties.put("mail.smtp.starttls.required", starttlsRequired);
        properties.put("mail.smtp.connectiontimeout", connectionTimeout);
        properties.put("mail.smtp.timeout", timeout);
        properties.put("mail.smtp.writetimeout", writeTimeout);

        return properties;
    }
}
