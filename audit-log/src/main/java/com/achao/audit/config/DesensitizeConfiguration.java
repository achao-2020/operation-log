package com.achao.audit.config;

import com.achao.audit.desensitize.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author achao
 * @date 2022/12/15 23:55
 */
@Configuration(proxyBeanMethods = false)
public class DesensitizeConfiguration {

    @Bean(name = "#desAddress")
    public AddressPattern addressPattern() {
        return new AddressPattern();
    }

    @Bean(name = "#desBirthday")
    public BirthdayPattern birthdayPattern() {
        return new BirthdayPattern();
    }

    @Bean(name = "#desEmail")
    public EmailPattern emailPattern() {
        return new EmailPattern();
    }

    @Bean(name = "#desIdCard")
    public IdCardPattern idCardPattern() {
        return new IdCardPattern();
    }

    @Bean(name = "#desName")
    public NamePattern namePattern() {
        return new NamePattern();
    }

    @Bean(name = "#desPayId")
    public PayIdPattern pattern() {
        return new PayIdPattern();
    }

    @Bean(name = "#desPhone")
    public PhonePattern phonePattern() {
        return new PhonePattern();
    }

    @Bean(name = "#desPostCode")
    public PostCodePattern postCodePattern() {
        return new PostCodePattern();
    }
}
