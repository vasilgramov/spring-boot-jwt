package com.jwt_auth.common.utils;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
public class TimeProvider {

        public Date now() {
        return new Date();
    }
}
