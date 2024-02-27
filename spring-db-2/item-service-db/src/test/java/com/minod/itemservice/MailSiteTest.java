package com.minod.itemservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MailSiteTest {

    @Autowired private MailSiteProperties mailSiteProperties;

    @Test
    void setMailSiteProperties() {
        System.out.println(mailSiteProperties);
    }
}
