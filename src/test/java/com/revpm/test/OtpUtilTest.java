package com.revpm.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.revpm.util.OtpUtil;

public class OtpUtilTest {

    @Test
    void testGenerateOtp() {
        String otp = OtpUtil.generateOtp();

        assertNotNull(otp);
        assertEquals(6, otp.length()); // 6-digit OTP
    }
}
