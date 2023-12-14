package com.demo_loc_engine.demo.Services;

import org.apache.commons.lang3.StringUtils;

public class MFTS {
    public String crc16(String data) {
        int crc = 0xFFFF;
        int polynomial = 0x1021;
        String result = "";

        byte[] bytes = data.getBytes();

        for (byte b : bytes) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit)
                    crc ^= polynomial;
            }
        }

        crc &= 0xffff;
        result = StringUtils.leftPad(Integer.toHexString(crc), 4, "0");

        return result;
    }
}
