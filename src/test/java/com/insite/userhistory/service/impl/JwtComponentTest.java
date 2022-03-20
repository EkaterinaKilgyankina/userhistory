package com.insite.userhistory.service.impl;

import io.jsonwebtoken.Claims;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class JwtComponentTest {
    JwtComponent jwtComponent = new JwtComponent();

    @Test
    void tokenLifecycleTest() {
        //given
        String name = "CLient";
        //when
        String jwt = jwtComponent.createJWT(name);
        String subject = jwtComponent.decodeJWT(jwt).getSubject();
        //then
        Assertions.assertThat(subject)
                .matches(name);
    }
}